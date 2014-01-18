/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.entry

import gov.nasa.worldwind.WorldWindow

/**
 * Класс обертка для создания и регистрации Airspace
 * Так как редается через несколько java классов
 * Created by Aleo on 18.01.14.
 */
class AirspaceEntryMessage(val wwd: WorldWindow, val method: AirspaceEntry => Unit);

/**
 * Класс сообщение на создание
 * @param _wwd WorldWindow
 * @param _register метод регистрации изменений
 */
case class CreateAirspaceEntryMessage(_wwd: WorldWindow, _register: AirspaceEntry => Unit) extends AirspaceEntryMessage(_wwd, _register)

/**
 * Класс сообщение на редактирование
 * @param _wwd WorldWindow
 * @param _register метод регистрации изменений
 */
case class EditAirspaceEntryMessage(airspaceEntry: AirspaceEntry, _wwd: WorldWindow, _register: AirspaceEntry => Unit) extends AirspaceEntryMessage(_wwd, _register)
