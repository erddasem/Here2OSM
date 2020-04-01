package HereApi;

import java.util.Locale;
import java.util.Scanner;

public class recursiveBBox {

    private static void queryBBox() {


    }

    private void getBBox() {
        double leftLat, leftLon, rightLat, rightLon;
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

        System.out.println("Geben Sie das abzufragende Gebiet als Bounding box ein.");
        System.out.print("Geben sie  ");
        leftLat = scanner.nextDouble();
        System.out.print("Enter the fuel efficiency: ");
        leftLon = scanner.nextDouble();
        System.out.print("Enter the fuel efficiency: ");
        rightLat = scanner.nextDouble();
        System.out.print("Enter the fuel efficiency: ");
        rightLon = scanner.nextDouble();
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
