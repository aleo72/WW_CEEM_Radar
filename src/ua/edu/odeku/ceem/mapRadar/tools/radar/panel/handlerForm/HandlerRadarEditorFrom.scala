/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.panel.handlerForm

import java.awt.event._
import javax.swing._

import ua.edu.odeku.ceem.mapRadar.settings.Settings
import ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.entry.{AirspaceEntry, AirspaceEntryMessage, CreateAirspaceEntryMessage, EditAirspaceEntryMessage}
import ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.factories.CeemRadarAirspaceFactory
import ua.edu.odeku.ceem.mapRadar.tools.radar.models.Radar
import ua.edu.odeku.ceem.mapRadar.tools.radar.panel.RadarEditorForm
import ua.edu.odeku.ceem.mapRadar.utils.gui.VisibleUtils
import ua.edu.odeku.ceem.mapRadar.utils.gui.forms.HandlerLocationForm

/**
 * User: Aleo Bakalov
 * Date: 16.01.14
 * Time: 15:01
 */
class HandlerRadarEditorFrom(val form: RadarEditorForm, private var message: AirspaceEntryMessage) {

  private var _airspaceEntry: AirspaceEntry = null
  private var methodOfController: AirspaceEntry => Unit = message.method
  // флаг на надобность сохранить как новый объект
  val handlerLocationForm = new HandlerLocationForm(form.locationForm)
  val handlerRadarParameterForm = new HandlerRadarParameterForm(form.radarParameterForm)

  private var savedNewAirspaceEntry = true

  private val radar: Radar = message match {
    case message: CreateAirspaceEntryMessage =>
      airspaceEntry = createAirspaceEntry(message)
      airspaceEntry.radar
    case message: EditAirspaceEntryMessage =>
      airspaceEntry = message.airspaceEntry
      airspaceEntry.radar
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

  updateRadar()
  initTextField()

  handlerLocationForm.changeLocationListener.updateFields(airspaceEntry.location)
  handlerRadarParameterForm.updateFields(this.radar)

  VisibleUtils.packFrame(form.parent)
  VisibleUtils.setMinMaxSizeFrame(form.parent)


  def createAirspaceEntry(message: CreateAirspaceEntryMessage): AirspaceEntry = {
    savedNewAirspaceEntry = false
    new AirspaceEntry(new CeemRadarAirspaceFactory(Radar.EMPTY_RADAR, message.wwd, false))
  }

  private def updateRadarLocation() {
    radar.latLon = handlerLocationForm.location
  }

  private def updateRadar(): Radar = {
    updateRadarParameter()
    updateRadarLocation()
    radar
  }

  def airspaceEntry: AirspaceEntry = _airspaceEntry

  private def airspaceEntry_=(value: AirspaceEntry): Unit = {
    _airspaceEntry = value
    _airspaceEntry.addAirspaceEditorListener(handlerLocationForm.changeLocationListener)
  }

  def initTextField() {
    form.nameAirspaceTextField = new JTextField(airspaceEntry.nameAirspaceEntry)
  }

  def eventCloseForm() {
    airspaceEntry.removeEditListener(handlerLocationForm.changeLocationListener)
  }

  def updateRadarParameter() {
    radar.altitude = handlerRadarParameterForm.altitude
    radar.pulsePower = handlerRadarParameterForm.pulsePower
    radar.antennaDiameter = handlerRadarParameterForm.antennaDiameter
    radar.attenuation = handlerRadarParameterForm.attenuation
    radar.durationPulse = handlerRadarParameterForm.durationPulse
    radar.gainFactor = handlerRadarParameterForm.grainFactor
    radar.wavelength = handlerRadarParameterForm.waveLength
    radar.reflectivityMeteoGoals = handlerRadarParameterForm.reflectivityMeteoGoals
    radar.radius = handlerRadarParameterForm.radius
  }
}