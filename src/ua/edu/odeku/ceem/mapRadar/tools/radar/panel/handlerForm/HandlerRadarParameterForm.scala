/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.panel.handlerForm

import javax.swing.SpinnerNumberModel

import ua.edu.odeku.ceem.mapRadar.settings.Settings
import ua.edu.odeku.ceem.mapRadar.tools.radar.panel.RadarParameterForm

/**
 * Created by Aleo on 29.07.2014.
 */
class HandlerRadarParameterForm(val form: RadarParameterForm) {

  def initSpinners() {
    form.altitudeSpinner.setModel(new SpinnerNumberModel(
      Settings.Program.Tools.Radar.defaultAltitude,
      Settings.Program.Tools.Radar.minAltitude,
      Settings.Program.Tools.Radar.maxAltitude,
      Settings.Program.Tools.Radar.stepAltitude)
    )

    form.pulsePowerSpinner.setModel(new SpinnerNumberModel(Settings.Program.Tools.Radar.pulsePower,-1000,1000,0.5) )
    form.waveLengthSpinner.setModel(new SpinnerNumberModel(Settings.Program.Tools.Radar.pulsePower, -1000.0, 1000, 0.5))
    form.antennaDiameterSpinner.setModel(new SpinnerNumberModel(Settings.Program.Tools.Radar.antennaDiameter, 0.1, 1000, 0.1))
    form.reflectivityMeteoGoalsSpinner.setModel(new SpinnerNumberModel(Settings.Program.Tools.Radar.reflectivityMeteoGoals, -1000, 1000, 0.5))
    form.attenuationSpinner.setModel(new SpinnerNumberModel(Settings.Program.Tools.Radar.attenuation, -1000.0, 1000.0, 0.5))
    form.radiusSpinner.setModel(new SpinnerNumberModel(Settings.Program.Tools.Radar.radius, 1.0, 1000000.0, 100))
    form.grainFactorSpinner.setModel(new SpinnerNumberModel(Settings.Program.Tools.Radar.grainFactor, 1.0, 1000000, 100))
  }

  def name = form.nameTextField.getText

  def altitude = form.altitudeSpinner.getValue.toString.toInt

  def pulsePower = form.pulsePowerSpinner.getValue.toString.toDouble

  def waveLength = form.waveLengthSpinner.getValue.toString.toDouble

  def antennaDiameter = form.antennaDiameterSpinner.getValue.toString.toDouble

  def reflectivityMeteoGoals = form.reflectivityMeteoGoalsSpinner.getValue.toString.toDouble

  def attenuation = form.attenuationSpinner.getValue.toString.toDouble

  def radius = form.radiusSpinner.getValue.toString.toDouble

  def grainFactor = form.grainFactorSpinner.getValue.toString.toDouble

  def durationPulse
}
