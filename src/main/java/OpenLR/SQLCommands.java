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

    static Line getLinie(long linien_id) {
        Result<Record> getLine = ctx.select().from(KANTEN).where(KANTEN.LINE_ID.eq(linien_id)).fetch();
        Line line = null;
        for (Record r : getLine) {
            long l_id = r.getValue(KANTEN.LINE_ID);
            long start_node = r.getValue(KANTEN.START_NODE);
            long end_node = r.getValue(KANTEN.END_NODE);
            String name = r.getValue(KANTEN.NAME);
            int fow = r.getValue(KANTEN.FOW);
            int frc = r.getValue(KANTEN.FOW);
            int length = r.getValue(KANTEN.LENGTH_METER).intValue();

            line = new OpenLRLine(l_id, start_node, end_node, frc, fow, length, name);

        }
        return line;
    }


}
