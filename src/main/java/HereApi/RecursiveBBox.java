package HereApi;

import java.awt.*;
import java.util.Locale;
import java.util.Scanner;

public class RecursiveBBox {

    public double[] getBBox() {
        String bboxString;
        double leftLat, leftLon, rightLat, rightLon;
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

        System.out.println("Geben Sie die Bounding Box im Format left Latitude,left Longitude;right Latitude,right Longitude ein:  ");
        bboxString = scanner.next();

        System.out.println("BBox String: " + bboxString);
        //get coordinates
        String[] coordArray = bboxString.split(",|;");
        System.out.println("Länge String Array: " + coordArray.length);
        if (coordArray.length != 4) {
            //TODO: add exception
        }
        double[] coordinates = new double[4];
        for (int i = 0; i < coordArray.length; i++) {
            double coord = Double.valueOf(coordArray[i]);
            coordinates[i] = coord;
        }
        ;
        return coordinates;
    }


    private void getRecrusiveBbox(double[] coordinates) {

    }

    /*private static void queryRectangle(Rectangle rectangle) {
        if (rectangle.width > 2 || rectangle.height > 2) {
            // lower left
            queryRectangle(new Rectangle(rectangle.x, rectangle.y, (int)rectangle.getWidth()/2, (int)rectangle.getHeight()/2));
            // upper left
            queryRectangle(new Rectangle(rectangle.x + (int)rectangle.getWidth()/2, rectangle.y, (int)rectangle.getWidth()/2, (int)rectangle.getHeight()/2));
            // lower right
            queryRectangle(new Rectangle(rectangle.x, rectangle.y + (int)rectangle.getHeight()/2, (int)rectangle.getWidth()/2, (int)rectangle.getHeight()/2));
            // upper right
            queryRectangle(new Rectangle(rectangle.x + (int)rectangle.getWidth()/2, rectangle.y + (int)rectangle.getHeight()/2, (int)rectangle.getWidth()/2, (int)rectangle.getHeight()/2));
        }
        // do query and store to database
    }*/
}
