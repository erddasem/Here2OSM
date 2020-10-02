package OpenLRImpl;

import Loader.OSMMapLoader;
import openlr.map.*;
import org.locationtech.jts.geom.Point;

import java.util.Iterator;
import java.util.Objects;

public class NodeImpl implements Node {

    long node_id;
    double lat;
    double lon;
    org.locationtech.jts.geom.Point pointGeometry;
    OSMMapLoader loader;
    // Geotools Point Geometry

    public NodeImpl(long node_id, double lat, double lon) {
        this.node_id = node_id;
        this.lat = lat;
        this.lon = lon;
        // this.PointGeometry = Methode aufrufen
    }

    public void setPointGeometry(Point pointGeometry) {
        this.pointGeometry = pointGeometry;
    }

    // Methode: ruft GeoemtyBuilder auf, builder.createPoint(lat, lon); return: Point
    public void setNodeOSMMapLoader(OSMMapLoader loader) {
        this.loader = loader;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public Point getPointGeometry() {
        return pointGeometry;
    }

    @Override
    public double getLatitudeDeg() {
        return lat;
    }

    @Override
    public double getLongitudeDeg() {
        return lon;
    }

    @Override
    public GeoCoordinates getGeoCoordinates() {
        GeoCoordinates coordinates = null;
        try {
            coordinates = new GeoCoordinatesImpl(lon, lat);
        } catch (InvalidMapDataException e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    @Override
    public Iterator<Line> getConnectedLines() {
        // add Function in Loader
        return null;
    }

    @Override
    public int getNumberConnectedLines() {
        // add Function in Loader
        return 0;
    }

    @Override
    public Iterator<Line> getOutgoingLines() {
        // add Function in Loader
        return null;
    }

    @Override
    public Iterator<Line> getIncomingLines() {
        // add function in Loader
        return null;
    }

    @Override
    public long getID() {
        return node_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeImpl node = (NodeImpl) o;
        return node_id == node.node_id &&
                Double.compare(node.lat, lat) == 0 &&
                Double.compare(node.lon, lon) == 0 &&
                pointGeometry.equals(node.pointGeometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node_id, lat, lon, pointGeometry);
    }
}
