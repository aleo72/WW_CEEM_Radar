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
 * Created by aleo on 17.08.14.
 */
case class CountryBorder(var id: Option[Long] = None, name: String, admin: String, admin0a3: String, visible: Boolean = false)


class CountryBorders(tag: Tag) extends Table[CountryBorder](tag, "COUNTRY_BORDER") {

  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def admin = column[String]("admin")

  def admin0a3 = column[String]("admin0a3")

  def visible = column[Boolean]("visible")

  override def * : ProvenShape[CountryBorder] = (id, name, admin, admin0a3, visible) <>(CountryBorder.tupled, CountryBorder.unapply)
}

object CountryBorders extends CeemTableObject {

  val objects = TableQuery[CountryBorders]

  val table = objects.baseTableRow

  val tableName = table.tableName

  override def createIfNotExists(existsTables: List[MTable], db: H2Driver.backend.DatabaseDef): Unit = {
    if (!existsTables.exists(_.name.name == tableName)) {
      db withSession { implicit session =>
        objects.ddl.create
      }
    }
  }

  def +=(countryBorder: CountryBorder) = DB.database withSession { implicit session => objects += countryBorder}

  def <=(countryBorder: CountryBorder) = {
    DB.database withSession { implicit session =>
      val x = (objects returning objects.map(_.id)) += countryBorder
      CountryBorder(x, countryBorder.name, countryBorder.admin, countryBorder.admin0a3)
    }
  }

  def apply(name: String, admin: String, admin0a3: String): CountryBorder = {
    DB.database withSession { implicit session =>
      objects.filter(_.name === name).filter(_.admin === admin).filter(_.admin0a3 === admin0a3).first
    }
  }

  def infoFields(): List[(String, String, String, Boolean)] = {
    val sql = StaticQuery.queryNA[(String, String, String, Boolean)](
      s"""
        Select
          ${table.name.toString()} ,
          ${table.admin.toString()} ,
          ${table.admin0a3.toString()},
          ${table.visible.toString()}
        From
          $tableName

       """.stripMargin)
    DB.database withSession { implicit session =>
      sql.list
    }
  }

  def updateVisibleBorders(borders: Iterable[(String, String, String, Boolean)]): Unit = {
    DB.database withSession { implicit session =>
      for (b <- borders) {
        (for {o <- objects if o.name === b._1 && o.admin === b._2 && o.admin0a3 === b._3} yield o.visible).update(b._4)

      }
    }
  }

  def visibleBorders: Iterable[CountryBorder] = {
    DB.database withSession { implicit session =>
      objects.filter(_.visible === true).list
    }
  }

}
