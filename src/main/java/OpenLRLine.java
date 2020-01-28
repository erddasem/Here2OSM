import openlr.map.*;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OpenLRLine implements openlr.map.Line {
    @Override
    public Node getStartNode() {
        return null;
    }

    @Override
    public Node getEndNode() {
        return null;
    }

    @Override
    public FormOfWay getFOW() {
        return null;
    }

    @Override
    public FunctionalRoadClass getFRC() {
        return null;
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
        return 0;
    }

    @Override
    public long getID() {
        return 0;
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
