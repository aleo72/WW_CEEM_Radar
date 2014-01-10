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

	def getSelectedIndices : Array[Int] = this.form.table.getSelectedRows

	def initComponents(){

	}
}
