package DataBase;

import org.jooq.*;
import org.jooq.impl.DSL;


import static org.jooq.sources.tables.Knoten.KNOTEN;
import static org.jooq.sources.tables.Kanten.KANTEN;

import DataBase.SpatialQueries;


import javax.sql.DataSource;

public class TestGetData {

    private DSLContext ctx;

    public void getData() {
        DataSource conn = DatasourceConfig.createDataSource();
        ctx = DSL.using(conn, SQLDialect.POSTGRES);


        double longitude = 13.748489;
        double latitude = 51.058306;
        int distance = 50;
        long id = 5;


        Field<Integer> DIST = DSL.field("Round(ST_Distance(geom::geography, st_pointfromtext('POINT(" + longitude + " "
                + latitude + ")', 4326)::geography))", Integer.class);


        Result<Record2<Integer, Long>> lines = ctx.select(DIST, KANTEN.LINE_ID)
                .from(KANTEN)
                .fetch();

        Result<Record1<Integer>> line = ctx.select(DIST)
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(id))
                .fetch();

        Record1<Integer> dist = ctx.select(SpatialQueries.stDistance(latitude, longitude).cast(Integer.class))
                .from(KANTEN)
                .where(KANTEN.LINE_ID.eq(id))
                .fetchOne();

        /*Field<?> WKT = DSL.field("ST_AsText(geom)");

        Result<? extends Record2<?, Long>> geom = ctx.select(SpatialQueries.geomAsText(KNOTEN.GEOM), KNOTEN.NODE_ID)
                .from(KNOTEN)
                .where(KNOTEN.NODE_ID.eq((long) 1))
                .fetch();*/

        System.out.println(dist);


    }
}
