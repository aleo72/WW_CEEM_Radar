/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import gov.nasa.worldwind.geom.{Extent, LatLon}
import scala.beans.BeanProperty
import ua.edu.odeku.ceem.mapRadar.models.radar.Radar
import gov.nasa.worldwind.render.airspaces._
import gov.nasa.worldwind.render.airspaces.editor.AirspaceEditor
import gov.nasa.worldwind.avlist.AVList
import java.util
import java.util.Map.Entry
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import java.lang.Iterable
import gov.nasa.worldwind.render.DrawContext
import gov.nasa.worldwind.globes.Globe
import scala.collection.mutable.ArrayBuffer

/**
 * Класс который скрывает в себе возможноть различного режима отображения
 *
 * Created by Aleo on 21.04.2014.
 */
class CeemRadarAirspace(val radar: Radar, val radarAirspace: RadarAirspace, val editorRadarAirspace: AirspaceEditor, val isolineAirspace: IsolineAirspace, val editorIsolineAirspace: AirspaceEditor) extends Airspace {

	private val airspaces = Array(radarAirspace, isolineAirspace)

	val editor = new CeemRadarAirspaceEditor(this)

	CeemRadarAirspace.listOfCeemRadarAirspace += this

	showRadarAirspace()

	def radarAirspaceAs[T] = radarAirspace.asInstanceOf[T]

	def isolineAirspaceAs[T] = isolineAirspace.asInstanceOf[T]

	@BeanProperty
	def location: LatLon = radar.latLon

	def location_=(latLon: LatLon): Unit = {
		radar.latLon = latLon
		radarAirspace.locationCenter = latLon
		isolineAirspace.locationCenter = latLon
	}

	@BeanProperty
	def radius = radarAirspace.radiusAirspace

	@BeanProperty
	def radius_=(d: Double): Unit = radarAirspace.radiusAirspace = d

	def visibleAirspace: Airspace = {
		if (radarAirspace.isVisible) {
			radarAirspace
		} else if (isolineAirspace.isVisible) {
			isolineAirspace
		} else {
			throw new IllegalArgumentException("Non visible airspace")
		}
	}

	def showRadarAirspace() {
		this.setVisible(visible = false)
		radarAirspace.setVisible(true)
	}

	def showIsolineAirspace() {
		this.setVisible(visible = false)
		updateIsolineAirspace()
		isolineAirspace.setVisible(true)
	}

	def updateIsolineAirspace(): Unit = {
		this.isolineAirspace.locationCenter = radarAirspace.locationCenter
		this.isolineAirspace.setAltitude(10000)
	}

	def remove(): Unit = CeemRadarAirspace.remove(this)

	override def render(dc: DrawContext): Unit = airspaces.foreach(_.render(dc))

	override def getGroundReference: LatLon = visibleAirspace.getGroundReference

	override def setGroundReference(groundReference: LatLon): Unit = airspaces.foreach(_.setGroundReference(groundReference))

	override def getAltitudeDatum: Array[String] = visibleAirspace.getAltitudeDatum

	override def setAltitudeDatum(lowerAltitudeDatum: String, upperAltitudeDatum: String): Unit = airspaces.foreach(_.setAltitudeDatum(lowerAltitudeDatum, upperAltitudeDatum))

	override def renderExtent(dc: DrawContext): Unit = airspaces.foreach(_.renderExtent(dc))

	override def renderGeometry(dc: DrawContext, drawStyle: String): Unit = airspaces.foreach(_.renderGeometry(dc, drawStyle))

	override def makeOrderedRenderable(dc: DrawContext, renderer: AirspaceRenderer): Unit = airspaces.foreach(_.makeOrderedRenderable(dc, renderer))

	override def getExtent(dc: DrawContext): Extent = visibleAirspace.getExtent(dc)

	override def getExtent(globe: Globe, verticalExaggeration: Double): Extent = visibleAirspace.getExtent(globe, verticalExaggeration)

	override def isAirspaceVisible(dc: DrawContext): Boolean = visibleAirspace.isAirspaceVisible(dc)

	override def setDetailLevels(detailLevels: util.Collection[DetailLevel]): Unit = airspaces.foreach(_.setDetailLevels(detailLevels))

	override def getDetailLevels: Iterable[DetailLevel] = visibleAirspace.getDetailLevels

	override def setEnableLevelOfDetail(enableLevelOfDetail: Boolean): Unit = airspaces.foreach(_.setEnableLevelOfDetail(enableLevelOfDetail))

