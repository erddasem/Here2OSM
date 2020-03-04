package OpenLR;

import DataBase.DatasourceConfig;
import DataBase.SpatialQueries;
import openlr.map.*;
import org.jooq.*;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.*;

import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;


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

    static DataSource conn = DatasourceConfig.createDataSource();
    static DSLContext ctx = DSL.using(conn, SQLDialect.POSTGRES);

    @Override
    public Node getStartNode() {

        return ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN)
                .where(KNOTEN.NODE_ID.eq(start_node))
                .fetchAny()
                .into(OpenLRNode.class);
    }

    @Override
    public Node getEndNode() {

        return ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN)
                .where(KNOTEN.NODE_ID.eq(end_node))
                .fetchAny()
                .into(OpenLRNode.class);
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

        //TODO: Returns point as Point2D.Double, depending on distance from start node. If distance exceeds line length
        // returns coordinates of end node.

        if (distanceAlong < length_meter) {
            // return coordinates of point along line. By using distance along line.
        } else {
            // return end node coordinates as Point2D.Double ( java.awt.geom)
        }

        // errechnen der Punktkoordinaten entlang der Linie basierend auf der Entfernung zum Startpunkt
        return null;
    }

    @Override
    public GeoCoordinates getGeoCoordinateAlongLine(int distanceAlong) {

        // berechnen der Geokoodinaten basierend auf der Entfernung zum Startpunkt der Linie
        //TODO: Gets point along line witch is distance (meter) away form start point of the line. Function returns Geocoord.
        // If distance exceeds length of the line end point is returnd
        if (distanceAlong < length_meter) {
            //return Geoocoord. of point alon line
        } else {
            //return end node, use select query for Knoten getting Coordinates using where clause node_id = end_node;
        }
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

            Condition andCon = (KANTEN.START_NODE.eq(end_node)).and(KANTEN.ONEWAY.eq(false));
            Condition finalCon = (KANTEN.END_NODE.eq(start_node)).or(andCon);

            List<Line> prevLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                    KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                    .from(KANTEN)
                    .where(finalCon)
                    .fetchInto(Line.class);

            return prevLines.iterator();
    }

    @Override
    public Iterator<Line> getNextLines() {

        /*Selects next lines depending on given line id. Next lines can be all lines using the end node of the given
        line as start node and all lines where oneway = true and the end node is the same.*/
        // select line_id from kanten where kanten.start_node = end_node or (kanten.start_node = end_node and oneway = false);
        Condition andCon = (KANTEN.END_NODE.eq(start_node)).and(KANTEN.ONEWAY.eq(false));
        Condition finalCon = (KANTEN.START_NODE.eq(end_node)).or(andCon);

        List<Line> nextLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                .from(KANTEN)
                .where(finalCon)
                .fetchInto(Line.class);

        return nextLines.iterator();
    }

    @Override
    public int distanceToPoint(double longitude, double latitude) {


        //TODO: Rückgabe als INT ist noch nicht möglich, Lösung finden
        Record1<Integer> dist = ctx.select(SpatialQueries.stDistance(latitude, longitude).cast(Integer.class))
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(line_id))
                .fetchOne();
        return 0;
    }

    @Override
    public int measureAlongLine(double longitude, double latitude) {

        //TODO: Abfrage schreiben, soll Distanze zum Startpunkt als Int Wert liefern

        // Formel zur Berechnung des Abstands zwischen einem gegebenen Punkt auf der Linie und dem Startpunkt der Linie
        // Verwenden ST_ClosestPoint um Punkt auf Linie zu projezieren, danach ST_Distance zur Berechnung Abstand zwischen
        // Startpunkt der Linie und projeziertem Punkt.

        // select Round(ST_Distance(n.geom::geography, ST_ClosestPoint(e.geom, 'SRID=4326;POINT(13.748489 51.058306)')::geography))
        // from kanten e, knoten n where e.line_id = 5 and n.node_id = e.start_node ;
        return 0;
    }

    @Override
    public Path2D.Double getShape() {

        // Geometrie als WKT, und dann als Path zurück geben. java.awt.geom
        // ist optional und wird daher zunächst ignoriert
        return null;
    }

    @Override
    public List<GeoCoordinates> getShapeCoordinates() {

        // optional, wird zunächst drauf verzchtet
        return null;
    }

    @Override
    public Map<Locale, List<String>> getNames() {

        // Rückgabe des Namens der Linie
        // return name, irgendwas in Verbindung mit locale
        return null;
    }


}
