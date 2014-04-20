/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.layers.IsolineAirspace

import gov.nasa.worldwind.render.airspaces._
import gov.nasa.worldwind.globes.Globe
import gov.nasa.worldwind.geom.{LatLon, Position, Vec4, Extent}
import java.util
import gov.nasa.worldwind.render.DrawContext
import gov.nasa.worldwind.util.Logging
import scala.beans.BeanProperty

/**
 * Airspace isoline
 * Created by Aleo on 13.04.2014.
 */
class IsolineAirspace(var _location: LatLon = IsolineAirspace.DEFAULT_LOCATION, var _radius: Double = IsolineAirspace.DEFAULT_RADIUS, attributes: AirspaceAttributes = new BasicAirspaceAttributes()) extends AbstractAirspace(attributes) {

	var subdivisions = IsolineAirspace.DEFAULT_SUBDIVISIONS
	this.makeDefaultDetailLevels()

	def this(attr: AirspaceAttributes) {
		this(IsolineAirspace.DEFAULT_LOCATION, IsolineAirspace.DEFAULT_RADIUS, attr)
	}

	private def makeDefaultDetailLevels() = {
		val levels = new util.ArrayList[DetailLevel]()
		val ramp = ScreenSizeDetailLevel.computeLinearScreenSizeRamp(7, 10.0, 600.0)

		var level: DetailLevel = null
		level = new ScreenSizeDetailLevel(ramp(0), "Detail-Level-0")
		level.setValue(AbstractAirspace.SUBDIVISIONS, 6)
		level.setValue(AbstractAirspace.DISABLE_TERRAIN_CONFORMANCE, false)
		levels.add(level)

		level = new ScreenSizeDetailLevel(ramp(1), "Detail-Level-1")
		level.setValue(AbstractAirspace.SUBDIVISIONS, 5)
		level.setValue(AbstractAirspace.DISABLE_TERRAIN_CONFORMANCE, false)
		levels.add(level)

		level = new ScreenSizeDetailLevel(ramp(2), "Detail-Level-2")
		level.setValue(AbstractAirspace.SUBDIVISIONS, 4)
		level.setValue(AbstractAirspace.DISABLE_TERRAIN_CONFORMANCE, false)
		levels.add(level)

		level = new ScreenSizeDetailLevel(ramp(3), "Detail-Level-3")
		level.setValue(AbstractAirspace.SUBDIVISIONS, 3)
		level.setValue(AbstractAirspace.DISABLE_TERRAIN_CONFORMANCE, false)
		levels.add(level)

		level = new ScreenSizeDetailLevel(ramp(4), "Detail-Level-4")
		level.setValue(AbstractAirspace.SUBDIVISIONS, 2)
		level.setValue(AbstractAirspace.DISABLE_TERRAIN_CONFORMANCE, false)
		levels.add(level)

		level = new ScreenSizeDetailLevel(ramp(5), "Detail-Level-5")
		level.setValue(AbstractAirspace.SUBDIVISIONS, 1)
		level.setValue(AbstractAirspace.DISABLE_TERRAIN_CONFORMANCE, false)
		levels.add(level)

		level = new ScreenSizeDetailLevel(ramp(6), "Detail-Level-6")
		level.setValue(AbstractAirspace.SUBDIVISIONS, 0)
		level.setValue(AbstractAirspace.DISABLE_TERRAIN_CONFORMANCE, false)
		levels.add(level)

		this.setDetailLevels(levels)
	}

	private def validateParameter() {
		if (_location == null) {
			val message: String = Logging.getMessage("nullValue.LocationIsNull")
			Logging.logger.severe(message)
			throw new IllegalArgumentException(message)
		}


		if (_radius < 0.0) {
			val message: String = Logging.getMessage("generic.ArgumentOutOfRange", "radius < 0")
			Logging.logger.severe(message)
			throw new IllegalArgumentException(message)
		}
	}

	@BeanProperty
	def location = _location

	def location_=(location: LatLon): Unit = {
		if (location == null) {
			val message: String = Logging.getMessage("nullValue.LocationIsNull")
			Logging.logger.severe(message)
			throw new IllegalArgumentException(message)
		}
		this._location = location
		this.setExtentOutOfDate()
	}

	def radius = _radius

	def radius_=(rad: Double): Unit = {
		if (radius < 0.0) {
			val message: String = Logging.getMessage("generic.ArgumentOutOfRange", "radius < 0")
			Logging.logger.severe(message)
			throw new IllegalArgumentException(message)
		}
		this._radius = rad
		this.setExtentOutOfDate()
	}

	override def getReferencePosition: Position = {
		val altitudes = this.getAltitudes
		new Position(this.location, altitudes(0))
	}

	protected override def doRenderGeometry(dc: DrawContext, drawStyle: String): Unit = ???

	protected override def computeMinimalGeometry(globe: Globe, verticalExaggeration: Double): util.List[Vec4] = null

	protected override def computeExtent(globe: Globe, verticalExaggeration: Double): Extent = ???
}

object IsolineAirspace {

	val DEFAULT_SUBDIVISIONS = 3
	val DEFAULT_LOCATION = LatLon.ZERO
	val DEFAULT_RADIUS = 1.0
}
