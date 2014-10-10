/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.utils.gui.forms

import java.awt.event._
import java.text.DecimalFormat

import gov.nasa.worldwind.geom.{LatLon, Position}
import gov.nasa.worldwindx.examples.util.ShapeUtils
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame
import ua.edu.odeku.ceem.mapRadar.tools.geoName.models.GeoNamesWithNameAndCoordinates
import ua.edu.odeku.ceem.mapRadar.settings.Settings
import ua.edu.odeku.ceem.mapRadar.utils.gui.UserMessage
import ua.edu.odeku.ceem.mapRadar.utils.gui.forms.actions.AirspaceChangeLocationOnFormListener

/**
 * Обработчик форми локації
 * Created by Aleo on 27.07.2014.
 */
class HandlerLocationForm(val form: LocationForm = new LocationForm) {

  val positionDecimalFormat = new DecimalFormat(Settings.Program.Tools.Radar.Location.decimalFormat)

  val position: Position = ShapeUtils.getNewShapePosition(AppCeemRadarFrame.wwd)

  val locationNameComboBoxKeyListener: KeyListener = new KeyListener {

    override def keyReleased(e: KeyEvent): Unit = Unit

    override def keyTyped(e: KeyEvent): Unit = Unit

    override def keyPressed(e: KeyEvent): Unit = {
      e.getSource match {
        case source: javax.swing.JTextField =>
          if (e.getKeyCode == KeyEvent.VK_ENTER) {
            val text = source.getText

            if (text.trim.length >= 3) {
              val list = GeoNamesWithNameAndCoordinates.getSettlements(text.trim)
              println(list.size)
              form.locationNameComboBox.removeAllItems()
              list.foreach { settlement => form.locationNameComboBox.addItem(settlement)}
            } else {
              UserMessage.warning(form.$$$getRootComponent$$$(), "Введите больше двух символов")
            }
          }
        case sourse =>
          println(sourse)
      }
    }
  }

  val locationNameHelpButton: ActionListener = new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      UserMessage.inform(form.$$$getRootComponent$$$(), "Введите более трех символов и нажмите ENTER для поиска")
    }
  }

  val changeLocationListener = new AirspaceChangeLocationOnFormListener(this.form)

  val selectedItemListener = new ItemListener {
    override def itemStateChanged(e: ItemEvent): Unit = {
      if (e.getStateChange == ItemEvent.SELECTED) {
        e.getItem match {
          case x: GeoNamesWithNameAndCoordinates =>
            val location = x.latlon
            form.latTextField.setText(positionDecimalFormat.format(location.getLatitude.degrees))
            form.lonTextField.setText(positionDecimalFormat.format(location.getLongitude.degrees))
          case _ =>
        }

      }
    }
  }

  def latitude: Double = form.latTextField.getText.replace(',', '.').toDouble

  def longitude: Double = form.lonTextField.getText.replace(',', '.').toDouble

  def location: LatLon = LatLon.fromDegrees(latitude, longitude)

  form.latTextField.setText(positionDecimalFormat.format(position.getLatitude.degrees))
  form.lonTextField.setText(positionDecimalFormat.format(position.getLongitude.degrees))

  form.locationHelp.addActionListener(locationNameHelpButton)
  form.locationNameComboBox.getEditor.getEditorComponent.addKeyListener(locationNameComboBoxKeyListener)
  form.locationNameComboBox.addItemListener(selectedItemListener)
}
