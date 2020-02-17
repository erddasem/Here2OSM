package OpenLR;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.sources.tables.Kanten;


import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;


import javax.sql.DataSource;

public class TestGetData {
    public void getData() {
    DataSource conn = DatasourceConfig.createDataSource();
    DSLContext ctx = DSL.using(conn, SQLDialect.POSTGRES);

    long node_id = 28;

        Condition a = KANTEN.START_NODE.eq(node_id);
        Condition b = KANTEN.END_NODE.eq(node_id);
        Condition c = KANTEN.ONEWAY.eq(false);

        Condition andCon = a.and(c);             // These OR-connected conditions form a new condition, wrapped in parentheses
        Condition finalCon = b.or(andCon);

        Result<Record1<Long>> countIncomming = ctx.select(KANTEN.LINE_ID).from(KANTEN)
                .where(finalCon)
                .fetch();

        System.out.println(countIncomming);





    }
}
