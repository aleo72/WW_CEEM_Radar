/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.render.airspaces.Airspace
import gov.nasa.worldwind.render.airspaces.editor.AirspaceEditor

/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:05
 */
trait AirspaceFactory {

	def createAirspace(wwd: WorldWindow, fitShapeToViewport: Boolean): Airspace

	def createEditor(airspace: Airspace): AirspaceEditor

}
