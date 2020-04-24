package HereApi;

public class BoundingBox {

    double height;
    double width;
    private double upperLeftLat;
    private double upperLeftLon;
    private double bottomRightLat;
    private double bottomRightLon;

    public BoundingBox(double upperLeftLat, double upperLeftLon, double bottomRightLat, double bottomRightLon) {
        this.upperLeftLat = upperLeftLat;
        this.upperLeftLon = upperLeftLon;
        this.bottomRightLat = bottomRightLat;
        this.bottomRightLon = bottomRightLon;
        this.height = upperLeftLat - bottomRightLat;
        this.width = bottomRightLon - upperLeftLon;
    }

    public double getUpperLeftLat() {
        return upperLeftLat;
    }

    public double getUpperLeftLon() {
        return upperLeftLon;
    }

    public double getBottomRightLat() {
        return bottomRightLat;
    }

    public double getBottomRightLon() {
        return bottomRightLon;
    }

    public double getWidth() {

        return width;
    }

    public double getHeight() {

        return height;
    }

    /**
     * Builds String from bounding box information to use in Here Api request
     *
     * @return Bounding Box String to use in Here Api request
     */
    public String getBboxRequestString() {
        return "&bbox=" + upperLeftLat + "," + upperLeftLon + ";" + bottomRightLat + "," + bottomRightLon;
    }


}
