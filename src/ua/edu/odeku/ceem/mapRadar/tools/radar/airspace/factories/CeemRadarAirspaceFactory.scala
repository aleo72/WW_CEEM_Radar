/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.factories

import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.render.airspaces.SphereAirspace
import ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.{CeemRadarAirspace, CeemRadarAirspaceEditor, IsolineAirspace, RadarAirspace}
import ua.edu.odeku.ceem.mapRadar.tools.radar.models.Radar

/**
 * Created by Aleo on 21.04.2014.
 */
class CeemRadarAirspaceFactory(val radar: Radar, wwd: WorldWindow, fitShapeToViewport: Boolean){

	private val radarAirspaceFactory = new RadarAirspaceFactory(radar, wwd, fitShapeToViewport)
	private val isolinesAirspaceFactory = new IsolineAirspaceFactory(radar, wwd, fitShapeToViewport)

	radar.latLon = radarAirspaceFactory.airspace.asInstanceOf[SphereAirspace].getLocation

	val airspace = new CeemRadarAirspace(radar, radarAirspaceFactory.airspace.asInstanceOf[RadarAirspace], radarAirspaceFactory.editor, isolinesAirspaceFactory.airspace.asInstanceOf[IsolineAirspace], isolinesAirspaceFactory.editor)
	val editor = new CeemRadarAirspaceEditor(airspace)

}
