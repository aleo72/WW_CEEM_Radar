/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.airspace

import gov.nasa.worldwind.render.airspaces.CappedCylinder
import gov.nasa.worldwind.geom.LatLon
import ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.entry.AirspaceEntry
import ua.edu.odeku.ceem.mapRadar.tools.radar.models.Radar

/**
 * Класс для отображения радара в виде изолиний
 *
 * Created by Aleo on 23.04.2014.
 */
class IsolineAirspace extends CappedCylinder with LocationAirspace with RadiusAirspace {

  private var realRadius = 0d

  override def locationCenter_=(latLon: LatLon): Unit = this.setCenter(latLon)

  override def locationCenter: LatLon = this.getCenter

  override def radiusAirspace_=(r: Double): Unit = {
    realRadius = r
//    this.setRadius(IsolineAirspace.calculationSectorRadius(realRadius, this.getAltitudes.apply(0)))
    this.setRadii(100000d, Radar.radiusOnElevation(realRadius, this.getAltitudes.apply(0)))
  }

  override def radiusAirspace: Double = this.getRadii.array(1)

}
