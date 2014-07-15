/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.viewGeoName

import javax.swing.table.AbstractTableModel

import ua.edu.odeku.ceem.mapRadar.db.model.{GeoName, GeoNames}
import ua.edu.odeku.ceem.mapRadar.resource.ResourceString

/**
 * User: Aleo Bakalov
 * Date: 04.07.2014
 * Time: 16:18
 */
class GeoNamesTableModel extends AbstractTableModel {

	private var _list: List[GeoName] = null

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
			case 0 => ResourceString.get("table_geoName_row")
			case 1 => ResourceString.get("table_geoName_name")
			case 2 => ResourceString.get("table_geoName_asciiName")
			case 3 => ResourceString.get("table_geoName_translate")
			case 4 => ResourceString.get("table_geoName_alternate-names")
			case 5 => ResourceString.get("table_geoName_country-code")
			case 6 => ResourceString.get("table_geoName_feature-class")
			case 7 => ResourceString.get("table_geoName_feature-code")
			case 8 => ResourceString.get("table_geoName_lat")
			case 9 => ResourceString.get("table_geoName_lon")
		}
	}
}
