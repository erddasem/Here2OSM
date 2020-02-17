package OpenLR;

import openlr.map.*;
import org.jooq.*;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.Iterator;

import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;

public class OpenLRNode implements Node {

    // Attribtute Knoten
    long node_id;
    double lon;
    double lat;
    int cnt_in;
    int cnt_out;

    // Konstruktor
    public OpenLRNode(long id, double lon, double lat, int cnt_in, int cnt_out) {
        this.node_id = id;
        this.lon = lon;
        this.lat = lat;
        this.cnt_in = cnt_in;
        this.cnt_out = cnt_out;
    }

    static DataSource conn = DatasourceConfig.createDataSource();
    static DSLContext ctx = DSL.using(conn, SQLDialect.POSTGRES);

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
            coordinates = new GeoCoordinatesImpl(lat, lon);
        } catch (InvalidMapDataException e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    @Override
    public Iterator<Line> getConnectedLines() {

        Result<Record1<Long>> connectedLines = ctx.select(KANTEN.LINE_ID).from(KANTEN)
                .where(KANTEN.START_NODE.eq(node_id))
                .or(KANTEN.END_NODE.eq(node_id))
                .fetch();

        // select line_id from kanten where start_node = node_id or end_node = node_id;
        return null;
    }

    @Override
    public int getNumberConnectedLines() {

        // select count(*) from kanten where start_node = node_id or end_node = node_id;
        int numberConnectedLines  = ctx.selectCount().from(KANTEN)
                .where(KANTEN.START_NODE.eq(node_id))
                .or(KANTEN.END_NODE.eq(node_id))
                .fetchOne().value1();
        return numberConnectedLines;
    }

    @Override
    public Iterator<Line> getOutgoingLines() {

        Condition a = KANTEN.START_NODE.eq(node_id);
        Condition b = KANTEN.END_NODE.eq(node_id);
        Condition c = KANTEN.ONEWAY.eq(false);

        Condition andCon = b.and(c);             // These OR-connected conditions form a new condition, wrapped in parentheses
        Condition finalCon = a.or(andCon);

        Result<Record1<Long>> outgoingLines = ctx.select(KANTEN.LINE_ID).from(KANTEN)
                .where(finalCon)
                .fetch();

        // select line_id from kanten where start_node = node_id or (end_node = node_id and oneway = false);
        // array mit jeweiligen line_ids, dann Objekte erstellen und als Iterator zurückgeben
        return null;
    }

    @Override
    public Iterator<Line> getIncomingLines() {

        Condition a = KANTEN.START_NODE.eq(node_id);
        Condition b = KANTEN.END_NODE.eq(node_id);
        Condition c = KANTEN.ONEWAY.eq(false);

        Condition andCon = a.and(c);             // These OR-connected conditions form a new condition, wrapped in parentheses
        Condition finalCon = b.or(andCon);

        Result<Record1<Long>> ingoingLines = ctx.select(KANTEN.LINE_ID).from(KANTEN)
                .where(finalCon)
                .fetch();

        // select line_id from kanten where end_node = 28 or (start_node = 28 and oneway = false);
        // ausgabe der line_id's als Aaray, Linien Objekte erstellen und als Iterator zurück geben.
        // for each line_id in array {getLine(id) add Line to lineIterator} return lineIterator
        return null;
    }

    @Override
    public long getID() {

        return node_id;
    }
}
