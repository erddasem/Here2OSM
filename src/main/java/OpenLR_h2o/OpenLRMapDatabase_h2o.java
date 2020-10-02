package OpenLR_h2o;

import DataBase.DatasourceConfig;
import DataBase.SpatialQueries;
import openlr.map.Line;
import openlr.map.Node;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.awt.geom.Rectangle2D;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;
import static org.jooq.sources.tables.Metadata.METADATA;

/**
 * Implementation of the OpenLR MapDatabase interface.
 */

public class OpenLRMapDatabase_h2o implements openlr.map.MapDatabase, AutoCloseable {

    private final DSLContext ctx;
    private final Connection con;

    public OpenLRMapDatabase_h2o() throws SQLException {
        con = DatasourceConfig.getConnection();
        ctx = DSL.using(con, SQLDialect.POSTGRES);
    }


    @Override
    public boolean hasTurnRestrictions() {
        return false;
    }

    @Override
    public Line getLine(long id) {


        return ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME)
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(id))
                .fetchAny().into(OpenLRLine_h2o.class);
    }

    @Override
    public Node getNode(long id) {

        return ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN)
                .where(KNOTEN.NODE_ID.eq(id))
                .fetchAny()
                .into(OpenLRNode_h2o.class);
    }

    @Override
    public Iterator<Node> findNodesCloseByCoordinate(double longitude, double latitude, int distance) {

        List<Node> nodesCloseBy = ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN)
                .where(SpatialQueries.stDWithin(longitude, latitude, distance))
                .fetchInto(OpenLRNode_h2o.class);

        return nodesCloseBy.iterator();
    }

    @Override
    public Iterator<Line> findLinesCloseByCoordinate(double longitude, double latitude, int distance) {

        List<Line> linesCloseBy = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME)
                .from(KANTEN)
                .where(SpatialQueries.stDWithin(longitude, latitude, distance))
                .fetchInto(OpenLRLine_h2o.class);

        return linesCloseBy.iterator();
    }

    @Override
    public boolean hasTurnRestrictionOnPath(List<? extends Line> path) {

        //TODO: Is optional and is not implemented
        return false;
    }

    @Override
    public Iterator<Node> getAllNodes() {

        List<Node> allNodes = ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN).fetchInto(OpenLRNode_h2o.class);

        return allNodes.iterator();
    }

    @Override
    public Iterator<Line> getAllLines() {

        List<Line> allLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME)
                .from(KANTEN).fetchInto(OpenLRLine_h2o.class);

        return allLines.iterator();
    }

    @Override
    public Rectangle2D.Double getMapBoundingBox() {

        double x = ctx.select(METADATA.LEFT_LAT).from(METADATA).fetchOne().value1();
        double y = ctx.select(METADATA.LEFT_LON).from(METADATA).fetchOne().value1();
        double width = ctx.select(METADATA.BBOX_WIDTH).from(METADATA).fetchOne().value1();
        double height = ctx.select(METADATA.BBOX_HEIGHT).from(METADATA).fetchOne().value1();

        return new Rectangle2D.Double(x, y, width, height);
    }

    @Override
    public int getNumberOfNodes() {

        return ctx.selectCount().from(KNOTEN).fetchOne().value1();
    }

    @Override
    public int getNumberOfLines() {

        return ctx.selectCount().from(KANTEN).fetchOne().value1();
    }

    @Override
    public void close() throws Exception {
        if (ctx != null) ctx.close();
        if (con != null) con.close();
    }
}
