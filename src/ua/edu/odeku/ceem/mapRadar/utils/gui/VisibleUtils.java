package ua.edu.odeku.ceem.mapRadar.utils.gui;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;

import javax.swing.*;
import java.awt.*;

/**
 * Класс с набором методов утилит
 * User: Aleo
 * Date: 03.11.13
 * Time: 15:33
 */
public final class VisibleUtils {

    /**
     * Метод встраивает слой следующим после слоя с компасом в WorldWindow
     * @param wwd WorldWindow object
     * @param сompass CompassLayer object
     */
    public static void insertBeforeCompass(WorldWindow wwd, Layer сompass)
    {
        // Insert the layer into the layer list just before the compass.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof CompassLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition, сompass);
    }

    public static void insertBeforePlaceNames(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just before the placeNames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof PlaceNameLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition, layer);
    }

    public static void insertAfterPlaceNames(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just after the placeNames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof PlaceNameLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition + 1, layer);
    }

    public static void insertBeforeLayerName(WorldWindow wwd, Layer layer, String targetName)
    {
        // Insert the layer into the layer list just before the target layer.
        int targetPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l.getName().contains(targetName))
            {
                targetPosition = layers.indexOf(l);
                break;
            }
        }
        layers.add(targetPosition, layer);
    }


    public static void insertBeforePlacenames(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just before the placenames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof PlaceNameLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition, layer);
    }

    public static void insertAfterPlacenames(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just after the placenames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof PlaceNameLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition + 1, layer);
    }

    /**
     * Упаковывает окно
     * @param frame окно которое необходимо упокавать
     */
    public static void packFrame(JFrame frame){
        frame.pack();
    }

    /**
     * Установит Минимальное и максимальное значение для окна
     * Приэтом окно упаковано
     * @param frame окно которое подлежит указанию границ
     */
    public static void setMinMaxSizeFrame(JFrame frame){
        setMinSizeFrame(frame);
        setMaxSizeFrame(frame);
        frame.setResizable(false);
    }

    /**
     * Установит Минимальное значение для окна
     * Приэтом окно упаковано
     * @param frame окно которое подлежит указанию границ
     */
    public static void setMinSizeFrame(JFrame frame){
        Dimension dimension = frame.getSize();
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setSize(dimension);
    }

    /**
     * Установит Максимальное значение для окна
     * Приэтом окно будет упаковано
     * @param frame окно которое подлежит указанию границ
     */
    public static void setMaxSizeFrame(JFrame frame){
        frame.pack();
        frame.setMaximumSize(frame.getSize());
    }
}
