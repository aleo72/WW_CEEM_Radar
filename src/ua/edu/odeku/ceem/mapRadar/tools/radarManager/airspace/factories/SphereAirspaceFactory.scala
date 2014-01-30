/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.factories

import gov.nasa.worldwind.render.airspaces.{SphereAirspace, Airspace}
import gov.nasa.worldwind.render.airspaces.editor.{SphereAirspaceEditor, AirspaceEditor}
import gov.nasa.worldwind.avlist.AVKey
import gov.nasa.worldwind.geom.{LatLon, Position}
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwindx.examples.util.ShapeUtils
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace._
import ua.edu.odeku.ceem.mapRadar.models.radar.Radar


/**
 *
 * Простоя реализация AirspaceFactory
 *
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:05
 */
class SphereAirspaceFactory(radar: Radar, wwd: WorldWindow, fitShapeToViewport: Boolean) extends AirspaceFactory(radar, wwd, fitShapeToViewport) {

	final val DEFAULT_SHAPE_SIZE_METERS: Double = 200000.0

	def createAirspace(wwd: WorldWindow, fitShapeToViewport: Boolean): Airspace = {
		val sphere: SphereAirspace = new SphereAirspace
		sphere.setAttributes(getDefaultAttributes)
		sphere.setValue(AVKey.DISPLAY_NAME, getNextName(this.toString))
		sphere.setAltitude(0.0)
		sphere.setTerrainConforming(true)
		this.initializeSphere(wwd, sphere, fitShapeToViewport)
		sphere
	}

	def createEditor(airspace: Airspace): AirspaceEditor = {
		val editor: SphereAirspaceEditor = new SphereAirspaceEditor(new RadarAirspaceControlPointRenderer)
		editor.setSphere(airspace.asInstanceOf[SphereAirspace])
		setEditorAttributes(editor)
		editor
	}

	def initializeSphere(wwd: WorldWindow, sphere: SphereAirspace, fitShapeToViewport: Boolean) {
		val position: Position = ShapeUtils.getNewShapePosition(wwd)
		val sizeInMeters: Double = if (fitShapeToViewport) ShapeUtils.getViewportScaleFactor(wwd) else radar.radius
		sphere.setLocation(new LatLon(position))
		sphere.setRadius(sizeInMeters)
	}

	override def toString = {
		"Airspace "
	}
}

