package OpenLR_h2o;

import DataBase.DatasourceConfig;
import DataBase.SpatialQueries;
import openlr.map.*;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;

/**
 * Implementation of the OpenLR Line interface.
 * Represents the edges in the road topology.
 */
public class OpenLRLine_h2o implements Line {

    long line_id;
    long start_node;
    long end_node;
    int frc;
    int fow;
    int length_meter;
    String name;
    boolean reversed;


    public long getLine_id() {
        return line_id;
    }

    public OpenLRLine_h2o(long line_id, long start_node, long end_node, int frc, int fow, int length_meter, String name, boolean reversed) {
        this.line_id = line_id;
        this.start_node = start_node;
        this.end_node = end_node;
        this.frc = frc;
        this.fow = fow;
        this.length_meter = length_meter;
        this.name = name;
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
    public Node getStartNode() {

        return ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN)
                .where(KNOTEN.NODE_ID.eq(start_node))
                .fetchAny()
                .into(OpenLRNode_h2o.class);
    }

    @Override
    public Node getEndNode() {

        return ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN)
                .where(KNOTEN.NODE_ID.eq(end_node))
                .fetchAny()
                .into(OpenLRNode_h2o.class);
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

        if (distanceAlong < length_meter) {
            // return coordinates of point along line. By using distance along line.
            Record1<?> xCoord = ctx.select(SpatialQueries.st_LineInterpolatePointX(distanceAlong))
                    .from(KANTEN)
                    .where(KANTEN.LINE_ID.eq(line_id))
                    .fetchOne();

            Record1<?> yCoord = ctx.select(SpatialQueries.st_LineInterpolatePointY(distanceAlong))
                    .from(KANTEN)
                    .where(KANTEN.LINE_ID.eq(line_id))
                    .fetchOne();

            return new Point2D.Double((double) xCoord.getValue(0), (double) yCoord.getValue(0));
        } else {

            Double xEndPoint = ctx.select(KNOTEN.LON).from(KNOTEN).where(KNOTEN.NODE_ID.eq(end_node)).fetchOne().value1();
            Double yEndPoint = ctx.select(KNOTEN.LAT).from(KNOTEN).where(KNOTEN.NODE_ID.eq(end_node)).fetchOne().value1();
            return new Point2D.Double(xEndPoint, yEndPoint);
        }

    }

    @Override
    public GeoCoordinates getGeoCoordinateAlongLine(int distanceAlong) {

        GeoCoordinates coordinatesAlongLine = null;
        if (distanceAlong < length_meter) {
            //longitude
            Record1<?> xCoord = ctx.select(SpatialQueries.st_LineInterpolatePointX(distanceAlong))
                    .from(KANTEN)
                    .where(KANTEN.LINE_ID.eq(line_id))
                    .fetchOne();

            //latitude
            Record1<?> yCoord = ctx.select(SpatialQueries.st_LineInterpolatePointY(distanceAlong))
                    .from(KANTEN)
                    .where(KANTEN.LINE_ID.eq(line_id))
                    .fetchOne();

            try {
                coordinatesAlongLine = new GeoCoordinatesImpl((double) xCoord.getValue(0), (double) yCoord.getValue(0));
            } catch (InvalidMapDataException e) {
                e.printStackTrace();
            }
            return coordinatesAlongLine;

        } else {
            Double xEndPoint = ctx.select(KNOTEN.LON).from(KNOTEN).where(KNOTEN.NODE_ID.eq(end_node)).fetchOne().value1();
            Double yEndPoint = ctx.select(KNOTEN.LAT).from(KNOTEN).where(KNOTEN.NODE_ID.eq(end_node)).fetchOne().value1();

            try {
                coordinatesAlongLine = new GeoCoordinatesImpl(yEndPoint, xEndPoint);

            } catch (InvalidMapDataException e) {
                e.printStackTrace();
            }
            return coordinatesAlongLine;
        }

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

        // Can be used if lines in DB are only digitalized once and have an oneway information
        /*Condition con1 = (KANTEN.END_NODE.eq(start_node));
        Condition con2 = (KANTEN.START_NODE.eq(start_node).and(KANTEN.ONEWAY.eq(false)));
        // Only if oneway = false for the requested line
        Condition con3 = (KANTEN.END_NODE.eq(end_node));
        Condition con4 = (KANTEN.START_NODE.eq(end_node).and(KANTEN.ONEWAY.eq(false)));


        List<Line> prevLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                .from(KANTEN)
                .where(con1.or(con2))
                .fetchInto(OpenLRLine_h2o.class);

        if (!oneway) {
            List<Line> lines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                    KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                    .from(KANTEN)
                    .where(con3.or(con4))
                    .fetchInto(OpenLRLine_h2o.class);

            prevLines.addAll(lines);
        }*/

        List<Line> prevLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME)
                .from(KANTEN)
                .where(KANTEN.END_NODE.eq(start_node))
                .fetchInto(OpenLRLine_h2o.class);

        return prevLines.iterator();
    }

    @Override
    public Iterator<Line> getNextLines() {

        // Can be used if lines in DB are only digitalized once and have an oneway information
        /*Condition con1 = (KANTEN.START_NODE.eq(end_node));
        Condition con2 = (KANTEN.END_NODE.eq(end_node).and(KANTEN.ONEWAY.eq(false)));
        // Only if oneway = false for requested line
        Condition con3 = (KANTEN.START_NODE.eq(start_node));
        Condition con4 = (KANTEN.END_NODE.eq(start_node).and(KANTEN.ONEWAY.eq(false)));

        List<Line> nextLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                .from(KANTEN)
                .where(con1.or(con2))
                .fetchInto(OpenLRLine_h2o.class);

        if (!oneway) {
            List<Line> lines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                    KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                    .from(KANTEN)
                    .where(con3.or(con4))
                    .fetchInto(OpenLRLine_h2o.class);

            nextLines.addAll(lines);
        }*/

        List<Line> nextLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME)
                .from(KANTEN)
                .where(KANTEN.START_NODE.eq(end_node))
                .fetchInto(OpenLRLine_h2o.class);

        return nextLines.iterator();
    }

    @Override
    public int distanceToPoint(double longitude, double latitude) {

        return ctx.select(SpatialQueries.stDistance(latitude, longitude).cast(Integer.class))
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(line_id))
                .fetchOne().value1();
    }

    @Override
    public int measureAlongLine(double longitude, double latitude) {

        return ctx.select(SpatialQueries.distAlongLine(latitude, longitude).cast(Integer.class))
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(line_id))
                .fetchOne().value1();
    }

    @Override
    public Path2D.Double getShape() {

        // TODO: Is optional and not implemented

        return null;
    }

    @Override
    public List<GeoCoordinates> getShapeCoordinates() {

        // TODO: Is optional and not implemented

        return null;
    }

    @Override
    public Map<Locale, List<String>> getNames() {

        //TODO: Is optional and not implemented

        return null;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenLRLine_h2o that = (OpenLRLine_h2o) o;
        return line_id == that.line_id &&
                start_node == that.start_node &&
                end_node == that.end_node &&
                frc == that.frc &&
                fow == that.fow &&
                length_meter == that.length_meter &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line_id, start_node, end_node, frc, fow, length_meter, name);
    }
}
