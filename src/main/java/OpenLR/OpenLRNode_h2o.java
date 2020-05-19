package OpenLR;

import DataBase.DatasourceConfig;
import openlr.map.*;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.jooq.sources.tables.Kanten.KANTEN;

public class OpenLRNode_h2o implements Node {

    long node_id;
    double lat;
    double lon;

    public OpenLRNode_h2o(long id, double lat, double lon) {
        this.node_id = id;
        this.lon = lon;
        this.lat = lat;
    }

    static DSLContext ctx;

    static {
        try {
            Connection con = DatasourceConfig.getConnection();
            ctx = DSL.using(con, SQLDialect.POSTGRES);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenLRNode_h2o that = (OpenLRNode_h2o) o;
        return node_id == that.node_id &&
                Double.compare(that.lat, lat) == 0 &&
                Double.compare(that.lon, lon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(node_id, lat, lon);
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

        List<Line> connectedLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                .from(KANTEN)
                .where(KANTEN.START_NODE.eq(node_id))
                .or(KANTEN.END_NODE.eq(node_id))
                .fetchInto(OpenLRLine_h2o.class);

        return connectedLines.iterator();
    }

    @Override
    public int getNumberConnectedLines() {

        return ctx.selectCount().from(KANTEN)
                .where(KANTEN.START_NODE.eq(node_id))
                .or(KANTEN.END_NODE.eq(node_id))
                .fetchOne().value1();
    }

    @Override
    public Iterator<Line> getOutgoingLines() {

        Condition andCon = (KANTEN.END_NODE.eq(node_id)).and(KANTEN.ONEWAY.eq(false));
        Condition finalCon = (KANTEN.START_NODE.eq(node_id)).or(andCon);

        List<Line> linesOut = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                .from(KANTEN)
                .where(finalCon)
                .fetchInto(OpenLRLine_h2o.class);

        return linesOut.iterator();
    }

    @Override
    public Iterator<Line> getIncomingLines() {

        Condition andCon = (KANTEN.START_NODE.eq(node_id)).and(KANTEN.ONEWAY.eq(false));
        Condition finalCon = (KANTEN.END_NODE.eq(node_id)).or(andCon);

        List<Line> linesIn = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                .from(KANTEN)
                .where(finalCon)
                .fetchInto(OpenLRLine_h2o.class);

        return linesIn.iterator();
    }

    @Override
    public long getID() {

        return node_id;
    }
}
