/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import gov.nasa.worldwind.render.airspaces.SphereAirspace
import gov.nasa.worldwind.geom.LatLon

/**
 * Класс для отображения радара в нормальном виде
 *
 * Created by Aleo on 23.04.2014.
 */
class RadarAirspace extends SphereAirspace with LocationAirspace with RadiusAirspace {

	override def location_=(latLon: LatLon): Unit = this.setLocation(latLon)

	override def location: LatLon = this.getLocation

	override def radius_=(r: Double): Unit = this.setRadius(r)

	override def radius: Double = this.getRadius

}
