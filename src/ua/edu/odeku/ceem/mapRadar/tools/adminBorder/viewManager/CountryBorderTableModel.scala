/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.viewManager

import java.lang
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.models.CountryBorders

/**
 * Created by aleo on 30.08.14.
 */
class CountryBorderTableModel extends TableModel {

  val data = CountryBorders.infoFields().toArray

  override def getRowCount: Int = data.size

  override def getColumnCount: Int = 4

  override def getColumnName(columnIndex: Int): String = {
    columnIndex match {
      case 0 => "Visible"
      case 1 => "Name"
      case 2 => "Admin"
      case 3 => "Admin0a3"
      case _ => "None"
    }
  }

  override def getColumnClass(columnIndex: Int): Class[_] = {
    columnIndex match {
      case 0 => classOf[java.lang.Boolean]
      case 1 => classOf[String]
      case 2 => classOf[String]
      case 3 => classOf[String]
      case _ => null
    }
  }

  override def isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = {
    if (columnIndex == 0) true else false
  }

  override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = {
    columnIndex match {
      case 0 => lang.Boolean.parseBoolean(data(rowIndex)._4.toString).asInstanceOf[AnyRef]
      case 1 => data(rowIndex)._1
      case 2 => data(rowIndex)._2
      case 3 => data(rowIndex)._3
      case _ => null
    }
  }

  override def setValueAt(aValue: scala.Any, rowIndex: Int, columnIndex: Int): Unit = {
    columnIndex match {
      case 0 =>
        val x = data(rowIndex)
        data(rowIndex) = (x._1, x._2, x._3, aValue.asInstanceOf[Boolean])
      case _ =>
    }
  }

  override def addTableModelListener(l: TableModelListener): Unit = Unit

  override def removeTableModelListener(l: TableModelListener): Unit = Unit
}
