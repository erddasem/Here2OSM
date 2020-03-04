package DataBase;

import org.jooq.Field;
import org.jooq.impl.DSL;

public class SpatialQueries {

    public static Field<?> geomAsText(Field<?> geom) {
        return DSL.field("ST_AsText({0})", geom);
    }

    /**
     * Returns given distance in degrees depending on latitude, distance must be given in meters.
     *
     * @param lat  latitude of the point
     * @param dist distance between point and geometry in meters
     * @return distance in degrees
     */
    public static double distToDeg(double lat, int dist) {
        return dist / (111.32 * 1000 * Math.cos(lat * (Math.PI / 180)));
    }

    /**
     * ST_DWithin PostGIS function as string for using in where clause. Uses geometry of used table in JOOQ query,
     * point geometry given as latitude and longitude (WGS84 coordinates (EPSG:4326)) and distance in meters between the two
     * geometries.
     *
     * @param lon
     * @param lat
     * @param dist
     * @return ST_DWithin query as String
     */
    public static String stDWithin(double lon, double lat, int dist) {

        double dist_deg = distToDeg(lat, dist);
        return "ST_DWithin(geom, ST_PointFromText('POINT(" + lon + " " +
                lat + ")', 4326)," + dist_deg + ")=true";
    }

    /**
     * ST_Distance PostGIS function as field for using in JOOQ select query, returns ditsance between line geometry and
     * given point (latitude, longitude) in meters as integer value.
     *
     * @param lat latitude of the point as WGS84 coordinate (EPSG: 4326)
     * @param lon longitude of the point as WGS84 coordinate (EPSG: 4326)
     * @return field for JOOQ select statement
     */
    public static Field<?> stDistance(double lat, double lon) {

        String query = "Round(ST_Distance(geom::geography, 'SRID=4326;POINT(" + lon + " " + lat + ")'::geography))";
        return DSL.field(query);
    }
}
