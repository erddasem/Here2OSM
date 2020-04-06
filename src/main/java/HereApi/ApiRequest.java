package HereApi;

import DataBase.DataCollector;
import DataBase.Incident;
import openlr.map.Line;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ApiRequest {

    // Klassenattribute:
    private static String baseUrl = "https://traffic.ls.hereapi.com";
    private static String apiKey = "?apiKey=qBXOVr1c_bOSy-NICB9WnOduxAUgxTIF7Tc9svGT1qI";
    private static String resource = "incidents";
    // Bounding Box: Theodor Heuss Brücke
    private static String criticality = "&criticality=minor";
    private static String incidents = "/traffic/6.3/";
    private static String flow = "/traffic/6.2/";
    // Bounding Box: Carola Brücke DD
    //private String bbox = "&bbox=51.057,13.744;51.053,13.751";
    private String bbox;
    private static String format = ".xml";
    private URL requestUrl;
    private String answer;

    private List<Incident> incidentList;
    private List<Line> affectedLinesList;

    public ApiRequest() {
        this.incidentList = new ArrayList<>();
        this.affectedLinesList = new ArrayList<>();
    }

    /**
     * Sets URL with given bbox.
     *
     * @throws MalformedURLException URL is in the wrong format or an unknown transmission protocol is specified.
     */
    private URL setUrl(String bbox) throws MalformedURLException {
        requestUrl = new URL(baseUrl + incidents + resource + format + apiKey + bbox + criticality);
        return requestUrl;
    }

    /**
     * Prints generated request URL to console.
     */
    public void printUrl() {
        System.out.println(requestUrl);
    }

    /**
     * Sends request to HERE API.
     * API returns xml, xml is converted to String.
     *
     * @param bboxString Coordinates for bbox given as String to use in Api Request URL.
     * @return HERE Api answer as String
     * @throws IOException Signals a general input / output error
     */
    public String sendRequest(String bboxString) throws IOException {
        setUrl(bboxString);
        printUrl();

        URL request = requestUrl;
        HttpURLConnection con = (HttpURLConnection) request.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/xml");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String readLine;
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            answer = response.toString();

        } else {
            System.out.println("GET Request failed.");
        }
        return answer;
    }

    /**
     * Returns XML as String.
     *
     * @return HERE Api Answer as String
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Prints API answer to console.
     */
    public void printAnswer() {
        System.out.println(answer);
    }

    public void setBoundingBox() {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

        System.out.println("Geben Sie die Koordinaten für die Bounding Box wie folgt ein (Format WGS84):" +
                "\nUpper Left Lat,Upper Left Lon;Bottom Right Lat,Bottom Right Lon" +
                "\nBeispiel: 51.057,13.744;51.053,13.751 ");
        String bboxString = scanner.next();

        //get coordinates as double values
        String[] coordArray = bboxString.split("[,;]");

        if (coordArray.length != 4) {
            //TODO: add exception
        }
        double[] coordinates = new double[4];

        BoundingBox bbox = new BoundingBox(Double.parseDouble(coordArray[0]), Double.parseDouble(coordArray[1]),
                Double.parseDouble(coordArray[2]), Double.parseDouble(coordArray[3]));

        try {
            getRecursiveBbox(bbox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRecursiveBbox(BoundingBox bbox) throws IOException {

        // Rekursive Abfrage
        if ((bbox.width > 2) || (bbox.height > 2)) {

            // Box upper left
            getRecursiveBbox(new BoundingBox(bbox.getUpperLeftLat(), bbox.getUpperLeftLon(), (bbox.getUpperLeftLat() - (bbox.getHeight() / 2)), (bbox.getUpperLeftLon() + (bbox.getWidth() / 2))));
            // Box upper right
            getRecursiveBbox(new BoundingBox(bbox.getUpperLeftLat(), (bbox.getUpperLeftLon() + (bbox.getWidth() / 2)), (bbox.getUpperLeftLat() - (bbox.getHeight() / 2)), bbox.getBottomRightLon()));
            // Box lower left
            getRecursiveBbox(new BoundingBox((bbox.getUpperLeftLat() - (bbox.getHeight() / 2)), bbox.getUpperLeftLon(), bbox.getBottomRightLat(), (bbox.getUpperLeftLon() - (bbox.getWidth() / 2))));
            // Box lower right
            getRecursiveBbox(new BoundingBox((bbox.getUpperLeftLat() - (bbox.getHeight() / 2)), (bbox.getUpperLeftLon() + (bbox.getWidth() / 2)), bbox.getUpperLeftLat(), bbox.getBottomRightLon()));
        } else {

            //String requestAnswer;
            //requestAnswer = sendRequest(bbox.getBboxRequestString());
            XMLParser parser = new XMLParser();
            //parser.parseXMLFromApi(answer);
            parser.parseXMlFromFile("/Users/emilykast/Desktop/CarolaTestXml.xml");


            // send request aufrufen + parsexml +  collectData
            //this.incidentList.addAll(DataCollector.getListIncidents());
            //this.affectedLinesList.add(DataCollector.getAffectedLinesList());
        }
    }

    public void updateIncidentData(BoundingBox bbox) {
        //getRecursiveBbox(bbox);

        //for each Schleife, für jedes Incidet insert mit Jooq
        // for each über all Line insert in Kreuztabelle LineID IncidentID
        // löschen alte Incidents Tabelle + umbennen temp. Tabelle in incidents Tabelle (in einer Transaktion)
    }
}
