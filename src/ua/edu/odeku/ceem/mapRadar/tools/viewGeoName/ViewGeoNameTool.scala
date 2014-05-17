/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.tools.viewGeoName

import ua.edu.odeku.ceem.mapRadar.tools.{ToolFrame, CeemRadarTool}
import javax.swing.{JTable, JPanel, JFrame, JComponent}
import java.awt.Dimension
import org.hibernate.{ScrollMode, ScrollableResults, SQLQuery, Session}
import ua.edu.odeku.ceem.mapRadar.db.DB
import java.lang.{Object, String}
import scala.Predef.String
import java.awt.event.{MouseAdapter, ActionEvent, ActionListener, MouseEvent}
import ua.edu.odeku.ceem.mapRadar.db.models.GeoName
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

	refreshTable()
	refreshCountry()
	refreshFeatureClass()

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


	viewPanel.table.addMouseListener(new MouseAdapter {
		override def mouseClicked(e: MouseEvent) {
			if (e.getClickCount == 2) {
				val geoName: GeoName =
					viewPanel.table.getModel.asInstanceOf[GeoNameTableModel].getListGeoName.get(viewPanel.table.getSelectedRow)
				val latLon: LatLon = LatLon.fromDegrees(geoName.getLat, geoName.getLon)
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
					viewPanel.featureCodeComboBox.addItem("")
					val session: Session = DB.createHibernateSession()
					val sql: String = """
                      SELECT G.FEATURE_CODE
                      FROM GEO_NAME G
                      WHERE G.FEATURE_CLASS = :featureClass
                        AND G.FEATURE_CODE IS NOT NULL
                      GROUP BY G.FEATURE_CODE
                      ORDER BY G.FEATURE_CODE;
					                  """
					val sqlQuery: SQLQuery = session.createSQLQuery(sql)
					sqlQuery.setParameter("featureClass", featureClass)
					sqlQuery.addScalar("FEATURE_CODE", DB.STRING_TYPE)
					val results: ScrollableResults = sqlQuery.scroll(ScrollMode.FORWARD_ONLY)
					results.beforeFirst()
					while (results.next) {
						viewPanel.featureCodeComboBox.addItem(results.getString(0))
					}
					results.close()
					DB.closeSession(session)
				}
			}).start()
		}
	}

	private def refreshFeatureClass() {
		new Thread(new Runnable {
			def run() {
				viewPanel.featureClassComboBox.removeAllItems()
				viewPanel.featureClassComboBox.addItem("")
				val session: Session = DB.createHibernateSession()
				val sql: String = """

                  SELECT G.FEATURE_CLASS
                  FROM GEO_NAME G
                  WHERE G.FEATURE_CLASS IS NOT NULL
                  GROUP BY G.FEATURE_CLASS
                  ORDER BY G.FEATURE_CLASS

				                  """

				val sqlQuery: SQLQuery = session.createSQLQuery(sql)
				sqlQuery.addScalar("FEATURE_CLASS", DB.STRING_TYPE)
				val results: ScrollableResults = sqlQuery.scroll(ScrollMode.FORWARD_ONLY)
				while (results.next) {
					val s: String = results.getString(0)
					viewPanel.featureClassComboBox.addItem(s)
				}
				results.close()
				DB.closeSession(session)
			}
		}).start()
	}

	protected def refreshCountry() {
		new Thread(new Runnable {
			def run() {
				viewPanel.countryComboBox.removeAllItems()
				viewPanel.countryComboBox.addItem("")
				val session: Session = DB.createHibernateSession()
				val sql: String = """

                SELECT COUNTRY_CODE  FROM GEO_NAME
                WHERE COUNTRY_CODE IS NOT NULL
                GROUP BY COUNTRY_CODE
                ORDER BY COUNTRY_CODE;

				                  """
				val sqlQuery: SQLQuery = session.createSQLQuery(sql)
				sqlQuery.addScalar("COUNTRY_CODE", DB.STRING_TYPE)
				val results: ScrollableResults = sqlQuery.scroll(ScrollMode.FORWARD_ONLY)
				while (results.next) {
					viewPanel.countryComboBox.addItem(results.getString(0))
				}
				results.close()
				DB.closeSession(session)
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
				viewPanel.table.setAutoCreateRowSorter(false)
				viewPanel.table.setModel(new GeoNameTableModel(finalPrefix, finalCountry, finalFeatureClass, finalFeatureCode))
				viewPanel.table.setAutoCreateRowSorter(false)
				viewPanel.table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS)
			}
		}).start()
	}
}
