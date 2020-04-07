package DataBase;

import org.jooq.*;
import org.jooq.impl.DSL;


import static HereApi.ApiRequest.to_regclass;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.sources.tables.Incidents.INCIDENTS;
import static org.jooq.sources.tables.Knoten.KNOTEN;
import static org.jooq.sources.tables.Kanten.KANTEN;

import DataBase.SpatialQueries;


import javax.sql.DataSource;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class TestGetData {
    // class to test JOOQ queries before implementing into openLR interfaces

    static DSLContext ctx;

    static {
        try {
            ctx = DSL.using(DatasourceConfig.getConnection(), SQLDialect.POSTGRES);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getData() {


        String result = String.valueOf(ctx.select(to_regclass("openlr", "incidents")).fetchOne().value1());
        System.out.println(result);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        Timestamp youngestEntry = ctx.select(max(INCIDENTS.GENERATIONDATE)).from(INCIDENTS).fetchOne().value1();
        System.out.println(youngestEntry);
        boolean after = timestamp.after(youngestEntry);
        System.out.println("Ausführungsdatum nach jüngstem Eintrag in Tabelle: " + after);

       /* double longitude = 13.748489;
        double latitude = 51.058306;
        int distance = 19;
        long id = 5;
        int length = 44;
*/



        /*Field<Integer> DIST = DSL.field("Round(ST_Distance(geom::geography, st_pointfromtext('POINT(" + longitude + " "
                + latitude + ")', 4326)::geography))", Integer.class);


        Result<Record2<Integer, Long>> lines = ctx.select(DIST, KANTEN.LINE_ID)
                .from(KANTEN)
                .fetch();

        Result<Record1<Integer>> line = ctx.select(DIST)
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(id))
                .fetch();*//*

        Record1<Integer> dist = ctx.select(SpatialQueries.stDistance(latitude, longitude).cast(Integer.class))
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(id))
                .fetchOne();
        int abstand = (int) dist.getValue(0);


        //String query = " Round(e.length_meter * ST_LineLocatePoint(e.geom,ST_ClosestPoint(e.geom, 'SRID=4326;POINT(13.748489 51.058306)')))";
        //from kanten e, knoten n where e.line_id = 5 and n.node_id = e.start_node ;
        Record1<Integer> distAlongLine = ctx.select(SpatialQueries.distAlongLine(latitude, longitude).cast(Integer.class))
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(id))
                .fetchOne();

        Record1<?> pointX = ctx.select(SpatialQueries.st_LineInterpolatePointX(distance, length))
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(id))
                .fetchOne();

        Double coordX = (Double) pointX.getValue(0);


        //double xCoord =  (double) pointX.getValues(0);


        *//*Field<?> WKT = DSL.field("ST_AsText(geom)");

        Result<? extends Record2<?, Long>> geom = ctx.select(SpatialQueries.geomAsText(KNOTEN.GEOM), KNOTEN.NODE_ID)
                .from(KNOTEN)
                .where(KNOTEN.NODE_ID.eq((long) 1))
                .fetch();*//*

        System.out.println(coordX);


    }*/

    }
}
