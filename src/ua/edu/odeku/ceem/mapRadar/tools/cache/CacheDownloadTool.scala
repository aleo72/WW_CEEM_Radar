/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.tools.cache

import ua.edu.odeku.ceem.mapRadar.tools.{ToolFrame, CeemRadarTool}
import javax.swing.JPanel
import gov.nasa.worldwind.WorldWindow
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame

/**
 * User: Aleo Bakalov
 * Date: 11.12.13
 * Time: 15:06
 */
class CacheDownloadTool extends CeemRadarTool {

	val cacheDownload = AppCeemRadarFrame.wwjPanel

	def startFunction = (frame: ToolFrame) => {}

	def endFunction = (frame: ToolFrame) => {}

	def rootPanel: JPanel = cacheDownload

	/**
	 * Метод для инициализации инструмента,
	 * вызовется при мервом вызове, а не в помент создания
	 */
	override def init(): Unit = { }
}
