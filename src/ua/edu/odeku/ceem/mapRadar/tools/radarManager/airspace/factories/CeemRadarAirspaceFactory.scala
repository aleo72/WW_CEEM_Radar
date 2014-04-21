/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.factories

import ua.edu.odeku.ceem.mapRadar.models.radar.Radar
import gov.nasa.worldwind.WorldWindow
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.CeemRadarAirspace

/**
 * Created by Aleo on 21.04.2014.
 */
class CeemRadarAirspaceFactory(radar: Radar, wwd: WorldWindow, fitShapeToViewport: Boolean){

	private val radarAirspaceFactory = new SphereAirspaceFactory(radar, wwd, fitShapeToViewport)
	private val isolinesAirspaceFactory = new SphereAirspaceFactory(radar, wwd, fitShapeToViewport)

	val airspace = new CeemRadarAirspace(radar, radarAirspaceFactory.airspace, radarAirspaceFactory.editor, isolinesAirspaceFactory.airspace, isolinesAirspaceFactory.editor)

}
