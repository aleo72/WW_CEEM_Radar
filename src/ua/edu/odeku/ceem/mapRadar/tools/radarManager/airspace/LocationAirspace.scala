/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import gov.nasa.worldwind.geom.LatLon

/**
 * Трейт для того что бы иметь возможность обобщить Airspace по поводу location
 * Created by Aleo on 23.04.2014.
 */
trait LocationAirspace {

	def location: LatLon

	def location_=(latLon: LatLon): Unit
}
