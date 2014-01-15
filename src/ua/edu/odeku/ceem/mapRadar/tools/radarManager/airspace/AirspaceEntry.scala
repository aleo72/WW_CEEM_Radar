/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import gov.nasa.worldwind.{WorldWindow, WWObjectImpl}
import gov.nasa.worldwind.render.airspaces.AirspaceAttributes
import gov.nasa.worldwind.avlist.AVKey
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.factories.AirspaceFactory

/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:04
 */
class AirspaceEntry(val factory: AirspaceFactory) extends WWObjectImpl {

	val airspace = factory.airspace
	val editor = factory.editor

	val attributes: AirspaceAttributes = airspace.getAttributes
	private var _editing: Boolean = false
	private var _selected: Boolean = false
	var _intersecting: Boolean = false

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
			this.airspace.setAttributes(getSelectionAndIntersectionAttributes)
		}
		else if (this.selected) {
			this.airspace.setAttributes(getSelectionAttributes)
		}
		else if (this.intersecting) {
			this.airspace.setAttributes(getIntersectionAttributes)
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
}

object AirspaceEntry {

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
		null
	}
}
