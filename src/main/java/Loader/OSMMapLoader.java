package Loader;

import DataBase.DatasourceConfig;
import DataBase.SpatialQueries;
import OpenLR_h2o.OpenLRLine_h2o;
import OpenLR_h2o.OpenLRNode_h2o;
import openlr.map.Line;
import openlr.map.Node;
import org.apache.commons.collections.ListUtils;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.awt.geom.Rectangle2D;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;
import static org.jooq.sources.tables.Metadata.METADATA;

public class OSMMapLoader {

    private final DSLContext ctx;
    private final Connection con;
    public List<Node> allNodes = getAllNodes();
    public List<OpenLRLine_h2o> allLines = getAllLines();

    public OSMMapLoader() throws SQLException {
        con = DatasourceConfig.getConnection();
        ctx = DSL.using(con, SQLDialect.POSTGRES);
    }

    // -------------------------- OpenLRMapDatabase Methods ----------------------------------
    public List<Node> getAllNodes() {

        return ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN).fetchInto(OpenLRNode_h2o.class);

        //return allNodes.iterator();
    }

    /**
     * Gets all lines from MapDatabase. Since lines with oneway=false are only saved for one direction
     * reversed lines are created with reversed start and end node.
     * @return
     */
    public List<OpenLRLine_h2o> getAllLines() {

        List<ReversedLine> reversedLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME)
                .from(KANTEN)
                .where(KANTEN.ONEWAY.eq(false))
                .fetchInto(ReversedLine.class);

        // Lambda Expression
        List<Line> convertedLines = new ArrayList<>();
        reversedLines.forEach(reversedLine -> convertedLines.add(ReversedLine2OpenLRLineConverter.convert(reversedLine)));

        /*convertedLines = reversedLines.stream().map(reversedLine -> {
            OpenLRLine_h2o openLRLineH2o = new OpenLRLine_h2o(reversedLine.line_id, reversedLine.start_node, reversedLine.end_node, reversedLine.frc, reversedLine.fow, reversedLine.length, reversedLine.name, reversedLine.reversed);
            return openLRLineH2o;
        }).collect(Collectors.toCollection(ArrayList::new));*/

        // set value reversed to true. - Missing
        // Change start and end Node > does not work the way it is implemented now
        List<Line> lines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                .from(KANTEN).fetchInto(OpenLRLine_h2o.class);

        return ListUtils.union(lines, convertedLines);

        //return allLines.iterator();
    }

    public Line getLine(long id) {

        Optional<OpenLRLine_h2o> matchingLine = allLines.stream().filter(l -> l.getID() == id).findFirst();
        return matchingLine.get();
        /*return ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME)
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(id))
                .fetchAny().into(OpenLRLine_h2o.class);*/

    }

    public Node getNode(long id) {

        Optional<Node> matchingNode = allNodes.stream().filter(n -> n.getID() == id).findFirst();
        return matchingNode.get();
    }

    public int getNumberOfNodes() {
        return allNodes.size();
    }

    public int getNumberOfLines() {
        return allLines.size();
    }

    public Rectangle2D.Double getMapBoundingBox() {

        // select * from mapdata;
        double x = ctx.select(METADATA.LEFT_LAT).from(METADATA).fetchOne().value1();
        double y = ctx.select(METADATA.LEFT_LON).from(METADATA).fetchOne().value1();
        double width = ctx.select(METADATA.BBOX_WIDTH).from(METADATA).fetchOne().value1();
        double height = ctx.select(METADATA.BBOX_HEIGHT).from(METADATA).fetchOne().value1();

        return new Rectangle2D.Double(x, y, width, height);
    }

    public Iterator<Line> findLinesCloseByCoordinate(double longitude, double latitude, int distance) {

        List<Long> lines = ctx.select().from(KANTEN).where(SpatialQueries.stDWithin(longitude, latitude, distance))
                .fetch(KANTEN.LINE_ID, Long.class);

        List<Line> closeByLines = new ArrayList<>();

        for (Long lineID : lines) {
            for (OpenLRLine_h2o line : allLines) {
                if (line.getLine_id() == lineID)
                    closeByLines.add(line);
            }
        }
        System.out.println(closeByLines.size());
        return closeByLines.iterator();
    }

}
