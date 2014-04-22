/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import gov.nasa.worldwind.render.airspaces.editor.{AirspaceControlPoint, AirspaceEditListener, AirspaceControlPointRenderer, AirspaceEditor}
import gov.nasa.worldwind.render.DrawContext
import java.awt.Point
import java.{util, lang}
import gov.nasa.worldwind.avlist.AVList
import java.util.Map.Entry
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import gov.nasa.worldwind.render.airspaces.Airspace
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.event.Message

/**
 *  Класс который скрывает в себе возможноть различного режима отображения
 *
 * Created by Aleo on 21.04.2014.
 */
class CeemRadarAirspaceEditor(val ceemRadarAirspace: CeemRadarAirspace) extends AirspaceEditor {

	val radarAirspaceEditor = ceemRadarAirspace.editorRadarAirspace
	val isolineAirspaceEditor = ceemRadarAirspace.editorIsolineAirspace
	val radar = ceemRadarAirspace.radar

	private val editors = Array(radarAirspaceEditor, isolineAirspaceEditor)

	private def editorOfVisibleAirspace: AirspaceEditor = {
		if (ceemRadarAirspace.radarAirspace.isVisible) {
			radarAirspaceEditor
		} else if (ceemRadarAirspace.isolineAirspace.isVisible) {
			isolineAirspaceEditor
		} else {
			throw new IllegalArgumentException("Non visible airspace")
		}

	}

	override def propertyChange(evt: PropertyChangeEvent): Unit = editors.foreach(_.propertyChange(evt))

	override def dispose(): Unit = editors.foreach(_.dispose())

	override def onMessage(msg: Message): Unit = editors.foreach(_.onMessage(msg))

	override def resizeAtControlPoint(wwd: WorldWindow, controlPoint: AirspaceControlPoint, mousePoint: Point, previousMousePoint: Point): Unit = editors.foreach(_.resizeAtControlPoint(wwd, controlPoint, mousePoint, previousMousePoint))

	override def moveControlPoint(wwd: WorldWindow, controlPoint: AirspaceControlPoint, mousePoint: Point, previousMousePoint: Point): Unit = editors.foreach(_.moveControlPoint(wwd, controlPoint, mousePoint, previousMousePoint))

	override def removeControlPoint(wwd: WorldWindow, controlPoint: AirspaceControlPoint): Unit = editors.foreach(_.removeControlPoint(wwd, controlPoint))

	override def addControlPoint(wwd: WorldWindow, airspace: Airspace, mousePoint: Point): AirspaceControlPoint = {
		val v = editorOfVisibleAirspace.addControlPoint(wwd, airspace, mousePoint)
		editors.foreach(_.addControlPoint(wwd, airspace, mousePoint))
		v
	}

	override def moveAirspaceVertically(wwd: WorldWindow, airspace: Airspace, mousePoint: Point, previousMousePoint: Point): Unit = editors.foreach(_.moveAirspaceVertically(wwd, airspace, mousePoint, previousMousePoint))

	override def moveAirspaceLaterally(wwd: WorldWindow, airspace: Airspace, mousePoint: Point, previousMousePoint: Point): Unit = editors.foreach(_.moveAirspaceLaterally(wwd, airspace, mousePoint, previousMousePoint))

	override def removeEditListener(listener: AirspaceEditListener): Unit = editors.foreach(_.removeEditListener(listener))

	override def addEditListener(listener: AirspaceEditListener): Unit = editors.foreach(_.addEditListener(listener))

	override def getEditListeners: Array[AirspaceEditListener] = editorOfVisibleAirspace.getEditListeners

	override def setControlPointRenderer(renderer: AirspaceControlPointRenderer): Unit = editors.foreach(_.setControlPointRenderer(renderer))

	override def getControlPointRenderer: AirspaceControlPointRenderer = editorOfVisibleAirspace.getControlPointRenderer

	override def setKeepControlPointsAboveTerrain(state: Boolean): Unit = editors.foreach(_.setKeepControlPointsAboveTerrain(state))

	override def isKeepControlPointsAboveTerrain: Boolean = editorOfVisibleAirspace.isKeepControlPointsAboveTerrain

	override def setUseRubberBand(state: Boolean): Unit = editors.foreach(_.setUseRubberBand(state))

	override def isUseRubberBand: Boolean = editorOfVisibleAirspace.isUseRubberBand

	override def setArmed(armed: Boolean): Unit = editors.foreach(_.setArmed(armed))

	override def isArmed: Boolean = editorOfVisibleAirspace.isArmed

	override def getAirspace: Airspace = editorOfVisibleAirspace.getAirspace

	override def clearList(): AVList = {
		val list = editorOfVisibleAirspace.clearList()
		editors.foreach(_.clearList())
		list
	}

	override def copy(): AVList = editorOfVisibleAirspace.copy()

	override def firePropertyChange(propertyChangeEvent: PropertyChangeEvent): Unit = editors.foreach(_.firePropertyChange(propertyChangeEvent))

