/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.handlerForm

import ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.RadarEditorForm
import javax.swing._
import ua.edu.odeku.ceem.mapRadar.models.radar.{RadarTypeParameters, RadarFactory, RadarTypes, Radar}
import java.awt.event._
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.entry.{AirspaceEntryMessage, AirspaceEntry}
import gov.nasa.worldwind.render.airspaces.SphereAirspace
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.factories.SphereAirspaceFactory
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.ActionListeners.airspaceActionListeners.AirspaceChangeLocationOnFormListener
import ua.edu.odeku.ceem.mapRadar.models.radar.RadarTypes._
import ua.edu.odeku.ceem.mapRadar.models.radar.RadarTypes.RadarType
import com.jgoodies.forms.layout.{FormLayout, CellConstraints}
import scala.collection.mutable
import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram
import ua.edu.odeku.ceem.mapRadar.utils.gui.{UserMessage, VisibleUtils}
import gov.nasa.worldwind.geom.LatLon
import ua.edu.odeku.ceem.mapRadar.models.radar.RadarTypeParameters.RadarTypeParameter
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.entry.EditAirspaceEntryMessage
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.entry.CreateAirspaceEntryMessage
import ua.edu.odeku.ceem.mapRadar.db.DB
import org.hibernate.ScrollMode

/**
 * User: Aleo Bakalov
 * Date: 16.01.14
 * Time: 15:01
 */
class HandlerRadarEditorFrom(val form: RadarEditorForm, private var message: AirspaceEntryMessage) {

	private var _airspaceEntry: AirspaceEntry = null
	private var methodOfController: AirspaceEntry => Unit = message.method
	private var radar: Radar = _
	private var savedNewAirspaceEntry = true
	// флаг на надобность сохранить как новый объект
	private val radarTypeMap = new collection.mutable.HashMap[RadarType, Radar]
	private val comboBoxParameterCurrentRadar = new mutable.HashMap[RadarTypeParameter, JComboBox[Double]]()

	val changeLocationListener = new AirspaceChangeLocationOnFormListener(form.locationForm)

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

	val typeRadarComboBoxItemListener = new ItemListener {
		def itemStateChanged(e: ItemEvent): Unit = {
			updateRadar()
		}
	}

	val changeRadarParameterComboBoxItemListener = new ItemListener {
		override def itemStateChanged(e: ItemEvent): Unit = {
			updateRadarParameter()
		}
	}

