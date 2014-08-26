/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.imports

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JTextField, JProgressBar, JFileChooser}
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
	private val chooserCountryButtonListener = new ChooserFileButtonListener(this, form.fileCountry)
	private val chooserProvincesButtonListener = new ChooserFileButtonListener(this, form.fileProvinces)
	form.chooserCountryButton.addActionListener(chooserCountryButtonListener)
	form.chooserProvincesButton.addActionListener(chooserProvincesButtonListener)

	form.importButton.addActionListener(new ActionListener {
		override def actionPerformed(e: ActionEvent): Unit = {

			val countryFile: File = chooserCountryButtonListener.file
			val provincesFile: File = chooserProvincesButtonListener.file

			if(countryFile != null){
				if(importer != null)
					importer.stopProcess = true

				importer = new Importer(countryFile, provincesFile, tool)
        importer.setPriority(Thread.MAX_PRIORITY)
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
		form.fileCountry.setText(file.getAbsolutePath)
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

private class ChooserFileButtonListener(val importer: ImporterAdminBorder, val textFieldButton: JTextField) extends ActionListener {

	var file: File = null

	val fileFilter = new FileFilter {

		override def accept(f: File): Boolean = {
			if (f.isDirectory)
				return true
			val nameFile = f.getName
			for (validName: String <- VALID_NAME_ADMIN_BORDER_FILES) {
				if (validName == nameFile)
					return true
			}
			false
		}

		override def getDescription: String = "*.csv"
	}

	val fileChooser = new JFileChooser()
	fileChooser.setFileFilter(fileFilter)
	fileChooser.setCurrentDirectory(new File("resources/"))

	override def actionPerformed(e: ActionEvent): Unit = {
		val res = fileChooser.showDialog(importer.tool.form.rootPanel().asInstanceOf[Component], ResourceBundle.getBundle("frameTitle").getString("importAdminBorder_dialog_fileChooser_title"))
		if (res == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile
			textFieldButton.setText(file.getAbsolutePath)
		}
	}
}

protected class Importer(val countryFile: File, val provincesFile: File, val tool: ImportAdminBorderTool) extends Thread with StopProcess {

	stopProcess = false

	override def run() {

		if(countryFile.getName == MAP_COUNTRIES && (provincesFile == null || provincesFile.getName == STATES_PROVINCES_SHP )) {
			ImporterAdminBorders(countryFile, provincesFile, this)
			tool.endFunction.apply(tool.parentToolFrame)
		} else {
			tool.importer.viewStopImport()
		}
	}
}