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
 * Created by Aleo on 21.04.2014.
 */
class CeemRadarAirspaceEditor(val ceemRadarAirspace: CeemRadarAirspace) extends AirspaceEditor {

	val radarAirspaceEditor = ceemRadarAirspace.editorRadarAirspace
	val isolineAirspaceEditor = ceemRadarAirspace.editorIsolineAirspace

	override def propertyChange(evt: PropertyChangeEvent): Unit = ???

	override def dispose(): Unit = ???

	override def onMessage(msg: Message): Unit = ???

	override def resizeAtControlPoint(wwd: WorldWindow, controlPoint: AirspaceControlPoint, mousePoint: Point, previousMousePoint: Point): Unit = ???

	override def moveControlPoint(wwd: WorldWindow, controlPoint: AirspaceControlPoint, mousePoint: Point, previousMousePoint: Point): Unit = ???

	override def removeControlPoint(wwd: WorldWindow, controlPoint: AirspaceControlPoint): Unit = ???

	override def addControlPoint(wwd: WorldWindow, airspace: Airspace, mousePoint: Point): AirspaceControlPoint = ???

	override def moveAirspaceVertically(wwd: WorldWindow, airspace: Airspace, mousePoint: Point, previousMousePoint: Point): Unit = ???

	override def moveAirspaceLaterally(wwd: WorldWindow, airspace: Airspace, mousePoint: Point, previousMousePoint: Point): Unit = ???

	override def removeEditListener(listener: AirspaceEditListener): Unit = ???

	override def addEditListener(listener: AirspaceEditListener): Unit = ???

	override def getEditListeners: Array[AirspaceEditListener] = ???

	override def setControlPointRenderer(renderer: AirspaceControlPointRenderer): Unit = ???

	override def getControlPointRenderer: AirspaceControlPointRenderer = ???

	override def setKeepControlPointsAboveTerrain(state: Boolean): Unit = ???

	override def isKeepControlPointsAboveTerrain: Boolean = ???

	override def setUseRubberBand(state: Boolean): Unit = ???

	override def isUseRubberBand: Boolean = ???

	override def setArmed(armed: Boolean): Unit = ???

	override def isArmed: Boolean = ???

	override def getAirspace: Airspace = ???

	override def clearList(): AVList = ???

	override def copy(): AVList = ???

	override def firePropertyChange(propertyChangeEvent: PropertyChangeEvent): Unit = ???

	override def firePropertyChange(propertyName: String, oldValue: scala.Any, newValue: scala.Any): Unit = ???

	override def removePropertyChangeListener(listener: PropertyChangeListener): Unit = ???

	override def addPropertyChangeListener(listener: PropertyChangeListener): Unit = ???

	override def removePropertyChangeListener(propertyName: String, listener: PropertyChangeListener): Unit = ???

	override def addPropertyChangeListener(propertyName: String, listener: PropertyChangeListener): Unit = ???

	override def removeKey(key: String): AnyRef = ???

	override def hasKey(key: String): Boolean = ???

	override def getEntries: util.Set[Entry[String, AnyRef]] = ???

	override def getStringValue(key: String): String = ???

	override def getValues: util.Collection[AnyRef] = ???

	override def getValue(key: String): AnyRef = ???

	override def setValues(avList: AVList): AVList = ???

	override def setValue(key: String, value: scala.Any): AnyRef = ???

	override def restoreState(stateInXml: String): Unit = ???

	override def getRestorableState: String = ???

	override def getMinEffectiveAltitude(radius: lang.Double): lang.Double = ???

	override def getMaxEffectiveAltitude(radius: lang.Double): lang.Double = ???

	override def isLayerActive(dc: DrawContext): Boolean = ???

	override def isLayerInView(dc: DrawContext): Boolean = ???

	override def setMaxActiveAltitude(maxActiveAltitude: Double): Unit = ???

	override def getMaxActiveAltitude: Double = ???

	override def setMinActiveAltitude(minActiveAltitude: Double): Unit = ???

	override def getMinActiveAltitude: Double = ???

	override def getExpiryTime: Long = ???

	override def setExpiryTime(expiryTime: Long): Unit = ???

	override def setNetworkRetrievalEnabled(networkRetrievalEnabled: Boolean): Unit = ???

	override def isNetworkRetrievalEnabled: Boolean = ???

	override def getScale: Double = ???

	override def isMultiResolution: Boolean = ???

	override def isAtMaxResolution: Boolean = ???

	override def pick(dc: DrawContext, pickPoint: Point): Unit = ???

	override def render(dc: DrawContext): Unit = ???

	override def preRender(dc: DrawContext): Unit = ???

	override def setPickEnabled(isPickable: Boolean): Unit = ???

	override def isPickEnabled: Boolean = ???

	override def setOpacity(opacity: Double): Unit = ???

	override def getOpacity: Double = ???

	override def setName(name: String): Unit = ???

	override def getName: String = ???

	override def setEnabled(enabled: Boolean): Unit = ???

	override def isEnabled: Boolean = ???
}
