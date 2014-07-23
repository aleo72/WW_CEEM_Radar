/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import javax.swing.{JMenu, JCheckBoxMenuItem, KeyStroke, JMenuItem}
import scala.collection.mutable.ArrayBuffer
import java.awt.event.{ActionEvent, ActionListener, InputEvent}
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.RadarManagerTool
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame
import ua.edu.odeku.ceem.mapRadar.tools.ToolFrame
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.entry.AirspaceEntry

/** *********************************************************************************************************************
  *
  * Created by Алексей on 31.05.2014.
  * *********************************************************************************************************************/
object MenuRadar extends MenuCreator {

	override def nameMenu: String = resourceBundle.getString("radar")

	override def menuItems: Array[JMenuItem] = {
    Array(
      createRadarManagerMenuItem(),
      createIsolineMenuItem()
    )
	}

  private def createRadarManagerMenuItem() = {
    // Создание пункта меню "Менеджер радаров"
    val radarManager = new JMenuItem(resourceBundle.getString("radar_manager"))
    radarManager.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK))
    radarManager.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val toolName = classOf[RadarManagerTool].getName
        val tool = AppCeemRadarFrame.toolsComponents.get(toolName)
        if (tool.isEmpty) {
          val frame = new ToolFrame(toolName)//, resourceBundle.getString("radar_manager"))
          frame.setVisible(true)
          AppCeemRadarFrame.toolsComponents.put(toolName, frame)
        } else {
          val frame = tool.get
          if (!frame.isVisible) {
            frame.setVisible(true)
          }
        }
      }
    })
    radarManager
  }

  private def createIsolineMenuItem(): JMenuItem = {
//    val isoLinesMenuItem = new JCheckBoxMenuItem(resourceBundle.getString("view_isoline"))
//    isoLinesMenuItem.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.ALT_DOWN_MASK))
//    isoLinesMenuItem.addActionListener(new ActionListener {
//      override def actionPerformed(e: ActionEvent): Unit = {
//        val item = e.getSource.asInstanceOf[JCheckBoxMenuItem]
//        AirspaceEntry.showIsolineViewMode(item.getState)
//      }
//    })
    val isoLinesMenu = new JMenu(resourceBundle.getString("view_isoline"))

    val altitudes = Array(100, 300, 500, 800, 1000, 2000, 3000)
    val menuItems = new ArrayBuffer[JCheckBoxMenuItem]()
    for(altitude <- altitudes) {
      val item = new JCheckBoxMenuItem()
      item.setText(s"$altitude m")
      item.setState(false)
      item.addActionListener(new ActionListener {
        val alt: Int = altitude
        override def actionPerformed(e: ActionEvent): Unit = {
          val item = e.getSource.asInstanceOf[JCheckBoxMenuItem]
          val state = item.getState
          AirspaceEntry.showIsolineViewMode(if (state) alt else 0)
          menuItems.foreach(_.setState(false))
          item.setState(state)
        }
      })
      isoLinesMenu.add(item)
      menuItems.+=(item)
    }
    isoLinesMenu
  }
}
