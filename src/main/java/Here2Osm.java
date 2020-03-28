import DataBase.CollectData;
import HereApi.ApiRequest;
import HereApi.XMLParser;

public class Here2Osm {
    // mainMethode
    public static void main(String[] args) throws Exception {
        // ausführen aller relevanter Methoden zum erhalten des Ergebnisses
        /*HereApi.ApiRequest request = new ApiRequest();
        request.sendRequest("incidents");
        String answer = request.getAnswer();
        XMLParser parser = new XMLParser();
        parser.parseXMLFromApi(answer);
*/
        XMLParser parser = new XMLParser();
        //parser.parseXMlFromFile("/Users/emilykast/Desktop/CarolaOhneOpenLRCodeTest.xml");
        parser.parseXMlFromFile("/Users/emilykast/Desktop/CarolaTestXml.xml");

        CollectData collection = new CollectData();
        collection.collectInformation(CollectData.trafficItemList);


    }
}
