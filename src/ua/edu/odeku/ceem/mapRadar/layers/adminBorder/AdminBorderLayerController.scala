/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.layers.adminBorder

import gov.nasa.worldwind.WorldWindow
import javax.swing.JCheckBoxMenuItem
import gov.nasa.worldwind.layers.AirspaceLayer
import gov.nasa.worldwind.render.airspaces.{Curtain, Airspace}
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.manager.AdminBorderManager
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.{Polygon, Admin0}
import scala.collection.mutable.ArrayBuffer
import gov.nasa.worldwind.geom.LatLon
import gov.nasa.worldwind.avlist.AVKey
import java.awt.Color
import gov.nasa.worldwind.render.Material
import gov.nasa.worldwind.util.WWUtil
import java.util
import java.awt.event.{ActionEvent, ActionListener}

/**
 * Объект для управлением за слоями отображеня границ
 *
 * Created by Aleo on 30.03.2014.
 */
object AdminBorderLayerController {

	val airspaceLayers = new AirspaceLayer
	airspaceLayers.setEnableBatchPicking(false)


	def apply(world: WorldWindow, menuItem: JCheckBoxMenuItem) {

		// Добавим если не существует
		if (!world.getModel.getLayers.contains(airspaceLayers)) {
			world.getModel.getLayers.add(world.getModel.getLayers.size(), airspaceLayers)
		}
		menuItem.addActionListener(new ActionListener {
			override def actionPerformed(e: ActionEvent): Unit = {
				airspaceLayers.removeAllAirspaces()
				if(e.getSource.asInstanceOf[JCheckBoxMenuItem].isSelected) {
					val collectionsOfAirspace: Array[Airspace] = createCollectionOfAirspaces()
					for (airspace <- collectionsOfAirspace) {
						airspaceLayers.addAirspace(airspace)
					}
				}
				world.redraw()
			}
		})
	}

	def createCollectionOfAirspaces(): Array[Airspace] = {
		val arrayBuffer = new ArrayBuffer[Airspace]()

		for ((iso, enabled) <- AdminBorderManager.viewCountryBorder.filter(_._2)) {
			val admin0: Admin0 = AdminBorderManager.admin(iso)
			for(polygon <- admin0.polygons){
				val airspace: Airspace = createAirspace(admin0, polygon)
				arrayBuffer += airspace
			}

		}

		arrayBuffer.toArray
	}

	def createAirspace(admin: Admin0, polygon: Polygon): Airspace = {
		val border = new Curtain
		border.setLocations(createLocations(polygon))
		border.setAltitudes(0.0, 100.0)
		border.setTerrainConforming(false, true)
		border.setValue(AVKey.DISPLAY_NAME, admin.name)
		setupDefaultMaterial(border, Color.CYAN)
		border
	}

	def createLocations(polygon: Polygon): java.util.LinkedList[LatLon] = {
		val list = new util.LinkedList[LatLon]()
		for(coordinates <- polygon.listCoordinates){
			list.add(LatLon.fromDegrees(coordinates._1, coordinates._2))
		}
		list
	}

	private def setupDefaultMaterial(airspace: Airspace, color: Color){
		val attr = airspace.getAttributes
		attr.setDrawOutline(true)
		attr.setMaterial(new Material(color))
		attr.setOutlineMaterial(new Material(color))
		attr.setOpacity(0.8)
		attr.setOutlineOpacity(0.5)
		attr.setOutlineWidth(3.0)
	}
}
