/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.tools.geoName.imports

import ua.edu.odeku.ceem.mapRadar.tools.{ToolFrame, CeemRadarTool}
import javax.swing.{JFrame, JPanel}
import java.awt.event.{ActionEvent, ActionListener}

/**
 * User: Aleo Bakalov
 * Date: 11.12.13
 * Time: 13:10
 */
class ImportGeoNameTool extends CeemRadarTool {

  val importGeoName = new ImportGeoName
  val importer = new GeoNameImporter(importGeoName.progressBar, closeFrame)

  def closeFrame() = parentToolFrame.dispose()

  def startFunction = (toolFrame : ToolFrame) => ()

  def endFunction = (frame : ToolFrame) => closeFrame()

  def rootPanel: JPanel = importGeoName.getRootPanel

  importGeoName.importButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent){
      if (importGeoName.fileChooserPanel.getFile != null && !importer.isAlive) {
        importGeoName.importButton.setEnabled(false)
        importGeoName.cancelButton.setEnabled(true)
        importer.stopFlag = false
        importer.setFileInput(importGeoName.fileChooserPanel.getFile)
        importer.start()
      }
    }
  })

  importGeoName.cancelButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      importGeoName.importButton.setEnabled(true)
      importGeoName.cancelButton.setEnabled(false)
      importer.stopFlag = true
      importer.setFileInput(null)
      endFunction.apply(parentToolFrame)
    }
  })

	/**
	 * Метод для инициализации инструмента,
	 * вызовется при мервом вызове, а не в помент создания
	 */
	override def init(): Unit = {
		// none
	}
}
