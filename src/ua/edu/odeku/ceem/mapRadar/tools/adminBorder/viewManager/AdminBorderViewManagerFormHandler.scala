/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.viewManager

import javax.swing.table.TableModel
import javax.swing.event.TableModelListener
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.manager.AdminBorderManager
import scala.collection.mutable.ArrayBuffer


/**
 * Обработчик
 *
 * Created by Aleo on 28.03.2014.
 */
class AdminBorderViewManagerFormHandler(val tool: AdminBorderViewManagerTool) {
	val form = tool.form
	val tableModel = new TableModelForAdminBorderViewTable
	form.table.setModel(tableModel)
}

class TableModelForAdminBorderViewTable extends TableModel {

	val tableSource: Array[(Boolean, String, String)] = {
		val buffer = new ArrayBuffer[(Boolean, String, String)]()
		for( (iso, flag) <- AdminBorderManager.viewCountryBorder ){
			val admin = AdminBorderManager.admin(iso)
			buffer += ((flag, admin.name, admin.admin0a3))
		}
		AdminBorderManager.clear()
		buffer.toArray
	}

	override def removeTableModelListener(l: TableModelListener): Unit = Unit

	override def addTableModelListener(l: TableModelListener): Unit = Unit

	override def setValueAt(aValue: scala.Any, rowIndex: Int, columnIndex: Int): Unit = {
		println("value = " + aValue + " | row = " + rowIndex + " | column = " + columnIndex)
	}

	override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = {
		val value: (Boolean, String, String) = tableSource(rowIndex)
		columnIndex match {
			case 0 => new java.lang.Boolean(value._1)
			case 1 => value._2
			case 2 => value._3
		}
	}

	override def isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = {
		columnIndex match {
			case 0 => true
			case _ => false
		}
	}

	override def getColumnClass(columnIndex: Int): Class[_] = {
		columnIndex match {
			case 0 => classOf[Boolean]
			case 1 => classOf[String]
			case 2 => classOf[String]
			case _ => classOf[Object]
		}
	}

	override def getColumnName(columnIndex: Int): String = {
		columnIndex match {
			case 0 => "Enabled"
			case 1 => "Name"
			case 2 => "ISO"
			case _ => "NONE"
		}
	}

	override def getColumnCount: Int = 3

	override def getRowCount: Int = AdminBorderManager.viewCountryBorder.size
}