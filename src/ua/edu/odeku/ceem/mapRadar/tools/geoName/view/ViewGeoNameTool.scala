/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.tools.geoName.view

import ua.edu.odeku.ceem.mapRadar.tools.geoName.view.dialogs.HandlerEditGeoNameDialog
import ua.edu.odeku.ceem.mapRadar.tools.{ToolFrame, CeemRadarTool}
import javax.swing.{JTable, JPanel, JFrame, JComponent}
import java.awt.Dimension
import org.hibernate.{ScrollMode, ScrollableResults, SQLQuery, Session}
import ua.edu.odeku.ceem.mapRadar.db.DB
import java.lang.{Object, String}
import scala.Predef.String
import java.awt.event.{MouseAdapter, ActionEvent, ActionListener, MouseEvent}
import ua.edu.odeku.ceem.mapRadar.db.model.{GeoNames, GeoName}
import gov.nasa.worldwind.geom.{Position, LatLon}
import gov.nasa.worldwind.WorldWindow
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame

/**
 * User: Aleo Bakalov
 * Date: 10.12.13
 * Time: 17:17
 */
class ViewGeoNameTool extends CeemRadarTool {

	val viewPanel = new ViewGeoNamePanel

	viewPanel.featureClassComboBox.addActionListener(new ActionListener {
		def actionPerformed(e: ActionEvent) {
			refreshFeatureCode()
		}
	})

	viewPanel.refreshButton.addActionListener(new ActionListener {
		def actionPerformed(e: ActionEvent) {
			refreshTable()
		}
	})

	viewPanel.editButton.addActionListener(new ActionListener {
		override def actionPerformed(e: ActionEvent): Unit = {
			val row = viewPanel.table.getSelectedRow
			if(row > 0) {
				val id = viewPanel.table.getModel.getValueAt(row, 0).asInstanceOf[Long]
				if (id > 0) {
					val dialog = new HandlerEditGeoNameDialog(GeoNames.get(id))
					dialog.show()
					if(dialog.needRefresh) refreshTable()
				}
			}
		}
	})


	viewPanel.table.addMouseListener(new MouseAdapter {
		override def mouseClicked(e: MouseEvent) {
			if (e.getClickCount == 2) {
				val geoName: GeoName =
					viewPanel.table.getModel.asInstanceOf[GeoNamesTableModel].list(viewPanel.table.getSelectedRow)
				val latLon: LatLon = LatLon.fromDegrees(geoName.lat, geoName.lon)
				val elevation: Double = 0x1 << 15
				AppCeemRadarFrame.wwd.getView.goTo(new Position(latLon, elevation), elevation)
			}
		}
	})

	def startFunction = (frame: ToolFrame) => frame.setMinimumSize(new Dimension(800, 600))

	def endFunction = (frame: ToolFrame) => {
		/* Ничего не реализовано по закрытии данного инструмента */
	}

	def rootPanel: JPanel = viewPanel.getRootPanel

	private def refreshFeatureCode() {
		val featureClass: String = viewPanel.featureClassComboBox.getSelectedItem.toString
		if (!featureClass.isEmpty) {
			new Thread(new Runnable {
				def run() {
					viewPanel.featureCodeComboBox.removeAllItems()
					for (code <- GeoNames.featureCodes(featureClass)) {
						viewPanel.featureCodeComboBox.addItem(code)
					}
				}
			}).start()
		}
	}

	private def refreshFeatureClass() {
		new Thread(new Runnable {
			def run() {
				viewPanel.featureClassComboBox.removeAllItems()
				viewPanel.featureClassComboBox.addItem("")
				for (clazz <- GeoNames.featureClass) {
					viewPanel.featureClassComboBox.addItem(clazz)
				}
			}
		}).start()
	}

	protected def refreshCountry() {
		new Thread(new Runnable {
			def run() {
				viewPanel.countryComboBox.removeAllItems()
				viewPanel.countryComboBox.addItem("")
				for (country <- GeoNames.coutres) {
					viewPanel.countryComboBox.addItem(country)
				}
			}
		}).start()
	}

	private def refreshTable() {
		var country: String = null
		var featureClass: String = null
		var featureCode: String = null

		var value: AnyRef = viewPanel.countryComboBox.getSelectedItem

		if (value != null) {
			country = value.toString
		}

		value = viewPanel.featureClassComboBox.getSelectedItem
		if (value != null) {
			featureClass = value.toString
		}

		value = viewPanel.featureCodeComboBox.getSelectedItem
		if (value != null) {
			featureCode = value.toString
		}
		val finalCountry: String = country
		val finalFeatureClass: String = featureClass
		val finalFeatureCode: String = featureCode
		val finalPrefix: String = viewPanel.textField.getText.trim

		new Thread(new Runnable {
			def run() {
				val model = new GeoNamesTableModel(finalPrefix, finalCountry, finalFeatureClass, finalFeatureCode)
				viewPanel.table.setAutoCreateRowSorter(false)
				viewPanel.table.setModel(model)
				viewPanel.table.setAutoCreateRowSorter(false)
				viewPanel.table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS)
				viewPanel.table.setEnabled(model.getRowCount > 0)
			}
		}).start()
	}

	/**
	 * Метод для инициализации инструмента,
	 * вызовется при мервом вызове, а не в помент создания
	 */
	override def init(): Unit = {
		refreshTable()
		refreshCountry()
		refreshFeatureClass()
	}
}
