/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.imports

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JProgressBar, JFileChooser}
import javax.swing.filechooser.FileFilter
import java.io.File
import java.util.ResourceBundle
import java.awt.Component
import ua.edu.odeku.ceem.mapRadar.utils.thread.StopProcess

/**
 * Данный класс занят импортом данных
 *
 * Created by Aleo on 22.03.14.
 */
class ImporterAdminBorder(val tool: ImportAdminBorderTool) {

	private val form = tool.form.asInstanceOf[ImportAdminBorderForm]
	private var importer: Importer = null
	private val chooserFileButtonListener: ChooserFileButtonListener = new ChooserFileButtonListener(this)

	form.chooserButton.addActionListener(chooserFileButtonListener)

	form.importButton.addActionListener(new ActionListener {
		override def actionPerformed(e: ActionEvent): Unit = {
			if(chooserFileButtonListener.file != null){
				if(importer != null)
					importer.stopProcess = true

				importer = new Importer(chooserFileButtonListener.file, tool)

				importer.start()
				viewStartImport()
			}
		}
	})

	form.cancelButton.addActionListener(new ActionListener {
		override def actionPerformed(e: ActionEvent): Unit = {
			if(importer != null){
				importer.stopProcess = true
				viewStopImport()
			}
		}
	})

	def changeSelectedFileName(file: File) {
		form.selectedFileTextField.setText(file.getAbsolutePath)
	}

	def viewStartImport(){
		form.progressBar.setIndeterminate(true)
		form.importButton.setEnabled(false)
		form.cancelButton.setEnabled(true)
	}

	def viewStopImport(){
		form.progressBar.setIndeterminate(false)
		form.importButton.setEnabled(true)
		form.cancelButton.setEnabled(false)
	}
}

object ImporterAdminBorder {
	val ADMIN_BORDER_FOLDER = "admin_border"

	val SUFFIX_ADMIN_BORDER_FILE = ".csv"

	val MAP_UNITS = "10m_admin_0_map_units" + SUFFIX_ADMIN_BORDER_FILE
	val STATES_PROVINCES_SHP = "10m_admin_1_states_provinces_shp" + SUFFIX_ADMIN_BORDER_FILE

	val VALID_NAME_ADMIN_BORDER_FILES = Array(MAP_UNITS, STATES_PROVINCES_SHP)
}

private class ChooserFileButtonListener(val importer: ImporterAdminBorder) extends ActionListener {

	var file: File = null

	val fileFilter = new FileFilter {

		override def accept(f: File): Boolean = {
			if (f.isDirectory)
				return true
			val nameFile = f.getName
			for (validName: String <- ImporterAdminBorder.VALID_NAME_ADMIN_BORDER_FILES) {
				if (validName == nameFile)
					return true
			}
			false
		}

		override def getDescription: String = "*.csv"
	}

	val fileChooser = new JFileChooser()
	fileChooser.setFileFilter(fileFilter)

	override def actionPerformed(e: ActionEvent): Unit = {
		val res = fileChooser.showDialog(importer.tool.form.rootPanel().asInstanceOf[Component], ResourceBundle.getBundle("frameTitle").getString("importAdminBorder_dialog_fileChooser_title"))
		if (res == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile
			importer.changeSelectedFileName(file)
		}
	}
}

private class Importer(val file: File, val tool: ImportAdminBorderTool) extends Thread with StopProcess {

	stopProcess = false

	/**
	 * Импорт стран
	 * @return
	 */
	def startImportMapUnits(): Boolean = ImportMapUnits(file, this)

	/**
	 * Импорт региональных границ
	 * @return
	 */
	def startImportStatesProvincesShp(): Boolean = ImportStatesProvinces(file, this)

	override def run() {
		val fileName = file.getName

		val resultMatch: Boolean = fileName match {
			case ImporterAdminBorder.MAP_UNITS => startImportMapUnits()
			case ImporterAdminBorder.STATES_PROVINCES_SHP => startImportStatesProvincesShp()
			case _ => false
		}

		if(resultMatch){
			tool.endFunction.apply(tool.parentToolFrame)
		} else {
			tool.importer.viewStopImport()
		}
	}
}