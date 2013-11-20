package ua.edu.odeku.ceem.mapRadar.tools.importGeoName;

import org.hibernate.Session;
import ua.edu.odeku.ceem.mapRadar.db.DB;
import ua.edu.odeku.ceem.mapRadar.db.models.GeoName;
import ua.edu.odeku.ceem.mapRadar.exceptions.db.models.GeoNameException;
import ua.edu.odeku.ceem.mapRadar.utils.gui.UserMessage;
import ua.edu.odeku.ceem.mapRadar.utils.thread.Handler;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Файл поток, занимает импортом данных из файла в базу данных
 * User: Aleo skype: aleo72
 * Date: 10.11.13
 * Time: 18:16
 */
public class GeoNameImporter extends Thread {

    public boolean stop = false;
    private File file;
    private JProgressBar progressBar;
    private String[] lines;
    private Handler handler;

    public GeoNameImporter(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public GeoNameImporter(JProgressBar progressBar, Handler handler) {
        this.progressBar = progressBar;
        this.handler = handler;
    }

    public void setFileInput(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        try{
            if (!stop && file != null) {
                progressBar.setValue(0);
                lines = readFile();
                Session session = DB.getSession();

                try {
                    if (lines != null && lines.length > 0) {
                        progressBar.setMaximum(lines.length);
                        for (String line : lines) {
                            if(stop)
                                break;
                            progressBar.setValue(progressBar.getValue() + 1);
                            GeoName geoName = GeoName.createGeoName(line);
                            try {
                                session.save(geoName);
                            }
                            catch (Exception e){
                                System.err.println(e.getMessage());
                            }
                        }

                    }
                } catch (GeoNameException e) {
                    e.printStackTrace();
                    UserMessage.error(null, e.getMessage());
                } finally {
                    DB.closeSession(session);
                }

            }
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            clear();
            if(handler != null){
                handler.start();
            }
        }
    }

    private void clear() {
        stop = false;
        progressBar.setValue(0);
        file = null;
        lines = null;
    }

    private String[] readFile() {
        String[] lines = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            LinkedList<String> list = new LinkedList<String>();
            while ((line = br.readLine()) != null && !stop) {
                list.add(line);
            }
            lines = list.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
            UserMessage.error(null, e.getMessage());
        }

        if (stop)
            lines = null;

        return lines;
    }

}
