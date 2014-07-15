/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.layers

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Color, Font}
import javax.swing.{JCheckBoxMenuItem, JMenu}

import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.layers.Layer
import ua.edu.odeku.ceem.mapRadar.layers.adminBorder.AdminBorderLayerController
import ua.edu.odeku.ceem.mapRadar.tools.geoName.GeoNamesFeatureClasses
import ua.edu.odeku.ceem.mapRadar.tools.geoName.layer.{GeoNameLayer, GeoNameLayerFactory}

/**
 * Created by ABakalov on 15.07.2014.
 */
object CeemRadarLayersManager {
  def insertGeoNameLayers(worldWindow: WorldWindow, menu: JMenu) {
    {
      val layer: Layer = GeoNameLayerFactory.createLayer(null, GeoNamesFeatureClasses.A, GeoNamesFeatureClasses.A.featureCodes.toList, Font.decode("Arial-BOLD-12"), Color.GREEN, GeoNameLayer.LEVEL_G, GeoNameLayer.LEVEL_C)
      val item: JCheckBoxMenuItem = new JCheckBoxMenuItem("GeoName A", false)
      layer.setEnabled(false)
      worldWindow.getModel.getLayers.add(layer)
      item.addActionListener(new ActionListener {
        def actionPerformed(e: ActionEvent) {
          val item: JCheckBoxMenuItem = e.getSource.asInstanceOf[JCheckBoxMenuItem]
          layer.setEnabled(item.isSelected)
        }
      })
      menu.add(item)
    }
    {
      val layer: Layer = GeoNameLayerFactory.createLayer(null, GeoNamesFeatureClasses.P, GeoNamesFeatureClasses.P.featureCodes.toList, Font.decode("Arial-BOLD-12"), Color.WHITE, 0.0, GeoNameLayer.LEVEL_I)
      val item: JCheckBoxMenuItem = new JCheckBoxMenuItem("GeoName P", false)
      layer.setEnabled(false)
      worldWindow.getModel.getLayers.add(layer)
      item.addActionListener(new ActionListener {
        def actionPerformed(e: ActionEvent) {
          val item: JCheckBoxMenuItem = e.getSource.asInstanceOf[JCheckBoxMenuItem]
          layer.setEnabled(item.isSelected)
        }
      })
      menu.add(item)
    }

    {
      val item: JCheckBoxMenuItem = new JCheckBoxMenuItem("Admin Border Country", false)
      AdminBorderLayerController.apply(worldWindow, item)
      menu.add(item)
    }
  }
}
