/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.geoName.layer

import java.awt.{Color, Font}

import gov.nasa.worldwind.geom.Sector
import ua.edu.odeku.ceem.mapRadar.tools.geoName.GeoNamesFeatureClasses.GeoNamesClass
import ua.edu.odeku.ceem.mapRadar.tools.geoName.GeoNamesFeatureCode
import ua.edu.odeku.ceem.mapRadar.tools.geoName.layer.GeoNameLayer._

/**
 * Created by ABakalov on 15.07.2014.
 */
object GeoNameLayerFactory {

  def createLayer(country: String, featureClass: GeoNamesClass, featureCodes: List[GeoNamesFeatureCode], font: Font, color: Color, minDisplayDistance: Double, maxDisplayDistance: Double) = {
    val geoNamesSet = new GeoNamesSet

    val names = for( code <- featureCodes) yield {
      val name = new GeoNames(country, featureClass.featureClass.toString, code.featureCode, Sector.FULL_SPHERE, GRID_8x16, font)
      name.minDisplayDistance = minDisplayDistance
      name.maxDisplayDistance = maxDisplayDistance

      name
    }
    geoNamesSet ++= names

    new GeoNameLayer(geoNamesSet)
  }

}
