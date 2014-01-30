/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.handlerForm

import ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.RadarEditorForm
import javax.swing.{JTextField, JComboBox, JSpinner}
import ua.edu.odeku.ceem.mapRadar.models.radar.{RadarFactory, RadarTypes, Radar}
import java.awt.event.{ActionEvent, ActionListener, ItemEvent, ItemListener}
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.entry.{EditAirspaceEntryMessage, CreateAirspaceEntryMessage, AirspaceEntryMessage, AirspaceEntry}
import gov.nasa.worldwind.render.airspaces.SphereAirspace
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.factories.SphereAirspaceFactory
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.ActionListeners.airspaceActionListeners.AirspaceChangeLocationOnFormListener
import ua.edu.odeku.ceem.mapRadar.models.radar.RadarTypes._

/**
 * User: Aleo Bakalov
 * Date: 16.01.14
 * Time: 15:01
 */
class HandlerRadarEditorFrom(val form: RadarEditorForm, private var message: AirspaceEntryMessage) {

	private var _airspaceEntry: AirspaceEntry = null
	private var methodOfController: AirspaceEntry => Unit = message.method
	private var radar: Radar = _
	private var savedNewAirspaceEntry = true // флаг на надобность сохранить как новый объект
	private val radarTypeMap = new collection.mutable.HashMap[RadarType, Radar]

	val changeLocationListener = new AirspaceChangeLocationOnFormListener(form.locationForm)

	val buttonActionListener = new ActionListener {
		def actionPerformed(e: ActionEvent): Unit = {
			println(e.getActionCommand)
			e.getActionCommand match {
				case "saveAirspace" =>
					airspaceEntry.radar = updateRadar()
					airspaceEntry.nameAirspaceEntry = form.nameAirspaceTextField.getText
					if (!savedNewAirspaceEntry) {
						message.method.apply(airspaceEntry)
						savedNewAirspaceEntry = true
					} else {
						message.wwd.redraw()
					}
			}
		}
	}

	val typeRadarComboBoxItemListener = new ItemListener {
		def itemStateChanged(e: ItemEvent): Unit = {
			updateRadar()
		}
	}

	initComboBoxes()
	updateRadar()

	message match {
		case message: CreateAirspaceEntryMessage =>
			airspaceEntry = createAirspaceEntry()
		case message: EditAirspaceEntryMessage =>
			airspaceEntry = message.airspaceEntry
			radar = airspaceEntry.radar
			form.typeRadarComboBox.setSelectedItem(radar.radarName)
	}

	initSpinners()
	initTextField()

	changeLocationListener.updateFields(airspaceEntry.airspace.asInstanceOf[SphereAirspace].getLocation)

	def createAirspaceEntry(): AirspaceEntry = {
		savedNewAirspaceEntry = false
		new AirspaceEntry(new SphereAirspaceFactory(radar, message.wwd, false))
	}

	def initSpinners() {
		form.altitudeSpinner = new JSpinner()
	}

	def initComboBoxes() {
		form.typeRadarComboBox = new JComboBox[RadarType]()
		for(radarType <- RadarTypes.values){
			form.typeRadarComboBox.addItem(radarType)
		}
		form.typeRadarComboBox.setSelectedIndex(0)
	}

	def updateRadar(): Radar = {
		val radarName = form.typeRadarComboBox.getSelectedItem.asInstanceOf[RadarType]
		if(radar == null || radarName != radar.radarName){
			if (radarTypeMap.keys.exists( radarName == _ )){
				radar = radarTypeMap(radarName)
			} else {
				radar = RadarFactory(radarName)
			}
			updateForm()
		}

		radarTypeMap.put(radarName, radar)
		radar
	}

	def airspaceEntry = _airspaceEntry

	private def airspaceEntry_=(value: AirspaceEntry): Unit = {
		_airspaceEntry = value
		_airspaceEntry.editor.addEditListener(changeLocationListener)
	}

	def initTextField() {
		form.nameAirspaceTextField = new JTextField(airspaceEntry.nameAirspaceEntry)
	}

	def eventCloseForm() {
		airspaceEntry.editor.removeEditListener(changeLocationListener)
	}

	/**
	 * Метод генирить данные формы
	 */
	def updateForm() {

	}
}
