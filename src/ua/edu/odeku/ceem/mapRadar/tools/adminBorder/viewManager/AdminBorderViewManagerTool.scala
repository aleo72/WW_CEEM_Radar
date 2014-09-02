/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.viewManager

import ua.edu.odeku.ceem.mapRadar.tools.{ToolFrame, CeemRadarTool}
import javax.swing.JPanel

/**
 * User: Aleo Bakalov
 * Date: 26.03.2014
 * Time: 9:51
 */
class AdminBorderViewManagerTool extends CeemRadarTool {

	val form = new AdminBorderViewForm

	override def rootPanel: JPanel = form.rootPanel()

	override def endFunction: (ToolFrame) => Unit = (tool: ToolFrame) => {}

	override def startFunction: (ToolFrame) => Unit = (tool: ToolFrame) => {}

	/**
	 * Метод для инициализации инструмента,
	 * вызовется при мервом вызове, а не в помент создания
	 */
	override def init(): Unit = {
    val handler = new AdminBorderViewManagerFormHandler(this)
	}
}
