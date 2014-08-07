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
  initSpinners()
  initTextField()

  handlerLocationForm.changeLocationListener.updateFields(airspaceEntry.location)

  VisibleUtils.packFrame(form.parent)
  VisibleUtils.setMinMaxSizeFrame(form.parent)


  def createAirspaceEntry(message: CreateAirspaceEntryMessage): AirspaceEntry = {
    savedNewAirspaceEntry = false
    new AirspaceEntry(new CeemRadarAirspaceFactory(Radar.EMPTY_RADAR, message.wwd, false))
  }

  private def initSpinners() {
    form.altitudeSpinner = new JSpinner(
      new SpinnerNumberModel(
        Settings.Program.Tools.Radar.defaultAltitude,
        Settings.Program.Tools.Radar.minAltitude,
        Settings.Program.Tools.Radar.maxAltitude,
        Settings.Program.Tools.Radar.stepAltitude
      ))
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
    if (form.altitudeSpinner != null)
      radar.altitude = form.altitudeSpinner.getValue.asInstanceOf[Int]
  }
}