/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import java.util.ResourceBundle
import javax.swing.{JMenu, JMenuItem}

import ua.edu.odeku.ceem.mapRadar.settings.Settings

/** *********************************************************************************************************************
  * Допомогає у створені меню
  *
  * Created by Алексей on 31.05.2014.
  * *********************************************************************************************************************/
trait MenuCreator {

  val resourceBundle = ResourceBundle.getBundle("menu", Settings.Program.locale)

  def nameMenu: String

  def menuItems: Array[JMenuItem]

  def createMenu(): JMenu = {
    val menu = new JMenu(this.nameMenu)

    menuItems.foreach(menu.add)

    menu
  }

}
