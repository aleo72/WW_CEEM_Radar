/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.factories

import ua.edu.odeku.ceem.mapRadar.models.radar.Radar
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.render.airspaces.Airspace
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace._
import gov.nasa.worldwind.avlist.AVKey
import gov.nasa.worldwind.render.airspaces.editor.AirspaceEditor
import gov.nasa.worldwind.geom.{LatLon, Position}
import gov.nasa.worldwindx.examples.util.ShapeUtils

/**
 *
 * Created by Aleo on 23.04.2014.
 */
class RadarAirspaceFactory(radar: Radar, wwd: WorldWindow, fitShapeToViewport: Boolean) extends AirspaceFactory(radar, wwd, fitShapeToViewport) {

	final val DEFAULT_SHAPE_SIZE_METERS: Double = 200000.0

	private var countRadar = 1

	def createAirspace(wwd: WorldWindow, fitShapeToViewport: Boolean): Airspace = {
		val radarAirspace = new RadarAirspace
		radarAirspace.setAttributes(getDefaultAttributes)
		radarAirspace.setValue(AVKey.DISPLAY_NAME, this.toString + countRadar)
		countRadar += 1
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
		radarAirspace.location = new LatLon(position)
		radarAirspace.radius = sizeInMeters
	}

	override def toString = {
		"Radar "
	}
}
