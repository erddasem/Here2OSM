package OpenLR;

import openlr.map.Line;
import openlr.map.Node;
import org.jooq.*;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import static OpenLR.SQLCommands.getKnoten;
import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;
import static org.jooq.sources.tables.Metadata.METADATA;

public class OpenLRMapDatabase implements openlr.map.MapDatabase{
    DataSource conn = DatasourceConfig.createDataSource();
    DSLContext ctx = DSL.using(conn, SQLDialect.POSTGRES);
    @Override
    public boolean hasTurnRestrictions() {
        return false;
    }

    @Override
    public Line getLine(long id) {

        return ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(id))
                .fetchAny().into(OpenLRLine.class);
    }

    @Override
    public Node getNode(long id) {

        return getKnoten(id);
    }

    @Override
    public Iterator<Node> findNodesCloseByCoordinate(double longitude, double latitude, int distance) {

        // Bedarf geometrischer Abfrage...
        return null;
    }

    @Override
    public Iterator<Line> findLinesCloseByCoordinate(double longitude, double latitude, int distance) {

        // Bedarf geometrischer Abfrage...
        return null;
    }

    @Override
    public boolean hasTurnRestrictionOnPath(List<? extends Line> path) {

        // Annahme, wenn oneway = true durfte turn restriction = true sein.
        return false;
    }

    @Override
    public Iterator<Node> getAllNodes() {
        Result<Record> getNodes = ctx.select().from(KNOTEN).fetch();

        return null;
    }

    @Override
    public Iterator<Line> getAllLines() {

        List<Line> alllines = ctx.select().from(KANTEN).fetchInto(Line.class);
        OpenLRLineIterable li = new OpenLRLineIterable(alllines);

        return li.iterator();
    }

    @Override
    public Rectangle2D.Double getMapBoundingBox() {

        // select * from mapdata;
        double x  = ctx.select(METADATA.LEFT_LAT).from(METADATA).fetchOne().value1();
        double y  = ctx.select(METADATA.LEFT_LON).from(METADATA).fetchOne().value1();
        double width  = ctx.select(METADATA.BBOX_WIDTH).from(METADATA).fetchOne().value1();
        double height  = ctx.select(METADATA.BBOX_HEIGHT).from(METADATA).fetchOne().value1();

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
}
