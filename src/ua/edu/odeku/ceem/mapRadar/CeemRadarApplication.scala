/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar

import java.awt.Toolkit
import java.io.File
import java.util.Locale
import javax.imageio.ImageIO
import javax.swing.{JFrame, JWindow}

import gov.nasa.worldwind.Configuration
import ua.edu.odeku.ceem.mapRadar.db.DB
import ua.edu.odeku.ceem.mapRadar.panels.ImagePanel
import ua.edu.odeku.ceem.mapRadar.settings.Settings

/**
 * Объект старта программы
 * и настройки ее
 *
 * Created by Aleo on 01.02.14.
 */
object CeemRadarApplication extends App {

  Application.initLocale()
	Application.initSystemProperty()
  Application.initLookAndFeel(Settings.Program.Style.lookAndFeelInfo)
	Application.initConfigurationProgram()


	Application.showStartWindow(visible = true)

	Application.initDatabaseConnection()
	Application.initAdminBorderManager()

  AppCeemRadarFrame.setTitle(Settings.Program.name)
	AppCeemRadarFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
	java.awt.EventQueue.invokeLater(new Runnable {
		override def run(): Unit = {
			AppCeemRadarFrame.setVisible(true)
		}
	})

	Application.showStartWindow(visible = false)


}

private object Application {

  /**
   * Ініціализация локалізації программи
   */
  def initLocale() {
    Locale.setDefault(Settings.Program.locale)
  }

	/**
	 * Инициализация системных настроек
	 */
	def initSystemProperty() {
		System.setProperty("java.net.useSystemProxies", "true")
		if (Configuration.isMacOS) {
			System.setProperty("apple.laf.useScreenMenuBar", "true")
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", Settings.Program.name)
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
	def initConfigurationProgram() {

		// Файл с настройками путей сохранения Cache
		Configuration.setValue(
			"gov.nasa.worldwind.avkey.DataFileStoreConfigurationFileName",
			"ua/edu/odeku/ceem/mapRadar/config/DataFileStore.xml"
		)

	}

	private var startWindow: JWindow = null

	/**
	 * Показ стартового окна
	 * @param visible true если нужно показать, false скрыть окно
	 */
	def showStartWindow(visible: Boolean) {

		if (visible)
			showStartImageWindow()
		else
			stopShowStartImageWindow()

		/**
		 * Вывести на экран стартовое окно
		 */
		def showStartImageWindow() {

			if (startWindow != null)
				startWindow.dispose()

			startWindow = new JWindow

			java.awt.EventQueue.invokeLater(new Runnable {

				override def run(): Unit = {
					try {
            val image = ImageIO.read(new File(Settings.Program.Start.fileStartWindow))
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
				startWindow.dispose()
			}
			startWindow = null
		}
	}

	/**
	 * Метод инициализации базы данных
	 */
  def initDatabaseConnection(): Unit = if (false) DB.database


	/**
	 * Инициализация AdminBorderManager
	 */
	def initAdminBorderManager() {
    //		AdminBorderManager.viewCountryBorder
	}
}