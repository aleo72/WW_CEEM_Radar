/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.factories

import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.render.airspaces.Airspace
import gov.nasa.worldwind.render.airspaces.editor.AirspaceEditor
import ua.edu.odeku.ceem.mapRadar.tools.radar.models.Radar

/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:05
 */
abstract class AirspaceFactory(val radar: Radar, val wwd: WorldWindow, val fitShapeToViewport: Boolean) {

	val airspace: Airspace = createAirspace(wwd, fitShapeToViewport)
	val editor: AirspaceEditor = createEditor(airspace)

	protected def createAirspace(wwd: WorldWindow, fitShapeToViewport: Boolean): Airspace

	protected def createEditor(airspace: Airspace): AirspaceEditor

}
