/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import javax.swing.JMenuItem
import java.awt.event.{ActionEvent, ActionListener}
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame

/***********************************************************************************************************************
  * Об'єкт який створює головне меню
  *
  * Created by Алексей on 31.05.2014.
  * *********************************************************************************************************************/
object MainMenu extends MenuCreator {

	override def nameMenu: String = resourceBundle.getString("program")

	override def menuItems: Array[JMenuItem] = createMenus()


	private def createMenus() = {
		val close = new JMenuItem("Close")
		close.addActionListener(new ActionListener {
			override def actionPerformed(e: ActionEvent): Unit = {
				System.exit(0)
				AppCeemRadarFrame.dispose()

			}
		})

		Array(close)
	}
}
