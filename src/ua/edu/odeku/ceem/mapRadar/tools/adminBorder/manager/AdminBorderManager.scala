/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.manager

import java.io.{File, FileInputStream, ObjectInputStream}

import ua.edu.odeku.ceem.mapRadar.settings.Settings
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.Admin0

import scala.collection.mutable
import scala.xml.{Attribute, Node, XML}

/**
 * User: Aleo Bakalov
 * Date: 26.03.2014
 * Time: 9:59
 */
object AdminBorderManager {


  private val _admins0: mutable.HashMap[String, Admin0] = initAdmins0()

  private var _viewCountryBorder: mutable.HashMap[String, Boolean] = null

  update()

  def admin(iso: String): Admin0 = {
    val admin = _admins0.get(iso)
    if (admin.get != null) {
      admin.get
    } else {
      //			val file = new File(PropertyProgram.CEEM_RADAR_DATA_ADMIN_BORDER_0_DIR + iso + ".admin0")
      val file = new File(Settings.Program.Data.folder + iso + ".admin0")
      if (file.exists()) {
        val input = new ObjectInputStream(new FileInputStream(file))
        val ob = input.readObject()
        ob match {
          case admin: Admin0 => {
            _admins0 += (admin.admin0a3 -> admin)
            admin
          }
        }
      } else {
        throw new Exception(iso + ".admin0 is not exists")
      }
    }

  }

  def viewCountryBorder = _viewCountryBorder

  def viewCountryBorderUpdate(iso: String, flag: Boolean) {
    _viewCountryBorder.put(iso, flag)
  }

  /**
   * Заполним ассоциативный массив
   * @return  ассоциативный массив с элиментами Admin0 которые у нас есть в наличии
   */
  private def initAdmins0(): mutable.HashMap[String, Admin0] = {
    //		val fileConfig = new File(PropertyProgram.CEEM_RADAR_CONFIG_ADMIN_BORDER_MANAGER)
    val fileConfig = new File(Settings.Program.Data.folder)
    var map: mutable.HashMap[String, Admin0] = mutable.HashMap()
    val xml = XML.loadFile(fileConfig)

    for (adminTag <- xml \ "admin0") {

      val option = adminTag.attribute("iso")
      val seq: Seq[Node] = option.orNull
      if (seq != null) {
        map += (seq.text -> null)
      }
    }
    map
  }

  /**
   * Метод должен обновить список загруженых в память стран
   */
  def update() {
    var mapForCountry: mutable.HashMap[String, Boolean] = mutable.HashMap()
    //		val xml = XML.loadFile(new File(PropertyProgram.CEEM_RADAR_CONFIG_ADMIN_BORDER_MANAGER))
    val xml = XML.loadFile(new File(Settings.Program.Data.folder))
    for (node <- xml \\ "admin0") {
      val iso = node.attribute("iso").getOrElse("").toString
      val viewCountry = node.attribute("viewCountryBorder").getOrElse("false").toString
      mapForCountry += (iso -> viewCountry.toBoolean)
    }
    _viewCountryBorder = mapForCountry
  }

  def save() {
    //		var xml = XML.loadFile(PropertyProgram.CEEM_RADAR_CONFIG_ADMIN_BORDER_MANAGER)
    var xml = XML.loadFile(Settings.Program.Data.folder)
    xml = xml.copy(child = Seq().asInstanceOf[Seq[scala.xml.Node]])

    val tagAdmin0 = <admin0/>

    for (iso <- _viewCountryBorder.keySet.toList.sorted) {
      val tag = tagAdmin0 % Attribute(null, "iso", iso, scala.xml.Null) % Attribute(null, "viewCountryBorder", _viewCountryBorder(iso).toString, scala.xml.Null)
      xml = xml.copy(child = xml.child ++ tag)
    }
    //		XML.save(PropertyProgram.CEEM_RADAR_CONFIG_ADMIN_BORDER_MANAGER, xml)
    XML.save(Settings.Program.Data.folder, xml)
  }

  def clear() {
    for ((string, boolean) <- this._viewCountryBorder) {
      if (!boolean) {
        this._admins0.remove(string)
      }
    }
  }
}
