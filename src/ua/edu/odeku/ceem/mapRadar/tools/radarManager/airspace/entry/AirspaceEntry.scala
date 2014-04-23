/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.entry

import gov.nasa.worldwind.{WorldWindow, WWObjectImpl}
import gov.nasa.worldwind.render.airspaces.{SphereAirspace, Airspace, AirspaceAttributes}
import gov.nasa.worldwind.avlist.AVKey
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.factories.{CeemRadarAirspaceFactory, AirspaceFactory}
import ua.edu.odeku.ceem.mapRadar.models.radar.Radar
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace._
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.dialogs.CreateEditRadarFrame
import gov.nasa.worldwind.render.airspaces.editor.{AirspaceEditorController, AirspaceEditListener, AirspaceEditor}
import gov.nasa.worldwind.geom.LatLon
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.ActionListeners.airspaceActionListeners.AirspaceChangeLocationOnFormListener
import gov.nasa.worldwind.layers.AirspaceLayer
import scala.collection.mutable.ArrayBuffer

/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:04
 */
class AirspaceEntry(val factory: CeemRadarAirspaceFactory) extends WWObjectImpl {

	val airspace: CeemRadarAirspace = factory.airspace
	val editor: AirspaceEditor = factory.editor.radarAirspaceEditor

	var _nameAirspaceEntry: String = airspace.getValue(AVKey.DISPLAY_NAME).toString

	val attributes: AirspaceAttributes = airspace.getAttributes
	private var _editing: Boolean = false
	private var _selected: Boolean = false
	var _intersecting: Boolean = false

	private var _radar: Radar = _

	AirspaceEntry.bufferOfAirspaceEntry += this

	def radar = _radar

	def radar_=(value: Radar): Unit = {
		_radar = value
		airspace.radius = _radar.radius
		airspace.setAltitude(_radar.altitude)
		if (_radar.latLon != null) {
			airspace.location = _radar.latLon
		}
	}

	def location = airspace.location

	def nameAirspaceEntry = _nameAirspaceEntry

	def nameAirspaceEntry_=(value: String): Unit = {
		_nameAirspaceEntry = value
		airspace.setValue(AVKey.DISPLAY_NAME, value)
	}

	def editing = _editing

	def editing_=(value: Boolean): Unit = {
		this._editing = value
		this.updateAttributes()
	}

	def selected = _selected

	def selected_=(value: Boolean): Unit = {
		this._selected = value
		this.updateAttributes()
	}

	def intersecting = _intersecting

	def intersecting_=(value: Boolean): Unit = {
		this._intersecting = value
		this.updateAttributes()
	}

	def name: String = this.getStringValue(AVKey.DISPLAY_NAME)

	def name_=(value: String): Unit = this.setValue(AVKey.DISPLAY_NAME, value)

	def updateAttributes() {
		if (this.selected && this.intersecting) {
			this.airspace.setAttributes(getSelectionAttributes)
		}
		else
		if (this.selected) {
			this.airspace.setAttributes(getSelectionAttributes)
		}
		else if (this.intersecting) {
			this.airspace.setAttributes(getDefaultAttributes)
		}
		else {
			this.airspace.setAttributes(this.attributes)
		}
	}

	override def toString = this.name

	override def getValue(key: String): AnyRef = {
		val value = super.getValue(key)
		if (value == null) this.airspace.getValue(key) else value
	}

	override def setValue(key: String, value: AnyRef): AnyRef = {
		if (AVKey.DISPLAY_NAME == key) {
			this.airspace.setValue(key, value)
		}
		else {
			super.setValue(key, value)
		}
	}

	def addAirspaceEditorListener(listener: AirspaceEditListener): Unit = editor.addEditListener(listener)

	def removeEditListener(listener: AirspaceEditListener): Unit = editor.removeEditListener(listener)

	def removeAirspaceFromAirspaceLayer(layer: AirspaceLayer) = {
		layer.removeAirspace(airspace.radarAirspace)
		layer.removeAirspace(airspace.isolineAirspace)
	}

	def addEditListener(controller: AirspaceController): Unit = this.editor.addEditListener(controller)

	def addAirspaceToAirspaceLayer(layer: AirspaceLayer): Unit = {
		layer.addAirspace(airspace.radarAirspace)
		layer.addAirspace(airspace.isolineAirspace)
	}

	def setArmedForAirspaceEditor(b: Boolean) = editor.setArmed(b)

	def remove(): Unit = AirspaceEntry.bufferOfAirspaceEntry -= this
}

object AirspaceEntry {

	val bufferOfAirspaceEntry = new ArrayBuffer[AirspaceEntry]()

	def showIsolineViewMode(b: Boolean): Unit = {
		if (b) {
			bufferOfAirspaceEntry.foreach(
				(entry: AirspaceEntry) => {
					entry.airspace.showIsolineAirspace()
					entry.editor.setEnabled(false)
				}
			)
		} else {
			bufferOfAirspaceEntry.foreach(
				(entry: AirspaceEntry) => {
					entry.airspace.showRadarAirspace()
					entry.editor.setEnabled(true)
				}
			)
		}
	}

	private var numberNewAirspaceEntry = 1

	def create(wwd: WorldWindow, methodOfController: AirspaceEntry => Unit) {
		apply(wwd, methodOfController)
	}

	/**
	 * Метод воздаст объект AirspaceEntry который будет обработан, через метод переданый из котроллера
	 * @param wwd объект WindWindow
	 * @param methodOfController метод из котроллера, которому необходимо предать объект AirspaceEntry,
	 *                           для того что бы он был зарегестрирован AirspaceController
	 */
	def apply(wwd: WorldWindow, methodOfController: AirspaceEntry => Unit) {
		/*
		Алгоритм действий:
		1) Создаем диалог (фрейм)
		2) Передаем ему эти же праметры
		3) Данный диалог, после нажатия кнопки создать, создает AirspaceEntry и регестрирует его по средством метода
		4) Airspace создается, но окно не закрывается. Airspace можно редактировать

		Airspace получает новое поле Radar
		 */
		val frame = new CreateEditRadarFrame(CreateAirspaceEntryMessage(wwd, methodOfController))
		frame.setVisible(true)
	}

	def edit(entry: AirspaceEntry, wwd: WorldWindow, methodOfController: AirspaceEntry => Unit) {
		val frame = new CreateEditRadarFrame(EditAirspaceEntryMessage(entry, wwd, methodOfController))
		frame.setVisible(true)
	}
}
