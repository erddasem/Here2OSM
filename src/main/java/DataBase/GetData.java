package DataBase;


import org.jooq.*;
import org.jooq.impl.DSL;


import static org.jooq.sources.tables.Knoten.KNOTEN;
import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Metadata.METADATA;

import DataBase.SpatialQueries;


import javax.sql.DataSource;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.List;

public class GetData {


    static DSLContext ctx;

    static {
        try {
            ctx = DSL.using(DatasourceConfig.getConnection(), SQLDialect.POSTGRES);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void getData() {

        long nodeId = 106;
        Result<Record3<Long, Double, Double>> result = ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON).from(KNOTEN)
                .where(KNOTEN.NODE_ID.eq(nodeId))
                .fetch();

        Result<Record> result1 = ctx.select().from(METADATA).fetch();


        System.out.println(result1);
    }

    public static void main(String[] args) {
        getData();
    }

}
