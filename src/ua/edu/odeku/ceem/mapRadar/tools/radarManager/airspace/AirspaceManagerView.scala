/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.AirspacePanel
import javax.swing.JPanel

/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:09
 */
class AirspaceManagerView(val model: AirspaceBuilderModel, val controller: AirspaceController) {

	val form: AirspacePanel = new AirspacePanel
	val panel: JPanel = form.getRootPanel
	var ignoreSelectEvents: Boolean = false

	def selectedIndices: Array[Int] = this.form.table.getSelectedRows

	def selectedIndices_=(indices: Array[Int]): Unit = {
		this.ignoreSelectEvents = true

		if (indices != null && indices.length != 0) {
			for (index: Int <- indices) {
				this.form.table.setRowSelectionInterval(index, index)
			}
		} else {
			this.form.table.clearSelection()
		}

		this.ignoreSelectEvents = false
	}

	def getSelectedFactory: AirspaceFactory = SphereAirspaceFactory.obj

	def initComponents() {

	}
}
