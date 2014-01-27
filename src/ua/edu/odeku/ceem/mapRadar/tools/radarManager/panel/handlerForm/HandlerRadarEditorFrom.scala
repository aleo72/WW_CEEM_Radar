/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.handlerForm

import ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.RadarEditorForm
import javax.swing.{JTextField, JComboBox, SpinnerNumberModel, JSpinner}
import javax.swing.event.{ChangeEvent, ChangeListener}
import ua.edu.odeku.ceem.mapRadar.models.radar.Radar
import java.lang.NumberFormatException
import ua.edu.odeku.ceem.mapRadar.models.{DoubleWhisPrefix_SI, Prefix_SI}
import java.awt.event.{ActionEvent, ActionListener, ItemEvent, ItemListener}
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.entry.{EditAirspaceEntryMessage, CreateAirspaceEntryMessage, AirspaceEntryMessage, AirspaceEntry}
import gov.nasa.worldwind.render.airspaces.SphereAirspace
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.factories.SphereAirspaceFactory

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

	message match {
		case message: CreateAirspaceEntryMessage =>
			radar = new Radar()
			_airspaceEntry = createAirspaceEntry()
		case message: EditAirspaceEntryMessage =>
			_airspaceEntry = message.airspaceEntry
			radar = _airspaceEntry.radar
	}

	def createAirspaceEntry(): AirspaceEntry = {
		savedNewAirspaceEntry = false
		new AirspaceEntry(new SphereAirspaceFactory(radar, message.wwd, false))
	}

	def updateForm() {

		def updateForm(value: Double, spinner: JSpinner, comboBox: JComboBox[Prefix_SI]) {
			val doublePrefixSI = new DoubleWhisPrefix_SI(value)
			spinner.setValue(doublePrefixSI._v)
			comboBox.setSelectedItem(doublePrefixSI._prefix)
		}

		updateForm(radar.transmitterPower, form.transmotterPowerSpinner, form.transmotterPowerComboBox)
		updateForm(radar.antennaGain, form.antenaGainSpinner, form.antenaGainComboBox)
		updateForm(radar.effectiveArea, form.effectiveAreaSpinner, form.effectiveAreaComboBox)
		updateForm(radar.minimumReceiverSensitivity, form.minimumReceiverSensitivitySpinner, form.minimumReceiverSensitivityComboBox)
		updateForm(radar.scatteringCrossSection, form.scatteringCrossSectionSpinner, form.scatteringCrossSectionComboBox)

		form.altitudeSpinner.setValue(radar.altitude)
	}

	def updateLocation(airspace: SphereAirspace = _airspaceEntry.airspace.asInstanceOf[SphereAirspace]) {
		if (airspace != null) {
			val location = airspace.getLocation

			form.lanTextField.setText(location.asDegreesArray()(0).toString)
			form.lonTextField.setText(location.asDegreesArray()(1).toString)
		}
	}

	def initSpinners() {
		val format: String = "#,##0.########"

		def initSpinner() = {
			val jSpinner: JSpinner = new JSpinner()
			val spinnerNumberModel: SpinnerNumberModel = new SpinnerNumberModel(1.0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1)
			jSpinner.setModel(spinnerNumberModel)
			jSpinner.setEditor(new JSpinner.NumberEditor(jSpinner, format))
			jSpinner.addChangeListener(valueChangeListener)
			jSpinner
		}

		form.antenaGainSpinner = initSpinner()
		form.effectiveAreaSpinner = initSpinner()
		form.minimumReceiverSensitivitySpinner = initSpinner()
		form.scatteringCrossSectionSpinner = initSpinner()
		form.transmotterPowerSpinner = initSpinner()

		form.altitudeSpinner = new JSpinner()
	}

	def initComboBoxes() {
		def initComboBox() = {
			val comboBox = new JComboBox[Prefix_SI](Prefix_SI.array)
			comboBox.setSelectedItem(Prefix_SI.ONE)
			comboBox.addItemListener(comboBoxItemListener)
			comboBox
		}

		form.transmotterPowerComboBox = initComboBox()
		form.antenaGainComboBox = initComboBox()
		form.effectiveAreaComboBox = initComboBox()
		form.scatteringCrossSectionComboBox = initComboBox()
		form.minimumReceiverSensitivityComboBox = initComboBox()
	}

	val valueChangeListener = new ChangeListener {
		def stateChanged(e: ChangeEvent): Unit = {
			updateCoverageTextField()
		}
	}

	val comboBoxItemListener = new ItemListener {
		def itemStateChanged(e: ItemEvent): Unit = {
			updateCoverageTextField()
		}
	}

	def updateCoverageTextField() {
		updateRadar()
		if (radar == null) {
			form.coverageTextField.setText("")
		}
		else {
			form.coverageTextField.setText(radar.coverage.toString)
		}
	}

	def updateRadar(): Radar = {
		try {
			val gain: Double = form.antenaGainSpinner.getValue.toString.toDouble * form.antenaGainComboBox.getSelectedItem.asInstanceOf[Prefix_SI].pow()
			val effective: Double = form.effectiveAreaSpinner.getValue.toString.toDouble * form.effectiveAreaComboBox.getSelectedItem.asInstanceOf[Prefix_SI].pow()
			val minimum: Double = form.minimumReceiverSensitivitySpinner.getValue.toString.toDouble * form.minimumReceiverSensitivityComboBox.getSelectedItem.asInstanceOf[Prefix_SI].pow()
			val scattening: Double = form.scatteringCrossSectionSpinner.getValue.toString.toDouble * form.scatteringCrossSectionComboBox.getSelectedItem.asInstanceOf[Prefix_SI].pow()
			val tranmotter: Double = form.transmotterPowerSpinner.getValue.toString.toDouble * form.transmotterPowerComboBox.getSelectedItem.asInstanceOf[Prefix_SI].pow()
			val altitude: Int = form.altitudeSpinner.getValue.toString.toInt

			if (radar == null) {
				radar = new Radar(
					transmitterPower = tranmotter,
					antennaGain = gain,
					effectiveArea = effective,
					scatteringCrossSection = scattening,
					minimumReceiverSensitivity = minimum,
					altitude = altitude
				)
			} else {
				radar.update(
					transmitterPower = tranmotter,
					antennaGain = gain,
					effectiveArea = effective,
					scatteringCrossSection = scattening,
					minimumReceiverSensitivity = minimum,
					altitude = altitude
				)
			}
		} catch {
			case ex: NumberFormatException =>
				ex.printStackTrace()
		}
		radar
	}

	def airspaceEntry = _airspaceEntry

	def initTextField() {
		form.coverageTextField = new JTextField()
		form.lanTextField = new JTextField()
		form.lonTextField = new JTextField()
		form.nameAirspaceTextField = new JTextField(airspaceEntry.nameAirspaceEntry)
	}

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





	initSpinners()
	initComboBoxes()
	initTextField()
	updateForm()
	updateLocation()
}
