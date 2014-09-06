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
case class ProvinceBorder(id: Option[Long], name: String, name1: String, iso: String, countryBorder: Long, visible: Boolean = false)

class ProvinceBorders(tag: Tag) extends Table[ProvinceBorder](tag, "PROVINCE_BORDERS") {

  def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def name1 = column[String]("NAME1")

  def iso = column[String]("ISO")

  def countryBorder = column[Long]("COUNTRYBORDER", O.NotNull)

  def countryBorderFK = foreignKey("COUNTRYBORDER_FK", countryBorder, CountryBorders.objects)(_.id.get)

  def visible = column[Boolean]("VISIBLE")

  def countryBorderJoin = CountryBorders.objects.filter(_.id === countryBorder)

  override def * : ProvenShape[ProvinceBorder] = (id, name, name1, iso, countryBorder, visible) <>(ProvinceBorder.tupled, ProvinceBorder.unapply)
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

  def apply(name: String, name1: String, iso: String): ProvinceBorder = {
    DB.database withSession { implicit session =>
      objects.filter(_.name === name).filter(_.name1 === name1).filter(_.iso === iso).first
    }
  }

  def infoFields(): List[(String, String, String, Boolean)] = {
    val sql = StaticQuery.queryNA[(String, String, String, Boolean)](
      s"""
        Select
          ${table.name.toString()} ,
          ${table.name1.toString()} ,
          ${table.iso.toString()},
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
        (for (o <- objects if o.name === b._1 && o.name1 === b._2 && o.iso === b._3) yield o.visible).update(b._4)
      }
    }
  }

  def visibleBorders: Iterable[ProvinceBorder] = {
    DB.database withSession { implicit session =>
      objects.filter(_.visible === true).list
    }
  }

}
