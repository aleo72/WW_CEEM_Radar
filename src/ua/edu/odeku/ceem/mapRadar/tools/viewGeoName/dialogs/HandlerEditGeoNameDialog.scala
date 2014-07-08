/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.viewGeoName.dialogs

import java.awt.event.{WindowEvent, WindowAdapter, ActionEvent, ActionListener}

import ua.edu.odeku.ceem.mapRadar.db.model.{GeoNames, GeoName}
import ua.edu.odeku.ceem.mapRadar.resource.ResourceString
import ua.edu.odeku.ceem.mapRadar.utils.geometry.LatitudeLongitudeUtils
import ua.edu.odeku.ceem.mapRadar.utils.gui.UserMessage

/**
 * User: Aleo Bakalov
 * Date: 08.07.2014
 * Time: 11:02
 */
class HandlerEditGeoNameDialog(val geoName: GeoName) {

	val dialog: EditGeoNameDialog = new EditGeoNameDialog

	dialog.sourceIdTextField.setText(geoName.id.toString)
	dialog.nameTextField.setText(geoName.name)
	dialog.asciiNameTextField.setText(geoName.ascii)
	dialog.translateTextField.setText(geoName.translateName.getOrElse(""))
	dialog.alternativeTextField.setText(geoName.alternateNames)
	dialog.featureClassTextField.setText(geoName.featureClass)
	dialog.latitudeFormattedTextField.setText(geoName.lat.toString)
	dialog.longitudeFormattedTextField.setText(geoName.lon.toString)

	initFeatureCode(geoName)
	initCountry(geoName)

	dialog.freezeSize()

	dialog.buttonOK.addActionListener(
		new ActionListener {
			override def actionPerformed(e: ActionEvent): Unit = {
				if (saveData) onClose()
			}
		}
	)

	dialog.buttonCancel.addActionListener(
		new ActionListener {
			override def actionPerformed(e: ActionEvent): Unit = onClose()
		}
	)

	dialog.addWindowListener(new WindowAdapter {
		override def windowClosing(e: WindowEvent): Unit = dialog.dispose()
	})

	dialog.deleteButton.addActionListener(new ActionListener {
		override def actionPerformed(e: ActionEvent): Unit = {
			if (UserMessage.ConfirmDialog(dialog.$$$getRootComponent$$$(),
				ResourceString.get("title_confirmDialog_delete"),
				ResourceString.get("message_geoName_delete-item"))) {
				GeoNames -= geoName
				onClose()
			}
		}
	})

	def onClose() {
		dialog.dispose()
	}

	def initFeatureCode(geo: GeoName) {
		dialog.featureCodeComboBox.removeAllItems()
		for (code <- GeoNames.featureCodes(geo.featureClass)) {
			dialog.featureCodeComboBox.addItem(code)
		}
		dialog.featureCodeComboBox.setSelectedItem(geo.featureCode)
	}

	def initCountry(geo: GeoName) {
		dialog.featureCodeComboBox.removeAllItems()
		for(country <- GeoNames.coutres){
			dialog.countryComboBox.addItem(country)
		}
		dialog.countryComboBox.setSelectedItem(geo.countryCode)
	}

	def isValidData: Boolean = {
		var data: String = null

		data = dialog.nameTextField.getText
		if (data.trim().isEmpty) {
			val message = ResourceString.get("field_geoName_name") + " " + ResourceString.get("message-must-not-be-empty")
			UserMessage.warning(dialog.$$$getRootComponent$$$(), message)
			dialog.nameTextField.setText(geoName.name)
			return false
		}
		data = dialog.asciiNameTextField.getText
		if (data.trim().isEmpty) {
			val message = ResourceString.get("string_field") + " " + ResourceString.get("field_geoName_asciiName") + " " + ResourceString.get("message-must-not-be-empty") + "!"
			UserMessage.warning(dialog.$$$getRootComponent$$$(), message)
			dialog.asciiNameTextField.setText(geoName.ascii)
			return false
		}
		data = dialog.translateTextField.getText
		if (data.trim().isEmpty) {
			val message = ResourceString.get("string_field") + " " + ResourceString.get("field_geoName_translate") + " " + ResourceString.get("message-must-not-be-empty") + "!"
			UserMessage.warning(dialog.$$$getRootComponent$$$(), message)
			dialog.translateTextField.setText(geoName.translateName.getOrElse(""))
			return false
		}
		if (!LatitudeLongitudeUtils.isValidLatitude(dialog.latitudeFormattedTextField.getText)) {
			val message = ResourceString.get("string_field") + " " + ResourceString.get("field_geoName_latitude") + " " + ResourceString.get("message_Do-not-satisfy-the-format-of-the-coordinates") + "!"
			UserMessage.warning(dialog.$$$getRootComponent$$$(), message)
			dialog.latitudeFormattedTextField.setText(geoName.lat.toString)
			return false
		}
		if (!LatitudeLongitudeUtils.isValidLongitude(dialog.longitudeFormattedTextField.getText)) {
			val message = ResourceString.get("string_field") + " " + ResourceString.get("field_geoName_longitude") + " " + ResourceString.get("message_Do-not-satisfy-the-format-of-the-coordinates") + "!"
			UserMessage.warning(dialog.$$$getRootComponent$$$(), message)
			dialog.longitudeFormattedTextField.setText(this.geoName.lon.toString)
			return false
		}
		true
	}

	private def saveData(): Boolean = {
		if (isValidData) {
			try {
				val newGeoName = GeoName(
					geoName.id,
					dialog.nameTextField.getText,
					dialog.asciiNameTextField.getText,
					dialog.alternativeTextField.getText,
					dialog.latitudeFormattedTextField.getText.toDouble,
					dialog.longitudeFormattedTextField.getText.toDouble,
					dialog.featureClassTextField.getText,
					dialog.featureCodeComboBox.getSelectedItem.asInstanceOf[String],
					dialog.countryComboBox.getSelectedItem.asInstanceOf[String],
					Some(dialog.translateTextField.getText)
				)

				GeoNames ! newGeoName

				true
			} catch {
				case e: Exception => e.printStackTrace()
					false
			}
		} else {
			false
		}

	}
}
