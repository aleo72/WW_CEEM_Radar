/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.airspace

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
	val isolineAirspaceEditor = radarAirspaceEditor
	val radar = ceemRadarAirspace.radar

	private val editors = Array(radarAirspaceEditor, isolineAirspaceEditor)

	private def enabledAirspaceEditor: AirspaceEditor = {
		if (radarAirspaceEditor.isEnabled) {
			radarAirspaceEditor
		} else if (isolineAirspaceEditor.isEnabled) {
			isolineAirspaceEditor
		} else {
			throw new IllegalArgumentException("Non visible airspace")
		}

	}

	override def propertyChange(evt: PropertyChangeEvent): Unit = editors.foreach(_.propertyChange(evt))

	override def dispose(): Unit = editors.foreach(_.dispose())

	override def onMessage(msg: Message): Unit = editors.foreach(_.onMessage(msg))

	override def resizeAtControlPoint(wwd: WorldWindow, controlPoint: AirspaceControlPoint, mousePoint: Point, previousMousePoint: Point): Unit = editors.foreach(_.resizeAtControlPoint(wwd, controlPoint, mousePoint, previousMousePoint))

	override def moveControlPoint(wwd: WorldWindow, controlPoint: AirspaceControlPoint, mousePoint: Point, previousMousePoint: Point): Unit = {
    editors.foreach(_.moveControlPoint(wwd, controlPoint, mousePoint, previousMousePoint))
  }

	override def removeControlPoint(wwd: WorldWindow, controlPoint: AirspaceControlPoint): Unit = editors.foreach(_.removeControlPoint(wwd, controlPoint))

	override def addControlPoint(wwd: WorldWindow, airspace: Airspace, mousePoint: Point): AirspaceControlPoint = {
		val v = enabledAirspaceEditor.addControlPoint(wwd, airspace, mousePoint)
		editors.foreach(_.addControlPoint(wwd, airspace, mousePoint))
		v
	}

	override def moveAirspaceVertically(wwd: WorldWindow, airspace: Airspace, mousePoint: Point, previousMousePoint: Point): Unit = {
    editors.foreach(_.moveAirspaceVertically(wwd, airspace, mousePoint, previousMousePoint))
  }

	override def moveAirspaceLaterally(wwd: WorldWindow, airspace: Airspace, mousePoint: Point, previousMousePoint: Point): Unit = {
    editors.foreach(_.moveAirspaceLaterally(wwd, airspace, mousePoint, previousMousePoint))
  }

	override def removeEditListener(listener: AirspaceEditListener): Unit = editors.foreach(_.removeEditListener(listener))

	override def addEditListener(listener: AirspaceEditListener): Unit = editors.foreach(_.addEditListener(listener))

	override def getEditListeners: Array[AirspaceEditListener] = enabledAirspaceEditor.getEditListeners

	override def setControlPointRenderer(renderer: AirspaceControlPointRenderer): Unit = editors.foreach(_.setControlPointRenderer(renderer))

	override def getControlPointRenderer: AirspaceControlPointRenderer = enabledAirspaceEditor.getControlPointRenderer

	override def setKeepControlPointsAboveTerrain(state: Boolean): Unit = editors.foreach(_.setKeepControlPointsAboveTerrain(state))

	override def isKeepControlPointsAboveTerrain: Boolean = enabledAirspaceEditor.isKeepControlPointsAboveTerrain

	override def setUseRubberBand(state: Boolean): Unit = editors.foreach(_.setUseRubberBand(state))

	override def isUseRubberBand: Boolean = enabledAirspaceEditor.isUseRubberBand

	override def setArmed(armed: Boolean): Unit = editors.foreach(_.setArmed(armed))

	override def isArmed: Boolean = enabledAirspaceEditor.isArmed

	override def getAirspace: Airspace = enabledAirspaceEditor.getAirspace

	override def clearList(): AVList = {
		val list = enabledAirspaceEditor.clearList()
		editors.foreach(_.clearList())
		list
	}

	override def copy(): AVList = enabledAirspaceEditor.copy()

	override def firePropertyChange(propertyChangeEvent: PropertyChangeEvent): Unit = editors.foreach(_.firePropertyChange(propertyChangeEvent))

	override def firePropertyChange(propertyName: String, oldValue: scala.Any, newValue: scala.Any): Unit = editors.foreach(_.firePropertyChange(propertyName, oldValue, newValue))

	override def removePropertyChangeListener(listener: PropertyChangeListener): Unit = editors.foreach(_.removePropertyChangeListener(listener))

	override def addPropertyChangeListener(listener: PropertyChangeListener): Unit = editors.foreach(_.addPropertyChangeListener(listener))

	override def removePropertyChangeListener(propertyName: String, listener: PropertyChangeListener): Unit = editors.foreach(_.removePropertyChangeListener(propertyName, listener))

	override def addPropertyChangeListener(propertyName: String, listener: PropertyChangeListener): Unit = editors.foreach(_.addPropertyChangeListener(propertyName, listener))

	override def removeKey(key: String): AnyRef = {
		val v = enabledAirspaceEditor.removeKey(key)
		editors.foreach(_.removeKey(key))
		v
	}

	override def hasKey(key: String): Boolean = enabledAirspaceEditor.hasKey(key)

	override def getEntries: util.Set[Entry[String, AnyRef]] = enabledAirspaceEditor.getEntries

	override def getStringValue(key: String): String = enabledAirspaceEditor.getStringValue(key)

	override def getValues: util.Collection[AnyRef] = enabledAirspaceEditor.getValues

	override def getValue(key: String): AnyRef = enabledAirspaceEditor.getValue(key)

	override def setValues(avList: AVList): AVList = {
		val v = enabledAirspaceEditor.setValues(avList)
		editors.foreach(_.setValues(avList))
		v
	}

	override def setValue(key: String, value: scala.Any): AnyRef = {
		val v = enabledAirspaceEditor.setValue(key, value)
		editors.foreach(_.setValue(key, value))
		v
	}

	override def restoreState(stateInXml: String): Unit = editors.foreach(_.restoreState(stateInXml))

	override def getRestorableState: String = enabledAirspaceEditor.getRestorableState

	override def getMinEffectiveAltitude(radius: lang.Double): lang.Double = enabledAirspaceEditor.getMinEffectiveAltitude(radius)

	override def getMaxEffectiveAltitude(radius: lang.Double): lang.Double = enabledAirspaceEditor.getMaxEffectiveAltitude(radius)

	override def isLayerActive(dc: DrawContext): Boolean = enabledAirspaceEditor.isLayerActive(dc)

	override def isLayerInView(dc: DrawContext): Boolean = enabledAirspaceEditor.isLayerActive(dc)

	override def setMaxActiveAltitude(maxActiveAltitude: Double): Unit = editors.foreach(_.setMaxActiveAltitude(maxActiveAltitude))

	override def getMaxActiveAltitude: Double = enabledAirspaceEditor.getMaxActiveAltitude

	override def setMinActiveAltitude(minActiveAltitude: Double): Unit = editors.foreach(_.setMinActiveAltitude(minActiveAltitude))

	override def getMinActiveAltitude: Double = enabledAirspaceEditor.getMinActiveAltitude

	override def getExpiryTime: Long = enabledAirspaceEditor.getExpiryTime

	override def setExpiryTime(expiryTime: Long): Unit = editors.foreach(_.setExpiryTime(expiryTime))

	override def setNetworkRetrievalEnabled(networkRetrievalEnabled: Boolean): Unit = editors.foreach(_.setNetworkRetrievalEnabled(networkRetrievalEnabled))

	override def isNetworkRetrievalEnabled: Boolean = enabledAirspaceEditor.isNetworkRetrievalEnabled

	override def getScale: Double = enabledAirspaceEditor.getScale

	override def isMultiResolution: Boolean = enabledAirspaceEditor.isMultiResolution

	override def isAtMaxResolution: Boolean = enabledAirspaceEditor.isAtMaxResolution

	override def pick(dc: DrawContext, pickPoint: Point): Unit = editors.foreach(_.pick(dc, pickPoint))

	override def render(dc: DrawContext): Unit = editors.foreach(_.render(dc))

	override def preRender(dc: DrawContext): Unit = editors.foreach(_.preRender(dc))

	override def setPickEnabled(isPickable: Boolean): Unit = editors.foreach(_.setPickEnabled(isPickEnabled))

	override def isPickEnabled: Boolean = enabledAirspaceEditor.isPickEnabled

	override def setOpacity(opacity: Double): Unit = editors.foreach(_.setOpacity(opacity))

	override def getOpacity: Double = enabledAirspaceEditor.getOpacity

	override def setName(name: String): Unit = editors.foreach(_.setName(name))

	override def getName: String = enabledAirspaceEditor.getName

	override def setEnabled(enabled: Boolean): Unit = editors.foreach(_.setEnabled(enabled))

	override def isEnabled: Boolean = enabledAirspaceEditor.isEnabled
}
