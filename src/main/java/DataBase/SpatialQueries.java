package DataBase;

import org.jooq.Field;
import org.jooq.impl.DSL;

public class SpatialQueries {

    public static Field<?> geomAsText(Field<?> geom) {
        return DSL.field("ST_AsText({0})", geom);
    }

    public static double distToDeg(double lat, int dist) {
        return dist / (111.32 * 1000 * Math.cos(lat * (Math.PI / 180)));
    }

    public static String stDWithin(double lon, double lat, int dist) {

        double dist_deg = distToDeg(lat, dist);
        return "ST_DWithin(geom, ST_PointFromText('POINT(" + lon + " " +
                lat + ")', 4326)," + dist_deg + ")=true";
    }


    public static Field<?> stDistance(double lat, double lon) {

        String query = "Round(ST_Distance(geom::geography, 'SRID=4326;POINT(" + lon + " " + lat + ")'::geography))";
        return DSL.field(query);
    }
}
