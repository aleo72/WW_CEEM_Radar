/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.geoName.models

import gov.nasa.worldwind.geom.LatLon
import ua.edu.odeku.ceem.mapRadar.db.{CeemTableObject, DB}
import ua.edu.odeku.ceem.mapRadar.exceptions.db.models.GeoNameException
import ua.edu.odeku.ceem.mapRadar.utils.CeemUtilsFunctions.stringToBoolean

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.slick.driver.H2Driver
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable
import scala.slick.jdbc.{GetResult, StaticQuery => Q}

/**
 * User: Aleo Bakalov
 * Date: 04.07.2014
 * Time: 12:58
 */

case class GeoName(id: Long,
                   name: String,
                   ascii: String,
                   alternateNames: String,
                   lat: Double,
                   lon: Double,
                   featureClass: String,
                   featureCode: String,
                   countryCode: String,
                   translateName: Option[String] = None) {

  val fields = Array(id, name, ascii, translateName, alternateNames, countryCode, featureClass, featureCode, lat, lon)
}


class GeoNames(tag: Tag) extends Table[GeoName](tag, "GEO_NAMES") {
  /**
   * integer id of record in geonames database
   * Данное поле необходимо для быстрой проверки на нахождения данного названия в таблице.
   */
  def id = column[Long]("ID", O.PrimaryKey)

  /**
   * name of geographical point (utf8) varchar(200)
   */
  def name = column[String]("NAME", O.NotNull)

  /**
   * name of geographical point in plain ascii characters, varchar(200)
   */
  def ascii = column[String]("ASCII_NAME", O.NotNull)

  /**
   * alternatenames, comma separated varchar(5000)
   */
  def alternateNames = column[String]("ALTERNATE_NAMES", O.NotNull)

  /**
   * latitude in decimal degrees (wgs84)
   */
  def lat = column[Double]("LAT", O.NotNull)

  /**
   * longitude in decimal degrees (wgs84)
   */
  def lon = column[Double]("LON", O.NotNull)

  /**
   * see http://www.geonames.org/export/codes.html, char(1)
   * <p/>
   * A country, state, region,...
   * H stream, lake, ...
   * L parks,area, ...
   * P city, village,...
   * R road, railroad
   * S spot, building, farm
   * T mountain,hill,rock,...
   * U undersea
   * V forest,heath,...
   */
  def featureClass = column[String]("FEATURE_CLASS", O.NotNull)

  /**
   * see http://www.geonames.org/export/codes.html, varchar(10)
   */
  def featureCode = column[String]("FEATURE_CODE", O.Nullable)

  /**
   * ISO-3166 2-letter country code, 2 characters
   */
  def countryCode = column[String]("COUNTRY_CODE", O.NotNull)

  def translateName = column[Option[String]]("TRANSLATE", O.Nullable)

  def alternateNamesOfArray = if (alternateNames == null) Array() else alternateNames.toString().split(",")

  override def * = (id, name, ascii, alternateNames, lat, lon, featureClass, featureCode, countryCode, translateName) <>(GeoName.tupled, GeoName.unapply)
}

object GeoNames extends CeemTableObject {

  val countFieldsInGeoName = 10

  val objects = TableQuery[GeoNames]

  val table = GeoNames.objects.baseTableRow

  implicit val getGeoNameResult = GetResult(r => GeoName(r.<<[Long], r.<<[String], r.<<[String], r.<<[String], r.<<[Double], r.<<[Double], r.<<[String], r.<<[String], r.<<[String], r.<<?[String]))

  val tableName: String = this.objects.baseTableRow.tableName

  def geoNameTable = tableName

  def nameColumn = table.name.toString()

  def asiiColumn = table.ascii.toString()

  def translateNameColumn = table.translateName.toString()

  def latColumn = table.lat.toString()

  def lonColumn = table.lon.toString()

  def featureClassColumn = table.featureClass.toString()

  def countryColumn = table.countryCode.toString()

  def featureCodeColumn = table.featureCode.toString()

  override def createIfNotExists(existsTables: List[MTable], db: H2Driver.backend.DatabaseDef = DB.database) {
    if (!existsTables.exists(_.name.name == tableName)) {
      db withSession { implicit session =>
        objects.ddl.create
      }
    }
  }

  def +=(geoName: GeoName) = {
    DB.database withSession { implicit session =>
      TableQuery[GeoNames] += geoName
    }
  }

  def ++=(values: Iterable[GeoName]) = {
    DB.database withSession { implicit session =>
      TableQuery[GeoNames] ++= values
    }
  }

  def -=(geoName: GeoName): Int = {
    DB.database withSession { implicit session =>
      TableQuery[GeoNames].filter(_.id === geoName.id).delete
    }
  }

  def !(geoName: GeoName): Int = {
    DB.database withSession { implicit session =>
      TableQuery[GeoNames].filter(_.id === geoName.id).update(geoName)
    }
  }

  def createGeoName(line: String) = {
    val c = line.split("\t")

    try {
      GeoName(
        id = c(0).toInt,
        name = c(1),
        ascii = c(2),
        alternateNames = c(3),
        lat = c(4).toDouble,
        lon = c(5).toDouble,
        featureClass = c(6),
        featureCode = c(7),
        countryCode = c(8)
      )
    } catch {

      case e: Exception =>
        val message = resourceBundle.getString("exception_GeoName")
        val exceptionMessage = e.getMessage
        throw new GeoNameException(s"$message\n$line\n$exceptionMessage")
    }
  }

