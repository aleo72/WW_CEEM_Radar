/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.menu

import java.awt.event.{ActionEvent, ActionListener, ItemEvent, ItemListener}
import java.awt.{Color, Font}
import javax.swing.{JCheckBoxMenuItem, JMenuItem}

import gov.nasa.worldwind.layers.Layer
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame
import ua.edu.odeku.ceem.mapRadar.layers.adminBorder.AdminBorderLayerController
import ua.edu.odeku.ceem.mapRadar.tools.geoName.GeoNamesFeatureClasses
import ua.edu.odeku.ceem.mapRadar.tools.geoName.layer.{GeoNameLayer, GeoNameLayerFactory}

import scala.collection.mutable.ArrayBuffer

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

		var layer: Layer = GeoNameLayerFactory.createLayer(null, GeoNamesFeatureClasses.A, GeoNamesFeatureClasses.A.featureCodes.toList, Font.decode("Arial-BOLD-12"), Color.GREEN, GeoNameLayer.LEVEL_G, GeoNameLayer.LEVEL_C)
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

		layer = GeoNameLayerFactory.createLayer(null, GeoNamesFeatureClasses.P, GeoNamesFeatureClasses.P.featureCodes.toList, Font.decode("Arial-BOLD-12"), Color.WHITE, 0.0, GeoNameLayer.LEVEL_I)
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
