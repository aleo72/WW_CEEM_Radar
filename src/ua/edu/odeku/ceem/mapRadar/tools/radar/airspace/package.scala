/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar

import java.lang.{StringBuilder, String}
import gov.nasa.worldwind.render.airspaces.{Airspace, SphereAirspace, BasicAirspaceAttributes, AirspaceAttributes}
import gov.nasa.worldwind.render.Material
import java.awt.Color
import gov.nasa.worldwind.render.airspaces.editor.AirspaceEditor
import gov.nasa.worldwind.geom.{Angle, Position, Vec4, LatLon}
import gov.nasa.worldwind.SceneController
import gov.nasa.worldwind.globes.Globe
import gov.nasa.worldwind.view.orbit.BasicOrbitView
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame

/**
 * Класс пакет
 * Created by Aleo on 08.01.14.
 */
package object airspace {
	val AIRSPACE_LAYER_NAME: String = "Airspace Shapes"
	val CLEAR_SELECTION: String = "AirspaceBuilder.ClearSelection"
	val SIZE_NEW_SHAPES_TO_VIEWPORT: String = "AirspaceBuilder.SizeNewShapesToViewport"
	val ENABLE_EDIT: String = "AirspaceBuilder.EnableEdit"
	val OPEN: String = "AirspaceBuilder.Open"
	val OPEN_URL: String = "AirspaceBuilder.OpenUrl"
	val OPEN_DEMO_AIRSPACES: String = "AirspaceBuilder.OpenDemoAirspaces"
	val NEW_AIRSPACE: String = "AirspaceBuilder.NewAirspace"
	val REMOVE_SELECTED: String = "AirspaceBuilder.RemoveSelected"
	val SAVE: String = "AirspaceBuilder.Save"
	val SELECTION_CHANGED: String = "AirspaceBuilder.SelectionChanged"
	val GO_TO_SELECTION_AIRSPACE: String = "AirspaceBuilder.GoToSelectionAirspace"

	var nextEntryNumber: Long = 1

	def getNextName(base: String): String = {
		val sb: StringBuilder = new StringBuilder
		sb.append(base)
		sb.append(nextEntryNumber)
		nextEntryNumber += 1
		sb.toString
	}

	def defaultAttributes: AirspaceAttributes = {
		val attributes: AirspaceAttributes = new BasicAirspaceAttributes
		attributes.setMaterial(new Material(Color.BLACK, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.BLACK, 0.0f))
		attributes.setOutlineMaterial(Material.DARK_GRAY)
		attributes.setDrawOutline(false)
		attributes.setOpacity(0.95)
		attributes.setOutlineOpacity(.95)
		attributes.setOutlineWidth(2)
		attributes
	}

	def setEditorAttributes(editor: AirspaceEditor) {
		editor.setUseRubberBand(true)
		editor.setKeepControlPointsAboveTerrain(true)
	}

	def selectionAndIntersectionAttributes: AirspaceAttributes = {
		val attributes: AirspaceAttributes = new BasicAirspaceAttributes
		attributes.setMaterial(Material.WHITE)
		attributes.setOpacity(0.8)
		attributes
	}

	def selectionAttributes = {
		val attributes: AirspaceAttributes = new BasicAirspaceAttributes
		attributes.setMaterial(Material.GREEN)
		attributes.setOutlineMaterial(Material.BLACK)
		attributes.setDrawOutline(true)
		attributes.setOpacity(0.8)
		attributes.setOutlineOpacity(0.8)
		attributes.setOutlineWidth(2)
    attributes.setDrawInterior(true)
		attributes
	}

	def intersectionAttributes = {
		val attributes: AirspaceAttributes = new BasicAirspaceAttributes
		attributes.setMaterial(Material.WHITE)
		attributes.setOpacity(0.8)
		attributes
	}

	def areShapesIntersecting(a1: Airspace, a2: Airspace): Boolean = {
		a1 match {
			case s1: CeemRadarAirspace if a2.isInstanceOf[CeemRadarAirspace] =>
				val s2: CeemRadarAirspace = a2.asInstanceOf[CeemRadarAirspace]
				val location1: LatLon = s1.location
				val location2: LatLon = s2.location
				val altitude1: Double = s1.getAltitudes()(0)
				val altitude2: Double = s2.getAltitudes()(0)
				val terrainConforming1: Boolean = s1.isTerrainConforming()(0)
				val terrainConforming2: Boolean = s2.isTerrainConforming()(0)
				val p1: Vec4 = if (terrainConforming1) getSurfacePoint(location1, altitude1) else getPoint(location1, altitude1)
				val p2: Vec4 = if (terrainConforming2) getSurfacePoint(location2, altitude2) else getPoint(location2, altitude2)
				val r1: Double = s1.radius
				val r2: Double = s2.radius
				val d: Double = p1.distanceTo3(p2)
				d <= (r1 + r2)
		}

	}

	def getSurfacePoint(latlon: LatLon, elevation: Double): Vec4 = {
		var point: Vec4 = null
		val sc: SceneController = AppCeemRadarFrame.wwd.getSceneController
		val globe: Globe = AppCeemRadarFrame.wwd.getModel.getGlobe
		if (sc.getTerrain != null) {
			point = sc.getTerrain.getSurfacePoint(latlon.getLatitude, latlon.getLongitude, elevation * sc.getVerticalExaggeration)
		}
		if (point == null) {
			val e: Double = globe.getElevation(latlon.getLatitude, latlon.getLongitude)
			point = globe.computePointFromPosition(latlon.getLatitude, latlon.getLongitude, (e + elevation) * sc.getVerticalExaggeration)
		}
		point
	}

	def getPoint(latlon: LatLon, elevation: Double): Vec4 = {
		val sc: SceneController = AppCeemRadarFrame.wwd.getSceneController
		val globe: Globe = AppCeemRadarFrame.wwd.getModel.getGlobe
		val e: Double = globe.getElevation(latlon.getLatitude, latlon.getLongitude)
		globe.computePointFromPosition(latlon.getLatitude, latlon.getLongitude, (e + elevation) * sc.getVerticalExaggeration)
	}

	def zoomTo(latLon: LatLon, heading: Angle, pitch: Angle, zoom: Double) {
		val view: BasicOrbitView = AppCeemRadarFrame.wwd.getView.asInstanceOf[BasicOrbitView]
		view.stopMovement()
		view.addPanToAnimator(new Position(latLon, 0), heading, pitch, zoom, true)
	}
}
