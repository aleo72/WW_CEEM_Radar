/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import gov.nasa.worldwind.render.airspaces.CappedCylinder
import gov.nasa.worldwind.geom.LatLon

/**
 * Класс для отображения радара в виде изолиний
 *
 * Created by Aleo on 23.04.2014.
 */
class IsolineAirspace extends CappedCylinder with LocationAirspace with RadiusAirspace {

  override def locationCenter_=(latLon: LatLon): Unit = this.setCenter(latLon)

  override def locationCenter: LatLon = this.getCenter

  override def radiusAirspace_=(r: Double): Unit = this.setRadius(r)

  override def radiusAirspace: Double = this.getRadii.array(1)
}
