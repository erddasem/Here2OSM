package Loader;

import DataBase.DatasourceConfig;
import Geometries.DirectLine;
import Geometries.LineConverter;
import Geometries.ReversedLine;
import OpenLRImpl.LineImpl;
import OpenLRImpl.MapDatabaseImpl;
import OpenLRImpl.NodeImpl;
import org.apache.commons.collections.ListUtils;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.geom.Point;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;
import static org.jooq.sources.tables.Metadata.METADATA;

public class OSMMapLoader {

    private final DSLContext ctx;
    private final Connection con;
    private final List<NodeImpl> allNodesList;
    private final List<LineImpl> allLinesList;
    private MapDatabaseImpl mdb;
    private final ArrayList<Double> boundingBoxInformation;

    public OSMMapLoader() throws SQLException {
        con = DatasourceConfig.getConnection();
        ctx = DSL.using(con, SQLDialect.POSTGRES);
        allNodesList = getAllNodes();
        allLinesList = getAllLines();
        boundingBoxInformation = getBoundingBox();
    }

    public List<NodeImpl> getAllNodesList() {
        return allNodesList;
    }

    public List<LineImpl> getAllLinesList() {
        return allLinesList;
    }

    public ArrayList<Double> getBoundingBoxInformation() {
        return boundingBoxInformation;
    }

    public void setMdb(MapDatabaseImpl mdb) {

        this.mdb = mdb;
        allNodesList.forEach(n -> n.setMdb(this.mdb));
        allLinesList.forEach(l -> l.setMdb(this.mdb));
        //setMdbForNodes(mdb);
    }

    public int numberOfNodes() {
        return allNodesList.size();
    }

    public int numberOfLines() {
        return allLinesList.size();
    }

    // get all Nodes from PostgresDB
    public List<NodeImpl> getAllNodes() {

        List<NodeImpl> allNodes = ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN).fetchInto(NodeImpl.class);

        setNodeGeometry(allNodes);
        setConnectedLinesList(allNodes);

