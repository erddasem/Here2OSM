import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.sources.tables.Kanten;
import org.jooq.sources.tables.Knoten;


import static org.jooq.impl.DSL.*;
import static org.jooq.sources.tables.Kanten.KANTEN;



import javax.sql.DataSource;

public class testGetData {
    public void getData() {
    DataSource conn = DatasourceConfig.createDataSource();
    DSLContext ctx = DSL.using(conn, SQLDialect.POSTGRES);
    Result<Record> result = ctx.select().from(Kanten.KANTEN).fetch();

    for (Record r : result) {
        Integer id = r.getValue(KANTEN.LINE_ID);

        System.out.println("ID: " + id);
    }



    }
}
