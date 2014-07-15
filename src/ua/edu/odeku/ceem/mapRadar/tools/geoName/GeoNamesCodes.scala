/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.geoName

/**
 * Case класс который моделирует код фичи
 * @param featureCode кодовое имя фичи
 * @param name  название фичи
 * @param description описание
 */
case class GeoNamesFeatureCode(featureCode: String, name: String, description: Option[String])

/**
 * Перечисление в котором хранятся данные о классе фич GeoNames
 *
 * Created by ABakalov on 15.07.2014.
 */
object GeoNamesFeatureClasses extends Enumeration {

  /**
   * Case класс который моделирует класс фич
   * @param featureClass кодовое имя класса фич
   * @param name имя класс (что оно обозначает)
   * @param featureCodes перечень кодов фич, которые относятся к этому классу
   */
  case class GeoNamesClass(featureClass: Character, name: String, featureCodes: Array[GeoNamesFeatureCode]) extends Val(featureClass.toString)

  val A = GeoNamesClass('A', "country, state, region,...", GeoNamesFeatureCodesFactory.A)
  val P = GeoNamesClass('P', "city, village,...", GeoNamesFeatureCodesFactory.P)

  implicit def valToGeoNamesClass(value: Value): GeoNamesClass = value.asInstanceOf[GeoNamesClass]
}

object GeoNamesFeatureCodesFactory {

  val A = Array(
    GeoNamesFeatureCode("ADM1", "first-order administrative division", Some("a primary administrative division of a country, such as a state in the United States"))
    ,GeoNamesFeatureCode("ADM1H", "historical first-order administrative division", Some("a former first-order administrative division"))
    ,GeoNamesFeatureCode("ADM2", "second-order administrative division", Some("a subdivision of a first-order administrative division"))
    ,GeoNamesFeatureCode("ADM2H", "historical second-order administrative division", Some("a former second-order administrative division"))
    ,GeoNamesFeatureCode("ADM3", "third-order administrative division", Some("a subdivision of a second-order administrative division"))
    ,GeoNamesFeatureCode("ADM3H", "historical third-order administrative division", Some("a former third-order administrative division"))
    ,GeoNamesFeatureCode("ADM4", "third-order administrative division", Some("a subdivision of a third-order administrative division"))
    ,GeoNamesFeatureCode("ADM4H", "historical third-order administrative division", Some("a former fourth-order administrative division"))
    ,GeoNamesFeatureCode("ADM5", "fifth-order administrative division", Some("a subdivision of a fourth-order administrative division"))
    ,GeoNamesFeatureCode("ADMD", "administrative division", Some("an administrative division of a country, undifferentiated as to administrative level"))
    ,GeoNamesFeatureCode("ADMDH", "historical administrative division", Some("a former administrative division of a political entity, undifferentiated as to administrative level"))
    ,GeoNamesFeatureCode("LTER", "leased area", Some("a tract of land leased to another country, usually for military installations"))
    ,GeoNamesFeatureCode("PCL", "political entity", None)
    ,GeoNamesFeatureCode("PCLD", "dependent political entity", None)
    ,GeoNamesFeatureCode("PCLF", "freely associated state", None)
    ,GeoNamesFeatureCode("PCLH", "historical political entity", Some("a former political entity"))
    ,GeoNamesFeatureCode("PCLI", "independent political entity", None)
    ,GeoNamesFeatureCode("PCLIX", "section of independent political entity", None)
    ,GeoNamesFeatureCode("PCLS", "semi-independent political entity", None)
    ,GeoNamesFeatureCode("PRSH", "parish", Some("an ecclesiastical district"))
    ,GeoNamesFeatureCode("TERR", "territory", None)
    ,GeoNamesFeatureCode("ZN", "zone", None)
    ,GeoNamesFeatureCode("ZNB", "buffer zone", Some("a zone recognized as a buffer between two nations in which military presence is minimal or absent"))
  )

  val P = Array(
    GeoNamesFeatureCode("PPL", "populated place", Some("a city, town, village, or other agglomeration of buildings where people live and work"))
    ,GeoNamesFeatureCode("PPLA", "seat of a first-order administrative division", Some("seat of a first-order administrative division (PPLC takes precedence over PPLA)"))
  )

  val R = Array(

  )
}
