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

    // Klassenattribut List of TrafficItems
    private static List<TrafficItem> trafficItems = new ArrayList<>();

    /**
     * Parses incidents request XML
     * @param requestAnswer XML as String
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void parseIncidents(String requestAnswer) throws ParserConfigurationException, IOException, SAXException {
        //List<TrafficItem> trafficItems = new ArrayList<>();
        TrafficItem trafficItem = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new InputSource( new StringReader(requestAnswer)));
        doc.getDocumentElement().normalize();

        // Node TRAFFIC_ITEM, can be more than one traffic item in the answer
        NodeList trfItem = doc.getElementsByTagName("TRAFFIC_ITEM");
        System.out.println(trfItem.getLength());
        // Node LOCATION contains OpenLRBase64 Code
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
                trafficItem.setShortDesc(eElement.getElementsByTagName("TRAFFIC_ITEM_DESCRIPTION").item(0).getTextContent());
            }

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
    }

    /**
     * Prints list of traffic items to console.
     */
    public void printTrafficItemsList() {
        System.out.println(trafficItems);
    }

}
