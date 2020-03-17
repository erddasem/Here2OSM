import DataBase.TestGetData;
import openlr.PhysicalFormatException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Here2Osm {
    // mainMethode
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, PhysicalFormatException {
        // ausführen aller relevanter Methoden zum erhalten des Ergebnisses
        /*HereApi.ApiRequest request = new ApiRequest();
        request.sendRequest("incidents");
        String answer = request.getAnswer();
        XMLParser parser = new XMLParser();
        parser.parseIncidents(answer);
        parser.printTrafficItemsList();*/

        //TestGetData test = new TestGetData();
        //test.getData();

        XMLParser parser = new XMLParser();
        parser.parseXMlFromFile("/Users/emilykast/Desktop/CarolaOhneOpenLRCodeTest.xml");

        //OpenLRDecoder_h2o decoder = new OpenLRDecoder_h2o();
        //decoder.binary2array();

    }
}
