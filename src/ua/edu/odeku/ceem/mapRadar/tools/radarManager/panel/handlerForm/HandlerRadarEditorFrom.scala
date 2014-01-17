/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.handlerForm

import ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.RadarEditorForm
import javax.swing.{JComboBox, SpinnerNumberModel, JSpinner}
import javax.swing.event.{ChangeEvent, ChangeListener}
import ua.edu.odeku.ceem.mapRadar.models.radar.Radar
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.AirspaceEntry
import java.lang.NumberFormatException
import ua.edu.odeku.ceem.mapRadar.models.Prefix_SI
import java.awt.event.{ItemEvent, ItemListener}

/**
 * User: Aleo Bakalov
 * Date: 16.01.14
 * Time: 15:01
 */
class HandlerRadarEditorFrom(val form: RadarEditorForm, private var _airspaceEntry: AirspaceEntry) {

	initSpinners()
	initComboBoxes()

	handleAirspaceEntry(_airspaceEntry)


	def airspaceEntry = _airspaceEntry
	/**
	 * Метод должен обработать переданый AirspaceEntry
	 * Если он null то его необходимо создать,
	 * иначе необходимо заполнить формы ввода относительно переданного AirspaceEntry
	 *
	 * @param airspaceEntry
	 */
	private def handleAirspaceEntry(airspaceEntry: AirspaceEntry) {

	}

	def initSpinners(){
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
	}

	def initComboBoxes(){
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
		val radar: Radar = updateRadar(airspaceEntry.radar)
		if (radar == null)
			form.coverageTextField.setText("")
		else
			form.coverageTextField.setText(radar.coverage.toString)
	}

	def updateRadar(_radar: Radar = null) : Radar = {
		var radar = _radar
		try{
			val gain: 		Double = form.antenaGainSpinner.getValue.toString.toDouble * form.antenaGainComboBox.getSelectedItem.asInstanceOf[Prefix_SI].pow()
			val effective: 	Double = form.effectiveAreaSpinner.getValue.toString.toDouble * form.effectiveAreaComboBox.getSelectedItem.asInstanceOf[Prefix_SI].pow()
			val minimum: 	Double = form.minimumReceiverSensitivitySpinner.getValue.toString.toDouble * form.minimumReceiverSensitivityComboBox.getSelectedItem.asInstanceOf[Prefix_SI].pow()
			val scattening: Double = form.scatteringCrossSectionSpinner.getValue.toString.toDouble * form.scatteringCrossSectionComboBox.getSelectedItem.asInstanceOf[Prefix_SI].pow()
			val tranmotter: Double = form.transmotterPowerSpinner.getValue.toString.toDouble * form.transmotterPowerComboBox.getSelectedItem.asInstanceOf[Prefix_SI].pow()
			val altitude: 	Int = form.altitudeSpinner.getValue.toString.toInt

			radar = if(radar == null){
				new Radar(
					transmitterPower = tranmotter,
					antennaGain = gain,
					effectiveArea = effective,
					scatteringCrossSection = scattening,
					minimumReceiverSensitivity = minimum,
					altitude = altitude
				)
			} else radar.update(
				transmitterPower = tranmotter,
				antennaGain = gain,
				effectiveArea = effective,
				scatteringCrossSection = scattening,
				minimumReceiverSensitivity = minimum,
				altitude = altitude
			)
		} catch {
			case ex: NumberFormatException =>
				ex.printStackTrace()
		}
		radar
	}
}
