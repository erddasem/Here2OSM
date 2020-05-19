package OpenLR;

import DataBase.DatasourceConfig;
import DataBase.SpatialQueries;
import openlr.map.*;
import org.jooq.*;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;


public class OpenLRLine_h2o implements Line {

    long line_id;
    long start_node;
    long end_node;
    int frc;
    int fow;
    int length_meter;
    String name;
    boolean oneway;

    public OpenLRLine_h2o(long line_id, long start_node, long end_node, int frc, int fow, int length_meter, String name, boolean oneway) {
        this.line_id = line_id;
        this.start_node = start_node;
        this.end_node = end_node;
        this.frc = frc;
        this.fow = fow;
        this.length_meter = length_meter;
        this.name = name;
        this.oneway = oneway;
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
            Record1<?> xCoord = ctx.select(SpatialQueries.st_LineInterpolatePointX(distanceAlong, length_meter))
                    .from(KANTEN)
                    .where(KANTEN.LINE_ID.eq(line_id))
                    .fetchOne();

            Record1<?> yCoord = ctx.select(SpatialQueries.st_LineInterpolatePointY(distanceAlong, length_meter))
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
            Record1<?> xCoord = ctx.select(SpatialQueries.st_LineInterpolatePointX(distanceAlong, length_meter))
                    .from(KANTEN)
                    .where(KANTEN.LINE_ID.eq(line_id))
                    .fetchOne();

            //latitude
            Record1<?> yCoord = ctx.select(SpatialQueries.st_LineInterpolatePointY(distanceAlong, length_meter))
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

            Condition andCon = (KANTEN.START_NODE.eq(end_node)).and(KANTEN.ONEWAY.eq(false));
            Condition finalCon = (KANTEN.END_NODE.eq(start_node)).or(andCon);

            List<Line> prevLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                    KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                    .from(KANTEN)
                    .where(finalCon)
                    .fetchInto(OpenLRLine_h2o.class);

            return prevLines.iterator();
    }

    @Override
    public Iterator<Line> getNextLines() {

        /*Selects next lines depending on given line id. Next lines can be all lines using the end node of the given
        line as start node and all lines where oneway = true and the end node is the same.*/
        // select line_id from kanten where kanten.start_node = end_node or (kanten.start_node = end_node and oneway = false);
        Condition andCon = (KANTEN.END_NODE.eq(start_node)).and(KANTEN.ONEWAY.eq(false));
        Condition finalCon = (KANTEN.START_NODE.eq(end_node)).or(andCon);

        List<Line> nextLines = ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                .from(KANTEN)
                .where(finalCon)
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

        //select Round(e.length_meter * ST_LineLocatePoint(e.geom,ST_ClosestPoint(e.geom, 'SRID=4326;POINT(13.748489 51.058306)'))) from kanten where line_id = line_id;

        return ctx.select(SpatialQueries.distAlongLine(latitude, longitude).cast(Integer.class))
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(line_id))
                .fetchOne().value1();
    }

    @Override
    public Path2D.Double getShape() {

        // TODO: Is optional and is not implemented

        return null;
    }

    @Override
    public List<GeoCoordinates> getShapeCoordinates() {

        // TODO: Is optional and is not implemented

        return null;
    }

    @Override
    public Map<Locale, List<String>> getNames() {

        //TODO: Is optional and is not implemented

        return null;
    }


}
