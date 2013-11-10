package ua.edu.odeku.ceem.mapRadar.tools;

import javax.swing.*;
import java.awt.*;

/**
 * User: Aleo skype: aleo72
 * Date: 10.11.13
 * Time: 15:57
 */
public class ToolFrame extends JFrame {

    protected JPanel panel;

    public ToolFrame(CeemRadarTool ceemRadarTool, String title){
        this.panel = ceemRadarTool.getPanel();
        ceemRadarTool.setParent(this);
        this.setTitle(title);

        initToolFrameSettings();
    }

    protected void initToolFrameSettings(){
        this.setAlwaysOnTop(true);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);

        this.pack();

        this.setMinimumSize(new Dimension(this.getWidth(), this.getHeight()));
    }
}
