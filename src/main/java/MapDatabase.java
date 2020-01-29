import openlr.map.Line;
import openlr.map.Node;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

public class MapDatabase implements openlr.map.MapDatabase{
    DataSource conn = DatasourceConfig.createDataSource();
    DSLContext ctx = DSL.using(conn, SQLDialect.POSTGRES);
    @Override
    public boolean hasTurnRestrictions() {
        return false;
    }

    @Override
    public Line getLine(long id) {
        // select * from kanten where id = id;
        return null;
    }

    @Override
    public Node getNode(long id) {
        // select * from knoten where id = id;
        // SQL Anfrage nach lat und long mit Hilfe der ID
        // als Variablen, über Konstruktor (Impl. Interface Node) für Node,
        return null;
    }

    @Override
    public Iterator<Node> findNodesCloseByCoordinate(double longitude, double latitude, int distance) {
        return null;
    }

    @Override
    public Iterator<Line> findLinesCloseByCoordinate(double longitude, double latitude, int distance) {
        return null;
    }

    @Override
    public boolean hasTurnRestrictionOnPath(List<? extends Line> path) {
        return false;
    }

    @Override
    public Iterator<Node> getAllNodes() {
        // select * from knoten;

        return null;
    }

    @Override
    public Iterator<Line> getAllLines() {

        // select * from lines,

        return null;
    }

    @Override
    public Rectangle2D.Double getMapBoundingBox() {

        // select * from mapdata;
        return null;
    }

    @Override
    public int getNumberOfNodes() {
        // select count(node_id) from knoten;
        return 0;
    }

    @Override
    public int getNumberOfLines() {
        // select count(id) from kanten;
        return 0;
    }
}
