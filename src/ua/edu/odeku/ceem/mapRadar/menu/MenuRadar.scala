/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import javax.swing.{KeyStroke, JMenuItem}
import scala.collection.mutable.ArrayBuffer
import java.awt.event.{ActionEvent, ActionListener, InputEvent}
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.RadarManagerTool
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame

/***********************************************************************************************************************
  *
  * Created by Алексей on 31.05.2014.
  **********************************************************************************************************************/
object MenuRadar extends MenuCreator {

	override def nameMenu: String = resourceBundle.getString("radar")

	override def menuItems: Array[JMenuItem] = {
		val buff = new ArrayBuffer[JMenuItem]

		val radarManager = new JMenuItem(resourceBundle.getString("radar_manager"))
		radarManager.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK))
		radarManager.addActionListener(new ActionListener {
			override def actionPerformed(e: ActionEvent): Unit = {
				val toolName = classOf[RadarManagerTool].getName
				if(AppCeemRadarFrame.toolsComponents.contains(toolName)){
					val tool = AppCeemRadarFrame.toolsComponents.get(toolName)

				}
			}
		})

		buff.toArray
	}
}
