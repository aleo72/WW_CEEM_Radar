/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar

import java.awt.{BorderLayout, Dimension}
import java.util.ResourceBundle
import javax.swing.{JFrame, JMenuBar, JOptionPane, JPanel}

import gov.nasa.worldwind.avlist.AVKey
import gov.nasa.worldwind.awt.WorldWindowGLCanvas
import gov.nasa.worldwind.event.{RenderingExceptionListener, SelectEvent, SelectListener}
import gov.nasa.worldwind.exception.WWAbsentRequirementException
import gov.nasa.worldwind.layers.{ViewControlsLayer, ViewControlsSelectListener, WorldMapLayer}
import gov.nasa.worldwind.util.{StatusBar, WWUtil}
import gov.nasa.worldwind.{Model, WorldWind}
import gov.nasa.worldwindx.examples.ClickAndGoSelectListener
import ua.edu.odeku.ceem.mapRadar.menu.ProgramBar
import ua.edu.odeku.ceem.mapRadar.settings.Settings
import ua.edu.odeku.ceem.mapRadar.utils.gui.VisibleUtils
import ua.edu.odeku.ceem.mapRadar.utils.{HighlightController, ToolTipController}

import scala.collection.mutable

/** *********************************************************************************************************************
  * Главное окно программы
  *
  * Created by Алексей on 17.05.2014.
  * *********************************************************************************************************************/
object AppCeemRadarFrame extends JFrame {

  private val resource =
    ResourceBundle.getBundle(DefaultValue.stringBundle, Settings.Program.locale)

  val canvasSize: Dimension = DefaultValue.dimension

  val wwjPanel = new AppMainPanel(canvasSize, DefaultValue.createStaticPanel)

  def wwd = wwjPanel.wwd

  val toolsComponents = new mutable.HashMap[String, JFrame]()

  val frameMenuBar = ProgramBar.createProgramMainBar()
  this.setJMenuBar(frameMenuBar)

  this.getContentPane.add(wwjPanel, BorderLayout.CENTER)

  initViewControlsLayer()

  this.wwjPanel.wwd.addRenderingExceptionListener(new RenderingExceptionListener {
    override def exceptionThrown(t: Throwable): Unit = {
      if (t.isInstanceOf[WWAbsentRequirementException]) {
        var message: String = "Computer does not meet minimum graphics requirements.\n"
        message += "Please install up-to-date graphics driver and try again.\n"
        message += "Reason: " + t.getMessage + "\n"
        message += "This program will end when you press OK."
        JOptionPane.showMessageDialog(AppCeemRadarFrame.this, message, "Unable to Start Program", JOptionPane.ERROR_MESSAGE)
        System.exit(-1)
      }
    }
  })

  import scala.collection.JavaConversions._

  for (layer <- wwd.getModel.getLayers) {
    layer match {
      case listener: SelectListener =>
        wwd.addSelectListener(listener)
      case _ =>
    }
  }

  this.pack()

  // Center the application on the screen.
  WWUtil.alignComponent(null, this, AVKey.CENTER)
  this.setResizable(true)

  // Search the layer list for layers that are also select listeners and register them with the World
  // Window. This enables interactive layers to be included without specific knowledge of them here.
  def initViewControlsLayer() {
    val viewControlsLayer = new ViewControlsLayer
    VisibleUtils.insertBeforeCompass(wwd, viewControlsLayer)
    this.wwd.addSelectListener(new ViewControlsSelectListener(wwd, viewControlsLayer))
  }

  def createMenuBar() = {
    val mainMenu = new JMenuBar
    // Todo create fill menu
    //		val mainMenu = new JMenu(ResourceBundle.getBundle("menu").getString("program"))

    //		val menuView = new JMenu()
    mainMenu
  }
}

/** *********************************************************************************************************************
  * Класс панели, где будет находится главный общект отображения WorldWindow
  *
  * @param canvasSize - размер WorldWindow
  * @param includeStatusBar - нужноли создавать статус бар у панели
  *
  *********************************************************************************************************************/
protected class AppMainPanel(canvasSize: Dimension, includeStatusBar: Boolean) extends JPanel(new BorderLayout()) {

  this.setPreferredSize(canvasSize)

  /**
   * WorldWindow
   */
  val wwd = new WorldWindowGLCanvas

  // set size
  wwd.setPreferredSize(canvasSize)

  // Create the default model as described in the current worldwind properties.
  this.wwd.setModel(WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME).asInstanceOf[Model])

  // Setup a select listener for the worldmap click-and-go feature
  this.wwd.addSelectListener(new ClickAndGoSelectListener(this.wwd, classOf[WorldMapLayer]))

  this.add(this.wwd, BorderLayout.CENTER)

  /**
   * Status Bar for AppCeemRadarFrame
   */
  val statusBar = if (!includeStatusBar) null
  else {
    val status = new StatusBar
    this.add(status, BorderLayout.PAGE_END)
    status.setEventSource(wwd)
    status
  }

  val toolTipController = new ToolTipController(this.wwd, AVKey.DISPLAY_NAME, null)
  val highlightController = new HighlightController(this.wwd, SelectEvent.ROLLOVER)
}

private object DefaultValue {

  val dimension = new Dimension(800, 600)
  val stringBundle = "strings"
  val createStaticPanel = true
}

