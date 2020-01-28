import openlr.map.GeoCoordinates;
import openlr.map.Line;

import java.util.Iterator;

public class OpenLRNode implements openlr.map.Node {
    @Override
    public double getLatitudeDeg() {
        return 0;
    }

    @Override
    public double getLongitudeDeg() {
        return 0;
    }

    @Override
    public GeoCoordinates getGeoCoordinates() {
        return null;
    }

    @Override
    public Iterator<Line> getConnectedLines() {
        return null;
    }

    @Override
    public int getNumberConnectedLines() {
        return 0;
    }

    @Override
    public Iterator<Line> getOutgoingLines() {
        return null;
    }

    @Override
    public Iterator<Line> getIncomingLines() {
        return null;
    }

    @Override
    public long getID() {
        return 0;
    }
}
