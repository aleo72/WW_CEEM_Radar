/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.geoName.view

import java.util.ResourceBundle
import javax.swing.table.AbstractTableModel

import ua.edu.odeku.ceem.mapRadar.tools.geoName.models.{GeoName, GeoNames}
import ua.edu.odeku.ceem.mapRadar.settings.Settings

/**
 * User: Aleo Bakalov
 * Date: 04.07.2014
 * Time: 16:18
 */
class GeoNamesTableModel extends AbstractTableModel {

	private var _list: List[GeoName] = null

  val resourceBundle: ResourceBundle = ResourceBundle.getBundle("strings", Settings.Program.locale)

	def list = _list

	def this(subName: String, country: String, featureClass: String, featureCode: String) {
		this()
		_list = GeoNames.list(subName, country, featureClass, featureCode)
	}

	override def getRowCount: Int = _list.length

	override def getColumnCount: Int = GeoNames.countFieldsInGeoName

	override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = {
		if (rowIndex < _list.length && columnIndex < getColumnCount) {
			_list(rowIndex).fields(columnIndex).asInstanceOf[AnyRef]
		} else {
			null
		}
	}

	override def getColumnClass(columnIndex: Int): Class[_] = {
		columnIndex match {
			case 0 => classOf[Long]
			case 1 => classOf[String]
			case 2 => classOf[String]
			case 3 => classOf[String]
			case 4 => classOf[String]
			case 5 => classOf[Double]
			case 6 => classOf[Double]
			case 7 => classOf[String]
			case 8 => classOf[String]
			case 9 => classOf[String]
		}

	}

	override def getColumnName(column: Int): String = {
		column match {
      case 0 => resourceBundle.getString("table_geoName_row")
      case 1 => resourceBundle.getString("table_geoName_name")
      case 2 => resourceBundle.getString("table_geoName_asciiName")
      case 3 => resourceBundle.getString("table_geoName_translate")
      case 4 => resourceBundle.getString("table_geoName_alternate-names")
      case 5 => resourceBundle.getString("table_geoName_country-code")
      case 6 => resourceBundle.getString("table_geoName_feature-class")
      case 7 => resourceBundle.getString("table_geoName_feature-code")
      case 8 => resourceBundle.getString("table_geoName_lat")
      case 9 => resourceBundle.getString("table_geoName_lon")
		}
	}
}
