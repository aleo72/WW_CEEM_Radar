/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager

import java.lang.{StringBuilder, String}
import gov.nasa.worldwind.render.airspaces.{BasicAirspaceAttributes, AirspaceAttributes}
import gov.nasa.worldwind.render.Material
import java.awt.Color
import gov.nasa.worldwind.render.airspaces.editor.AirspaceEditor

/**
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

	var nextEntryNumber: Long = 1

	def getNextName(base: String): String = {
		val sb: StringBuilder = new StringBuilder
		sb.append(base)
		sb.append(nextEntryNumber)
		nextEntryNumber += 1
		sb.toString
	}

	def getDefaultAttributes: AirspaceAttributes = {
		val attributes: AirspaceAttributes = new BasicAirspaceAttributes
		attributes.setMaterial(new Material(Color.BLACK, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.BLACK, 0.0f))
		attributes.setOutlineMaterial(Material.DARK_GRAY)
		attributes.setDrawOutline(true)
		attributes.setOpacity(0.95)
		attributes.setOutlineOpacity(.95)
		attributes.setOutlineWidth(2)
		return attributes
	}

	def setEditorAttributes(editor: AirspaceEditor) {
		editor.setUseRubberBand(true)
		editor.setKeepControlPointsAboveTerrain(true)
	}
}
