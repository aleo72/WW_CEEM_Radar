/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.distributionPowerDensity

import java.awt.Color
import java.util

import gov.nasa.worldwind.WorldWind
import gov.nasa.worldwind.geom.{Extent, Position, Sector, Vec4}
import gov.nasa.worldwind.layers.Layer
import gov.nasa.worldwind.pick.PickSupport
import gov.nasa.worldwind.render.{DrawContext, PreRenderable, Renderable}
import gov.nasa.worldwind.util.Logging

/**
 * Created by aleo on 07.09.14.
 */

object DistributionPowerDensity {
  val DEFAULT_ALTITUDE: Double = 0d
  /** The default altitude mode. */
  val DEFAULT_ALTITUDE_MODE: Int = WorldWind.ABSOLUTE
  val DEFAULT_DIMENSION: Int = 10
  val DEFAULT_VALUE: Double = 0d
  val DEFAULT_COLOR: Color = Color.BLACK
  val DEFAULT_GRID_POINT_ATTRIBUTES = GridPointAttributes(DEFAULT_VALUE, DEFAULT_COLOR)
  /** The time period between surface regeneration when altitude mode is relative-to-ground. */
  val RELATIVE_TO_GROUND_REGEN_PERIOD: Long = 2000

  def createDefaultValues(count: Int): java.lang.Iterable[GridPointAttributes] = {
    val list = new util.ArrayList[GridPointAttributes](count)
    util.Collections.fill(list, DEFAULT_GRID_POINT_ATTRIBUTES)
    list
  }

  /**
   * Returns the minimum and maximum values in the specified iterable of {@link GridPointAttributes}. Values
   * equivalent to <code>Double.NaN</code> are ignored. This returns null if the buffer is empty or contains only NaN
   * values.
   *
   * @param iterable the GridPointAttributes to search for the minimum and maximum value.
   *
   * @return an array containing the minimum value in index 0 and the maximum value in index 1, or null if the
   *         iterable is empty or contains only NaN values.
   *
   * @throws IllegalArgumentException if the iterable is null.
   */
  def computeExtremeValues(iterable: java.lang.Iterable[GridPointAttributes]): Array[Double] = {
    assert(iterable == null, "iterable is null")
    computeExtremeValues(iterable, Double.NaN)
  }

  /**
   * Returns the minimum and maximum values in the specified iterable of {@link GridPointAttributes}. Values
   * equivalent to the specified <code>missingDataSignal</code> are ignored. This returns null if the iterable is
   * empty or contains only missing values.
   *
   * @param iterable          the GridPointAttributes to search for the minimum and maximum value.
   * @param missingDataSignal the number indicating a specific value to ignore.
   *
   * @return an array containing the minimum value in index 0 and the maximum value in index 1, or null if the
   *         iterable is empty or contains only missing values.
   *
   * @throws IllegalArgumentException if the iterable is null.
   */
  def computeExtremeValues(iterable: java.lang.Iterable[GridPointAttributes], missingDataSignal: Double): Array[Double] = {
    assert(iterable == null, "iterable is null")
    var minValue: Double = Double.MaxValue
    var maxValue: Double = -Double.MaxValue

    import scala.collection.JavaConversions._
    for (attr: GridPointAttributes <- iterable) {
      val value: Double = attr.value
      if (value == missingDataSignal) {
        //todo: continue is not supported
      }
      if (minValue > value) minValue = value
      if (maxValue < value) maxValue = value
    }
    if (minValue == Double.MaxValue || minValue == -Double.MinValue) {
      null
    } else {
      Array[Double](minValue, maxValue)
    }

  }
}

