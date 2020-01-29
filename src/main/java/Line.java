import openlr.map.*;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Line implements openlr.map.Line {
    int line_id;
    int start_node;
    int end_node;
    int frc;
    int fow;
    int line_length;

    public Line(int line_id, int start_node, int end_node, int frc, int fow, int line_length) {
        this.line_id = line_id;
        this.start_node = start_node;
        this.end_node = end_node;
        this.frc = frc;
        this.fow = fow;
        this.line_length = line_length;
    }

    @Override
    public NodeOLR getStartNode() {
        return null;
    }

    @Override
    public NodeOLR getEndNode() {
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
    public Iterator<openlr.map.Line> getPrevLines() {
        return null;
    }

    @Override
    public Iterator<openlr.map.Line> getNextLines() {
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
