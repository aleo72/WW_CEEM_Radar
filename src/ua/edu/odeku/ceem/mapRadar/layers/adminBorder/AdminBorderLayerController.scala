/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.layers.adminBorder

import java.awt.Color
import java.awt.event.{ActionEvent, ActionListener}
import java.util
import javax.swing.JCheckBoxMenuItem

import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.geom.LatLon
import gov.nasa.worldwind.layers.RenderableLayer
import gov.nasa.worldwind.render._
import gov.nasa.worldwind.render.airspaces.Airspace
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.models.Polygons

/**
 * Объект для управлением за слоями отображеня границ
 *
 * Created by Aleo on 30.03.2014.
 */
object AdminBorderLayerController {

  val renderableLayer = new RenderableLayer
  //	renderableLayer.setEnableBatchPicking(false)
  val countryForegroundAttrs: ShapeAttributes = new BasicShapeAttributes {
    setOutlineMaterial(new Material(Color.YELLOW))
    setOutlineStipplePattern(0xAAAA.asInstanceOf[Short])
    setOutlineStippleFactor(8)
  }

  val polygonForegroundAttrs: ShapeAttributes = new BasicShapeAttributes {
    setOutlineMaterial(new Material(Color.YELLOW))
    setOutlineStipplePattern(0xAAAA.asInstanceOf[Short])
    setOutlineStippleFactor(8)
  }

  def apply(world: WorldWindow, menuItem: JCheckBoxMenuItem) {

    // Добавим если не существует
    if (!world.getModel.getLayers.contains(renderableLayer)) {
      world.getModel.getLayers.add(world.getModel.getLayers.size(), renderableLayer)
    }
    menuItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        renderableLayer.removeAllRenderables()
        if (e.getSource.asInstanceOf[JCheckBoxMenuItem].isSelected) {
          for (border <- polygonsToArrayOfRenderable(Polygons.visibleCountryPolygons)) {
            border.setAttributes(countryForegroundAttrs)
            renderableLayer.addRenderable(border)
          }

          for (border <- polygonsToArrayOfRenderable(Polygons.visibleCountryPolygons)) {
            border.setAttributes(polygonForegroundAttrs)
            renderableLayer.addRenderable(border)
          }

        }
        world.redraw()
      }
    })
  }

  def polygonsToArrayOfRenderable(polygons: Iterable[Array[(Double, Double)]]) = {
    polygons.map(createRenderableFromCoordinates _)
  }

  def createRenderableFromCoordinates(coordinates: Array[(Double, Double)]) = {
    val corners = new util.ArrayList[LatLon]()
    for (coord <- coordinates) {
      corners.add(LatLon.fromDegrees(coord._1, coord._2))
    }
    createSurfacePolyline(corners)
  }

  def createSurfacePolyline(corners: util.ArrayList[LatLon]) = {
    val border = new SurfacePolyline
    border.setLocations(corners)
    border.setClosed(true)
    border
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
