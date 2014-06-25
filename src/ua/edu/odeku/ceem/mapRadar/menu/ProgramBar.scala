/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import javax.swing.JMenuBar

/** *********************************************************************************************************************
  * Об'єкт котрий створює меню бар для програми
  *
  * Created by Алексей on 31.05.2014.
  * *********************************************************************************************************************/
object ProgramBar {

	val menuCreators: Array[MenuCreator] = Array(
		MainMenu
		, MenuView
//		, MenuTools
		, MenuRadar
	)

	def createProgramMainBar(): JMenuBar = {
		val bar = new JMenuBar

		for (creator <- menuCreators) {
			bar.add(creator.createMenu())
		}

		bar
	}

}
