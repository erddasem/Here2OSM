package OpenLR;

import openlr.map.Line;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.sources.tables.Kanten;


import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;


import javax.sql.DataSource;
import java.util.List;

public class TestGetData {
    public void getData() {
    DataSource conn = DatasourceConfig.createDataSource();
    DSLContext ctx = DSL.using(conn, SQLDialect.POSTGRES);


        double longitude = 13.749407;
        double latitude = 51.054077;
        int distance = 50;

        double distance_deg = distance / (111.32 * 1000 * Math.cos(latitude * (Math.PI / 180)));
        System.out.println(distance_deg);

        String whereCon = "ST_DWithin(geom, ST_PointFromText('POINT("+ longitude + " " +
                latitude + ")', 4326)," + distance_deg + ")=true";
        String whereCon1 = "ST_DWithin(geom::geography, ST_PointFromText('POINT("+ longitude + " " +
                latitude + ")', 4326)::geography," + distance + ")=true";

        Result<Record1<Long>> nodes = ctx.select(KNOTEN.NODE_ID)
                .from(KNOTEN)
                .where(whereCon)
                .fetch();

        System.out.println(nodes);





    }
}
