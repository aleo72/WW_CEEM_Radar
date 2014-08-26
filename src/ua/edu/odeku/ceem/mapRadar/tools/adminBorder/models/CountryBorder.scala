/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.models

import ua.edu.odeku.ceem.mapRadar.db.{DB, CeemTableObject}

import scala.slick.driver.H2Driver
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable
import scala.slick.lifted.ProvenShape
import scala.slick.jdbc.StaticQuery

/**
 * Created by aleo on 17.08.14.
 */
case class CountryBorder(var id: Option[Long] = None,name :String, admin: String, admin0a3: String)


class CountryBorders(tag: Tag) extends Table[CountryBorder](tag, "COUNTRY_BORDER") {

  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def admin = column[String]("admin")

  def admin0a3 = column[String]("admin0a3")

  override def * : ProvenShape[CountryBorder] = (id, name, admin, admin0a3) <> (CountryBorder.tupled, CountryBorder.unapply)
}

object CountryBorders extends CeemTableObject {

  val objects = TableQuery[CountryBorders]

  val table = objects.baseTableRow

  val tableName = table.tableName

  override def createIfNotExists(existsTables: List[MTable], db: H2Driver.backend.DatabaseDef): Unit = {
    if(!existsTables.exists(_.name.name == tableName)){
      db withSession { implicit session =>
        objects.ddl.create
      }
    }
  }

  def +=(countryBorder: CountryBorder) = DB.database withSession{ implicit session => objects += countryBorder }

  def <= (countryBorder: CountryBorder) = {
    DB.database withSession{ implicit session =>
      val x = (objects returning objects.map (_.id)) += countryBorder
      CountryBorder(x, countryBorder.name, countryBorder.admin, countryBorder.admin0a3)
    }
  }
}