	val locationNameComboBoxKeyListener: KeyListener = new KeyListener {

		override def keyReleased(e: KeyEvent): Unit = Unit

		override def keyTyped(e: KeyEvent): Unit = Unit

		override def keyPressed(e: KeyEvent): Unit = {
			e.getSource match {
				case source: javax.swing.JTextField =>
					if (e.getKeyCode == KeyEvent.VK_ENTER) {
						val text = source.getText

						if (text.trim.length >= 3) {
							val list = Settlement.getSettlements(text.trim)
							println(list.size)

							form.locationNameComboBox.removeAllItems()
							for(settlement: Settlement <- list){
								form.locationNameComboBox.addItem(settlement)
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

	initComboBoxes()
	form.panelParm = new JPanel()
	updateRadar()
	updateForm()

	message match {
		case message: CreateAirspaceEntryMessage =>
			airspaceEntry = createAirspaceEntry()
		case message: EditAirspaceEntryMessage =>
			airspaceEntry = message.airspaceEntry
			radar = airspaceEntry.radar
			form.typeRadarComboBox.setSelectedItem(radar.radarName)
	}

	initSpinners()
	initTextField()

	changeLocationListener.updateFields(airspaceEntry.airspace.asInstanceOf[SphereAirspace].getLocation)

	def createAirspaceEntry(): AirspaceEntry = {
		savedNewAirspaceEntry = false
		new AirspaceEntry(new SphereAirspaceFactory(radar, message.wwd, false))
	}

	def initSpinners() {
		form.altitudeSpinner = new JSpinner(
			new SpinnerNumberModel(
				PropertyProgram.getDefaultAltitudeForRadar,
				PropertyProgram.getMinAltitudeForRadar,
				PropertyProgram.getMaxAltitudeForRadar,
				PropertyProgram.getStepAltitudeForRadar
			))
	}

	def initComboBoxes() {
		form.typeRadarComboBox = new JComboBox[RadarType]
		for (radarType <- RadarTypes.values) {
			form.typeRadarComboBox.addItem(radarType)
		}
		form.typeRadarComboBox.setSelectedIndex(0)
		form.typeRadarComboBox.addItemListener(typeRadarComboBoxItemListener)

		form.locationNameComboBox = new JComboBox[Settlement]
		form.locationNameComboBox.setEditable(true)
		form.locationNameComboBox.getEditor.getEditorComponent.addKeyListener(locationNameComboBoxKeyListener)
	}

	def updateRadarLocation(){
		form.locationNameComboBox.getSelectedItem match {
			case settlement: Settlement =>
				radar.latLon = settlement.latlon
			case _ =>

		}
	}

	def updateRadar(): Radar = {
		val radarName = form.typeRadarComboBox.getSelectedItem.asInstanceOf[RadarType]
		if (radar == null || radarName != radar.radarName) {
			if (radarTypeMap.keys.exists(radarName == _)) {
				radar = radarTypeMap(radarName)
			} else {
				radar = RadarFactory(radarName)
			}
			updateForm()
		}
		updateRadarParameter()
		updateRadarLocation()
		radarTypeMap.put(radarName, radar)
		radar
	}

	def airspaceEntry: AirspaceEntry = _airspaceEntry

	private def airspaceEntry_=(value: AirspaceEntry): Unit = {
		_airspaceEntry = value
		_airspaceEntry.editor.addEditListener(changeLocationListener)
	}

	def initTextField() {
		form.nameAirspaceTextField = new JTextField(airspaceEntry.nameAirspaceEntry)
	}

	def eventCloseForm() {
		airspaceEntry.editor.removeEditListener(changeLocationListener)
	}

	/**
	 * Метод генирить данные формы
	 */
	def updateForm() {
		form.locationHelp = new JButton()
		form.locationHelp.addActionListener(locationNameHelpButton)

		form.panelParm.removeAll()
		comboBoxParameterCurrentRadar.clear()

		val cc: CellConstraints = new CellConstraints

		val columns = "fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):grow"
		val rows = "center:d:noGrow,top:4dlu:noGrow" + (",center:max(d;4px):noGrow,top:4dlu:noGrow" * radar.radarParameters.size)

		form.panelParm.setLayout(new FormLayout(columns, rows))

		val label = new JLabel
		label.setHorizontalAlignment(4)
		label.setText(RadarTypeParameters.FREQUENCY_BAND.descriptions + ":")
		form.panelParm.add(label, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL))

		val comboBox = new JComboBox[Char]()
		comboBox.addItem(radar.FREQUENCY_BAND)
		comboBox.setEnabled(false)

		form.panelParm.add(comboBox, cc.xy(3, 1))

		var index = 1 + 2
		for (parm <- radar.radarParameters) {
			val label = new JLabel()
			label.setHorizontalAlignment(4)
			label.setText(parm._1.descriptions + ":")
			form.panelParm.add(label, cc.xy(1, index))

			val comboBox = new JComboBox[Double]()

			for (v <- radar.radarParameters(parm._1)) comboBox.addItem(v)

			if (comboBox.getItemCount == 1) comboBox.setEnabled(false)

			comboBox.setSelectedItem(radar.setRadarParameters(parm._1))
			comboBox.addItemListener(changeRadarParameterComboBoxItemListener)
			form.panelParm.add(comboBox, cc.xy(3, index))

			comboBoxParameterCurrentRadar += (parm._1 -> comboBox)

			index += 2
		}

		VisibleUtils.packFrame(form.parent)
		VisibleUtils.setMinMaxSizeFrame(form.parent)
	}

	def updateRadarParameter() {
		for (tuple <- comboBoxParameterCurrentRadar) {
			radar.setRadarParameters.update(tuple._1, tuple._2.getSelectedItem.asInstanceOf[Double])
		}

		if (form.altitudeSpinner != null)
			radar.altitude = form.altitudeSpinner.getValue.asInstanceOf[Int]
	}
}

case class Settlement(name: String, latlon: LatLon){

	override def toString = {
		name
	}
}

object Settlement {

	def getSettlements(text: String): List[Settlement] = {
		val list = new mutable.MutableList[Settlement]

		val sql = """
			SELECT
				CASE WHEN TRANSLATE IS NOT NULL THEN TRANSLATE ELSE NAME END AS NAME,
				LAT,
				LON
			FROM
				GEO_NAME
			WHERE
		        FEATURE_CLASS != 'A'
		            AND
				(
					UPPER(NAME) LIKE :startName
						OR
					UPPER(TRANSLATE) LIKE :startName
				)
			ORDER BY
				NAME;
		          """

		val session = DB.createHibernateSession()

		val sqlQuery = session.createSQLQuery(sql)

		sqlQuery.addScalar("NAME", DB.STRING_TYPE)
		sqlQuery.addScalar("LAT", DB.DOUBLE_TYPE)
		sqlQuery.addScalar("LON", DB.DOUBLE_TYPE)

		sqlQuery.setParameter("startName", text.trim.toUpperCase + "%", DB.STRING_TYPE)

		val result = sqlQuery.scroll(ScrollMode.FORWARD_ONLY)

		while(result.next()){
			val name = result.getString(0)
			val lat = result.getDouble(1)
			val lon = result.getDouble(2)
			val latLon = LatLon.fromDegrees(lat, lon)

			val settlement = new Settlement(name, latLon)

			list += settlement
		}

		result.close()
		session.close()

		list.toList
	}
}
