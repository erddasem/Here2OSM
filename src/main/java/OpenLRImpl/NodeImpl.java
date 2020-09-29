package OpenLRImpl;

import Loader.OSMMapLoader;
import openlr.map.*;
import org.opengis.geometry.primitive.Point;

import java.util.Iterator;

public class NodeImpl implements Node {

    long node_id;
    double lat;
    double lon;
    Point pointGeometry;
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
}
