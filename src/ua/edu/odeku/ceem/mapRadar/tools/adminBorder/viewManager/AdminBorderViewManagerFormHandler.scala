/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.viewManager

import java.awt.event.{ActionEvent, ActionListener}

import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.models.{CountryBorders, ProvinceBorders}


/**
 * Обработчик
 *
 * Created by Aleo on 28.03.2014.
 */
class AdminBorderViewManagerFormHandler(val tool: AdminBorderViewManagerTool) {
	val form = tool.form

  val countryBorderModel = new CountryBorderTableModel
  val provinceBorderModel = new ProvinceBorderTableModel

  form.countryBorderTable.setModel(countryBorderModel)
  form.countryBorderTable.setAutoCreateRowSorter(true)

  form.provinceBorderTable.setModel(provinceBorderModel)
  form.provinceBorderTable.setAutoCreateRowSorter(true)

	form.saveButton.addActionListener(new ActionListener {
		override def actionPerformed(e: ActionEvent): Unit = {

      CountryBorders.updateVisibleBorders(countryBorderModel.data)
      ProvinceBorders.updateVisibleBorders(provinceBorderModel.data)

		}
  })
}
