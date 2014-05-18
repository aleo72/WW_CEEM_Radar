/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager

import ua.edu.odeku.ceem.mapRadar.tools.{ToolFrame, CeemRadarTool}
import javax.swing.JPanel
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.{AirspaceBuilderModel, AirspaceController, AirspaceManagerView}
import gov.nasa.worldwind.layers.AirspaceLayer
import ua.edu.odeku.ceem.mapRadar.utils.gui.VisibleUtils
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame

/**
 * User: Aleo Bakalov
 * Date: 11.12.13
 * Time: 17:34
 */
class RadarManagerTool extends CeemRadarTool {

	val tableModel: AirspaceBuilderModel = new AirspaceBuilderModel
	val controller: AirspaceController = new AirspaceController(this)
	val view: AirspaceManagerView = new AirspaceManagerView(tableModel, controller)

	controller.view = view
	controller.model = tableModel

	val airspaceLayer: AirspaceLayer = new AirspaceLayer
	this.airspaceLayer.setName(airspace.AIRSPACE_LAYER_NAME)
	VisibleUtils.insertBeforePlaceNames(AppCeemRadarFrame.wwd, this.airspaceLayer)

	controller.resizeNewShapesToViewport = true

	def startFunction: (ToolFrame) => Unit = (ToolFrame) => { println("RadarManagerTool_startFunction") }

	def endFunction: (ToolFrame) => Unit = (ToolFrame) => {println("RadarManagerTool_endFunction")}

	def rootPanel: JPanel = view.getRootPanel
}
