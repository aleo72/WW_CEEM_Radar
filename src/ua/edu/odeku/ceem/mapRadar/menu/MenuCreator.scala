/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JMenuItem, JMenu}
import java.util.ResourceBundle
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame
import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram
import ua.edu.odeku.ceem.mapRadar.tools.{CeemRadarTool, ToolFrame}

/***********************************************************************************************************************
  * Допомогає у створені меню
  *
  * Created by Алексей on 31.05.2014.
  * *********************************************************************************************************************/
trait MenuCreator {

	val resourceBundle = ResourceBundle.getBundle("menu", PropertyProgram.getCurrentLocale)

	def nameMenu:String

	def menuItems: Array[JMenuItem]

	def createMenu(): JMenu = {
		val menu = new JMenu(this.nameMenu)

		menuItems.foreach(menu.add)

		menu
	}

  def createMenuItemsForCeemRadarTool(tools: Array[ Class[_ <: CeemRadarTool]] ) = {
    for(classOfTool <- tools) yield {
      val item = new JMenuItem

      val tool = new ToolFrame(classOfTool.getName)
      AppCeemRadarFrame.toolsComponents.put(classOfTool.getName, tool)

      item.setText(tool.ceemRadarTool.name)

      item.addActionListener(new ActionListener {
        override def actionPerformed(e: ActionEvent): Unit = {
          val toolFrameOption = AppCeemRadarFrame.toolsComponents.get(classOfTool.getName)
          if(toolFrameOption.isEmpty){
            val tool = new ToolFrame(classOfTool.getName)
            AppCeemRadarFrame.toolsComponents.put(classOfTool.getName, tool)
            tool.setVisible(true)
          } else {
            if(!toolFrameOption.get.isVisible){
              toolFrameOption.get.setVisible(true)
            }
          }

        }
      })
      item
    }
  }
}
