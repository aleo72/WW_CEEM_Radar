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

	override def locationCenter_=(latLon: LatLon): Unit = this.setLocation(latLon)

	override def locationCenter: LatLon = this.getLocation

	override def radiusAirspace_=(r: Double): Unit = this.setRadius(r)

	override def radiusAirspace: Double = this.getRadius

}
