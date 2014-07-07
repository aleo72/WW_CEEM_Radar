/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.db.model

import ua.edu.odeku.ceem.mapRadar.db.{CeemTableObject, DB}
import ua.edu.odeku.ceem.mapRadar.exceptions.db.models.GeoNameException
import ua.edu.odeku.ceem.mapRadar.resource.ResourceString
import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram

import scala.slick.driver.H2Driver
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable

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

	val fields = Array(id, name, ascii, alternateNames, lat, lon, featureClass, featureCode, countryCode, translateName)
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

	def tableName: String = this.objects.baseTableRow.tableName

	override def createIfNotExists(existsTables: List[MTable], db: H2Driver.backend.DatabaseDef = DB.database) {
		if (!existsTables.exists(_.name.name == tableName)) {
			db withSession { implicit session =>
				if(PropertyProgram.DEBUG) println(objects.ddl.createStatements.mkString("\n"))
				objects.ddl.create
			}
		}
	}

	def +=(geoName: GeoName) = {
		DB.database withSession {
			implicit session =>
				TableQuery[GeoNames] += geoName
		}
	}

	def ++=(values: Iterable[GeoName]) = {
		DB.database withSession {
			implicit session =>
				TableQuery[GeoNames] ++= values
		}
	}

	def createGeoName(line: String) = {
		val c = line.split(",")

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
				val message = ResourceString.get("exception_GeoName")
				val exceptionMessage = e.getMessage
				throw new GeoNameException(s"$message\n$line\n$exceptionMessage")
		}
	}

	def list(subName: String = null, country: String = null, featureClass: String = null, featureCode: String = null) = {

		var query: Query[GeoNames, GeoNames#TableElementType, Seq] = null

		query = if (country != null) objects.filter(_.countryCode === country) else query

		query = if (featureClass != null) {
			query = if (query == null) objects.filter(_.featureClass === featureClass) else query.filter(_.featureClass === featureClass)

			query = if (featureCode != null) query.filter(_.featureCode === featureCode) else query

			query
		} else query



		query = if (subName != null && !subName.trim.isEmpty) {
			val nameLike = s"%$subName%"
			query.filter(_.name like nameLike) union query.filter(_.translateName like nameLike) union query.filter(_.ascii like nameLike) union query.filter(_.alternateNames like nameLike)
		} else query

		query = if (query == null) GeoNames.objects.filter(_.id >= 0L) else query

		query = query.sortBy(_.alternateNames).sortBy(_.ascii).sortBy(_.name).sortBy(_.translateName)

		DB.database withSession {
			implicit session =>

				if (PropertyProgram.DEBUG) println(query.selectStatement)

				query.list.toArray
		}
	}
}

object Test extends App {
//	DB.database withSession {
//		implicit session =>
//			GeoNames.objects.ddl.drop
//			GeoNames.objects.ddl.create
//
//	}
	for(i <- 1 to 1000) {
		GeoNames += GeoName(i, "test"+ i, i+"test2", i+"test3,test4"+i, 7234.90, 7294.94, "F", "FF", "UA", null)
	}

//	GeoNames += GeoName(1, "test", "test2", "test3,test4", 7234.90, 7294.94, "F", "FF", "UA", null)

	println(GeoNames.list("", "UA", "FF", "FF").mkString(","))
	println(GeoNames.list("st4", "UA", "F", "FF").mkString(","))

	print(GeoNames.list().mkString(","))
}