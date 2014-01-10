/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.AirspacePanel

/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:09
 */
class AirspaceManagerView(val model : AirspaceBuilderModel, val controller : AirspaceController) {

	val form : AirspacePanel = new AirspacePanel
	var ignoreSelectEvents: Boolean = false

	def getSelectedIndices : Array[Int] = this.form.table.getSelectedRows

	def setSelectedIndices(indices : Array[Int]) {
		this.ignoreSelectEvents = true

		if(indices != null && indices.length != 0){
			for (index : Int <- indices){
				this.form.table.setRowSelectionInterval(index, index)
			}
		} else {
			this.form.table.clearSelection()
		}

		this.ignoreSelectEvents = false
	}

	def getSelectedFactory : AirspaceFactory = SphereAirspaceFactory.obj

	def initComponents(){

	}
}
