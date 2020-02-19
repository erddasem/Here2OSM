package OpenLR;

import openlr.map.*;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.function.Consumer;


public class OpenLRLine implements Line{

    long line_id;
    long start_node;
    long end_node;
    int frc;
    int fow;
    int length_meter;
    String name;
    boolean oneway;

    public OpenLRLine(long line_id, long start_node, long end_node, int frc, int fow, int length_meter, String name, boolean oneway) {
        this.line_id = line_id;
        this.start_node = start_node;
        this.end_node = end_node;
        this.frc = frc;
        this.fow = fow;
        this.length_meter = length_meter;
        this.name = name;
        this.oneway = oneway;
    }

    private Line[] lineList;
    private int currentSize;

    public OpenLRLine(Line[] newArray) {
        this.lineList = newArray;
        this.currentSize = lineList.length;

    }

    @Override
    public Node getStartNode() {

        return SQLCommands.getKnoten(start_node);
    }

    @Override
    public Node getEndNode() {

        return SQLCommands.getKnoten(end_node);
    }

    @Override
    public FormOfWay getFOW() {

        return FormOfWay.getFOWs().get(fow); // weil Enum
    }

    @Override
    public FunctionalRoadClass getFRC() {

        return FunctionalRoadClass.getFRCs().get(frc); // weil Enum
    }

    @Override
    public Point2D.Double getPointAlongLine(int distanceAlong) {

        // errechnen der Punktkoordinaten entlang der Linie basierend auf der Entfernung zum Startpunkt
        return null;
    }

    @Override
    public GeoCoordinates getGeoCoordinateAlongLine(int distanceAlong) {

        // berechnen der Geokoodinaten basierend auf der Entfernung zum Startpunkt der Linie
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

        // slect line_id from kanten where kanten.end_node = start_node or (kanten.end_node = start_node and oneway = false);
        // create iterator
        return null;
    }

    @Override
    public Iterator<Line> getNextLines() {

        // select line_id from kanten where kanten.start_node = end_node or (kanten.start_node = end_node and oneway = false);
        return null;
    }

    @Override
    public int distanceToPoint(double longitude, double latitude) {

        // Formel zur Berechnung der Entfernung der Linie zum Punkt
        return 0;
    }

    @Override
    public int measureAlongLine(double longitude, double latitude) {

        // Formel zur Berechnung des Abstands zwischen einem gegebenen Punkt auf der Linie und dem Startpunkt der Linie
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

        // Rückgabe des Namens der Linie
        // return name, irgendwas in Verbindung mit locale
        return null;
    }


}
