/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.layers.adminBorder

import gov.nasa.worldwind.WorldWindow
import javax.swing.JCheckBoxMenuItem
import gov.nasa.worldwind.layers.{RenderableLayer, AirspaceLayer}
import gov.nasa.worldwind.render.airspaces.{Curtain, Airspace}
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.manager.AdminBorderManager
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.{Admin1, Polygon, Admin0}
import scala.collection.mutable.ArrayBuffer
import gov.nasa.worldwind.geom.LatLon
import java.awt.Color
import gov.nasa.worldwind.render._
import java.util
import java.awt.event.{ActionEvent, ActionListener}

/**
 * Объект для управлением за слоями отображеня границ
 *
 * Created by Aleo on 30.03.2014.
 */
object AdminBorderLayerController {

	val renderableLayer = new RenderableLayer
//	renderableLayer.setEnableBatchPicking(false)
	val foregroundAttrs: ShapeAttributes = new BasicShapeAttributes
	foregroundAttrs.setOutlineMaterial(new Material(Color.YELLOW))
	foregroundAttrs.setOutlineStipplePattern(0xAAAA.asInstanceOf[Short])
	foregroundAttrs.setOutlineStippleFactor(8)

	def apply(world: WorldWindow, menuItem: JCheckBoxMenuItem) {

		// Добавим если не существует
		if (!world.getModel.getLayers.contains(renderableLayer)) {
			world.getModel.getLayers.add(world.getModel.getLayers.size(), renderableLayer)
		}
		menuItem.addActionListener(new ActionListener {
			override def actionPerformed(e: ActionEvent): Unit = {
				renderableLayer.removeAllRenderables();
				if (e.getSource.asInstanceOf[JCheckBoxMenuItem].isSelected) {
					val collections: Array[Renderable] = createCollectionOfLines()
					for (border <- collections) {
						renderableLayer.addRenderable(border)
					}
				}
				world.redraw()
			}
		})
	}

	def createCollectionOfLines(): Array[Renderable] = {
		val arrayBuffer = new ArrayBuffer[Renderable]()

		for ((iso, enabled) <- AdminBorderManager.viewCountryBorder.filter(_._2)) {
			val admin0: Admin0 = AdminBorderManager.admin(iso)
			arrayBuffer ++= createBorders(admin0)
		}
		arrayBuffer.toArray
	}

	def createBorders(admins: Array[Admin1]): Array[Renderable] = {
		val buffer = new ArrayBuffer[Renderable]()
		for (admin <- admins) {
			buffer ++= createSurfacePolylines(admin.polygons)
			//buffer ++= createPolylines(admin.polygons)
		}
		buffer.toArray
	}

	def createBorders(admin0: Admin0): Array[Renderable] = {
		if (admin0.admin1Array.isEmpty) {
			createSurfacePolylines(admin0.polygons) //++: createPolylines(admin0.polygons)
		} else {
			createBorders(admin0.admin1Array)
		}
	}

	def createSurfacePolylines(polygons: Array[Polygon]): Array[Renderable] = {
		for {polygon <- polygons} yield createSurfacePolyline(polygon)
	}

	def createPolylines(polygons: Array[Polygon]): Array[Renderable] = {
		for {polygon <- polygons} yield createPolyline(polygon)
	}

	def createSurfacePolyline(polygon: Polygon): Renderable = {
		val border = new SurfacePolyline
		border.setLocations(createLocations(polygon))
		border.setClosed(true)
		border.setAttributes(foregroundAttrs)
		border
	}

	def createPolyline(polygon: Polygon): Renderable = {
		val border = new Polyline(createLocations(polygon), 0)
		border.setFollowTerrain(true)
		border.setClosed(true)
		border.setPathType(Polyline.RHUMB_LINE)
		border.setColor(foregroundAttrs.getOutlineMaterial.getDiffuse)
		border.setLineWidth(foregroundAttrs.getOutlineWidth)
		border.setStipplePattern(foregroundAttrs.getOutlineStipplePattern)
		border.setStippleFactor(foregroundAttrs.getOutlineStippleFactor)
		border
	}

	def createLocations(polygon: Polygon): java.util.LinkedList[LatLon] = {
		val list = new util.LinkedList[LatLon]()
		for (coordinates <- polygon.listCoordinates) {
			list.add(LatLon.fromDegrees(coordinates._1, coordinates._2))
		}
		list
	}

	def createLocationsForPolyLine(polygon: Polygon): java.util.LinkedList[LatLon] = {
		val list = new util.LinkedList[LatLon]()
		for (coordinates <- polygon.listCoordinates) {
			list.add(LatLon.fromDegrees(coordinates._1, coordinates._2).add(LatLon.fromDegrees(2,0)))
		}
		list
	}

	private def setupDefaultMaterial(airspace: Airspace, color: Color) {
		val attr = airspace.getAttributes
		attr.setDrawOutline(true)
		attr.setMaterial(new Material(color))
		attr.setOutlineMaterial(new Material(color))
		attr.setOpacity(0.8)
		attr.setOutlineOpacity(0.5)
		attr.setOutlineWidth(3.0)
	}
}
