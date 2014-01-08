/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import gov.nasa.worldwind.render.airspaces.{SphereAirspace, Airspace}
import gov.nasa.worldwind.render.airspaces.editor.{SphereAirspaceEditor, AirspaceEditor}
import gov.nasa.worldwind.avlist.AVKey
import gov.nasa.worldwind.geom.{LatLon, Position}
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwindx.examples.util.ShapeUtils


/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:05
 */
class SphereAirspaceFactory extends AirspaceFactory {

	val DEFAULT_SHAPE_SIZE_METERS: Double = 200000.0

	def createAirspace(wwd: WorldWindow, fitShapeToViewport: Boolean) = {
		val sphere: SphereAirspace = new SphereAirspace
		sphere.setAttributes(getDefaultAttributes)
		sphere.setValue(AVKey.DISPLAY_NAME, getNextName(this.toString))
		sphere.setAltitude(0.0)
		sphere.setTerrainConforming(true)
		this.initializeSphere(wwd, sphere, fitShapeToViewport)
		sphere
	}

	def createEditor(airspace: Airspace): AirspaceEditor = {
		val editor: SphereAirspaceEditor = new SphereAirspaceEditor
		editor.setSphere(airspace.asInstanceOf[SphereAirspace])
		setEditorAttributes(editor)
		editor
	}

	def initializeSphere(wwd: WorldWindow, sphere: SphereAirspace, fitShapeToViewport: Boolean) {
		val position: Position = ShapeUtils.getNewShapePosition(wwd)
		val sizeInMeters: Double = if (fitShapeToViewport) ShapeUtils.getViewportScaleFactor(wwd) else DEFAULT_SHAPE_SIZE_METERS
		sphere.setLocation(new LatLon(position))
		sphere.setRadius(sizeInMeters / 2.0)
	}

	override def toString = {
		"Sphere"
	}
}
