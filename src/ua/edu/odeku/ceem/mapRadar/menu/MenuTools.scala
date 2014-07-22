/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import javax.swing.JMenuItem
import scala.collection.mutable.ArrayBuffer
import ua.edu.odeku.ceem.mapRadar.tools.geoName.imports.{ImportGeoNameTool, ImportGeoName}
import ua.edu.odeku.ceem.mapRadar.tools.geoName.view.ViewGeoNameTool
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.imports.ImportAdminBorderTool
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.viewManager.AdminBorderViewManagerTool
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame
import ua.edu.odeku.ceem.mapRadar.tools.{CeemRadarTool, ToolFrame}
import java.awt.event.{ActionEvent, ActionListener}

/***********************************************************************************************************************
  * Об'єкт створює меню для інструментів
  *
  * Created by Алексей on 31.05.2014.
  * *********************************************************************************************************************/
object MenuTools extends MenuCreator {

	val tools: Array[Class[_ <: CeemRadarTool]] = Array(
		classOf[ImportGeoNameTool]
		,classOf[ViewGeoNameTool]
		//,classOf[ImportAdminBorderTool]
		//,classOf[AdminBorderViewManagerTool]
	)

	override def nameMenu: String = resourceBundle.getString("tools")

	override def menuItems: Array[JMenuItem] = {
		createMenuItemsForCeemRadarTool(tools)
	}

}
