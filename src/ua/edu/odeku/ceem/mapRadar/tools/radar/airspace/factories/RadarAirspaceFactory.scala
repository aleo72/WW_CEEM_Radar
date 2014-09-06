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
class RadarAirspaceFactory(radar: Radar, wwd: WorldWindow, fitShapeToViewport: Boolean) extends AirspaceFactory(radar, wwd, fitShapeToViewport) {

	final val DEFAULT_SHAPE_SIZE_METERS: Double = 200000.0

	def createAirspace(wwd: WorldWindow, fitShapeToViewport: Boolean): Airspace = {
		val radarAirspace = new RadarAirspace
		radarAirspace.setAttributes(defaultAttributes)
		radarAirspace.setValue(AVKey.DISPLAY_NAME, this.toString + RadarAirspaceFactory.countEntry)
		radarAirspace.setAltitude(0.0)
		radarAirspace.setTerrainConforming(true)
		this.initializeSphere(wwd, radarAirspace, fitShapeToViewport)
		radarAirspace
	}

	def createEditor(airspace: Airspace): AirspaceEditor = {
		val editor: RadarAirspaceEditor = new RadarAirspaceEditor(new RadarAirspaceControlPointRenderer)
		editor.setSphere(airspace.asInstanceOf[RadarAirspace])
		setEditorAttributes(editor)
		editor
	}

	private def initializeSphere(wwd: WorldWindow, radarAirspace: LocationAirspace with RadiusAirspace, fitShapeToViewport: Boolean) {
		val position: Position = ShapeUtils.getNewShapePosition(wwd)
		val sizeInMeters: Double = if (fitShapeToViewport) ShapeUtils.getViewportScaleFactor(wwd) else radar.radius
		radarAirspace.locationCenter = new LatLon(position)
		radarAirspace.radiusAirspace = sizeInMeters
	}

	override def toString = {
		"Radar "
	}
}

object RadarAirspaceFactory {

	private var _countEntry = 0

	def countEntry = {
		_countEntry += 1
		_countEntry
	}

}