import DataBase.CollectData;
import HereApi.XMLParser;

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
        /*OpenLRDecoder_h2o decoder = new OpenLRDecoder_h2o();
        ByteArray byteArray = decoder.openLR2byteArray("CwnGsiRN4Qo/CP+VAbIKbzIY");
        decoder.decode(byteArray);*/

        CollectData collection = new CollectData();
        collection.collectInformation(CollectData.trafficItemList);


    }
}
