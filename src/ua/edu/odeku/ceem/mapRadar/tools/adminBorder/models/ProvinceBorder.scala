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

/**
 * Created by aleo on 17.08.14.
 */
case class ProvinceBorder(id: Option[Long], name: String, name1: String, iso: String, countryBorder: Long)

class ProvinceBorders(tag: Tag) extends Table[ProvinceBorder](tag, "PROVINCE_BORDERS") {

  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def name1 = column[String]("name1")

  def iso = column[String]("iso")

  def countryBorder = column[Long]("countryBorder", O.NotNull)

  def countryBorderFK = foreignKey("countryBorder_fk", countryBorder, CountryBorders.objects)(_.id.get)

  def countryBorderJoin = CountryBorders.objects.filter(_.id === countryBorder)

  override def * : ProvenShape[ProvinceBorder] = (id, name, name1, iso, countryBorder) <> (ProvinceBorder.tupled, ProvinceBorder.unapply)
}

object ProvinceBorders extends CeemTableObject {


  val objects = TableQuery[ProvinceBorders]

  val table = objects.baseTableRow

  val tableName = table.tableName

  override def createIfNotExists(existsTables: List[MTable], db: H2Driver.backend.DatabaseDef): Unit = {
    if(!existsTables.exists(_.name.name == tableName)){
      db withSession { implicit session =>
        objects.ddl.create
      }
    }
  }

  def += (provinceBorder: ProvinceBorder) = DB.database withSession ( implicit session => objects += provinceBorder )

  def <= (provinceBorder: ProvinceBorder) = {
    DB.database withSession { implicit session =>
      val v = (objects returning objects.map(_.id)) += provinceBorder
      ProvinceBorder(v, provinceBorder.name, provinceBorder.name1, provinceBorder.iso, provinceBorder.countryBorder)
    }
  }

}
