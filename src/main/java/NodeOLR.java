import openlr.map.GeoCoordinates;
import openlr.map.Line;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.Iterator;

public class NodeOLR implements openlr.map.Node {

    long node_id;
    double lon;
    double lat;
    int cnt_in;
    int cnt_out;

    public NodeOLR(long id, double lon, double lat, int cnt_in, int cnt_out) {
        this.node_id = id;
        this.lon = lon;
        this.lat = lat;
        this.cnt_in = cnt_in;
        this.cnt_out = cnt_out;
    }


    DataSource conn = DatasourceConfig.createDataSource();
    DSLContext ctx = DSL.using(conn, SQLDialect.POSTGRES);

    // Konstruktor der auf einzelne Teile des Nodes verweisen

    @Override
    public double getLatitudeDeg() {
        return lat;
    }

    @Override
    public double getLongitudeDeg() {
        return lon;
    }

    @Override
    public GeoCoordinates getGeoCoordinates() {

        return null;
    }

    @Override
    public Iterator<Line> getConnectedLines() {
        return null;
    }

    @Override
    public int getNumberConnectedLines() {
        return 0;
    }

    @Override
    public Iterator<Line> getOutgoingLines() {
        return null;
    }

    @Override
    public Iterator<Line> getIncomingLines() {
        return null;
    }

    @Override
    public long getID() {
        return node_id;
    }
}
