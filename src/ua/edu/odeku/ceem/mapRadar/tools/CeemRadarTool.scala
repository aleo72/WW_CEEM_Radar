/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.tools

import javax.swing.JFrame

/**
 * User: Aleo Bakalov
 * Date: 10.12.13
 * Time: 16:38
 */
trait CeemRadarTool extends NamingTool with PanelTool {

  var parentToolFrame : ToolFrame = _

  def setParent(frame: ToolFrame) { parentToolFrame = frame }

  def startFunction : Function1[ToolFrame, Unit]

  def endFunction : Function1[ToolFrame, Unit]
}
