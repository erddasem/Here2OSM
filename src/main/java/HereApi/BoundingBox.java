package HereApi;

public class BoundingBox {

    private double leftLat;
    private double leftLon;
    private double rightLat;
    private double rightLon;

    public BoundingBox(double leftLat, double leftLon, double rightLat, double rightLon) {
        this.leftLat = leftLat;
        this.leftLon = leftLon;
        this.rightLat = rightLat;
        this.rightLon = rightLon;
    }

    public double getLeftLat() {
        return leftLat;
    }

    public double getLeftLon() {
        return leftLon;
    }

    public double getRightLat() {
        return rightLat;
    }

    public double getRightLon() {
        return rightLon;
    }

    public String returnBbox() {

        String boundingBox = leftLat + "," + leftLon + "," + rightLat + "," + rightLon;
        return boundingBox;
    }

    public double width() {

        double width = rightLon - leftLon;
        return width;
    }

    public double height() {

        double height = leftLat - rightLat;
        return height;
    }
//"&bbox=51.057,13.744;51.053,13.751";


}
