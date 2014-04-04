/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder

import scala.collection.mutable.ArrayBuffer

/**
 * User: Aleo Bakalov
 * Date: 24.03.2014
 * Time: 15:38
 */
package object imports {

	val ADMIN_BORDER_FOLDER = "admin_border"

	val SUFFIX_ADMIN_BORDER_FILE = ".csv"

	val MAP_COUNTRIES = "10m_admin_0_countries" + SUFFIX_ADMIN_BORDER_FILE
	val STATES_PROVINCES_SHP = "10m_admin_1_states_provinces_shp" + SUFFIX_ADMIN_BORDER_FILE

	val VALID_NAME_ADMIN_BORDER_FILES = Array(MAP_COUNTRIES, STATES_PROVINCES_SHP)


}
