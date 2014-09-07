/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.factories

import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.avlist.AVKey
import gov.nasa.worldwind.geom.{LatLon, Position}
import gov.nasa.worldwind.render.airspaces.Airspace
import gov.nasa.worldwind.render.airspaces.editor.AirspaceEditor
import gov.nasa.worldwindx.examples.util.ShapeUtils
import ua.edu.odeku.ceem.mapRadar.tools.radar.airspace._
import ua.edu.odeku.ceem.mapRadar.tools.radar.models.Radar

/**
 *
 * Created by Aleo on 23.04.2014.
 */
class IsolineAirspaceFactory(radar: Radar, wwd: WorldWindow, fitShapeToViewport: Boolean) extends AirspaceFactory(radar, wwd, fitShapeToViewport) {

	final val DEFAULT_SHAPE_SIZE_METERS: Double = 200000.0

	def createAirspace(wwd: WorldWindow, fitShapeToViewport: Boolean): Airspace = {
		val isoLineAirspace = new IsolineAirspace
		isoLineAirspace.setAttributes(this.initAttribute(isoLineAirspace))
		isoLineAirspace.setValue(AVKey.DISPLAY_NAME, this.toString + IsolineAirspaceFactory.countEntry)
		isoLineAirspace.setAltitudes(100000.0, 500000.0)
		isoLineAirspace.setTerrainConforming(false)
		this.initializeSphere(wwd, isoLineAirspace, fitShapeToViewport)
		isoLineAirspace
	}

	def createEditor(airspace: Airspace): AirspaceEditor = null

	private def initializeSphere(wwd: WorldWindow, airspace: LocationAirspace with RadiusAirspace, fitShapeToViewport: Boolean) {
		val position: Position = ShapeUtils.getNewShapePosition(wwd)
		val sizeInMeters: Double = if (fitShapeToViewport) ShapeUtils.getViewportScaleFactor(wwd) else radar.radius
		airspace.locationCenter = new LatLon(position)
		airspace.radiusAirspace = sizeInMeters
	}

	override def toString = {
		"Isoline "
	}

	private def initAttribute(a: IsolineAirspace) = {
    defaultIsolineAttributes
	}
}

object IsolineAirspaceFactory {

	private var _countEntry = 0

	def countEntry = {
		_countEntry += 1
		_countEntry
	}

}