        return allNodes;
    }

    private void setConnectedLinesList(List<NodeImpl> allNodesList) {

        allNodesList.forEach(n -> {
            List<Long> connectedLinesIDs = ctx.select().from(KANTEN)
                    .where(KANTEN.START_NODE.eq(n.getID()))
                    .or(KANTEN.END_NODE.eq(n.getID()))
                    .fetch().getValues(KANTEN.LINE_ID);

            n.setConnectedLinesIDs(connectedLinesIDs);
        });

    }

    private void setNodeGeometry(List<NodeImpl> allNodesList) {

        GeometryFactory factory = new GeometryFactory();

        allNodesList.forEach(n -> {
            // Create point geometry for each node
            Point point = factory.createPoint(new Coordinate(n.getLon(), n.getLat()));
            n.setPointGeometry(point);
            n.setMdb(mdb);
        });
    }


    //Get all lines from Postgres DB
    public List<LineImpl> getAllLines() {

        List<LineImpl> linesDirect = new ArrayList<>();
        List<LineImpl> linesReversed = new ArrayList<>();

        /*convertedLines = reversedLines.stream().map(reversedLine -> {
            OpenLRLine_h2o openLRLineH2o = new OpenLRLine_h2o(reversedLine.line_id, reversedLine.start_node, reversedLine.end_node, reversedLine.frc, reversedLine.fow, reversedLine.length, reversedLine.name, reversedLine.reversed);
            return openLRLineH2o;
        }).collect(Collectors.toCollection(ArrayList::new));*/

        // set value reversed to true. - Missing
        // Get all lines from database
        List<DirectLine> directLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME)
                .from(KANTEN)
                .fetchInto(DirectLine.class);

        // Convert directLine to TomTomLine
        directLines.forEach(directLine -> linesDirect.add(LineConverter.directLineToOpenLRLine(directLine)));

        // get all lines from database where oneway=false as reversed line > start and end node are siwtched within the constructor
        List<ReversedLine> reversedLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME)
                .from(KANTEN)
                .where(KANTEN.ONEWAY.eq(false))
                .fetchInto(ReversedLine.class);

        // Convert reversedLine to tomtomLine
        reversedLines.forEach(reversedLine -> linesReversed.add(LineConverter.reversedLineToOpenLRLine(reversedLine)));

        List allLines = ListUtils.union(linesDirect, linesReversed);

        setLineNodes(allLines);
        setLineGeometry(allLines);

        return allLines;

        //return allLines.iterator();
    }

    private void setLineNodes(List<LineImpl> lines) {
        lines.forEach(l -> {

            long startNodeID = l.getStartNodeID();
            Optional<NodeImpl> startNode = allNodesList.stream().filter(n -> n.getID() == startNodeID).findFirst();
            startNode.ifPresent(l::setStartNode);

            long endNodeID = l.getEndNodeID();
            Optional<NodeImpl> endNode = allNodesList.stream().filter(n -> n.getID() == endNodeID).findFirst();
            endNode.ifPresent(l::setEndNode);
        });
    }

    private static Field<?> st_asText(boolean isReversed) {
        if(!isReversed)
            return DSL.field("ST_AsText(geom)");
        else {
            return DSL.field("ST_AsText(ST_Reverse(geom))");
        }
    }

    private void setLineGeometry(List<LineImpl> lines) {

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        WKTReader reader = new WKTReader( geometryFactory );

        lines.forEach(l -> {
            String wktString;
            wktString = ctx.select(st_asText(l.isReversed())).from(KANTEN).where(KANTEN.LINE_ID.eq(l.getID())).fetchOne().value1().toString();
            try {
                 l.setLineGeometry((LineString) reader.read(wktString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            l.setMdb(mdb);
        });
    }

    public ArrayList<Double> getBoundingBox() {

        double x = ctx.select(METADATA.LEFT_LAT).from(METADATA).fetchOne().value1();
        double y = ctx.select(METADATA.LEFT_LON).from(METADATA).fetchOne().value1();
        double width = ctx.select(METADATA.BBOX_WIDTH).from(METADATA).fetchOne().value1();
        double height = ctx.select(METADATA.BBOX_HEIGHT).from(METADATA).fetchOne().value1();

        ArrayList<Double> bboxInformation = new ArrayList<>();
        bboxInformation.add(x);
        bboxInformation.add(y);
        bboxInformation.add(width);
        bboxInformation.add(height);

        return bboxInformation;
    }

    /*private void setMdbForNodes(MapDatabaseImpl mdb) {
        allNodesList.forEach(n -> n.setMdb(mdb));
    } */

    public void close() throws Exception {
        if (ctx != null) ctx.close();
        if (con != null) con.close();
    }


    /*public Line getLine(long id) {

        Optional<LineImpl> matchingLine = allLinesList.stream().filter(l -> l.getID() == id).findFirst();
        return matchingLine.get();
    }

    public Node getNode(long id) {

        Optional<NodeImpl> matchingNode = allNodesList.stream().filter(n -> n.getID() == id).findFirst();
        return matchingNode.get();
    }

    public int getNumberOfNodes() {
        return allNodesList.size();
    }

    public int getNumberOfLines() {
        return allLinesList.size();
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

        List<Long> lines = ctx.select()
                .from(KANTEN)
                .where(SpatialQueries.stDWithin(longitude, latitude, distance))
                .fetch(KANTEN.LINE_ID, Long.class);

        List<Line> closeByLines = new ArrayList<>();

        for (Long lineID : lines) {
            for (Line line : allLinesList) {
                if (line.getID() == lineID)
                    closeByLines.add(line);
            }
        }
        System.out.println(closeByLines.size());
        return closeByLines.iterator();
    }

    public Iterator<Node> findNodesCloseByCoordinate(double longitude, double latitude, int distance) {
        List<Long> nodes = ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN)
                .where(SpatialQueries.stDWithin(longitude, latitude, distance))
                .fetch(KNOTEN.NODE_ID, Long.class);

        List<Node> closeByNodes = new ArrayList<>();
        for(Long nodeID : nodes) {
            for (Node node : allNodesList) {
                if (node.getID() == nodeID)
                    closeByNodes.add(node);
            }
        }
        return closeByNodes.iterator();
    }

    // -------------------------------- Node Interface -------------------------------------------------

    public Iterator<Line> getConnectedLines(long nodeID) {
        List<Line> conntectedLines = new ArrayList<>();
        for (LineImpl l : allLinesList) {
            if (l.getStartNodeID() == nodeID || l.getEndNodeID() == nodeID) {
                conntectedLines.add(l);
            }
        }
        return conntectedLines.iterator();
    }

    public int getNumberConnectedLines(long nodeID) {
        int numberConnectedLines = 0;
        for (LineImpl l : allLinesList) {
            if (l.getStartNodeID() == nodeID || l.getEndNodeID() == nodeID) {
                numberConnectedLines++;
            }
        }
        return numberConnectedLines;
    }

    public Iterator<Line> getOutgoingLines(long nodeID) {
        List<Line> outgoingLines = allLinesList.stream().filter(l -> l.getStartNodeID() == nodeID).collect(Collectors.toList());
        return outgoingLines.iterator();
    }

    public Iterator<Line> getIncomingLines(long nodeID) {
        List<Line> incomingLines = allLinesList.stream().filter(l -> l.getEndNodeID() == nodeID).collect(Collectors.toList());
        return incomingLines.iterator();
    }

    // -------------------------------- Line Interface -------------------------------------------------


    public Point2D.Double getPointAlongLine(int distanceAlong) {
        return null;
    }

    public GeoCoordinates getGeoCoordinateAlongLine(int distanceAlong) {
        return null;
    }


    public Iterator<Line> getPrevLines(long startNodeID) {

        List<Line> prevLines = allLinesList.stream().filter(l -> l.getEndNodeID() == startNodeID).collect(Collectors.toList());
        return prevLines.iterator();
    }

    public Iterator<Line> getNextLines(long endNodeID) {

        List<Line> nextLines = allLinesList.stream().filter(l -> l.getStartNodeID() == endNodeID).collect(Collectors.toList());
        return nextLines.iterator();
    }

    public int distanceToPoint(double longitude, double latitude, long lineID) {
        return ctx.select(SpatialQueries.stDistance(latitude, longitude).cast(Integer.class))
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(lineID))
                .fetchOne().value1();
    }

    public int measureAlongLine(double longitude, double latitude, long lineID, boolean reversed) {
        if (reversed) {
            return ctx.select(SpatialQueries.distAlongLineReversed(latitude, longitude).cast(Integer.class))
                    .from(KANTEN)
                    .where(KANTEN.LINE_ID.eq(lineID))
                    .fetchOne().value1();
        }
        else {
            return ctx.select(SpatialQueries.distAlongLine(latitude, longitude).cast(Integer.class))
                    .from(KANTEN)
                    .where(KANTEN.LINE_ID.eq(lineID))
                    .fetchOne().value1();
        }
    }
*/
}
