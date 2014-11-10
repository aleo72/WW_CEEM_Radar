/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.db

import java.util.ResourceBundle

import ua.edu.odeku.ceem.mapRadar.tools.geoName.models.GeoNames
import ua.edu.odeku.ceem.mapRadar.settings.Settings
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.models._

import scala.slick.driver.H2Driver
import scala.slick.driver.H2Driver.{simple => DatabaseH2}
import scala.slick.jdbc.StaticQuery
import scala.slick.jdbc.meta.MTable

/**
 * Класс инкапсулирует работу с базой данных
 * User: Aleo Bakalov
 * Date: 10.12.13
 * Time: 14:38
 */
object DB {

	object H2 {
		val pathToDB = "./CeemRadarData/database/h2/mapRadar"
		val options = Array("MVCC=true", "DB_CLOSE_ON_EXIT=FALSE")
		val driver = "org.h2.Driver"
		val user = "ceem"
		val password = "db_ceem"

		def option = options.mkString(";")

		def url = s"jdbc:h2:$pathToDB;$option"

		def urlForMemory = s"jdbc:h2:mem;$option"

		private lazy val _db: H2Driver.backend.DatabaseDef = DatabaseH2.Database.forURL(url = url, user = user, password = password, driver = driver)

		lazy val db_memory = DatabaseH2.Database.forURL(url = urlForMemory, user = user, password = password, driver = driver)

		val ceemTables = List(GeoNames)

		def db = {
			var tableList: List[MTable] = null

      _db withSession { implicit session =>
				tableList = MTable.getTables.list
			}

			GeoNames.createIfNotExists(tableList, _db)

      CountryBorders.createIfNotExists(tableList, _db)
      ProvinceBorders.createIfNotExists(tableList, _db)
      Polygons.createIfNotExists(tableList, _db)

			_db
		}
	}

	def database: H2Driver.backend.DatabaseDef = H2.db

	def database_memory = H2.db_memory
}

trait CeemTableObject {

  val tableName: String

  val resourceBundle: ResourceBundle = ResourceBundle.getBundle("strings", Settings.Program.locale)

	def createIfNotExists(existsTables: List[MTable], db: H2Driver.backend.DatabaseDef = DB.database): Unit

  def clear: Unit = DB.database withSession { implicit session=>
    StaticQuery.updateNA(s"truncate table $tableName")
  }

}