  def get(id: Long): GeoName = {
    DB.database withSession { implicit session =>
      TableQuery[GeoNames].filter(_.id === id).first
    }
  }

  /**
   * Метод возращает список GeoName который удовлетворяет заданым параметрам
   * @param subName подстрока которая должна присутствовать в полях Name, translateName, ascii, alternateNames
   * @param country код города
   * @param featureClass класс фичи
   * @param featureCode код фичи
   * @return List[GeoName]
   */
  def list(subName: String = null, country: String = null, featureClass: String = null, featureCode: String = null) = {

    val sqlSelect = s" SELECT * FROM $geoNameTable WHERE 1 = 1"
    val whereConditions = new ArrayBuffer[String]

    if (country) whereConditions += s"$countryColumn = '$country'"
    if (featureClass) {
      whereConditions += s"$featureClassColumn = '$featureClass'"
      if (featureCodeColumn) whereConditions += s"$featureCodeColumn = '$featureCode'"
    }

    val sqlSimple = if (whereConditions.size > 0) sqlSelect + " AND " + whereConditions.mkString(" AND ") else sqlSelect

    val sql = if (subName) {
      val sqlUnionA = new ArrayBuffer[String]
      for (column <- Array(nameColumn, asiiColumn, translateNameColumn)) {
        sqlUnionA += s"$column like '%$subName%''"
      }
      s"$sqlSimple AND (${sqlUnionA.mkString(" OR ")}"
    } else sqlSimple

    DB.database withSession {
      implicit session =>

        Q.queryNA[GeoName](sql).list
    }
  }

  /**
   * Возращает список feature codes
   * @param featureClass класс фич, из которых необходимо выбрать коды фич
   * @return список код фич
   */
  def featureCodes(featureClass: String): List[String] = {
    val selectedColumn = featureCodeColumn
    DB.database withSession { implicit session =>
      val sql = Q.queryNA[String](
        s"""
					SELECT
						$selectedColumn
		      FROM
						$geoNameTable
					WHERE
		        $selectedColumn IS NOT NULL
						AND $featureClassColumn = '$featureClass'
					GROUP BY
		        $selectedColumn
					ORDER BY
						$selectedColumn
				 """)
      sql.list
    }
  }

  def featureClass: List[String] = {
    val selectedColumn = featureClassColumn
    DB.database withSession { implicit session =>
      val sql = Q.queryNA[String](
        s"""
					SELECT
						$selectedColumn
		      FROM
						$geoNameTable
					WHERE
		        $selectedColumn IS NOT NULL
					GROUP BY
		        $selectedColumn
					ORDER BY
						$selectedColumn
				 """)
      sql.list
    }
  }

  def coutres: List[String] = {
    val selectedColumn = countryColumn
    DB.database withSession { implicit session =>
      val sql = Q.queryNA[String](
        s"""
					SELECT
						$selectedColumn
		      FROM
						$geoNameTable
					WHERE
		        $selectedColumn IS NOT NULL
					GROUP BY
		        $selectedColumn
					ORDER BY
						$selectedColumn
				 """)

      sql.list
    }
  }

  object SavingCache {
    private val list = new ArrayBuffer[GeoName]()

    val LIMIT = 500

    def +=(geo: GeoName): Unit = {
      list += geo

      if (list.size >= LIMIT) {
        saveCache()
      }
    }

    def saveCache() {
      GeoNames ++= list
      list.clear()
    }

    def flush(): Unit = saveCache()
  }

}

case class GeoNamesWithNameAndCoordinates(name: String, latlon: LatLon) {

  override def toString = name
}

object GeoNamesWithNameAndCoordinates {

  import ua.edu.odeku.ceem.mapRadar.tools.geoName.models.GeoNames._

  def getSettlements(text: String): List[GeoNamesWithNameAndCoordinates] = {

    val sql = Q.queryNA[(String, Double, Double)](
      s"""
			  SELECT
			    CASE
			      WHEN $translateNameColumn IS NOT NULL
			        THEN $translateNameColumn
			        ELSE NAME
			    END AS NAME,
			    $latColumn,
			  	$lonColumn
			  FROM
			    $geoNameTable
				WHERE
					$featureClassColumn = 'P'
			  	AND (
						UPPER($nameColumn) LIKE '%${text.toUpperCase}%'
							OR
						UPPER($translateNameColumn) LIKE '%${text.toUpperCase}%'
			  	)
			  	ORDER BY
			  				$nameColumn
			""")

    val list = new mutable.MutableList[GeoNamesWithNameAndCoordinates]

    DB.database withSession { implicit session =>
      sql foreach {
        v => list += GeoNamesWithNameAndCoordinates(v._1, LatLon.fromDegrees(v._2, v._3))
      }
    }
    list.toList
  }
}


object Test extends App {
  //	DB.database withSession {
  //		implicit session =>
  //			GeoNames.objects.ddl.drop
  //			GeoNames.objects.ddl.create
  //
  //	}
  //	for (i <- 1 to 1000) {
  //		GeoNames += GeoName(i, "test" + i, i + "test2", i + "test3,test4" + i, 7234.90, 7294.94, "F", "FF", "UA", null)
  //	}

  //	GeoNames += GeoName(1, "test", "test2", "test3,test4", 7234.90, 7294.94, "F", "FF", "UA", null)

  //	println(GeoNames.list("", "UA", "FF", "FF").mkString(","))
  //	println(GeoNames.list("st4", "UA", "F", "FF").mkString(","))
  //
  //	print(GeoNames.list().mkString(","))
}