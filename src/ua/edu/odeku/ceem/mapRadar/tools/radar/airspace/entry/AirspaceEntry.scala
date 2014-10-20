/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.entry

import gov.nasa.worldwind.avlist.AVKey
import gov.nasa.worldwind.layers.AirspaceLayer
import gov.nasa.worldwind.render.airspaces.AirspaceAttributes
import gov.nasa.worldwind.render.airspaces.editor.{AirspaceEditListener, AirspaceEditor}
import gov.nasa.worldwind.{WWObjectImpl, WorldWindow}
import ua.edu.odeku.ceem.mapRadar.tools.radar.airspace._
import ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.factories.CeemRadarAirspaceFactory
import ua.edu.odeku.ceem.mapRadar.tools.radar.dialogs.CreateEditRadarFrame
import ua.edu.odeku.ceem.mapRadar.tools.radar.models.Radar
import ua.edu.odeku.ceem.mapRadar.tools.radar.surface.SurfaceDistributionPowerDensityManager

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
  private var _radar: Radar = factory.radar

	var _intersecting: Boolean = false

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

	private def updateAttributes() {
		if (this.selected && this.intersecting) {
			this.airspace ! AirspaceMessage.selectAndIntersecting
		}
    else if (this.selected) {
      this.airspace ! AirspaceMessage.select
		}
		else if (this.intersecting) {
      this.airspace ! AirspaceMessage.intersecting
		}
		else {
			this.airspace ! AirspaceMessage.setDefaultAttribute
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

  private var numberNewAirspaceEntry = 1

	val bufferOfAirspaceEntry = new ArrayBuffer[AirspaceEntry]()

  /**
   * Переменная указывающая на то, на какой выстоте необходимо отображать изолинию
   */
  private var altitudeIsolineView: Int = 0

  def showIsolineViewMode(altitute: Int): Unit = {
    if (altitute > 0) {
      bufferOfAirspaceEntry.foreach(
        (entry: AirspaceEntry) => {
          entry.airspace.showRadarAirspace(flag = false)
          entry.editor.setEnabled(false)
        }
      )
      SurfaceDistributionPowerDensityManager.show(altitute, bufferOfAirspaceEntry.map(entry => entry.radar).toArray)
    } else {
      bufferOfAirspaceEntry.foreach(
        (entry: AirspaceEntry) => {
          entry.airspace.showRadarAirspace(flag = true)
          entry.editor.setEnabled(true)
        }
      )
      SurfaceDistributionPowerDensityManager.hiden()
    }
  }

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
