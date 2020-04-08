package HereApi;

import DataBase.DatasourceConfig;
import Exceptions.InvalidBboxException;
import Exceptions.InvalidWGS84CoordinateException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.min;
import static org.jooq.sources.tables.Incidents.INCIDENTS;
import static org.jooq.sources.tables.Kantenincidents.KANTENINCIDENTS;


public class ApiRequest {

    private URL requestUrl;
    private String answer;

    private List<Incident> incidentList;
    private List<AffectedLine> affectedLinesList;

    public ApiRequest() {
        this.incidentList = new ArrayList<>();
        this.affectedLinesList = new ArrayList<>();
    }

    // needed for SQL queries
    static DSLContext ctx;

    static {
        try {
            ctx = DSL.using(DatasourceConfig.getConnection(), SQLDialect.POSTGRES);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Query to check whether table exists in the database. Returns null if not available, otherwise schema.name.
     *
     * @param schema Name of the schema where table should be available.
     * @param table  Name of the table to be checked.
     * @return Field to use in select query.
     */
    public static Field<?> to_regclass(String schema, String table) {

        String query = "to_regclass('" + schema + "." + table + "')";
        return DSL.field(query);
    }

    /**
     * Prints generated request URL to console.
     */
    public void printUrl() {
        System.out.println(requestUrl);
    }

    /**
     * Sets URL with given bbox.
     *
     * @throws MalformedURLException URL is in the wrong format or an unknown transmission protocol is specified.
     */
    private URL setUrl(String bbox) throws MalformedURLException {
        String baseUrl = "https://traffic.ls.hereapi.com";
        String incidents = "/traffic/6.3/";
        String flow = "/traffic/6.2/";
        String resource = "incidents";
        String format = ".xml";
        String apiKey = "?apiKey=qBXOVr1c_bOSy-NICB9WnOduxAUgxTIF7Tc9svGT1qI";
        String criticality = "&criticality=minor";
        requestUrl = new URL(baseUrl + incidents + resource + format + apiKey + bbox);
        System.out.println(requestUrl);
        return requestUrl;
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

    /**
     * Sends request to HERE API.
     * API returns xml, xml is converted to String.
     *
     * @param bboxString Coordinates for bbox given as String to use in Api Request URL.
     * @return HERE Api answer as String
     * @throws IOException Signals a general input / output error
     */
    private String sendRequest(String bboxString) throws IOException {
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
     * Generates current timestamp
     *
     * @return timestamp
     */
    @NotNull
    @Contract(" -> new")
    private Timestamp getTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    private void checkWGS84validity(double[] coordinatesArray) throws InvalidBboxException, InvalidWGS84CoordinateException {
        if (coordinatesArray.length != 4)
            throw new InvalidBboxException();

        for (int i = 0; i < 4; i++) {
            if (i == 0 || i == 2) {
                boolean validLat = (-180 <= coordinatesArray[i]) && (coordinatesArray[i] <= 180);
                if (!validLat)
                    throw new InvalidWGS84CoordinateException();
            }
            if (i == 1 || i == 3) {
                boolean validLon = (-90 <= coordinatesArray[i]) && (coordinatesArray[i] <= 90);
                if (!validLon)
                    throw new InvalidWGS84CoordinateException();
            }
        }
    }

    /**
     * Method to set bounding box size in terminal window.
     * Bounding box needs to be given in WGS84.
     * Example: 51.057,13.744;51.053,13.751
     */
    public BoundingBox setBoundingBox() throws InvalidBboxException, InvalidWGS84CoordinateException {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

        System.out.println("Geben Sie die Koordinaten für die Bounding Box wie folgt ein (Format WGS84):" +
                "\nUpper Left Lat,Upper Left Lon;Bottom Right Lat,Bottom Right Lon" +
                "\nBeispiel: 51.057,13.744;51.053,13.751 ");
        String bboxString = scanner.next();

        //get coordinates as double values
        Pattern pattern = Pattern.compile("[,;]");

        double[] coordinates = pattern.splitAsStream(bboxString)
                .mapToDouble(Double::parseDouble)
                .toArray();

        checkWGS84validity(coordinates);

        return new BoundingBox(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
    }

    private void getRecursiveBbox(@NotNull BoundingBox bbox) {

        // Recursive query
        if ((bbox.width > 2) || (bbox.height > 2)) {

            // Box upper left
            getRecursiveBbox(new BoundingBox(bbox.getUpperLeftLat(), bbox.getUpperLeftLon(),
                    (bbox.getUpperLeftLat() - (bbox.getHeight() / 2)), (bbox.getUpperLeftLon() + (bbox.getWidth() / 2))));
            // Box upper right
            getRecursiveBbox(new BoundingBox(bbox.getUpperLeftLat(), (bbox.getUpperLeftLon() + (bbox.getWidth() / 2)),
                    (bbox.getUpperLeftLat() - (bbox.getHeight() / 2)), bbox.getBottomRightLon()));
            // Box lower left
            getRecursiveBbox(new BoundingBox((bbox.getUpperLeftLat() - (bbox.getHeight() / 2)),
                    bbox.getUpperLeftLon(), bbox.getBottomRightLat(), (bbox.getUpperLeftLon() - (bbox.getWidth() / 2))));
            // Box lower right
            getRecursiveBbox(new BoundingBox((bbox.getUpperLeftLat() - (bbox.getHeight() / 2)),
                    (bbox.getUpperLeftLon() + (bbox.getWidth() / 2)), bbox.getUpperLeftLat(), bbox.getBottomRightLon()));
        } else {

            //Get Here Api request answer
            String requestAnswer;
            try {
                requestAnswer = sendRequest(bbox.getBboxRequestString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Parse answer or file
            XMLParser parser = new XMLParser();
            //parser.parseXMLFromApi(answer);
            parser.parseXMlFromFile("/Users/emilykast/Desktop/CarolaOhneOpenLRCodeTest.xml");

            // Collect relevant data per incident, decoding
            DataCollector collector = new DataCollector();
            try {
                collector.collectInformation(parser.getListTrafficItems());
                System.out.println(parser.getListTrafficItems());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Collect incident data
            this.incidentList.addAll(collector.getListIncidents());
            this.affectedLinesList.addAll(collector.getListAffectedLines());
        }
    }

    public void updateIncidentData() throws InvalidBboxException, InvalidWGS84CoordinateException {

        // Get current timestamp
        Timestamp currentTimestamp = getTimeStamp();

        // Get recursive bounding boxes if bbox is bigger than 2 degrees
        getRecursiveBbox(setBoundingBox());

        // Begin Transaction
        ctx.transaction(configuration -> {

            // Checks if table incidents is part of the database
            String tableExists = String.valueOf(ctx.select(to_regclass("openlr", "incidents"))
                    .fetchOne().value1());

            // Create incident and foreign key table, if tables are not in database yet
            if (tableExists.equals("null")) {
                // create incidents table
                ctx.createTable("incidents")
                        .column("incident_id", SQLDataType.CHAR(64).nullable(false))
                        .column("incidentType", SQLDataType.CHAR(50))
                        .column("status", SQLDataType.CHAR(50))
                        .column("start_date", SQLDataType.TIMESTAMP)
                        .column("end_date", SQLDataType.TIMESTAMP)
                        .column("openlrcode", SQLDataType.CHAR(100))
                        .column("shortdesc", SQLDataType.CLOB)
                        .column("longdesc", SQLDataType.CLOB)
                        .column("roadclosure", SQLDataType.BOOLEAN)
                        .column("posoff", SQLDataType.INTEGER)
                        .column("negoff", SQLDataType.INTEGER)
                        .column("generationdate", SQLDataType.TIMESTAMP.defaultValue(DSL.field("now()", SQLDataType.TIMESTAMP)))
                        .column("numberaffectedlines", SQLDataType.INTEGER)
                        .constraints(
                                constraint("pk").primaryKey("incident_id")
                        )
                        .execute();

                // Create foreign key table
                ctx.createTable("kantenincidents")
                        .column("incident_id", SQLDataType.CHAR(64).nullable(false))
                        .column("line_id", SQLDataType.INTEGER.nullable(false))
                        .constraints(
                                constraint("incident_id_fk").foreignKey("incident_id")
                                        .references("opnelr.incidents"),
                                constraint("line_id_fk").foreignKey("line_id")
                                        .references("openlr.kanten")
                        )
                        .execute();
            }

            // Get youngest entry in incident table
            Timestamp youngestEntry = ctx.select(min(INCIDENTS.GENERATIONDATE)).from(INCIDENTS).fetchOne().value1();

            if (youngestEntry != null && currentTimestamp.after(youngestEntry)) {

                //truncate data in incident and foreign key table
                ctx.truncate(INCIDENTS).cascade().execute();
                ctx.truncate(KANTENINCIDENTS).execute();
            }

            // Fill incident table with incident data
            for (Incident incident : this.incidentList) {
                ctx.insertInto(INCIDENTS,
                        INCIDENTS.INCIDENT_ID, INCIDENTS.TYPE, INCIDENTS.STATUS, INCIDENTS.START_DATE,
                        INCIDENTS.END_DATE, INCIDENTS.OPENLRCODE, INCIDENTS.SHORTDESC, INCIDENTS.LONGDESC,
                        INCIDENTS.ROADCLOSURE, INCIDENTS.POSOFF, INCIDENTS.NEGOFF)
                        .values(incident.getIncidentId(), incident.getType(), incident.getStatus(), incident.getStart(),
                                incident.getEnd(), incident.getOpenLRCode(), incident.getShortDesc(),
                                incident.getLongDesc(), incident.getRoadClosure(), incident.getPosOff(),
                                incident.getNegOff())
                        .execute();

            }

            // Fill foreign key table
            for (AffectedLine affectedLine : this.affectedLinesList) {
                ctx.insertInto(KANTENINCIDENTS,
                        KANTENINCIDENTS.LINE_ID, KANTENINCIDENTS.INCIDENT_ID,
                        KANTENINCIDENTS.POSOFF, KANTENINCIDENTS.NEGOFF)
                        .values(affectedLine.getLineId(), affectedLine.getIncidentId(),
                                affectedLine.getPosOff(), affectedLine.getNegOff())
                        .execute();
            }

        });
        System.out.println("Programm beenedet");
    }

}
