/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.viewManager

import javax.swing.table.TableModel
import javax.swing.event.TableModelListener

/**
 * Created by Aleo on 28.03.2014.
 */
class AdminBorderViewManagerFormHandler(val tool: AdminBorderViewManagerTool) {
	val form = tool.form

	def tableModelForAdminBorderViewTable = {
		val model = new TableModel {

			override def addTableModelListener(l: TableModelListener): Unit = Unit

			override def setValueAt(aValue: scala.Any, rowIndex: Int, columnIndex: Int): Unit = Unit

			override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = new Object

			override def removeTableModelListener(l: TableModelListener): Unit = Unit

			override def getColumnName(columnIndex: Int): String = null

			override def getColumnCount: Int = 0

			override def isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = false

			override def getColumnClass(columnIndex: Int): Class[_] = Int.getClass

			override def getRowCount: Int = 0
		}
	}
}
