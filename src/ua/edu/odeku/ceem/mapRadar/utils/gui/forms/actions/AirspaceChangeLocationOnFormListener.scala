/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.utils.gui.forms.actions

import java.text.DecimalFormat
import javax.swing.text.JTextComponent

import gov.nasa.worldwind.geom.LatLon
import gov.nasa.worldwind.render.airspaces.editor.{AirspaceEditEvent, AirspaceEditListener}
import gov.nasa.worldwind.render.airspaces.{CappedCylinder, SphereAirspace}
import ua.edu.odeku.ceem.mapRadar.settings.Settings
import ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.CeemRadarAirspace
import ua.edu.odeku.ceem.mapRadar.utils.gui.forms.LocationForm

/**
 * Слушатель изменяет значения JTextComponent в зависимости от перемещения Airspace
 *
 * User: Aleo Bakalov
 * Date: 28.01.14
 * Time: 16:24
 */
class AirspaceChangeLocationOnFormListener(val lat: JTextComponent, val lon: JTextComponent) extends AirspaceEditListener {

  private val decimalFormat = new DecimalFormat(Settings.Program.Tools.Radar.Location.decimalFormat)

	def this(location: LocationForm) {
		this(location.latTextField, location.lonTextField)
	}

	def updateFields(location: LatLon) {
		lat.setText(decimalFormat.format(location.latitude.degrees))
		lon.setText(decimalFormat.format(location.longitude.degrees))
	}

	def airspaceMoved(e: AirspaceEditEvent): Unit = {
		e.getAirspace match {
			case airspace: SphereAirspace => updateFields(airspace.getLocation)
			case airspace: CappedCylinder => updateFields(airspace.getCenter)
			case airspace: CeemRadarAirspace => updateFields(airspace.location)
		}
	}

	def airspaceResized(e: AirspaceEditEvent): Unit = {}

	def controlPointAdded(e: AirspaceEditEvent): Unit = {}

	def controlPointRemoved(e: AirspaceEditEvent): Unit = {}

	def controlPointChanged(e: AirspaceEditEvent): Unit = {}
}
