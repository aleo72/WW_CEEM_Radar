/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.factories

import ua.edu.odeku.ceem.mapRadar.models.radar.Radar
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.render.airspaces.Airspace
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace._
import gov.nasa.worldwind.geom.{Position, LatLon}
import gov.nasa.worldwindx.examples.util.ShapeUtils
import gov.nasa.worldwind.avlist.AVKey
import gov.nasa.worldwind.render.airspaces.editor.AirspaceEditor
import gov.nasa.worldwind.render.Material
import gov.nasa.worldwind.util.WWUtil
import java.awt.Color

/**
 *
 * Created by Aleo on 23.04.2014.
 */
class IsolineAirspaceFactory(radar: Radar, wwd: WorldWindow, fitShapeToViewport: Boolean) extends AirspaceFactory(radar, wwd, fitShapeToViewport) {

	final val DEFAULT_SHAPE_SIZE_METERS: Double = 200000.0

	private var countIsoline = 1

	def createAirspace(wwd: WorldWindow, fitShapeToViewport: Boolean): Airspace = {
		val isoLineAirspace = new IsolineAirspace
		isoLineAirspace.setAttributes(this.initAttribute(isoLineAirspace))
		isoLineAirspace.setValue(AVKey.DISPLAY_NAME, this.toString + countIsoline)
		countIsoline += 1
		isoLineAirspace.setAltitudes(100000.0, 500000.0)
		isoLineAirspace.setTerrainConforming(false)
		this.initializeSphere(wwd, isoLineAirspace, fitShapeToViewport)
		isoLineAirspace
	}

	def createEditor(airspace: Airspace): AirspaceEditor = null

	private def initializeSphere(wwd: WorldWindow, airspace: LocationAirspace with RadiusAirspace, fitShapeToViewport: Boolean) {
		val position: Position = ShapeUtils.getNewShapePosition(wwd)
		val sizeInMeters: Double = if (fitShapeToViewport) ShapeUtils.getViewportScaleFactor(wwd) else radar.radius
		airspace.location = new LatLon(position)
		airspace.radius = sizeInMeters
	}

	override def toString = {
		"Isoline "
	}

	private def initAttribute(a: IsolineAirspace) = {
		a.getAttributes.setDrawOutline(false)
		a.getAttributes.setMaterial(new Material(Color.MAGENTA))
		//		a.getAttributes.setOutlineMaterial(new Material(WWUtil.makeColorBrighter(Color.MAGENTA)))
		a.getAttributes.setOpacity(0.8)
		a.getAttributes.setOutlineOpacity(0.9)
		a.getAttributes.setOutlineWidth(3.0)
		a.getAttributes
	}
}
