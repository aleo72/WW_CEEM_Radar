/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.settings

import javax.swing.JPanel

import ua.edu.odeku.ceem.mapRadar.tools.settings.view.SettingsFormView
import ua.edu.odeku.ceem.mapRadar.tools.{ToolFrame, CeemRadarTool}

/**
 * Created by ABakalov on 22.07.2014.
 */
class SettingTool extends CeemRadarTool {

  val form = new SettingsFormView

  /**
   * Метод для инициализации инструмента,
   * вызовется при мервом вызове, а не в помент создания
   */
  override def init(): Unit = Unit

  override def startFunction = (toolFrame: ToolFrame) =>  Unit

  override def endFunction: (ToolFrame) => Unit = (toolFrame: ToolFrame) =>  Unit

  override def rootPanel: JPanel = form.$$$getRootComponent$$$().asInstanceOf[JPanel]
}
