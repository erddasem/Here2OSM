package DataBase;

import org.jooq.Field;
import org.jooq.impl.DSL;

public class SpatialQueries {

    /**
     * Returns given distance in degrees depending on latitude, distance must be given in meters.
     *
     * @param lat  Latitude of the point.
     * @param dist Distance between point and geometry in meters.
     * @return Distance in degrees as double value.
     */
    public static double distToDeg(double lat, int dist) {
        return dist / (111.32 * 1000 * Math.cos(lat * (Math.PI / 180)));
    }

    /**
     * ST_DWithin PostGIS function as string for using in where clause. Uses geometry of used table in JOOQ query,
     * point geometry given as latitude and longitude (WGS84 coordinates (EPSG:4326)) and distance in meters between
     * the two geometries.
     * Can only be used in where clause.
     * Note: "geom" refers to the geometry of the line or node for which the query is being asked.
     * It is assumed that the geometry column in the data base is labeled "geom".
     * @param lon Longitude of the position as WGS84 coordinate (EPSG: 4326)
     * @param lat Latitude of the position as WGS84 coordinate (EPSG: 4326)
     * @param dist Radius around the position where the geometries should be located
     * @return ST_DWithin query as String
     */
    public static String stDWithin(double lon, double lat, int dist) {

        double dist_deg = distToDeg(lat, dist);
        return "ST_DWithin(geom, ST_PointFromText('POINT(" + lon + " " +
                lat + ")', 4326)," + dist_deg + ")=true";
    }

    public static String stDWithinReversed(double lon, double lat, int dist) {
        double dist_deg = distToDeg(lat, dist);
        return "ST_DWithin(ST_Reverse(geom), ST_PointFromText('POINT(" + lon + " " +
                lat + ")', 4326)," + dist_deg + ")=true";
    }

    /**
     * ST_Distance PostGIS function as field for using in JOOQ select query. Returns the distance between line geometry
     * and the point (latitude and longitude) in meters as an integer value.
     * Note: "geom" refers to the geometry of the line for which the query is being asked.
     * It is assumed that the geometry column in the data base is labeled "geom".
     *
     * @param lat Latitude of the point as WGS84 coordinate (EPSG: 4326)
     * @param lon Longitude of the point as WGS84 coordinate (EPSG: 4326)
     * @return Field for JOOQ select statement
     */
    public static Field<?> stDistance(double lat, double lon) {

        String query = "Round(ST_Distance(geom::geography, 'SRID=4326;POINT(" + lon + " " + lat + ")'::geography))";
        return DSL.field(query);
    }

    public static Field<?> stDistanceReversed(double lat, double lon) {
        String query = "Round(ST_Distance(ST_Reverse(geom::geography), 'SRID=4326;POINT(" + lon + " " + lat + ")'::geography))";
        return DSL.field(query);
    }

    /**
     * Function to get the distance along the line between a point on the line and the starting point of the line.
     * The function creates point on the line based on given latitude and longitude (WGS84, EPSG:4326), calculates
     * the distance between the point and the starting point as a fraction of the total 2D line length
     * (float between 0 and 1) and returns the distance as an integer value (meters).
     * Note: "geom" refers to the geometry of the line for which the query is being asked.
     * It is assumed that the geometry column in the data base is labeled "geom".
     *
     * @param lat Latitude of the position as WGS84 coordinate (EPSG: 4326)
     * @param lon Longitude of the position as WGS84 coordinate (EPSG: 4326)
     * @return Field for JOOQ select statement
     */
    public static Field<?> distAlongLine(double lat, double lon) {
        String query = " Round(length_meter * ST_LineLocatePoint(geom,ST_ClosestPoint(geom, 'SRID=4326;POINT(" +
                lon + " " + lat + ")')))";
        return DSL.field(query);
    }

    /**
     * Calculates fraction of the total 2d line length.
     *
     * @param distance     Distance between the starting point and the point on the line
     * @param length_meter Total length of the line in meters
     * @return fraction as double value (between 0 and 1)
     */
    public static double fraction(int distance, int length_meter) {
        return distance / length_meter;
    }

    /**
     * Returns x coordinate of a point along a line, based on the length of the line and the distance between the
     * starting point of the line and the point.
     * Note: "geom" refers to the geometry of the line for which the query is being asked.
     * It is assumed that the geometry column in the data base is labeled "geom".
     *
     * @param dist Distance between starting point and point in meters as integer value
     * @return Field for JOOQ select statement
     */
    public static Field<?> st_LineInterpolatePointX(int dist) {
        return DSL.field("ST_X(ST_LineInterpolatePoint(geom, {0}/ST_Length(geom::geography)))", dist);
    }

    /**
     * Returns y coordinate of a point along a line, based on the length of the line and the distance between the
     * starting point of the line and the point.
     * Note: "geom" refers to the geometry of the line for which the query is being asked.
     * It is assumed that the geometry column in the data base is labeled "geom".
     *
     * @param dist   Distance between starting point and point in meters as integer value
     * @return Field for JOOQ select statement
     */
    public static Field<?> st_LineInterpolatePointY(int dist) {
        return DSL.field("ST_Y(ST_LineInterpolatePoint(geom, {0}/ST_Length(geom::geography)))", dist);
    }
}
