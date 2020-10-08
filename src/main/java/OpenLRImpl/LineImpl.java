package OpenLRImpl;

import GeometryFunctions.GeometryFunctions;
import openlr.map.*;
import org.locationtech.jts.geom.*;
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
    boolean isReversed;
    LineString lineGeometry;
    MapDatabaseImpl mdb;

    GeometryFactory geometryFactory = new GeometryFactory();

    public LineImpl(long line_id, long startNode_id, long endNode_id, int frc, int fow, int length_meter, String name, boolean isReversed) {
        this.line_id = line_id;
        this.startNode_id = startNode_id;
        this.endNode_id = endNode_id;
        this.frc = frc;
        this.fow = fow;
        this.length_meter = length_meter;
        this.name = name;
        this.isReversed = isReversed;
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

    public boolean isReversed() { return isReversed; }

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

        //TODO: Check method. Use testcase, check for distance and position along the line using QGIS and DB. (ST_ postgis queries)
        if(distanceAlong < length_meter) {
            int segmentLengthTotal = 0;
            Coordinate[] lineCoordinates = lineGeometry.getCoordinates();

            for(int i = 0; i <= lineCoordinates.length-2; i++) {
                LineSegment segment = new LineSegment(lineCoordinates[i], lineCoordinates[i+1]);
                //Get segment length
                int segmentLength = GeometryFunctions.distToMeter(segment.getLength());
                segmentLengthTotal += segmentLength;

                if(segmentLengthTotal >= distanceAlong) {
                    int newDistAlong = segmentLength-(segmentLengthTotal-distanceAlong);
                    double fraction = GeometryFunctions.getFraction(newDistAlong, segmentLength);
                    Coordinate pointAlongCoordinates = segment.pointAlong(newDistAlong/segmentLength);
                    Coordinate[] nearestPoints = DistanceOp.nearestPoints(segment.toGeometry(geometryFactory), geometryFactory.createPoint(pointAlongCoordinates));
                    Point theNearestPoint = geometryFactory.createPoint(nearestPoints[0]);
                    boolean isVertex = lineGeometry.isCoordinate(theNearestPoint.getCoordinate());
                    return new Point2D.Double(theNearestPoint.getX(), theNearestPoint.getY());
                }
            }
        }
        return new Point2D.Double(endNode.getLongitudeDeg(), endNode.getLatitudeDeg());
    }

    @Override
    public GeoCoordinates getGeoCoordinateAlongLine(int distanceAlong) {

        Point2D.Double pointAlongLine = getPointAlongLine(distanceAlong);
        GeoCoordinates coordinatesAlongLine = null;
        try {
            coordinatesAlongLine = new GeoCoordinatesImpl(pointAlongLine.getX(), pointAlongLine.getY());
        } catch (InvalidMapDataException e) {
            e.printStackTrace();
        }
        return coordinatesAlongLine;
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

        //TODO: Check distance methods if conversion degree to meter is correct. Using ST_distance postgis function and geography
        Point p = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        double distanceDeg  = DistanceOp.distance(lineGeometry, p);

        return GeometryFunctions.distToMeter(distanceDeg);
    }

    @Override
    public int measureAlongLine(double longitude, double latitude) {

        //TODO: Check method, maybe transform geometry to EPSG: 3857.

        double length = 0;
        // create point to check for intersection with line
        Point p = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        Point projectionPoint = geometryFactory.createPoint(DistanceOp.nearestPoints(lineGeometry, p)[0]);
        boolean isVertex = lineGeometry.isCoordinate(projectionPoint.getCoordinate());
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
                return GeometryFunctions.distToMeter(length);
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
                isReversed == line.isReversed &&
                startNode.equals(line.startNode) &&
                endNode.equals(line.endNode) &&
                Objects.equals(name, line.name) &&
                lineGeometry.equals(line.lineGeometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line_id, startNode_id, endNode_id, startNode, endNode, frc, fow, length_meter, name, isReversed, lineGeometry);
    }
}
