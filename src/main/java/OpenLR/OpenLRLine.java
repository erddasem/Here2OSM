package OpenLR;

import openlr.map.*;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.function.Consumer;

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