	override def isEnableLevelOfDetail: Boolean = visibleAirspace.isEnableLevelOfDetail

	override def setTerrainConforming(terrainConformant: Boolean): Unit = airspaces.foreach(_.setTerrainConforming(terrainConformant))

	override def setTerrainConforming(lowerTerrainConformant: Boolean, upperTerrainConformant: Boolean): Unit = airspaces.foreach(_.setTerrainConforming(lowerTerrainConformant, upperTerrainConformant))

	override def isTerrainConforming: Array[Boolean] = visibleAirspace.isTerrainConforming

	override def setAltitude(altitude: Double): Unit = airspaces.foreach(_.setAltitude(altitude))

	override def setAltitudes(lowerAltitude: Double, upperAltitude: Double): Unit = airspaces.foreach(_.setAltitudes(lowerAltitude, upperAltitude))

	override def getAltitudes: Array[Double] = visibleAirspace.getAltitudes

	override def setAttributes(attributes: AirspaceAttributes): Unit = airspaces.foreach(_.setAttributes(attributes))

	override def getAttributes: AirspaceAttributes = visibleAirspace.getAttributes

	override def setVisible(visible: Boolean): Unit = airspaces.foreach(_.setVisible(visible))

	override def isVisible: Boolean = visibleAirspace.isVisible

	override def clearList(): AVList = {
		val oldList = visibleAirspace.clearList()
		airspaces.foreach(_.clearList())
		oldList
	}

	override def copy(): AVList = visibleAirspace.copy()

	override def firePropertyChange(propertyChangeEvent: PropertyChangeEvent): Unit = airspaces.foreach(_.firePropertyChange(propertyChangeEvent))

	override def firePropertyChange(propertyName: String, oldValue: scala.Any, newValue: scala.Any): Unit = airspaces.foreach(_.firePropertyChange(propertyName, oldValue, newValue))

	override def removePropertyChangeListener(listener: PropertyChangeListener): Unit = airspaces.foreach(_.removePropertyChangeListener(listener))

	override def addPropertyChangeListener(listener: PropertyChangeListener): Unit = airspaces.foreach(_.addPropertyChangeListener(listener))

	override def removePropertyChangeListener(propertyName: String, listener: PropertyChangeListener): Unit = airspaces.foreach(_.removePropertyChangeListener(propertyName, listener))

	override def addPropertyChangeListener(propertyName: String, listener: PropertyChangeListener): Unit = airspaces.foreach(_.addPropertyChangeListener(propertyName, listener))

	override def removeKey(key: String): AnyRef = {
		val k = visibleAirspace.removeKey(key)
		airspaces.foreach(_.removeKey(key))
		k
	}

	override def hasKey(key: String): Boolean = visibleAirspace.hasKey(key)

	override def getEntries: util.Set[Entry[String, AnyRef]] = visibleAirspace.getEntries

	override def getStringValue(key: String): String = visibleAirspace.getStringValue(key)

	override def getValues: util.Collection[AnyRef] = visibleAirspace.getValues

	override def getValue(key: String): AnyRef = visibleAirspace.getValue(key)

	override def setValues(avList: AVList): AVList = {
		val oldValues = visibleAirspace.setValues(avList)
		airspaces.foreach(_.setValues(avList))
		oldValues
	}

	override def setValue(key: String, value: scala.Any): AnyRef = {
		val old = visibleAirspace.setValue(key, value)
		airspaces.foreach(_.setValue(key, value))
		old
	}

	override def restoreState(stateInXml: String): Unit = airspaces.foreach(_.restoreState(stateInXml))

	override def getRestorableState: String = visibleAirspace.getRestorableState

	override def equals(o: Any): Boolean = o match {
		case air: CeemRadarAirspace => this.radar == air.radar
		case _ => false
	}

	override def hashCode: Int = radar.hashCode()
}

object CeemRadarAirspace {

	private val listOfCeemRadarAirspace = new ArrayBuffer[CeemRadarAirspace]()

	def showIsolineView(): Unit = listOfCeemRadarAirspace.foreach(_.showIsolineAirspace())

	def showRadarView(): Unit = listOfCeemRadarAirspace.foreach(_.showRadarAirspace())

	def list: Array[CeemRadarAirspace] = listOfCeemRadarAirspace.toArray

	def remove(airspace: CeemRadarAirspace): Unit = listOfCeemRadarAirspace -= airspace
}