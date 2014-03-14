/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar

import gov.nasa.worldwind.Configuration
import ua.edu.odeku.ceem.mapRadar.settings.Property
import javax.swing.{JFrame, JWindow}
import javax.imageio.ImageIO
import java.io.File
import ua.edu.odeku.ceem.mapRadar.panels.ImagePanel
import java.awt.Toolkit
import ua.edu.odeku.ceem.mapRadar.db.DB
import ua.edu.odeku.ceem.mapRadar.frames.AppCeemRadarFrame

/**
 * Объект старта программы
 * и настройки ее
 *
 * Created by Aleo on 01.02.14.
 */
object CeemRadarApplication extends App {

	Application.initSystemProperty()
	Application.initLookAndFeel(Property.LOOK_AND_FEEL_INFO.toString)
	Application.initConfigurationProgram()

	try {
		Application.showStartWindow(visible = true)

		if (Property.INIT_DB.toBoolean) {
			DB.createEntityManager()
		}

		val frameProgram = new AppCeemRadarFrame
		frameProgram.setTitle(Property.NAME_PROGRAM.toString)
		frameProgram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
		java.awt.EventQueue.invokeLater(new Runnable {
			override def run(): Unit = {
				frameProgram.setVisible(true)
			}
		})

		Application.showStartWindow(visible = false)
	}

}

private object Application {

	/**
	 * Инициализация системных настроек
	 */
	def initSystemProperty() {
		System.setProperty("java.net.useSystemProxies", "true")
		if (Configuration.isMacOS) {
			System.setProperty("apple.laf.useScreenMenuBar", "true")
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", Property.NAME_PROGRAM.toString)
			System.setProperty("com.apple.mrj.application.growbox.intrudes", "false")
			System.setProperty("apple.awt.brushMetalLook", "true")
		}
		else if (Configuration.isWindowsOS) {
			System.setProperty("sun.awt.noerasebackground", "true")
		}
	}

	/**
	 * Инициализация внешнего вида программы
	 * @param lookAndFeel название внешнего вида, по умолчанию "Nimbus"
	 */
	def initLookAndFeel(lookAndFeel: String = "Nimbus") {
		try {
			for (info <- javax.swing.UIManager.getInstalledLookAndFeels if lookAndFeel == info.getName) {
				javax.swing.UIManager.setLookAndFeel(info.getClassName)
			}
		}
		catch {
			case ex: Any =>
				java.util.logging.Logger.getLogger(
					classOf[CeemRadarApplicationTemplate].getName
				).log(java.util.logging.Level.SEVERE, null, ex)
		}
	}

	/**
	 * Метод инициализации конфигурации WorldWind
	 */
	def initConfigurationProgram(){

		// Файл с настройками путей сохранения Cache
		Configuration.setValue (
			"gov.nasa.worldwind.avkey.DataFileStoreConfigurationFileName",
			"ua/edu/odeku/ceem/mapRadar/config/DataFileStore.xml"
		)

	}

	private var startWindow: JWindow = null

	/**
	 * Показ стартового окна
	 * @param visible true если нужно показать, false скрыть окно
	 */
	def showStartWindow(visible: Boolean){

		if(visible)
			showStartImageWindow()
		else
			stopShowStartImageWindow()

		/**
		 * Вывести на экран стартовое окно
		 */
		def showStartImageWindow() {

			if(startWindow != null)
				startWindow.dispose()

			startWindow = new JWindow

			java.awt.EventQueue.invokeLater(new Runnable {

				override def run(): Unit = {
					try {
						val image = ImageIO.read(new File(Property.FILE_START_WINDOW.toString))
						val panel = new ImagePanel(image)

						startWindow.add(panel)
						startWindow.setSize(image.getWidth, image.getHeight)

						val dimension = Toolkit.getDefaultToolkit.getScreenSize

						startWindow.setLocation(
							dimension.width / 2 - startWindow.getWidth / 2,
							dimension.height / 2 - startWindow.getHeight / 2
						)
						startWindow.setVisible(true)
					} catch {
						case ex: java.io.IOException =>
							ex.printStackTrace()
					}
				}
			})
		}

		/**
		 * Закрываем стартовое окно
		 */
		def stopShowStartImageWindow() {
			if (startWindow != null && startWindow.isVisible) {
				startWindow = null
			}
			startWindow = null
		}
	}
}