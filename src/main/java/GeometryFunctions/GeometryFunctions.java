package GeometryFunctions;

import OpenLRImpl.LineImpl;
import OpenLRImpl.NodeImpl;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;

import java.util.ArrayList;
import java.util.List;

public class GeometryFunctions {

    private static Geometry createSearchArea(double latitude, double longitude, double distance) {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(32);
        shapeFactory.setCentre(new Coordinate(latitude, latitude));
        shapeFactory.setSize(distance * 2);
        return shapeFactory.createCircle();
    }

    public static List<NodeImpl> getNodesWithinDistance(List<NodeImpl> nodes, double latitude, double longitude, double distance) {
        Polygon area = createSearchArea(latitude, longitude, distance).getFactory().createPolygon();
        List<NodeImpl> nodesWithinDistance = new ArrayList<>();
        nodes.forEach(n -> {
            if (n.getPointGeometry().within(area)) {
                nodesWithinDistance.add(n);
            }
        });

        return nodesWithinDistance;
    }


    public static List<LineImpl> getLinesWithinDistance(List<LineImpl> lines, double latitude, double longitude, double distance) {
        Polygon area = createSearchArea(latitude, longitude, distance).getFactory().createPolygon();
        List<LineImpl> linesWithinDistance = new ArrayList<>();
        lines.forEach(l -> {
            if (l.getLineGeometry().within(area)) {
                linesWithinDistance.add(l);
            }
        });

        return linesWithinDistance;
    }

    public static double distToDeg(double lat, double dist) {
        return dist / (111.32 * 1000 * Math.cos(lat * (Math.PI / 180)));
    }

    public static int distToMeter(double distDeg) {
        return (int) Math.round(distDeg * (Math.PI/180) * 6378137);
    }

}
