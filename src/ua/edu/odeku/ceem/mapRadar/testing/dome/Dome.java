/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.testing.dome;

import com.jogamp.common.nio.Buffers;
import gov.nasa.worldwind.Exportable;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.render.AbstractShape;
import gov.nasa.worldwind.render.BasicWWTexture;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.WWTexture;
import gov.nasa.worldwind.terrain.Terrain;
import gov.nasa.worldwind.util.Logging;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    public Dome(){
        this.nThetaIntervals = this.nThetaPoints - 1;
        this.nPhiIntervals = this.nPhiPoints - 1;
    }

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
        ShapeData shapeData = this.getCurrent();

        if (shapeData.indices == null)
            this.makeIndices();

        if (shapeData.normals == null)
            this.makeNormals();

        return true;
    }

    private void makeNormals() {
        ShapeData shapeData = this.getCurrent();
        Vec4 vecA, vecB, vecC, vecD, vecX1, vecX2;

        shapeData.normals = Buffers.newDirectFloatBuffer(shapeData.vertices.limit());
        for(int j = 0; j <= this.nThetaIntervals; j++){

            for (int i = 0; i <= this.nPhiIntervals; i++){

                Vec4 vec0 = this.getVec(shapeData, i, j);

                if (i == 0 && j == 0) {

                    vecA = this.getVec(shapeData, i, j + 1).subtract3(vec0);
                    vecB = this.getVec(shapeData, i + 1, j).subtract3(vec0);
                    this.putVec(i, j, vecA.cross3(vecB).normalize3(), shapeData.normals);

                } else if ( i == this.nPhiIntervals && j == 0){

                    vecA = this.getVec(shapeData, i - 1, j).subtract3(vec0);
                    vecB = this.getVec(shapeData, i, j + 1).subtract3(vec0);
                    this.putVec(i, j, vecA.cross3(vecB).normalize3(), shapeData.normals);

                } else if (i == 0 && j == this.nThetaIntervals){

                    vecA = this.getVec(shapeData, i + 1, j).subtract3(vec0);
                    vecB = this.getVec(shapeData, i, j + 1).subtract3(vec0);
                    this.putVec(i, j, vecA.cross3(vecB).normalize3(), shapeData.normals);

                } else if (i == this.nPhiIntervals && j == nThetaIntervals){

                    vecA = this.getVec(shapeData, i, j - 1).subtract3(vec0);
                    vecB = this.getVec(shapeData, i - 1, j).subtract3(vec0);
                    this.putVec(i, j, vecA.cross3(vecB).normalize3(), shapeData.normals);

                } else if( i == 0) {

                    vecA = this.getVec(shapeData, i, j - 1).subtract3(vec0);
                    vecB = this.getVec(shapeData, i + 1, j).subtract3(vec0);
                    vecC = this.getVec(shapeData, i, j - 1).subtract3(vec0);

                    vecX1 = vecA.cross3(vecB).multiply3(0.5);
                    vecX2 = vecB.cross3(vecC).multiply3(0.5);

                    this.putVec(i, j, vecX1.add3(vecX2).normalize3(), shapeData.normals);
                } else if (i == this.nPhiIntervals ){
                    vecA = this.getVec(shapeData, i, j - 1).subtract3(vec0);
                    vecB = this.getVec(shapeData, i - 1, j).subtract3(vec0);
                    vecC = this.getVec(shapeData, i, j + 1).subtract3(vec0);

                    vecX1 = vecA.cross3(vecB).multiply3(0.5);
                    vecX2 = vecB.cross3(vecC).multiply3(0.5);

                    this.putVec(i, j, vecX1.add3(vecX2).normalize3(), shapeData.normals);
                } else if (j == 0) {
                    vecA = this.getVec(shapeData, i - 1, j).subtract3(vec0);
                    vecB = this.getVec(shapeData, i, j + 1).subtract3(vec0);
                    vecC = this.getVec(shapeData, i + 1, j).subtract3(vec0);

                    vecX1 = vecA.cross3(vecB).multiply3(0.5);
                    vecX2 = vecB.cross3(vecC).multiply3(0.5);

                    this.putVec(i, j, vecX1.add3(vecX2).normalize3(), shapeData.normals);
                } else if (j == this.nThetaIntervals){
                    vecA = this.getVec(shapeData, i + 1, j).subtract3(vec0);
                    vecB = this.getVec(shapeData, i, j - 1).subtract3(vec0);
                    vecC = this.getVec(shapeData, i - 1, j).subtract3(vec0);

                    vecX1 = vecA.cross3(vecB).multiply3(0.5);
                    vecX2 = vecB.cross3(vecC).multiply3(0.5);

                    this.putVec(i, j, vecX1.add3(vecX2).normalize3(), shapeData.normals);
                } else {
                    vecA = this.getVec(shapeData, i, j - 1).subtract3(vec0);
                    vecB = this.getVec(shapeData, i - 1, j).subtract3(vec0);
                    vecC = this.getVec(shapeData, i, j + 1).subtract3(vec0);
                    vecD = this.getVec(shapeData, i + 1, j).subtract3(vec0);

                    vecX1 = vecA.cross3(vecB).multiply3(0.25);
                    vecX2 = vecB.cross3(vecC).multiply3(0.25);
                    Vec4 vecX3 = vecC.cross3(vecD).multiply3(0.25);
                    Vec4 vecX4 = vecD.cross3(vecA).multiply3(0.25);

                    this.putVec(i, j, vecX1.add3(vecX2).add3(vecX3).add3(vecX4).normalize3(), shapeData.normals);
                }
            }
        }
    }

    private void putVec(int i, int j, Vec4 vec, FloatBuffer buffer) {
        int k = 3 * (j * this.nPhiIntervals + i);
        buffer.put(k, (float) vec.getX());
        buffer.put(k + 1, (float) vec.getY());
        buffer.put(k + 1, (float) vec.getZ());
    }

    private Vec4 getVec(ShapeData shapeData, int i, int j) {
        int k = 3 * (j * this.nPhiIntervals + i);
        float x = shapeData.vertices.get(k);
        float y = shapeData.vertices.get(k + 1);
        float z = shapeData.vertices.get(k + 2);
        return new Vec4(x, y, z);
    }

    private void makeIndices() {
        ShapeData shapeData = this.getCurrent();

        shapeData.indices = new IntBuffer[this.nThetaIntervals];

        for (int j = 0; j < this.nThetaIntervals; j++){
            shapeData.indices[j] = Buffers.newDirectIntBuffer(2 * this.nPhiIntervals + 2);

            for( int i =0; i <= this.nPhiIntervals; i++){
                int k1 = i + j * (this.nPhiIntervals + 1);
                int k2 = k1 + this.nPhiIntervals + 1;
                shapeData.indices[j].put(k1).put(k2);
            }
        }
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

                    double r = this.getR();

                    double s = r;

                    shapeData.texCoords.put((float) s).put(0);

                    double rScaled = (r + this.gainOffset) * this.gainScale;

                    double z = rScaled * Math.sin(t) * Math.cos(p);
                    double x = rScaled * Math.sin(t) * Math.sin(p);
                    double y = rScaled * Math.cos(t);

                    xMax = Math.max(Math.abs(x), xMax);
                    yMax = Math.max(Math.abs(y), yMax);
                    zMax = Math.max(Math.abs(z), zMax);

                    shapeData.vertices.put( (float) x).put( (float) y ).put( (float) z);
                }
            }
            shapeData.setExtent( new Sphere(rp, Math.sqrt(xMax * xMax + yMax * yMax + zMax * zMax)));
        }
    }

    private ShapeData getCurrent() {
        return (ShapeData) this.getCurrentData();
    }

    @Override
    protected boolean isOrderedRenderableValid(DrawContext dc) {
        ShapeData shapeData = this.getCurrent();

        return shapeData.vertices != null && shapeData.indices != null && shapeData.normals != null;
    }

    @Override
    protected void doDrawOutline(DrawContext dc) {
        this.drawModel(dc, DISPLAY_MODE_LINE, !this.isHighlighted());
    }

    @Override
    protected void doDrawInterior(DrawContext dc) {
        this.drawModel(dc, DISPLAY_MODE_FILL, true);
    }

    private void drawModel(DrawContext dc, int displayModeLine, boolean showTexture) {
        ShapeData shapeData = this.getCurrent();
        GL2 gl = dc.getGL().getGL2();
        if (this.texture == null)
            this.makeTexture();

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, displayModeLine);

        if(!dc.isPickingMode() && showTexture){
            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
            gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, shapeData.texCoords.rewind());
            this.texture.bind(dc);
        }

        gl.glPushMatrix();

        gl.glRotated(this.getPosition().getLongitude().degrees, 0, 1, 0);

        gl.glRotated(Math.abs(90 - this.getPosition().getLatitude().degrees), 1, 0, 0);

        // Apply the azimuth.
        if (this.getAzimuth() != null)
            gl.glRotated(-this.getAzimuth().degrees, 0, 1, 0);

        // Apply the elevation angle.
        if (this.getElevationAngle() != null)
            gl.glRotated(this.getElevationAngle().degrees, 1, 0, 0);

        gl.glVertexPointer(3, GL.GL_FLOAT, 0, shapeData.vertices.rewind());

        if (!dc.isPickingMode() && this.mustApplyLighting(dc, null))
            gl.glNormalPointer(GL.GL_FLOAT, 0, shapeData.normals.rewind());

        for (IntBuffer iBuffer : shapeData.indices)
        {
            gl.glDrawElements(GL.GL_TRIANGLE_STRIP, iBuffer.limit(), GL.GL_UNSIGNED_INT, iBuffer.rewind());
        }

        gl.glPopMatrix();

        if (!dc.isPickingMode())
            gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

    }

    private void makeTexture() {
        BufferedImage image = new BufferedImage(240, 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        for (int i = 0; i < image.getWidth(); i++)
        {
            g.setPaint(Color.getHSBColor((float) ((image.getWidth() - i) / 360d), 1f, 1f));
            g.fillRect(i, 0, 1, 2);
        }

        this.texture = new BasicWWTexture(image, true);
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
