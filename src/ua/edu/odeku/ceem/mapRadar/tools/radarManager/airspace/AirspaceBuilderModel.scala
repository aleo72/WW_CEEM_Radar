/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import javax.swing.table.AbstractTableModel
import java.lang.String
import gov.nasa.worldwind.avlist.AVKey
import java.util

/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:05
 */
class AirspaceBuilderModel extends AbstractTableModel {

	val entryList = new util.ArrayList[AirspaceEntry]()

	override def getColumnName(columnIndex: Int): String = AirspaceBuilderModel.columnName(columnIndex)

	override def getColumnClass(columnIndex: Int): Class[_] = AirspaceBuilderModel.columnClass(columnIndex)

	def getRowCount: Int = this.entryList.size()

	def getColumnCount: Int = 1

	def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = {
		val entry = this.entryList.get(rowIndex)
		entry.getValue(AirspaceBuilderModel.columnAttribute(columnIndex))
	}

	override def isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = true

	def entries: util.List[AirspaceEntry] = java.util.Collections.unmodifiableList(this.entryList)

	def entries_=(value: Iterable[_ <: AirspaceEntry]): Unit = {
		this.entryList.clear()
		if (value != null) {
			for (entry : AirspaceEntry <- value ) {
				this.entryList add entry
			}
		}

		this.fireTableDataChanged()
	}

	def addEntry(entry : AirspaceEntry) {
		this.entryList.add(entry)
		val index = this.entryList.size() - 1;
		this.fireTableRowsInserted(index, index)
	}

	def removeEntry(entry : AirspaceEntry) {
		val index = this.entryList.indexOf(entry)
		if (index != -1){
			this.entryList.remove(entry)
			this.fireTableRowsDeleted(index, index)
		}
	}

	def removeAllEntries(){
		this.entryList.clear()
		this.fireTableDataChanged()
	}

	def getEntry(index : Int) : AirspaceEntry = this.entryList.get(index)

	def setEntry(index : Int, entry : AirspaceEntry) : AirspaceEntry = this.entryList.set(index, entry)

	def getIndexForEntry(entry : AirspaceEntry) : Int = this.entryList.indexOf(entry)
}

object AirspaceBuilderModel {
	val columnName: Array[String] = Array("Name")
	val columnClass: Array[Class[_]] = Array(classOf[String])
	val columnAttribute: Array[String] = Array(AVKey.DISPLAY_NAME)
}
