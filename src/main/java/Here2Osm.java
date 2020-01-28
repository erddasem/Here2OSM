import openlr.PhysicalFormatException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Here2Osm {
    // mainMethode
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, PhysicalFormatException {
        // ausführen aller relevanter Methoden zum erhalten des Ergebnisses
        ApiRequest request = new ApiRequest();
        request.sendRequest("incidents");
        String answer = request.getAnswer();
        XMLParser parser = new XMLParser();
        parser.parseIncidents(answer);
        parser.printTrafficItemsList();
        OpenLRDecoder decoder = new OpenLRDecoder();
        decoder.binary2array();
        DatabaseConnection dbConn = new DatabaseConnection();
        dbConn.connectDB();

        }
}
