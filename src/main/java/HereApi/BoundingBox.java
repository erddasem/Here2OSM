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

   /* public String returnBbox() {

        String boundingBox = upperLeftLat + "," + upperLeftLon + "," + bottomRightLat + "," + bottomRightLon;
        return boundingBox;
    }*/

    public double getWidth() {

        return width;
    }

    public double getHeight() {

        return height;
    }

    public String getBboxRequestString() {
        return "&bbox=" + upperLeftLat + "," + upperLeftLon + ";" + bottomRightLat + "," + bottomRightLon;
    }
//"&bbox=51.057,13.744;51.053,13.751";


}
