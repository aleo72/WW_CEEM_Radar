/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.manager

import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.models.{CountryBorder, CountryBorders, ProvinceBorder, ProvinceBorders}

import scala.collection.mutable

/**
 * User: Aleo Bakalov
 * Date: 26.03.2014
 * Time: 9:59
 */
object AdminBorderManager {

  private val _countryBorders = new mutable.HashMap[(String, String, String), CountryBorder]()
  private val _provincesBorders = new mutable.HashMap[(String, String, String), ProvinceBorder]()

  def countryBorder(name: String, admin: String, admin0a3: String): CountryBorder = {
    if (_countryBorders.contains((name, admin, admin0a3))) {
      _countryBorders((name, admin, admin0a3))
    } else {
      val value = CountryBorders(name, admin, admin0a3)
      _countryBorders += (name, admin, admin0a3) -> value
      value
    }
  }

  def provinceBorder(name: String, name1: String, iso: String): ProvinceBorder = {
    if (_provincesBorders.contains((name, name1, iso))) {
      _provincesBorders((name, name1, iso))
    } else {
      val value = ProvinceBorders(name, name1, iso)
      _provincesBorders += (name, name1, iso) -> value
      value
    }
  }

}
