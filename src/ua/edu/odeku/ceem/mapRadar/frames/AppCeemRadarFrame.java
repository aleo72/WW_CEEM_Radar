package ua.edu.odeku.ceem.mapRadar.frames;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.util.PerformanceStatistic;
import gov.nasa.worldwind.util.StatisticsPanel;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwindx.examples.LayerPanel;
import gov.nasa.worldwindx.examples.util.HighlightController;
import gov.nasa.worldwindx.examples.util.ToolTipController;
import ua.edu.odeku.ceem.mapRadar.panels.AppMainPanel;
import ua.edu.odeku.ceem.mapRadar.resource.ResourceString;
import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

import static ua.edu.odeku.ceem.mapRadar.utils.gui.VisibleUtils.insertBeforeCompass;

/**
 * Класс главного Фрейма программы
 * User: Aleo
 * Date: 03.11.13
 * Time: 15:46
 */
public class AppCeemRadarFrame extends JFrame {
    private Dimension canvasSize = new Dimension(800, 600);

    ResourceBundle resource = ResourceBundle.getBundle("strings", PropertyProgram.getCurrentLocale());

    protected AppMainPanel wwjPanel;
    protected LayerPanel layerPanel;
    protected StatisticsPanel statsPanel;
    protected JMenuBar menuBar;

    public AppCeemRadarFrame() {
        this.initialize(true, true, false);
    }

    public AppCeemRadarFrame(Dimension size) {
        this.canvasSize = size;
        this.initialize(true, true, false);
    }

    public AppCeemRadarFrame(boolean includeStatusBar, boolean includeLayerPanel, boolean includeStatsPanel) {
        this.initialize(includeStatusBar, includeLayerPanel, includeStatsPanel);
    }

    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuMain = new JMenu(ResourceString.get("program")); // Пункт меню "Программа"

        JMenu menuView = new JMenu(ResourceString.get("view")); // Пункт меню "Программа"
        fillMenuView(menuView);

        menuBar.add(menuMain);
        menuBar.add(menuView);

        return menuBar;
    }

    protected void fillMenuView(JMenu menu) {
        Model model = wwjPanel.getWwd().getModel();

        for (final Layer  layer : model.getLayers() ){
            String name = layer.getName();
            if (name.trim().isEmpty()){
                name = "Elevation";
            }
            final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(name);
            menuItem.setSelected(layer.isEnabled());
            menuItem.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                    layer.setEnabled(item.isSelected());
                }
            });
            menu.add(menuItem);
            System.out.println(layer.getName());
        }

    }

    protected void initialize(boolean includeStatusBar, boolean includeLayerPanel, boolean includeStatsPanel) {
        // Create the WorldWindow.
        this.wwjPanel = this.createAppMainPanel(this.canvasSize, includeStatusBar);
        this.wwjPanel.setPreferredSize(canvasSize);

        // Put the pieces together.
        this.getContentPane().add(wwjPanel, BorderLayout.CENTER);

        this.menuBar = createMenuBar();
        this.setJMenuBar(menuBar);

        // Create and install the view controls layer and register a controller for it with the World Window.
        ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
        insertBeforeCompass(getWwd(), viewControlsLayer);
        this.getWwd().addSelectListener(new ViewControlsSelectListener(this.getWwd(), viewControlsLayer));

        // Register a rendering exception listener that's notified when exceptions occur during rendering.
        this.wwjPanel.getWwd().addRenderingExceptionListener(new RenderingExceptionListener() {
            public void exceptionThrown(Throwable t) {
                if (t instanceof WWAbsentRequirementException) {
                    String message = "Computer does not meet minimum graphics requirements.\n";
                    message += "Please install up-to-date graphics driver and try again.\n";
                    message += "Reason: " + t.getMessage() + "\n";
                    message += "This program will end when you press OK.";

                    JOptionPane.showMessageDialog(AppCeemRadarFrame.this, message, "Unable to Start Program",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
        });

        // Search the layer list for layers that are also select listeners and register them with the World
        // Window. This enables interactive layers to be included without specific knowledge of them here.
        for (Layer layer : this.wwjPanel.getWwd().getModel().getLayers()) {
            if (layer instanceof SelectListener) {
                this.getWwd().addSelectListener((SelectListener) layer);
            }
        }

        this.pack();

        // Center the application on the screen.
        WWUtil.alignComponent(null, this, AVKey.CENTER);
        this.setResizable(true);
    }

    protected AppMainPanel createAppMainPanel(Dimension canvasSize, boolean includeStatusBar) {
        return new AppMainPanel(canvasSize, includeStatusBar);
    }

    public Dimension getCanvasSize() {
        return canvasSize;
    }

    public AppMainPanel getWwjPanel() {
        return wwjPanel;
    }

    public WorldWindow getWwd() {
        return this.wwjPanel.getWwd();
    }

    public StatusBar getStatusBar() {
        return this.wwjPanel.getStatusBar();
    }

    public LayerPanel getLayerPanel() {
        return layerPanel;
    }

    public StatisticsPanel getStatsPanel() {
        return statsPanel;
    }

    public void setToolTipController(ToolTipController controller) {
        if (this.wwjPanel.toolTipController != null)
            this.wwjPanel.toolTipController.dispose();

        this.wwjPanel.toolTipController = controller;
    }

    public void setHighlightController(HighlightController controller) {
        if (this.wwjPanel.highlightController != null)
            this.wwjPanel.highlightController.dispose();

        this.wwjPanel.highlightController = controller;
    }
}