	override def firePropertyChange(propertyName: String, oldValue: scala.Any, newValue: scala.Any): Unit = editors.foreach(_.firePropertyChange(propertyName, oldValue, newValue))

	override def removePropertyChangeListener(listener: PropertyChangeListener): Unit = editors.foreach(_.removePropertyChangeListener(listener))

	override def addPropertyChangeListener(listener: PropertyChangeListener): Unit = editors.foreach(_.addPropertyChangeListener(listener))

	override def removePropertyChangeListener(propertyName: String, listener: PropertyChangeListener): Unit = editors.foreach(_.removePropertyChangeListener(propertyName, listener))

	override def addPropertyChangeListener(propertyName: String, listener: PropertyChangeListener): Unit = editors.foreach(_.addPropertyChangeListener(propertyName, listener))

	override def removeKey(key: String): AnyRef = {
		val v = editorOfVisibleAirspace.removeKey(key)
		editors.foreach(_.removeKey(key))
		v
	}

	override def hasKey(key: String): Boolean = editorOfVisibleAirspace.hasKey(key)

	override def getEntries: util.Set[Entry[String, AnyRef]] = editorOfVisibleAirspace.getEntries

	override def getStringValue(key: String): String = editorOfVisibleAirspace.getStringValue(key)

	override def getValues: util.Collection[AnyRef] = editorOfVisibleAirspace.getValues

	override def getValue(key: String): AnyRef = editorOfVisibleAirspace.getValue(key)

	override def setValues(avList: AVList): AVList = {
		val v = editorOfVisibleAirspace.setValues(avList)
		editors.foreach(_.setValues(avList))
		v
	}

	override def setValue(key: String, value: scala.Any): AnyRef = {
		val v = editorOfVisibleAirspace.setValue(key, value)
		editors.foreach(_.setValue(key, value))
		v
	}

	override def restoreState(stateInXml: String): Unit = editors.foreach(_.restoreState(stateInXml))

	override def getRestorableState: String = editorOfVisibleAirspace.getRestorableState

	override def getMinEffectiveAltitude(radius: lang.Double): lang.Double = editorOfVisibleAirspace.getMinEffectiveAltitude(radius)

	override def getMaxEffectiveAltitude(radius: lang.Double): lang.Double = editorOfVisibleAirspace.getMaxEffectiveAltitude(radius)

	override def isLayerActive(dc: DrawContext): Boolean = editorOfVisibleAirspace.isLayerActive(dc)

	override def isLayerInView(dc: DrawContext): Boolean = editorOfVisibleAirspace.isLayerActive(dc)

	override def setMaxActiveAltitude(maxActiveAltitude: Double): Unit = editors.foreach(_.setMaxActiveAltitude(maxActiveAltitude))

	override def getMaxActiveAltitude: Double = editorOfVisibleAirspace.getMaxActiveAltitude

	override def setMinActiveAltitude(minActiveAltitude: Double): Unit = editors.foreach(_.setMinActiveAltitude(minActiveAltitude))

	override def getMinActiveAltitude: Double = editorOfVisibleAirspace.getMinActiveAltitude

	override def getExpiryTime: Long = editorOfVisibleAirspace.getExpiryTime

	override def setExpiryTime(expiryTime: Long): Unit = editors.foreach(_.setExpiryTime(expiryTime))

	override def setNetworkRetrievalEnabled(networkRetrievalEnabled: Boolean): Unit = editors.foreach(_.setNetworkRetrievalEnabled(networkRetrievalEnabled))

	override def isNetworkRetrievalEnabled: Boolean = editorOfVisibleAirspace.isNetworkRetrievalEnabled

	override def getScale: Double = editorOfVisibleAirspace.getScale

	override def isMultiResolution: Boolean = editorOfVisibleAirspace.isMultiResolution

	override def isAtMaxResolution: Boolean = editorOfVisibleAirspace.isAtMaxResolution

	override def pick(dc: DrawContext, pickPoint: Point): Unit = editors.foreach(_.pick(dc, pickPoint))

	override def render(dc: DrawContext): Unit = editors.foreach(_.render(dc))

	override def preRender(dc: DrawContext): Unit = editors.foreach(_.preRender(dc))

	override def setPickEnabled(isPickable: Boolean): Unit = editors.foreach(_.setPickEnabled(isPickEnabled))

	override def isPickEnabled: Boolean = editorOfVisibleAirspace.isPickEnabled

	override def setOpacity(opacity: Double): Unit = editors.foreach(_.setOpacity(opacity))

	override def getOpacity: Double = editorOfVisibleAirspace.getOpacity

	override def setName(name: String): Unit = editors.foreach(_.setName(name))

	override def getName: String = editorOfVisibleAirspace.getName

	override def setEnabled(enabled: Boolean): Unit = editors.foreach(_.setEnabled(enabled))

	override def isEnabled: Boolean = editorOfVisibleAirspace.isEnabled
}
