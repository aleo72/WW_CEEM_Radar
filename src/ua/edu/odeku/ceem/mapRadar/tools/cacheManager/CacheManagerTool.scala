/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.cacheManager

import ua.edu.odeku.ceem.mapRadar.tools.{ToolFrame, CeemRadarTool}
import javax.swing.JPanel

/**
 * Created by Aleo on 02.02.14.
 */
class CacheManagerTool extends CeemRadarTool {


	override def rootPanel: JPanel = ???

	override def endFunction = (ToolFrame) => {
		println("CacheManagerTool_endFunction")
	}

	override def startFunction = (ToolFrame) => {
		println("CacheManagerTool+startFunction")
	}
}
