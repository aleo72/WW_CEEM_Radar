/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import javax.swing.{JMenu, JCheckBoxMenuItem, KeyStroke, JMenuItem}
import ua.edu.odeku.ceem.mapRadar.tools.radar.surface.SurfaceDistributionPowerDensityManager

import scala.collection.mutable.ArrayBuffer
import java.awt.event.{ActionEvent, ActionListener, InputEvent}
import ua.edu.odeku.ceem.mapRadar.tools.radar.RadarManagerTool
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame
import ua.edu.odeku.ceem.mapRadar.tools.ToolFrame
import ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.entry.AirspaceEntry

/** *********************************************************************************************************************
  *
  * Created by Алексей on 31.05.2014.
  * *********************************************************************************************************************/
object MenuRadar extends MenuCreator {

  override def nameMenu: String = resourceBundle.getString("radar")

  override def menuItems: Array[JMenuItem] = {
    Array(
      createRadarManagerMenuItem(),
      createIsolineMenuItem(),
      create2DGraf(),
      create3DGraf()
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
          val frame = new ToolFrame(toolName) //, resourceBundle.getString("radar_manager"))
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
    val isoLinesMenu = new JMenu(resourceBundle.getString("view_isoline"))

    val altitudes = Array(100, 300, 500, 800, 1000, 2000, 3000, 10000, 20000, 100000, 150000, 200000)
    val menuItems = new ArrayBuffer[JCheckBoxMenuItem]()
    for (altitude <- altitudes) {
      val item = new JCheckBoxMenuItem()
      item.setText(s"$altitude m")
      item.setState(false)
      item.addActionListener(new ActionListener {
        val alt: Int = altitude

        override def actionPerformed(e: ActionEvent): Unit = {
          val item = e.getSource.asInstanceOf[JCheckBoxMenuItem]
          val state = item.getState
          AirspaceEntry.showViewMode(SurfaceDistributionPowerDensityManager.SHOW_TYPE_ISOLINE, if (state) alt else 0)
          menuItems.foreach(_.setState(false))
          item.setState(state)
        }
      })
      isoLinesMenu.add(item)
      menuItems.+=(item)
    }
    isoLinesMenu
  }

  private def create2DGraf(): JMenuItem = {
    val menu = new JMenu(resourceBundle.getString("view_2D_graf"))

    val altitudes = Array(100, 300, 500, 800, 1000, 2000, 3000, 10000, 20000, 100000, 150000, 200000)
    val menuItems = new ArrayBuffer[JCheckBoxMenuItem]()
    for (altitude <- altitudes) {
      val item = new JCheckBoxMenuItem()
      item.setText(s"$altitude m")
      item.setState(false)
      item.addActionListener(new ActionListener {
        val alt: Int = altitude

        override def actionPerformed(e: ActionEvent): Unit = {
          val item = e.getSource.asInstanceOf[JCheckBoxMenuItem]
          val state = item.getState
          AirspaceEntry.showViewMode(SurfaceDistributionPowerDensityManager.SHOW_TYPE_2D, if (state) alt else 0)
          menuItems.foreach(_.setState(false))
          item.setState(state)
        }
      })
      menu.add(item)
      menuItems.+=(item)
    }
    menu
  }

  private def create3DGraf(): JMenuItem = {
    val menu = new JMenu(resourceBundle.getString("view_3D_graf"))

    val altitudes = Array(100, 300, 500, 800, 1000, 2000, 3000, 10000, 20000, 100000, 150000, 200000)
    val menuItems = new ArrayBuffer[JCheckBoxMenuItem]()
    for (altitude <- altitudes) {
      val item = new JCheckBoxMenuItem()
      item.setText(s"$altitude m")
      item.setState(false)
      item.addActionListener(new ActionListener {
        val alt: Int = altitude

        override def actionPerformed(e: ActionEvent): Unit = {
          val item = e.getSource.asInstanceOf[JCheckBoxMenuItem]
          val state = item.getState
          AirspaceEntry.showViewMode(SurfaceDistributionPowerDensityManager.SHOW_TYPE_3D, if (state) alt else 0)
          menuItems.foreach(_.setState(false))
          item.setState(state)
        }
      })
      menu.add(item)
      menuItems.+=(item)
    }
    menu
  }
}
