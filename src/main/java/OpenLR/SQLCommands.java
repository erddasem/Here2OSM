package OpenLR;

import openlr.map.Line;
import openlr.map.Node;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;

public class SQLCommands {


    static DataSource conn = DatasourceConfig.createDataSource();
    static DSLContext ctx = DSL.using(conn, SQLDialect.POSTGRES);

    static Node getKnoten(long knoten_id) {
        Result<Record> getNode = ctx.select().from(KNOTEN).where(KNOTEN.NODE_ID.eq(knoten_id)).fetch();
        Node node = null;
        for (Record r : getNode) {
            long node_id = r.getValue((KNOTEN.NODE_ID));
            double lon = r.getValue(KNOTEN.LON);
            double lat = r.getValue(KNOTEN.LAT);
            int cnt_in = r.getValue(KNOTEN.INGOING);
            int cnt_out = r.getValue(KNOTEN.OUTGOING);

            node = new OpenLRNode(node_id, lon, lat, cnt_in, cnt_out);
        }
        return node;
    }




}
