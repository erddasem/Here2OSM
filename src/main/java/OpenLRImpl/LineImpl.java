package OpenLRImpl;

import GeometryFunctions.GeometryFunctions;
import openlr.map.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.distance.DistanceOp;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.*;


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
    MapDatabaseImpl mdb;

    GeometryFactory geometryFactory = new GeometryFactory();

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

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public void setLineGeometry(LineString lineGeometry) {
        this.lineGeometry = lineGeometry;
    }

    public void setMdb(MapDatabaseImpl mdb) {
        this.mdb = mdb; }

    public long getStartNodeID() { return startNode_id; }

    public long getEndNodeID() { return endNode_id; }

    public LineString getLineGeometry() { return lineGeometry; }

    public boolean isReversedGeom() { return reversedGeom; }

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

        //TODO: Geometry Function
        return null;
    }

    @Override
    public GeoCoordinates getGeoCoordinateAlongLine(int distanceAlong) {

        //TODO: Geometry Function
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

        List<Line> previousLines = new ArrayList<>();
        Iterator<Line> allLines = mdb.getAllLines();
        while(allLines.hasNext()) {
            Line line = allLines.next();
            if(line.getEndNode().getID() == startNode_id)
                previousLines.add(line);
        }
        return previousLines.iterator();
    }

    @Override
    public Iterator<Line> getNextLines() {

        List<Line> nextLines = new ArrayList<>();
        Iterator<Line> allLines = mdb.getAllLines();
        while(allLines.hasNext()) {
            Line line = allLines.next();
            if(line.getStartNode().getID() == endNode_id)
                nextLines.add(line);
        }
        return nextLines.iterator();
    }

    /*
    The curvature of the earth is ignored.
    At a distance of 100m, the influence of the curvature is about 0.8mm. Since integers are returned and the standard
    search radius is set to 100m, the curvature is ignored. If the parameter for the search radius
    (OpenLR-Decoder-Properties.xml) is set to more than 100m, the method must be adapted.
     */

    @Override
    public int distanceToPoint(double longitude, double latitude) {

        Point p = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        double distanceDeg  = DistanceOp.distance(lineGeometry, p);

        return GeometryFunctions.distToMeter(distanceDeg);
    }

    @Override
    public int measureAlongLine(double longitude, double latitude) {

        double length = 0;
        // create point to check for intersection with line
        Point p = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        Point projectionPoint = geometryFactory.createPoint(DistanceOp.nearestPoints(lineGeometry, p)[0]);
        Coordinate[] theLineCoordinates = lineGeometry.getCoordinates();
        // iterate over linestring and create sub-lines for each coordinate pair
        for(int i = 1; i < theLineCoordinates.length; i++){
            LineString currentLine = geometryFactory.createLineString(new Coordinate[]{theLineCoordinates[i-1], theLineCoordinates[i]});
            // check if coordinateOnTheLine is on currentLine
            if(currentLine.intersects(projectionPoint)){
                // create new currentLine with coordinateOnTheLine as endpoint and calculate length
                currentLine = geometryFactory.createLineString(new Coordinate[]{theLineCoordinates[i-1], projectionPoint.getCoordinate()});
                length += currentLine.getLength();
                // return result length
                int distanceMeter = GeometryFunctions.distToMeter(length);
                return distanceMeter;
            }
            length += currentLine.getLength();
        }
        // coordinate was not on the line -> return length of complete linestring
        return length_meter;
    }

    @Override
    public Path2D.Double getShape() {

        //Optional method
        return null;
    }

    @Override
    public List<GeoCoordinates> getShapeCoordinates() {

        //Optional method
        return null;
    }

    @Override
    public Map<Locale, List<String>> getNames() {

        //Optional method
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineImpl line = (LineImpl) o;
        return line_id == line.line_id &&
                startNode_id == line.startNode_id &&
                endNode_id == line.endNode_id &&
                frc == line.frc &&
                fow == line.fow &&
                length_meter == line.length_meter &&
                reversedGeom == line.reversedGeom &&
                startNode.equals(line.startNode) &&
                endNode.equals(line.endNode) &&
                Objects.equals(name, line.name) &&
                lineGeometry.equals(line.lineGeometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line_id, startNode_id, endNode_id, startNode, endNode, frc, fow, length_meter, name, reversedGeom, lineGeometry);
    }
}
