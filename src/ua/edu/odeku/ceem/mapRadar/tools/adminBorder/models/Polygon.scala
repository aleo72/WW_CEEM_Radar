/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.models

import ua.edu.odeku.ceem.mapRadar.db.{CeemTableObject, DB}

import scala.slick.driver.H2Driver
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.StaticQuery
import scala.slick.jdbc.meta.MTable
import scala.slick.lifted.ProvenShape

/**
 * Модель класу який описує
 * Created by aleo on 17.08.14.
 */
case class Polygon(id: Option[Long], listCoordinates: String, countryBorder: Option[Long], provinceBorder: Option[Long])

class Polygons(tag: Tag) extends Table[Polygon](tag, "POLYGONS") {

  def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)

  def listCoordinates = column[String]("LIST_COORDINATES", O.NotNull)

  def countryBorder = column[Option[Long]]("COUNTRY_BORDER", O.Nullable)

  def provinceBorder = column[Option[Long]]("PROVINCE_BORDER", O.Nullable)

  def countryBorderFK = foreignKey("COUNTRY_BORDER_FK", countryBorder, CountryBorders.objects)(_.id)

  def provinceBorderFK = foreignKey("PROVINCE_BORDER_FK", provinceBorder, ProvinceBorders.objects)(_.id)

  def countryBorderJoin = CountryBorders.objects.filter(_.id === countryBorder)

  def provinceBorderJoin = ProvinceBorders.objects.filter(_.id === provinceBorder)

  override def * : ProvenShape[Polygon] = (id, listCoordinates, countryBorder, provinceBorder) <>(Polygon.tupled, Polygon.unapply)
}

object Polygons extends CeemTableObject {

  val objects = TableQuery[Polygons]

  val table = Polygons.objects.baseTableRow

  val tableName = table.tableName

  override def createIfNotExists(existsTables: List[MTable], db: H2Driver.backend.DatabaseDef): Unit = {
    if (!existsTables.exists(_.name.name == tableName)) {
      db withSession { implicit session =>
        objects.ddl.create
      }
    }
  }

  def +=(polygon: Polygon) = DB.database withSession { implicit session => objects += polygon}

  def ++=(polygons: Iterable[Polygon]) = DB.database withSession { implicit session => objects ++= polygons}

  def <=(polygon: Polygon) = {
    DB.database withSession { implicit session =>
      val v = objects returning objects.map(_.id) += polygon
      Polygon(v, polygon.listCoordinates, polygon.countryBorder, polygon.provinceBorder)
    }
  }


  def visibleCountryPolygons = {
    val sql =
      s"""
        Select
          ${table.listCoordinates},
          ${CountryBorders.table.name}
        From $tableName
          inner join ${CountryBorders.tableName} on ${table.countryBorder} = ${CountryBorders.table.id}
        Where
          ${CountryBorders.table.visible} = true
       """.stripMargin
    val result = DB.database withSession { implicit sesstion =>
      StaticQuery.queryNA[(String, String)](sql).list
    }
    listCoordinatesToPupleDouble(result)
  }

  def visibleProvincesPolygons = {
    val sql =
      s"""
        Select
          ${table.listCoordinates},
          ${ProvinceBorders.table.name1}
        From $tableName
          inner join ${ProvinceBorders.tableName} on ${table.provinceBorder} = ${ProvinceBorders.table.id}
        Where
          ${ProvinceBorders.table.visible}  = true
       """.stripMargin
    val result: List[(String, String)] = DB.database withSession { implicit sesstion =>
      StaticQuery.queryNA[(String, String)](sql).list
    }
    listCoordinatesToPupleDouble(result)
  }

  def listCoordinatesToPupleDouble(list: Iterable[(String, String)]) = {
    for (tuple <- list) yield
      (for (coordinate <- tuple._1.split(" ")) yield {
        val sp = coordinate.split('|')
        (sp(0).toDouble, sp(1).toDouble)
      }, tuple._2)
  }
}
