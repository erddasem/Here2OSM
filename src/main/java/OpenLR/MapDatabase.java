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
import static OpenLR.SQLCommands.getLinie;
import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;

public class MapDatabase implements openlr.map.MapDatabase{
    DataSource conn = DatasourceConfig.createDataSource();
    DSLContext ctx = DSL.using(conn, SQLDialect.POSTGRES);
    @Override
    public boolean hasTurnRestrictions() {
        return false;
    }

    @Override
    public Line getLine(long id) {

        return getLinie(id);
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

        // select * from lines,
        Result<Record> getLines = ctx.select().from(KANTEN).fetch();
        // erstellen Linien Objekte aus erhaltenen IDs, Linien Objekte dann in Iterator packen

        return null;
    }

    @Override
    public Rectangle2D.Double getMapBoundingBox() {

        // select * from mapdata;
        return null;
    }

    @Override
    public int getNumberOfNodes() {

        int numberOfNodes  = ctx.selectCount().from(KNOTEN).fetchOne().value1();
        return numberOfNodes;
    }

    @Override
    public int getNumberOfLines() {

        int numberOfLines  = ctx.selectCount().from(KANTEN).fetchOne().value1();
        return numberOfLines;
    }
}
