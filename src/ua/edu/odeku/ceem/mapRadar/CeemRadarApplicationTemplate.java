package ua.edu.odeku.ceem.mapRadar;

import gov.nasa.worldwind.Configuration;
import ua.edu.odeku.ceem.mapRadar.db.DB;
import ua.edu.odeku.ceem.mapRadar.frames.AppCeemRadarFrame;
import ua.edu.odeku.ceem.mapRadar.panels.ImagePanel;
import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Базовый класс для программы CeemRadar
 * User: Aleo
 * Date: 03.11.13
 * Time: 14:58
 */
public class CeemRadarApplicationTemplate {

    static final JWindow window = new JWindow();

    static {
        System.setProperty("java.net.useSystemProxies", "true");
        if (Configuration.isMacOS()) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", PropertyProgram.getNameProgram());
            System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
            System.setProperty("apple.awt.brushMetalLook", "true");
        } else if (Configuration.isWindowsOS()) {
            System.setProperty("sun.awt.noerasebackground", "true"); // prevents flashing during window resizing
        }

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CeemRadarApplicationTemplate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public static AppCeemRadarFrame start(String appName, Class appFrameClass) {
        if (Configuration.isMacOS() && appName != null) {
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
        }

        try {

            showStartImageWindow();

            // Init DB
            DB.createEntityManager();

            final AppCeemRadarFrame frame = (AppCeemRadarFrame) appFrameClass.newInstance();
            frame.setTitle(appName);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    frame.setVisible(true);
                }
            });

            showStopStartImageWindow();

            return frame;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        start(PropertyProgram.getNameProgram(), AppCeemRadarFrame.class);
    }

    private static void showStartImageWindow(){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BufferedImage image = null;
                try {
                    image = ImageIO.read(new File("resources/ww_start.png"));
                    ImagePanel panel = new ImagePanel(image);
                    window.add(panel);
                    window.setSize(image.getWidth(), image.getHeight());
                    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                    window.setLocation(dimension.width / 2 - window.getWidth() / 2, dimension.height / 2 - window.getHeight() / 2);
                    window.setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }

    private static void showStopStartImageWindow(){
        if(window.isVisible()){
            window.dispose();
        }
    }
}
