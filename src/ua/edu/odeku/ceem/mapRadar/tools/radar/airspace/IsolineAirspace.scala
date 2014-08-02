/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.airspace

import gov.nasa.worldwind.render.airspaces.CappedCylinder
import gov.nasa.worldwind.geom.LatLon
import ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.entry.AirspaceEntry

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
    this.setRadii(100000d, IsolineAirspace.calculationSectorRadius(realRadius, this.getAltitudes.apply(0)))
  }

  override def radiusAirspace: Double = this.getRadii.array(1)

}

object IsolineAirspace {

  /**
   * Метод высчитывает радус изолиний на нужной высоте
   *
   * @param realRadius - реальный радиус который имеет сфера
   * @param altitude - высота для которой высчитывается радиус отображения
   * @return - радиус плоскости на данной высоте сферы
   */
  def calculationSectorRadius(realRadius: Double, altitude: Double): Double = {
    Math.sqrt(realRadius * realRadius - altitude * altitude)
  }
}
