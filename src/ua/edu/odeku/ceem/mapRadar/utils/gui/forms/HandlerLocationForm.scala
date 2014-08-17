/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.utils.gui.forms

import java.awt.event.{ActionEvent, ActionListener, KeyEvent, KeyListener}
import java.text.DecimalFormat

import gov.nasa.worldwind.geom.{LatLon, Position}
import gov.nasa.worldwindx.examples.util.ShapeUtils
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame
import ua.edu.odeku.ceem.mapRadar.db.model.GeoNamesWithNameAndCoordinates
import ua.edu.odeku.ceem.mapRadar.utils.gui.UserMessage
import ua.edu.odeku.ceem.mapRadar.utils.gui.forms.actions.AirspaceChangeLocationOnFormListener

/**
 * Обработчик форми локації
 * Created by Aleo on 27.07.2014.
 */
class HandlerLocationForm(val form: LocationForm = new LocationForm) {

  val positionDecimalFormat = new DecimalFormat("#.000000");

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
              for (settlement: GeoNamesWithNameAndCoordinates <- list) {
                //                form.locationNameComboBox.addItem(settlement.asInstanceOf[Object])
              }

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

  def latitude: Double = form.latTextField.getText.replace(',', '.').toDouble

  def longitude: Double = form.lonTextField.getText.replace(',', '.').toDouble

  def location: LatLon = LatLon.fromDegrees(latitude, longitude)

  val position: Position = ShapeUtils.getNewShapePosition(AppCeemRadarFrame.wwd)

  form.latTextField.setText(positionDecimalFormat.format(position.getLatitude.degrees))
  form.lonTextField.setText(positionDecimalFormat.format(position.getLongitude.degrees))

  form.locationHelp.addActionListener(locationNameHelpButton)
  form.locationNameComboBox.getEditor.getEditorComponent.addKeyListener(locationNameComboBoxKeyListener)

}
