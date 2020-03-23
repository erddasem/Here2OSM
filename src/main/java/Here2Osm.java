import DataBase.TestGetData;
import HereApi.CollectData;
import HereApi.TrafficItem;
import HereApi.XMLParser;
import OpenLR.OpenLRDecoder_h2o;
import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Here2Osm {
    // mainMethode
    public static void main(String[] args) throws Exception {
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
        //parser.parseXMlFromFile("/Users/emilykast/Desktop/CarolaOhneOpenLRCodeTest.xml");
        parser.parseXMlFromFile("/Users/emilykast/Desktop/CarolaTestXml.xml");
        //OpenLRDecoder_h2o decoder = new OpenLRDecoder_h2o();
        //ByteArray byteArray = decoder.openLR2byteArray("CwnGsiRN4Qo/CP+VAbIKbzIY");
        //decoder.decode(byteArray);

        CollectData collection = new CollectData();
        collection.collectInformation(parser.trafficItemList);


    }
}
