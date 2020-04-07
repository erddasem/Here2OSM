import DataBase.TestGetData;
import HereApi.ApiRequest;

public class Here2Osm {
    // mainMethode
    public static void main(String[] args) throws Exception {

        //TODO: Übergabe BBox über Terminal
        // ausführen aller relevanter Methoden zum erhalten des Ergebnisses
        /*HereApi.ApiRequest request = new ApiRequest();
        request.setBoundingBox();*/
        /*XMLParser parser = new XMLParser();
        parser.parseXMLFromApi(answer);*/

        /*XMLParser parser = new XMLParser();
        //parser.parseXMlFromFile("/Users/emilykast/Desktop/CarolaOhneOpenLRCodeTest.xml");
        parser.parseXMlFromFile("/Users/emilykast/Desktop/CarolaTestXml.xml");

        DataCollector collection = new DataCollector();
        collection.collectInformation(DataCollector.trafficItemList);
*/
        ApiRequest request = new ApiRequest();
        request.updateIncidentData();
    }
}
