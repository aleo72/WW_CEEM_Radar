/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.distributionPowerDensity

import java.awt.Color
import java.nio.BufferOverflowException
import java.util
import javax.media.opengl.fixedfunc.{GLLightingFunc, GLPointerFunc}
import javax.media.opengl.{GL, GL2, GL2ES1}

import gov.nasa.worldwind.WorldWind
import gov.nasa.worldwind.geom._
import gov.nasa.worldwind.layers.Layer
import gov.nasa.worldwind.pick.PickSupport
import gov.nasa.worldwind.render.{DrawContext, PreRenderable, Renderable}
import gov.nasa.worldwind.util.{OGLUtil, WWMath}

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
    assert(iterable != null, "iterable is null")
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
    assert(iterable != null, "iterable is null")
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
  private var _expired: Boolean = true
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

  def expired = _expired

  def expired_=(value: Boolean): Unit = {
    this._expired = value
    if (this._expired) {
      if (this.clampToGroundSurface != null) {
        this.clampToGroundSurface.markAsModified()
      }
      if (this.shadowSurface != null) {
        this.shadowSurface.markAsModified()
      }
    }
  }

  def isExpired(dc: DrawContext): Boolean = {
    if (!this._expired) {
      if (this.altitudeMode == WorldWind.RELATIVE_TO_GROUND) {
        if (dc.getFrameTimeStamp - this.regenTime > DistributionPowerDensity.RELATIVE_TO_GROUND_REGEN_PERIOD) {
          return true
        }
      }
      if (this.altitudeMode == WorldWind.RELATIVE_TO_GROUND || this.altitudeMode == WorldWind.ABSOLUTE) {
        val gsk = dc.getGlobe.getStateKey(dc)
        if (if (this.globeStateKey != null) this.globeStateKey == gsk else gsk != null) {
          return true
        }
      }
      false
    } else {
      true
    }
  }


  def validateParameters(): Unit = {
    assert(_sector != null, "sector == null")
    assert(width > 0, "width <= 0")
    assert(height > 0, "height <= 0")
    assert(values != null, "values == 0")
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
    assert(value != null, "sector is null")
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
    assert(width > 0)
    assert(height > 0)
    this.width = width
    this.height = height
    this.expired = true
  }

  def values = _values

  def values_=(value: java.lang.Iterable[GridPointAttributes]): Unit = {
    assert(value != null, "values is null")
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
    assert(value != null, "value is null")
    this._attributes = value.copy()
    this.expired = true
  }

  /**
   * {@inheritDoc}
   *
   * @param dc
   */
  override def preRender(dc: DrawContext): Unit = {
    assert(dc != null, "dc is null")
    if (this.visible) {
      if (this.intersectsFrustum(dc)) {
        if (this.isExpired(dc)) {
          this.update(dc)
        }
        if (!this.isExpired(dc)) {
          this.preRenderDistributionPowerDensityObject(dc)
        }
      }
    }
  }

  def preRenderDistributionPowerDensityObject(dc: DrawContext): Unit = {
    if (this.attributes.drawShadow) {
      if (this.shadowSurface == null) {
        this.shadowSurface = this.createShadowSurface()
      }
      this.shadowSurface.preRender(dc)
    }

    if (this.altitudeMode == WorldWind.CLAMP_TO_GROUND) {
      if (this.clampToGroundSurface == null) {
        this.clampToGroundSurface = this.createClampToGroundSurface()
      }
      this.clampToGroundSurface.preRender(dc)
    }
  }

  /**
   * {@inheritDoc}
   *
   * @param dc the <code>DrawContext</code> to be used
   */
  override def render(dc: DrawContext): Unit = {
    assert(dc != null, "dc is null")
    if (this.visible) {
      if (this.intersectsFrustum(dc)) {
        if (this.expired) {
          this.update(dc)
        }
        if (!this.expired) {
          this.makeOrderedRenderable(dc)
          this.drawSurfaceObjects(dc)
        }
      }
    }
  }

  def drawSurfaceObjects(dc: DrawContext): Unit = {
    if (this.attributes.drawShadow) {
      if (!dc.isPickingMode) {
        this.shadowSurface.render(dc)
      }
    }
    if (this.altitudeMode == WorldWind.CLAMP_TO_GROUND) {
      this.clampToGroundSurface.render(dc)
    }
  }

  def createClampToGroundSurface() = new ClampToGroundSurface(this)

  def createShadowSurface() = new ShadowSurface(this)

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
    assert(dc != null)
    if (this.altitudeMode == WorldWind.CLAMP_TO_GROUND) {
      Sector.computeBoundingBox(dc.getGlobe, dc.getVerticalExaggeration, this.sector)
    } else {
      var minAltitude = this.altitude
      var maxAltitude = this.altitude
      val minAndMaxElevations = dc.getGlobe.getMinAndMaxElevations(this.sector)

      if (this.extremeValues != null) {
        minAltitude = this.altitude + this.verticalScale * this.extremeValues(0)
        maxAltitude = this.altitude + this.verticalScale * this.extremeValues(1)
      }
      if (minAndMaxElevations != null) {
        if (this.altitudeMode == WorldWind.RELATIVE_TO_GROUND) {
          minAltitude -= minAndMaxElevations(0)
          maxAltitude += minAndMaxElevations(1)
        }

        if (minAltitude > minAndMaxElevations(0)) {
          minAltitude = minAndMaxElevations(0)
        }
        if (maxAltitude < minAndMaxElevations(1)) {
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
    if (extent == null) {
      false
    } else {
      if (dc.isPickingMode) {
        dc.getPickFrustums.intersectsAny(extent)
      } else {
        dc.getView.getFrustumInModelCoordinates.intersects(extent)
      }
    }
  }

  def makeOrderedRenderable(dc: DrawContext): Unit = {
    if (this.altitudeMode != WorldWind.CLAMP_TO_GROUND) {
      val extent = this.extent(dc)
      var eyeDistance = dc.getView.getEyePoint.distanceTo3(extent.getCenter) - extent.getRadius
      if (eyeDistance < 1)
        eyeDistance = 1
      dc.addOrderedRenderable(new OrderedDistributionPowerDensity(this, eyeDistance))
    }
  }

  def drawOrderedRenderable(dc: DrawContext): Unit = {
    this.beginDrawing(dc)
    try {
      this.doDrawOrderedRenderable(dc)
    } finally {
      this.endDrawing(dc)
    }
  }

  protected def beginDrawing(dc: DrawContext) {
    val gl: GL2 = dc.getGL.getGL2 // GL initialization checks for GL2 compatibility.
    gl.glPushAttrib(
      GL.GL_COLOR_BUFFER_BIT // for alpha test func and ref, blend func
        | GL2.GL_CURRENT_BIT
        | GL.GL_DEPTH_BUFFER_BIT
        | GL2.GL_LINE_BIT // for line width
        | GL2.GL_POLYGON_BIT // for cull face
        | (if (!dc.isPickingMode) GL2.GL_LIGHTING_BIT else 0) // for lighting.
        | (if (!dc.isPickingMode) GL2.GL_TRANSFORM_BIT else 0) // for normalize state.
    )
    gl.glPushClientAttrib(GL2.GL_CLIENT_VERTEX_ARRAY_BIT)
    gl.glEnable(GL2ES1.GL_ALPHA_TEST)
    gl.glAlphaFunc(GL.GL_GREATER, 0.0f)
    gl.glEnable(GL.GL_CULL_FACE)
    gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY)
    gl.glEnableClientState(GLPointerFunc.GL_NORMAL_ARRAY)
    if (dc.isPickingMode) {
      val color: Color = dc.getUniquePickColor
      this.pickSupport.addPickableObject(color.getRGB,
        if (this.pickObject != null) this.pickObject else this,
        new Position(this.sector.getCentroid, this.altitude), false)
      gl.glColor3ub(color.getRed.toByte, color.getGreen.toByte, color.getBlue.toByte)
    }
    else {
      // Enable blending in non-premultiplied color mode. Premultiplied colors don't work with GL fixed
      // functionality lighting.
      gl.glEnable(GL.GL_BLEND)
      OGLUtil.applyBlending(gl, false)
      // Enable lighting with GL_LIGHT1.
      gl.glDisable(GLLightingFunc.GL_COLOR_MATERIAL)
      gl.glDisable(GLLightingFunc.GL_LIGHT0)
      gl.glEnable(GLLightingFunc.GL_LIGHTING)
      gl.glEnable(GLLightingFunc.GL_LIGHT1)
      gl.glEnable(GLLightingFunc.GL_NORMALIZE)
      // Configure the lighting model for two-sided smooth shading.
      gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL.GL_TRUE)
      gl.glLightModeli(GL2ES1.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_TRUE)
      gl.glShadeModel(GLLightingFunc.GL_SMOOTH)
      // Configure GL_LIGHT1 as a white light eminating from the viewer's eye point.
      OGLUtil.applyLightingDirectionalFromViewer(gl, GLLightingFunc.GL_LIGHT1, new Vec4(1.0, 0.5, 1.0).normalize3)
    }
    dc.getView.pushReferenceCenter(dc, this.referencePoint)
  }

  def doDrawOrderedRenderable(dc: DrawContext): Unit = {
    this.bind(dc)
    // If the outline and interior will be drawn, then draw the outline color, but do not affect the depth
    // buffer. When the interior is drawn, it will draw on top of these colors, and the outline will be visible
    // behind the potentially transparent interior.
    if (this.attributes.drawOutline && this.attributes.drawInterior) {
      dc.getGL.glDepthMask(false)
      this.drawOutline(dc)
      dc.getGL.glDepthMask(true)
    }
  }

  def drawOutline(dc: DrawContext): Unit = {
    val gl = dc.getGL.getGL2
    if (!dc.isPickingMode) {
      gl.glEnable(GL.GL_LINE_SMOOTH)
      // Unbind the shapes vertex colors as the diffuse material parameter.
      gl.glDisable(GLLightingFunc.GL_LIGHTING)
      gl.glDisable(GLLightingFunc.GL_COLOR_MATERIAL)
      // Set the outline color.
      val color: Color = this.attributes.outlineMaterial.getDiffuse
      // Convert the floating point opacity from the range [0, 1] to the unsigned byte range [0, 255].
      val alpha: Int = (255 * this.attributes.outlineOpacity + 0.5).toInt
      gl.glColor4ub(color.getRed.toByte, color.getGreen.toByte, color.getBlue.toByte, alpha.toByte)
    }
    gl.glDisableClientState(GLPointerFunc.GL_COLOR_ARRAY)
    gl.glLineWidth(this.attributes.outlineWidth.asInstanceOf[Float])
    this.renderInfo.drawOutline(dc)

    if (!dc.isPickingMode) {
      gl.glEnable(GLLightingFunc.GL_LIGHTING)
      gl.glDisable(GL.GL_LINE_SMOOTH)
      gl.glDisable(GL2.GL_LINE_STIPPLE)
    }
  }

  protected def bind(dc: DrawContext) {
    val gl: GL2 = dc.getGL.getGL2
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, this.renderInfo.cartesianVertexBuffer)
    gl.glNormalPointer(GL.GL_FLOAT, 0, this.renderInfo.cartesianNormalBuffer)
    gl.glColorPointer(4, GL.GL_UNSIGNED_BYTE, 0, this.renderInfo.colorBuffer)
  }

  def endDrawing(dc: DrawContext): Unit = {
    val gl = dc.getGL.getGL2
    dc.getView.popReferenceCenter(dc)
    gl.glPopAttrib()
    gl.glPopClientAttrib()
  }

  def update(dc: DrawContext): Unit = {
    if (!this.updateFailed) {
      try {
        this.doUpdate(dc)
        this.expired = false
        this.globeStateKey = dc.getGlobe.getStateKey(dc)
        this.regenTime = dc.getFrameTimeStamp
      } catch {
        case e: Exception => e.printStackTrace()
          this.updateFailed = true
      }
    }
  }

  def doUpdate(dc: DrawContext): Unit = {
    this.referencePos = new Position(this.sector.getCentroid, this.altitude)
    this.referencePoint = dc.getGlobe.computePointFromPosition(this.referencePos)
    if (this.renderInfo == null || this.renderInfo.gridWidth != width || this.renderInfo.gridHeight != this.height) {
      this.renderInfo = new RenderInfo(this.width, this.height)
    }
    this.updateSurfacePoints(dc, this.renderInfo)
    this.updateSurfaceNormals(this.renderInfo)
  }

  def updateSurfacePoints(dc: DrawContext, outRenderInfo: RenderInfo): Unit = {
    val iter = this.values.iterator()

    val latStep = -this.sector.getDeltaLatDegrees / (this.height - 1).toDouble
    val lonStep = this.sector.getDeltaLonDegrees / (this.width - 1).toDouble

    var lat = this.sector.getMaxLatitude.degrees
    for (y <- 0 to this.height) {
      var lon = this.sector.getMinLatitude.degrees
      for (x <- 0 to this.width) {
        val attr: GridPointAttributes = if (iter.hasNext) {
          iter.next()
        } else {
          null
        }

        this.updateNextSurfacePoint(dc, Angle.fromDegrees(lat), Angle.fromDegrees(lon), attr, outRenderInfo)
        lon += lonStep
      }
      lat += latStep
    }
    outRenderInfo.cartesianVertexBuffer.rewind()
    outRenderInfo.geographicVertexBuffer.rewind()
    outRenderInfo.colorBuffer.rewind()
    outRenderInfo.shadowColorBuffer.rewind()
  }

  def updateNextSurfacePoint(dc: DrawContext, lat: Angle, lon: Angle, attr: GridPointAttributes, outRenderInfo: RenderInfo): Unit = {
    val color = if (attr != null) attr.color else DistributionPowerDensity.DEFAULT_COLOR
    // Convert the floating point opacity from the range [0, 1] to the unsigned byte range [0, 255].
    val alpha = (color.getAlpha * this.attributes.interiorOpacity + 0.5).toInt
    outRenderInfo.colorBuffer.put(color.getRed.toByte)
    outRenderInfo.colorBuffer.put(color.getGreen.toByte)
    outRenderInfo.colorBuffer.put(color.getBlue.toByte)
    outRenderInfo.colorBuffer.put(color.getAlpha.toByte)
    // We need geographic vertices if the surface's altitude mode is clamp-to-ground, or if we're drawing the
    // surface's shadow.
    if (this.altitudeMode == WorldWind.CLAMP_TO_GROUND || this.attributes.drawShadow) {
      outRenderInfo.geographicVertexBuffer.put((lon.degrees - this.referencePos.getLongitude.degrees).toFloat)
      outRenderInfo.geographicVertexBuffer.put((lat.degrees - this.referencePos.getLatitude.degrees).toFloat)
      outRenderInfo.geographicVertexBuffer.put(1)
    }

    // We need cartesian vertices if the surface's altitude mode is absolute or relative-to-ground.
    if (this.altitudeMode != WorldWind.CLAMP_TO_GROUND) {
      // WorldWind.ABSOLUTE or WorldWind.RELATIVE_TO_GROUND
      val point: Vec4 = this.computeSurfacePoint(dc, lat, lon, if (attr != null) attr.value else DistributionPowerDensity.DEFAULT_VALUE)
      outRenderInfo.cartesianVertexBuffer.put((point.x - this.referencePoint.x).toFloat)
      outRenderInfo.cartesianVertexBuffer.put((point.y - this.referencePoint.y).toFloat)
      outRenderInfo.cartesianVertexBuffer.put((point.z - this.referencePoint.z).toFloat)
    }

    // We need shadow colors if the surface's shadow is enabled.
    if (this.attributes.drawShadow) {
      // Convert the floating point opacity from the range [0, 1] to the unsigned byte range [0, 255].
      val shadowAlpha = (alpha * this.attributes.shadowOpacity + 0.5).toInt
      outRenderInfo.shadowColorBuffer.put(0.toByte)
      outRenderInfo.shadowColorBuffer.put(0.toByte)
      outRenderInfo.shadowColorBuffer.put(0.toByte)
      outRenderInfo.shadowColorBuffer.put(shadowAlpha.toByte)
    }
  }

  /**
   * Высчитываем высоты точки с ее коориднатами
   * Подсчет будет производится с учетом того что прорисовка может находится над уровнем моря
   * @param dc контекст
   * @param lat широта
   * @param lon долгота
   * @param value значение
   * @return Vec4 которая имеет заданые координаты и высчитаую выстоты.
   */
  def computeSurfacePoint(dc: DrawContext, lat: Angle, lon: Angle, value: Double): Vec4 = {
    val offset = this.altitude + this.verticalScale * value

    if (this.altitudeMode == WorldWind.RELATIVE_TO_GROUND) {
      dc.computeTerrainPoint(lat, lon, dc.getVerticalExaggeration * offset)
    } else {
      // WorldWind.ABSOLUTE
      dc.getGlobe.computePointFromPosition(lat, lon, offset)
    }
  }

  def updateSurfaceNormals(outRenderInfo: RenderInfo): Unit = {
    WWMath.computeNormalsForIndexedTriangleStrip(
      outRenderInfo.interiorIndexBuffer,
      outRenderInfo.cartesianVertexBuffer,
      outRenderInfo.cartesianNormalBuffer
    )
  }

}


