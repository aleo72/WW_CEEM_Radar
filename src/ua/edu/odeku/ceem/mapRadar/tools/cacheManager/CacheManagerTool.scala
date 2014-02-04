/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.cacheManager

import ua.edu.odeku.ceem.mapRadar.tools.{ToolFrame, CeemRadarTool}
import javax.swing.JPanel
import ua.edu.odeku.ceem.mapRadar.tools.cacheManager.panel.CacheDownloaderForm

/**
 * Created by Aleo on 02.02.14.
 */
class CacheManagerTool extends CeemRadarTool {

	val view = new CacheDownloaderForm(this)

	override def rootPanel: JPanel = view.$$$getRootComponent$$$().asInstanceOf[JPanel]

	override def endFunction = (ToolFrame) => {
		println("CacheManagerTool_endFunction")
	}

	override def startFunction = (ToolFrame) => {
		println("CacheManagerTool+startFunction")
	}
}
