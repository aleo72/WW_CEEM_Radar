/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.tools.importGeoName

import java.io.{IOException, File}
import javax.swing.JProgressBar
import ua.edu.odeku.ceem.mapRadar.utils.thread.Handler
import org.hibernate.Session
import ua.edu.odeku.ceem.mapRadar.db.DB
import ua.edu.odeku.ceem.mapRadar.db.models.GeoName
import ua.edu.odeku.ceem.mapRadar.utils.gui.UserMessage
import ua.edu.odeku.ceem.mapRadar.tools.ToolFrame
import ua.edu.odeku.ceem.mapRadar.exceptions.db.models.GeoNameException
import java.util

/**
 * User: Aleo Bakalov
 * Date: 11.12.13
 * Time: 13:34
 */
class GeoNameImporter(val progressBar : JProgressBar, val closeHandler : Function0[Unit]) extends Thread{

  var stopFlag: Boolean = false
  private var file: File = null
  private var lines: Array[String] = null

  def this(progressBar: JProgressBar) {
    this(progressBar, () => {} )
  }

  def setFileInput(file: File) {
    this.file = file
  }

  override def run() {
    try {
      if (!stopFlag && file != null) {
        progressBar.setValue(0)
        lines = readFile
        val session: Session = DB.createHibernateSession()
        try {
          if (lines != null && lines.length > 0) {

            progressBar.setMaximum(lines.length)

            for (line <- lines ; if !stopFlag) {

              progressBar.setValue(progressBar.getValue + 1)
              val geoName: GeoName = GeoName.createGeoName(line)

              try {
                session.save(geoName)
              }
              catch {
                case e: Exception => System.err.println(e.getMessage)
              }

            }

          }
        }
        catch {
          case e: GeoNameException =>
            e.printStackTrace()
            UserMessage.error(null, e.getMessage)
        }
        finally {
          DB.closeSession(session)
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
      for( line <- iter; if !line.trim.isEmpty ; if !stopFlag ){
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
