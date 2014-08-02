/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.utils.gui.forms

import java.awt.event.{ActionEvent, ActionListener, KeyEvent, KeyListener}

import gov.nasa.worldwind.geom.LatLon
import ua.edu.odeku.ceem.mapRadar.db.model.GeoNamesWithNameAndCoordinates
import ua.edu.odeku.ceem.mapRadar.utils.gui.UserMessage
import ua.edu.odeku.ceem.mapRadar.utils.gui.forms.actions.AirspaceChangeLocationOnFormListener

/**
 * Created by Aleo on 27.07.2014.
 */
class HandlerLocationForm(val form: LocationForm = new LocationForm) {

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

  def location: LatLon = LatLon.fromDegrees(form.latTextField.getText.toDouble, form.lonTextField.getText.toDouble)

  form.latTextField.setText(LatLon.ZERO.getLatitude.degrees.toString)
  form.lonTextField.setText(LatLon.ZERO.getLongitude.degrees.toString)

  form.locationHelp.addActionListener(locationNameHelpButton)
  form.locationNameComboBox.getEditor.getEditorComponent.addKeyListener(locationNameComboBoxKeyListener)

}
