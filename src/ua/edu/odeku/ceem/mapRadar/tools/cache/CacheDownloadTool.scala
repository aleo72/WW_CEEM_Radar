/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.tools.cache

import ua.edu.odeku.ceem.mapRadar.tools.{ToolFrame, CeemRadarTool}
import javax.swing.JPanel
import gov.nasa.worldwind.WorldWindow

/**
 * User: Aleo Bakalov
 * Date: 11.12.13
 * Time: 15:06
 */
class CacheDownloadTool(val wwd : WorldWindow) extends CeemRadarTool {

  val cacheDownload = new CacheDownload(wwd)

  def startFunction = (frame : ToolFrame ) => {}

  def endFunction = (frame : ToolFrame ) => {}

  def rootPanel: JPanel = cacheDownload
}
