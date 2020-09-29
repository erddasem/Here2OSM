package OpenLRImpl;

import Loader.OSMMapLoader;
import openlr.map.*;
import org.locationtech.jts.geom.LineString;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class LineImpl implements Line {

    long line_id;
    long startNode_id;
    long endNode_id;
    Node startNode;
    Node endNode;
    int frc;
    int fow;
    int length_meter;
    String name;
    boolean reversedGeom;
    LineString lineGeometry;
    //String wktGeometryRepresentation; > über Loader? und nur Geometrierepräsentation
    OSMMapLoader loader;
    // LineGeometry über geoTools WKT Abfrage anlegen > über Setter wie bei Node
    MapDatabaseImpl mdb;

    public LineImpl(long line_id, long startNode_id, long endNode_id, int frc, int fow, int length_meter, String name, boolean reversedGeom) {
        this.line_id = line_id;
        this.startNode_id = startNode_id;
        this.endNode_id = endNode_id;
        this.frc = frc;
        this.fow = fow;
        this.length_meter = length_meter;
        this.name = name;
        this.reversedGeom = reversedGeom;
    }

    public void setLineOSMMapLoader(OSMMapLoader loader) {
        this.loader = loader;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public void setLineGeometry(LineString lineGeometry) {
        this.lineGeometry = lineGeometry;
    }

    public long getStartNodeID() {
        return startNode_id;
    }

    public long getEndNodeID() {
        return endNode_id;
    }

    public boolean isReversedGeom() {
        return reversedGeom;
    }

    @Override
    public Node getStartNode() {
        return startNode;
    }

    @Override
    public Node getEndNode() {
        return endNode;
    }

    @Override
    public FormOfWay getFOW() {

        return FormOfWay.getFOWs().get(fow);
    }

    @Override
    public FunctionalRoadClass getFRC() {
        return FunctionalRoadClass.getFRCs().get(frc);
    }

    @Override
    public Point2D.Double getPointAlongLine(int distanceAlong) {
        return null;
    }

    @Override
    public GeoCoordinates getGeoCoordinateAlongLine(int distanceAlong) {
        return null;
    }

    @Override
    public int getLineLength() {
        return length_meter;
    }

    @Override
    public long getID() {
        return line_id;
    }

    @Override
    public Iterator<Line> getPrevLines() {
        return null;
    }

    @Override
    public Iterator<Line> getNextLines() {
        return null;
    }

    @Override
    public int distanceToPoint(double longitude, double latitude) {
        return 0;
    }

    @Override
    public int measureAlongLine(double longitude, double latitude) {
        return 0;
    }

    @Override
    public Path2D.Double getShape() {
        return null;
    }

    @Override
    public List<GeoCoordinates> getShapeCoordinates() {
        return null;
    }

    @Override
    public Map<Locale, List<String>> getNames() {
        return null;
    }
}
