/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import javax.swing.{JMenuItem, JMenu}
import java.util.ResourceBundle
import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram

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

}
