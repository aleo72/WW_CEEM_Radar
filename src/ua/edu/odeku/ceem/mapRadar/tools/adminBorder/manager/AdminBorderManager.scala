/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.manager

import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.Admin0
import scala.xml.{Attribute, XML}
import java.io.{FileInputStream, ObjectInputStream, File}
import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram
import scala.collection.mutable

/**
 * User: Aleo Bakalov
 * Date: 26.03.2014
 * Time: 9:59
 */
object AdminBorderManager {


	private val _admins0: mutable.Map[String, Admin0] = initAdmins0()

	private var _viewCountryBorder: mutable.Map[String, Boolean] = null
	private var _viewProvincesBorder: mutable.Map[String, Boolean] = null

	update()

	def admins = _admins0

	def viewCountryBorder = _viewCountryBorder

	/**
	 * Заполним ассоциативный массив
	 * @return  ассоциативный массив с элиментами Admin0 которые у нас есть в наличии
	 */
	private def initAdmins0(): mutable.Map[String, Admin0] = {
		val dir = new File(PropertyProgram.CEEM_RADAR_DATA_ADMIN_BORDER_0_DIR)
		var map: mutable.Map[String, Admin0] = mutable.Map()
		for (file <- dir.listFiles()) {
			val input = new ObjectInputStream(new FileInputStream(file))
			val ob = input.readObject()
			ob match {
				case admin: Admin0 => map += (admin.admin0a3 -> null)
			}
		}
		map
	}

	/**
	 * Метод должен обновить список загруженых в память стран
	 */
	def update() {
		var mapForCountry: mutable.Map[String, Boolean] = mutable.Map()
		var mapForProvinces: mutable.Map[String, Boolean] = mutable.Map()
		val xml = XML.loadFile(new File(PropertyProgram.CEEM_RADAR_CONFIG_ADMIN_BORDER_MANAGER))
		for (node <- xml \ "admin0") {
			val iso = node.attribute("iso").getOrElse("").toString
			val viewCountry = node.attribute("viewCountryBorder").getOrElse("false").toString
			val viewProvinces = node.attribute("viewProvincesBorder").getOrElse("false").toString
			mapForCountry += (iso -> viewCountry.toBoolean)
			mapForProvinces += (iso -> viewProvinces.toBoolean)
		}
		_viewCountryBorder = mapForCountry
		_viewProvincesBorder = mapForProvinces
	}

	def save() {
		var xml = XML.loadFile(PropertyProgram.CEEM_RADAR_CONFIG_ADMIN_BORDER_MANAGER)
		xml = xml.copy(child = Seq().asInstanceOf[Seq[scala.xml.Node]])

		val tagAdmin0 = <admin0/>

		for((iso, admin) <- _admins0){
			val tag = tagAdmin0 % Attribute(null, "viewCountryBorder", _viewCountryBorder(iso).toString, scala.xml.Null ) % Attribute(null, "viewProvincesBorder", _viewProvincesBorder(iso).toString, scala.xml.Null) % Attribute(null, "iso", iso, scala.xml.Null)
			xml = xml.copy(child = xml.child ++ tag)
		}
		XML.save(PropertyProgram.CEEM_RADAR_CONFIG_ADMIN_BORDER_MANAGER, xml)
	}
}
