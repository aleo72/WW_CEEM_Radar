/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import javax.swing.{JCheckBoxMenuItem, JMenuItem}
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame
import scala.collection.mutable.ArrayBuffer
import java.awt.event.{ActionEvent, ActionListener, ItemEvent, ItemListener}
import gov.nasa.worldwind.layers.Layer
import ua.edu.odeku.ceem.mapRadar.layers.geoName.madeLayers.{PGeoNameLayer, AGeoNameLayer}
import ua.edu.odeku.ceem.mapRadar.layers.adminBorder.AdminBorderLayerController

/** *********************************************************************************************************************
  * Об'єкт створює меню для шарів видображення
  *
  * Created by Алексей on 31.05.2014.
  * *********************************************************************************************************************/
object MenuView extends MenuCreator {

	override def nameMenu: String = resourceBundle.getString("view")

	override def menuItems: Array[JMenuItem] = {
		val buff = new ArrayBuffer[JMenuItem]()
		val model = AppCeemRadarFrame.wwd.getModel

		val layers = model.getLayers

		for (index <- 0 until layers.size()) {
			val layer = layers.get(index)

			var name = layer.getName

			if (name.trim.isEmpty) {
				name = "Elevation"
			}
			val menuItem = new JCheckBoxMenuItem(name)
			menuItem.setSelected(layer.isEnabled)
			menuItem.addItemListener(new ItemListener {
				override def itemStateChanged(e: ItemEvent): Unit = {
					val item = e.getSource.asInstanceOf[JCheckBoxMenuItem]
					layer.setEnabled(item.isSelected)
				}
			})
			buff += menuItem
		}

		buff ++= ceemRadarLayers()

		buff.toArray
	}


	private def ceemRadarLayers() = {
		val buff = new ArrayBuffer[JCheckBoxMenuItem]

		var layer: Layer = new AGeoNameLayer
		var item: JCheckBoxMenuItem = new JCheckBoxMenuItem("GeoName A", false)
		layer.setEnabled(false)
		AppCeemRadarFrame.wwd.getModel.getLayers.add(layer)
		item.addActionListener(new ActionListener {
			def actionPerformed(e: ActionEvent) {
				val item: JCheckBoxMenuItem = e.getSource.asInstanceOf[JCheckBoxMenuItem]
				layer.setEnabled(item.isSelected)
			}
		})
		buff += item

		layer = new PGeoNameLayer
		item = new JCheckBoxMenuItem("GeoName P", false)
		layer.setEnabled(false)
		AppCeemRadarFrame.wwd.getModel.getLayers.add(layer)
		item.addActionListener(new ActionListener {
			def actionPerformed(e: ActionEvent) {
				val item: JCheckBoxMenuItem = e.getSource.asInstanceOf[JCheckBoxMenuItem]
				layer.setEnabled(item.isSelected)
			}
		})
		buff += item


		item = new JCheckBoxMenuItem("Admin Border Country", false)
		AdminBorderLayerController.apply(AppCeemRadarFrame.wwd, item)

		buff += item

	}
}
