/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.settings

import java.awt.BorderLayout
import java.util.ResourceBundle
import javax.swing._

import com.jhlabs.awt.ParagraphLayout

/**
 * Created by aleo on 06.08.14.
 */
class SettingsFrame extends JDialog {

  this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE)

  this.setTitle(ResourceBundle.getBundle("frameTitle").getString("title.settings"))

  this.setLayout(new BorderLayout())

  val panelHelp = new JPanel()


  val panelContent: JPanel = createContentPanel()
  val scrollPane = new JScrollPane(panelContent)

  this.add(panelHelp, BorderLayout.NORTH)
  this.add(scrollPane, BorderLayout.CENTER)

  this.pack()

  def createContentPanel(): JPanel = {
    val panel = new JPanel()
//    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))

    import ua.edu.odeku.ceem.mapRadar.settings.Settings.file

    val programPanel = new JPanel(new ParagraphLayout()); panel.add(programPanel)
    programPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("label").getString("settings_border_program")))
    programPanel.add(Settings.Program.programNameProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    programPanel.add(Settings.Program.programNameProperty.editableComponent)

    programPanel.add(Settings.Program.localeProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    programPanel.add(Settings.Program.localeProperty.editableComponent)

    programPanel.add(Settings.Program.debugProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    programPanel.add(Settings.Program.debugProperty.editableComponent)

    /**********************/

    val toolsPanel = new JPanel(new ParagraphLayout()); panel.add(toolsPanel)
    toolsPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("label").getString("settings_border_tools")))

    val radarPanel = new JPanel(new ParagraphLayout()); toolsPanel.add(radarPanel)
    radarPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("label").getString("settings_border_tools_radar")))

    radarPanel.add(Settings.Program.Tools.Radar.maxAltitudeForRadarProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.maxAltitudeForRadarProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.minAltitudeForRadarProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.minAltitudeForRadarProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.defaultAltitudeForRadarProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.defaultAltitudeForRadarProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.stepAltitudeForRadarProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.stepAltitudeForRadarProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.viewToRadarHeightProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.viewToRadarHeightProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.counterRadarProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.counterRadarProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.pulsePowerProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.pulsePowerProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.waveLengthProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.waveLengthProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.antennaDiameterProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.antennaDiameterProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.reflectivityMeteoGoalsProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.reflectivityMeteoGoalsProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.attenuationProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.attenuationProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.radiusProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.radiusProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.grainFactorProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.grainFactorProperty.editableComponent)

    radarPanel.add(Settings.Program.Tools.Radar.Isolines.SanitaryStandards.radiationPowerProperty.jLabel, ParagraphLayout.NEW_PARAGRAPH)
    radarPanel.add(Settings.Program.Tools.Radar.Isolines.SanitaryStandards.radiationPowerProperty.editableComponent)

    panel
  }
}
