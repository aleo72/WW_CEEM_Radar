/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.tools

import java.util.ResourceBundle
import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram

/**
 * User: Aleo Bakalov
 * Date: 10.12.13
 * Time: 17:08
 */
trait NamingTool {

	def nameTool: String = this.getClass.getName

	def name: String = {
		if(ResourceBundle.getBundle("tools", PropertyProgram.getCurrentLocale).containsKey(nameTool)){
			ResourceBundle.getBundle("tools", PropertyProgram.getCurrentLocale).getString(nameTool)
		} else {
			nameTool
		}
	}

}
