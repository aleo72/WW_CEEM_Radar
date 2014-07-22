/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import javax.swing.JMenuItem
import java.awt.event.{ActionEvent, ActionListener}
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame
import ua.edu.odeku.ceem.mapRadar.tools.CeemRadarTool
import ua.edu.odeku.ceem.mapRadar.tools.geoName.imports.ImportGeoNameTool
import ua.edu.odeku.ceem.mapRadar.tools.geoName.view.ViewGeoNameTool
import ua.edu.odeku.ceem.mapRadar.tools.settings.SettingTool

/***********************************************************************************************************************
  * Об'єкт який створює головне меню
  *
  * Created by Алексей on 31.05.2014.
  * *********************************************************************************************************************/
object MainMenu extends MenuCreator {

	override def nameMenu: String = resourceBundle.getString("program")

	override def menuItems: Array[JMenuItem] = createMenus()


  val tools: Array[Class[_ <: CeemRadarTool]] = Array(
    classOf[SettingTool]
  )

	private def createMenus() = {

		val close = new JMenuItem("Close")
		close.addActionListener(new ActionListener {
			override def actionPerformed(e: ActionEvent): Unit = {
				System.exit(0)
				AppCeemRadarFrame.dispose()

			}
		})

		createMenuItemsForCeemRadarTool(tools) ++ Array(close)
	}
}
