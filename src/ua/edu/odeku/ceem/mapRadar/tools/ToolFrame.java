package ua.edu.odeku.ceem.mapRadar.tools;

import ua.edu.odeku.ceem.mapRadar.utils.thread.Handler;

import javax.swing.*;
import java.awt.*;

/**
 * Общее окно для всех Tool компонентов программы
 *
 *
 * User: Aleo skype: aleo72
 * Date: 10.11.13
 * Time: 15:57
 */
public class ToolFrame extends JFrame {

    protected JPanel panel;
    protected Handler handler;

    public ToolFrame(CeemRadarTool ceemRadarTool, String title){
        this.panel = ceemRadarTool.getPanel();
        ceemRadarTool.setParent(this);
        handler = ceemRadarTool.getHandlerForJFrame(this);
        this.setTitle(title);
        this.setLocationByPlatform(true);
        initToolFrameSettings();
    }

    protected void initToolFrameSettings(){
        this.setAlwaysOnTop(true);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);

        this.pack();

        this.setMinimumSize(new Dimension(this.getWidth(), this.getHeight()));

        if(handler != null){
            handler.start();
        }
    }
}
