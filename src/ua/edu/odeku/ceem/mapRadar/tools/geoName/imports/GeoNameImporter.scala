/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.tools.geoName.imports

import java.io.{File, IOException}
import java.util
import javax.swing.JProgressBar

import ua.edu.odeku.ceem.mapRadar.db.DB
import ua.edu.odeku.ceem.mapRadar.tools.geoName.models.GeoNames
import ua.edu.odeku.ceem.mapRadar.exceptions.db.models.GeoNameException
import ua.edu.odeku.ceem.mapRadar.settings.Settings
import ua.edu.odeku.ceem.mapRadar.utils.gui.UserMessage

/**
 * User: Aleo Bakalov
 * Date: 11.12.13
 * Time: 13:34
 */
class GeoNameImporter(val progressBar: JProgressBar, val closeHandler: Function0[Unit]) extends Thread {

  var stopFlag: Boolean = false
  private var file: File = null
  private var lines: Array[String] = null

  def this(progressBar: JProgressBar) {
    this(progressBar, () => {})
  }

  def setFileInput(file: File) {
    this.file = file
  }

  override def run() {
    try {
      if (!stopFlag && file != null) {
        progressBar.setValue(0)
        lines = readFile
        try {
          if (lines != null && lines.length > 0) {
            progressBar.setMaximum(lines.length)
            DB.database withSession { implicit session =>
              for (line <- lines; if !stopFlag) {
                progressBar.setValue(progressBar.getValue + 1)
                val geoName = GeoNames.createGeoName(line)
                try {
                  GeoNames.SavingCache += geoName
                  if (Settings.Program.debug) println(geoName)
                }
                catch {
                  case e: Exception => System.err.println(e.getMessage)
                }
              }
              GeoNames.SavingCache.flush()
            }
          }
        }
        catch {
          case e: GeoNameException =>
            e.printStackTrace()
            UserMessage.error(null, e.getMessage)
        }
      }
    }
    catch {
      case ex: Exception => ex.printStackTrace()
    }
    finally {
      clear()
      closeHandler.apply()
    }
  }

  private def clear() {
    stopFlag = false
    progressBar.setValue(0)
    file = null
    lines = null
  }

  private def readFile: Array[String] = {
    var lines: Array[String] = null
    try {
      var line: String = null
      val list: util.LinkedList[String] = new util.LinkedList[String]
      val iter = scala.io.Source.fromFile(file).getLines()
      for (line <- iter; if !line.trim.isEmpty; if !stopFlag) {
        list add line
      }
      lines = list.toArray(new Array[String](0))
    }
    catch {
      case e: IOException =>
        e.printStackTrace()
        UserMessage.error(null, e.getMessage)
    }
    if (stopFlag) lines = null
    lines
  }
}
