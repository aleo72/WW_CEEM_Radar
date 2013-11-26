/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.testing.dome;

import com.jogamp.common.nio.Buffers;
import gov.nasa.worldwind.Exportable;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.render.AbstractShape;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.WWTexture;
import gov.nasa.worldwind.terrain.Terrain;
import gov.nasa.worldwind.util.Logging;

import javax.media.opengl.GL2;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

/**
 * User: Aleo Bakalov
 * Date: 26.11.13
 * Time: 16:43
 */
public class Dome extends AbstractShape {

    public static final int DISPLAY_MODE_FILL = GL2.GL_FILL;
    public static final int DISPLAY_MODE_LINE = GL2.GL_LINE;
    public static final int DISPLAY_MODE_POINT = GL2.GL_POINT;

    protected int nThetaIntervals;
    protected int nPhiIntervals;
    protected WWTexture texture;

    protected Position position = Position.ZERO;

    protected Angle azimuth;
    protected Angle elevationAngle;
    protected double gainOffset = 0;
    protected double gainScale = 1;
    protected int nThetaPoints = 61;
    protected int nPhiPoints = 121;

    @Override
    public List<Intersection> intersect(Line line, Terrain terrain) throws InterruptedException {
        return null;
    }

    @Override
    protected void initialize() {   }

    @Override
    protected boolean mustApplyTexture(DrawContext dc) {
        return true;
    }

    @Override
    protected boolean doMakeOrderedRenderable(DrawContext dc) {
        if(!this.intersectsFrustum(dc)){
            return false;
        }

        this.makeVertices(dc);

    }

    private void makeVertices(DrawContext dc) {
        ShapeData shapeData = this.getCurrent();

        Vec4 rp = this.computePoint(dc.getTerrain(), this.getPosition());
        if(shapeData.getReferencePoint() == null || !shapeData.getReferencePoint().equals(rp)){
            shapeData.setReferencePoint(rp);

            int nVertices = (this.nThetaIntervals + 1) * (this.nPhiIntervals + 1);

            shapeData.vertices = Buffers.newDirectFloatBuffer(3 * nVertices);
            shapeData.texCoords = Buffers.newDirectFloatBuffer(3 * nVertices);

            double rScale = 1.0 / (this.getR());

            double xMax = -Double.MAX_VALUE;
            double yMax = -Double.MAX_VALUE;
            double zMax = -Double.MAX_VALUE;

            double dTheta = 180 / this.nThetaIntervals;
            double dPhi = 360 / this.nPhiIntervals;

            for (int i = 0; i <= this.nThetaIntervals; i++){
                for (int j = 0; j <= this.nPhiIntervals; j++){
                    double theta = i * dTheta;
                    double phi = j * dPhi;
                    double t = theta * Math.PI / 180;
                    double p = phi * Math.PI / 180;

                    Double r = this.getR();
                }
            }
        }
    }

    private ShapeData getCurrent() {
        return (ShapeData) this.getCurrentData();
    }

    @Override
    protected boolean isOrderedRenderableValid(DrawContext dc) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void doDrawOutline(DrawContext dc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void doDrawInterior(DrawContext dc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void fillVBO(DrawContext dc) {    }

    @Override
    protected void doExportAsKML(XMLStreamWriter xmlWriter) throws IOException, XMLStreamException {
        throw new UnsupportedOperationException("KML output not supported for Dome");
    }

    @Override
    protected AbstractShapeData createCacheEntry(DrawContext dc) {
        return new ShapeData(dc, this);
    }

    @Override
    public Sector getSector() {
        return this.sector;
    }

    @Override
    public Position getReferencePosition() {
        return this.getPosition();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position pos){
        if (pos == null){
            String message = Logging.getMessage("nullValue.PositionIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        this.position = pos;
        this.reset();
    }

    @Override
    public void moveTo(Position position) { }

    @Override
    public String isExportFormatSupported(String mimeType) {
        return Exportable.FORMAT_NOT_SUPPORTED;
    }

    public Angle getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(Angle azimuth) {
        this.azimuth = azimuth;
    }

    public Angle getElevationAngle() {
        return elevationAngle;
    }

    public void setElevationAngle(Angle elevationAngle) {
        this.elevationAngle = elevationAngle;
    }

    public double getGainOffset() {
        return gainOffset;
    }

    public void setGainOffset(double gainOffset) {
        this.gainOffset = gainOffset;
    }

    public double getGainScale() {
        return gainScale;
    }

    public void setGainScale(double gainScale) {
        this.gainScale = gainScale;
    }

    public int getnThetaPoints() {
        return nThetaPoints;
    }

    public void setnThetaPoints(int nThetaPoints) {
        this.nThetaPoints = nThetaPoints;
    }

    public int getnPhiPoints() {
        return nPhiPoints;
    }

    public void setnPhiPoints(int nPhiPoints) {
        this.nPhiPoints = nPhiPoints;
    }

    public WWTexture getTexture() {
        return texture;
    }

    public void setTexture(WWTexture texture) {
        this.texture = texture;
    }

    public int getnPhiIntervals() {
        return nPhiIntervals;
    }

    public void setnPhiIntervals(int nPhiIntervals) {
        this.nPhiIntervals = nPhiIntervals;
    }

    public int getnThetaIntervals() {
        return nThetaIntervals;
    }

    public void setnThetaIntervals(int nThetaIntervals) {
        this.nThetaIntervals = nThetaIntervals;
    }

    public double getR() {
        return 5000;
    }

    /**
     * This class holds globe-specific data for this shape. It's managed via the shape-data cache in {@link
     * gov.nasa.worldwind.render.AbstractShape.AbstractShapeData}.
     */
    protected static class ShapeData extends AbstractShapeData
    {
        protected FloatBuffer vertices;
        protected FloatBuffer texCoords;
        protected IntBuffer[] indices;
        protected FloatBuffer normals;

        /**
         * Construct a cache entry using the boundaries of this shape.
         *
         * @param dc    the current draw context.
         * @param shape this shape.
         */
        public ShapeData(DrawContext dc, Dome shape)
        {
            super(dc, shape.minExpiryTime, shape.maxExpiryTime);
        }
    }
}