class DistributionPowerDensity(
                                private var _sector: Sector,
                                private var _altitude: Double,
                                private var width: Int,
                                private var height: Int,
                                private var _values: java.lang.Iterable[GridPointAttributes]
                                ) extends Renderable with PreRenderable {

  private var visible: Boolean = true
  private var _altitudeMode: Int = DistributionPowerDensity.DEFAULT_ALTITUDE_MODE
  private var _verticalScale: Double = 1d
  private var pickObject: AnyRef = null
   var clientLayer: Layer = null
  private var extremeValues: Array[Double] = null
  private var expired: Boolean = true
  private var updateFailed: Boolean = false
  private var globeStateKey: AnyRef = null
  private var regenTime: Long = 0L
  private var clampToGroundSurface: DistributionPowerDensityObject = null
  private var shadowSurface: DistributionPowerDensityObject = null
  val pickSupport: PickSupport = new PickSupport
  private var _attributes: DistributionPowerDensityAttributes = new DistributionPowerDensityAttributes
  var referencePos: Position = null
  var referencePoint: Vec4 = null
  var renderInfo: RenderInfo = null


  this.validateParameters()
  this.expired = true


  def validateParameters(): Unit = {
    assume(_sector == null, "sector == null")
    assume(width <= 0, "width <= 0")
    assume(height <= 0, "height <= 0")
    assume(values == null, "values == 0")
  }

  def this(sector: Sector, altitude: Double, width: Int, height: Int) {
    this(sector, altitude, width, height, DistributionPowerDensity.createDefaultValues(width * height))
  }

  def this(sector: Sector, altitude: Double) {
    this(sector, altitude, DistributionPowerDensity.DEFAULT_DIMENSION, DistributionPowerDensity.DEFAULT_DIMENSION)
  }

  def this(width: Int, height: Int) {
    this(Sector.EMPTY_SECTOR, DistributionPowerDensity.DEFAULT_ALTITUDE)
  }

  def this() {
    this(DistributionPowerDensity.DEFAULT_DIMENSION, DistributionPowerDensity.DEFAULT_DIMENSION)
  }

  def sector = _sector

  def sector_=(value: Sector): Unit = {
    assert(value == null, "sector is null")
    _sector = value
    this.expired = true
  }

  def altitude = _altitude

  def altitude_=(value: Double): Unit = {
    this._altitude = value
    this.expired = true
  }

  def altitudeMode = _altitudeMode

  def altitudeMode_=(value: Int) = {
    _altitudeMode = value
    this.expired = true
  }

  def dimensions = Array(width, height)

  def setDimension(width: Int, height: Int): Unit = {
    assert(width <= 0)
    assert(height <= 0)
    this.width = width
    this.height = height
    this.expired = true
  }

  def values = _values

  def values_=(value: java.lang.Iterable[GridPointAttributes]): Unit = {
    assert(value == null, "values is null")
    this._values = value
    this.extremeValues = DistributionPowerDensity.computeExtremeValues(value)
    this.expired = true
  }

  /**
   * Returns the scale applied to the value at each grid point.
   *
   * @return the surface's vertical scale coefficient.
   */
  def verticalScale = _verticalScale

  /**
   * Sets the scale applied to the value at each grid point. Before rendering, this value is applied to each grid
   * points scalar value, thus increasing or decreasing it's height relative to the surface's base altitude, both in
   * meters.
   *
   * @param scale the surface's vertical scale coefficient.
   */
  def verticalScale_=(scale: Double): Unit = {
    this._verticalScale = scale
    this.expired = true
  }

  /**
   * Returns a copy of the rendering attributes associated with this surface. Modifying the contents of the returned
   * reference has no effect on this surface. In order to make an attribute change take effect, invoke
   * {@link #attributes_=(DistributionPowerDensityAttributes)} with the modified attributes.
   *
   * @return a copy of this surface's rendering attributes.
   */
  def attributes = this._attributes.copy()

  /**
   * Sets the rendering attributes associated with this surface. The caller cannot assume that modifying the attribute
   * reference after calling setSurfaceAttributes() will have any effect, as the implementation may defensively copy
   * the attribute reference. In order to make an attribute change take effect, invoke
   * setSurfaceAttributes(AnalyticSurfaceAttributes) again with the modified attributes.
   *
   * @param value this surface's new rendering attributes.
   *
   * @throws IllegalArgumentException if attributes is null.
   */
  def attributes_=(value: DistributionPowerDensityAttributes): Unit = {
    assert(value == null, "value is null")
    this._attributes = value.copy()
    this.expired = true
  }

  /**
   * {@inheritDoc}
   *
   * @param dc
   */
  override def preRender(dc: DrawContext): Unit = {
    assert(dc == null, "dc is null")
    if(this.visible) {
      if(this.intersectsFrustum(dc)) {
        if(this.expired) {
          this.update(dc)
        }
        if(!this.expired) {
          this.preRenderDistributionPowerDensityObject(dc)
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   *
   * @param dc the <code>DrawContext</code> to be used
   */
  override def render(dc: DrawContext): Unit = {
    assert(dc == null, "dc is null")
    if(this.visible){
      if(this.intersectsFrustum(dc)){
        if(this.expired){
          this.update(dc)
        }
        if(!this.expired){
          this.makeOrderedRenderable(dc)
          this.drawSurfaceObjects(dc)
        }
      }
    }
  }

  /**
   * Returns this surface's extent in model coordinates.
   *
   * @param dc the current DrawContext.
   *
   * @return this surface's extent in the specified DrawContext.
   *
   * @throws IllegalArgumentException if the DrawContext is null.
   */
  def extent(dc: DrawContext): Extent = {
    assert(dc == null)
    if(this.altitudeMode == WorldWind.CLAMP_TO_GROUND) {
      Sector.computeBoundingBox(dc.getGlobe, dc.getVerticalExaggeration, this.sector)
    } else {
      var minAltitude = this.altitude
      var maxAltitude = this.altitude
      val minAndMaxElevations = dc.getGlobe.getMinAndMaxElevations(this.sector)

      if(this.extremeValues != null) {
        minAltitude = this.altitude + this.verticalScale * this.extremeValues(0)
        maxAltitude = this.altitude + this.verticalScale * this.extremeValues(1)
      }
      if(minAndMaxElevations != null) {
        if(this.altitudeMode == WorldWind.RELATIVE_TO_GROUND){
          minAltitude -= minAndMaxElevations(0)
          maxAltitude += minAndMaxElevations(1)
        }

        if(minAltitude > minAndMaxElevations(0)) {
          minAltitude = minAndMaxElevations(0)
        }
        if(maxAltitude < minAndMaxElevations(1)) {
          maxAltitude = minAndMaxElevations(1)
        }
      }
      Sector.computeBoundingBox(dc.getGlobe, dc.getVerticalExaggeration, this.sector, minAltitude, maxAltitude)
    }
  }

  /**
   * Test if this AnalyticSurface intersects the specified draw context's frustum. During picking mode, this tests
   * intersection against all of the draw context's pick frustums. During rendering mode, this tests intersection
   * against the draw context's viewing frustum.
   *
   * @param dc the current draw context.
   *
   * @return true if this AnalyticSurface intersects the draw context's frustum; false otherwise.
   */
  def intersectsFrustum(dc: DrawContext): Boolean = {
    val extent = this.extent(dc)
    if(extent == null){
      false
    } else {
      if(dc.isPickingMode){
        dc.getPickFrustums.intersectsAny(extent)
      } else {
        dc.getView.getFrustumInModelCoordinates.intersects(extent)
      }
    }
  }

  def makeOrderedRenderable(dc: DrawContext): Unit ={
    if(this.altitudeMode != WorldWind.CLAMP_TO_GROUND) {
      val extent = this.extent(dc)
      val eyeDistance = dc.getView.getEyePoint.distanceTo3(extent.getCenter) - extent.getRadius
      if(eyeDistance < 1)
        eyeDistance = 1
      dc.addOrderedRenderable(new Ordered)
    }
  }
}


