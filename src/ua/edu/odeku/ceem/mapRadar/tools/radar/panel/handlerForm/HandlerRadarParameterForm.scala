/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.panel.handlerForm

import javax.swing.{SpinnerNumberModel, SpinnerModel}
import javax.swing.event.ChangeListener

import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram
import ua.edu.odeku.ceem.mapRadar.tools.radar.panel.RadarParameterForm

/**
 * Created by Aleo on 29.07.2014.
 */
class HandlerRadarParameterForm(val form: RadarParameterForm) {

  def initSpinners() {
    form.altitudeSpinner.setModel(new SpinnerNumberModel(
      PropertyProgram.getDefaultAltitudeForRadar,
      PropertyProgram.getMinAltitudeForRadar,
      PropertyProgram.getMaxAltitudeForRadar,
      PropertyProgram.getStepAltitudeForRadar)
    )


  }

  def name = form.nameTextField.getText

  def altitude = form.altitudeSpinner.getValue.toString.toInt

  def durationPulse = form.pulsePowerSpinner.getValue.toString.toDouble

  def waveLength = form.waveLengthSpinner.getValue.toString.toDouble

  def antennaDiameter = form.antennaDiameterSpinner.getValue.toString.toDouble

  def reflectivityMeteoGoals = form.reflectivityMeteoGoalsSpinner.getValue.toString.toDouble

  def attenuation = form.attenuationSpinner.getValue.toString.toDouble

  def radius = form.radiusSpinner.getValue.toString.toDouble

  def grainFactor = form.grainFactorSpinner.getValue.toString.toDouble
}
