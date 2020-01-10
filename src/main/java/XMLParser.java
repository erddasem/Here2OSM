import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import org.w3c.dom.*;
import javax.xml.parsers.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class XMLParser {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        List<TrafficItem> items = XMLParser.parseXML("incident");
        System.out.println(items);
        // Anmerkung: Liefert Liste mit Objekten TrafficItem. Jedes Objekt liefert (siehe Klasse TrafficItem)
    }

    public static List<TrafficItem> parseXML(String requestType) throws ParserConfigurationException, SAXException, IOException {

        List<TrafficItem> trafficItems = new ArrayList<>();
        TrafficItem trafficItem = null;

        String xml = ApiRequest.request(requestType);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new InputSource( new StringReader(xml)));
        doc.getDocumentElement().normalize();

        NodeList trfItem = doc.getElementsByTagName("TRAFFIC_ITEM");
        NodeList location = doc.getElementsByTagName("LOCATION");

        for (int temp = 0; temp < trfItem.getLength(); temp++)
        {
            Node node = trfItem.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                trafficItem = new TrafficItem();
                trafficItem.setMid(eElement.getAttribute("mid"));
                trafficItem.setId(eElement.getElementsByTagName("TRAFFIC_ITEM_ID").item(0).getTextContent());
                trafficItem.setType(eElement.getElementsByTagName("TRAFFIC_ITEM_TYPE_DESC").item(0).getTextContent());
                trafficItem.setDesc1(eElement.getElementsByTagName("TRAFFIC_ITEM_DESCRIPTION").item(0).getTextContent());
                trafficItem.setDesc1(eElement.getElementsByTagName("TRAFFIC_ITEM_DESCRIPTION").item(1).getTextContent());
                trafficItem.setDesc1(eElement.getElementsByTagName("TRAFFIC_ITEM_DESCRIPTION").item(2).getTextContent());
            }

            // Annahme: Jedes Traffic Item enthält OpenLR Code
            Node node1 = location.item(temp);
            if (node1.getNodeType() == Node.ELEMENT_NODE)
            {
                Element loc = (Element) node1;
                if (trafficItem != null) {
                    trafficItem.setOpenLR(loc.getElementsByTagName("TPEGOpenLRBase64").item(0).getTextContent());
                }
            }

            trafficItems.add(trafficItem);
        }
        return trafficItems;

    }
}
