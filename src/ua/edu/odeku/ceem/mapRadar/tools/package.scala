/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar

import ua.edu.odeku.ceem.mapRadar.tools.geoName.imports.ImportGeoNameTool
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.RadarManagerTool
import ua.edu.odeku.ceem.mapRadar.tools.geoName.view.ViewGeoNameTool
import scala.Predef._
import ua.edu.odeku.ceem.mapRadar.tools.cache.CacheDownloadTool

/**
 * Created by Aleo on 13.01.14.
 */
package object tools {
	val arrayTool: Array[String] = Array(
		classOf[ImportGeoNameTool].getName,
		classOf[CacheDownloadTool].getName,
		classOf[ViewGeoNameTool].getName,
		classOf[RadarManagerTool].getName
	)
}
