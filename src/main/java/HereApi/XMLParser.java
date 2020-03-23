package HereApi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {


// File path: "/Users/emilykast/Desktop/CarolaTestXml.xml"

    /**
     * Method to parse XML form given file path.
     *
     * @param path Filepath of the XML file as String
     */
    public void parseXMlFromFile(String path) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            try {
                Document document = builder.parse(new File(path));
                parseXML(document);

            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to parse answer from HereApi request.
     *
     * @param requestAnswer Request answer as String
     */
    public void parseXMLFromApi(String requestAnswer) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            try {
                Document document = builder.parse(new InputSource(new StringReader(requestAnswer)));
                parseXML(document);
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to parse given XML Document.
     * Runs through each traffic item node and checks for OpenLR code, if available relevant information are
     * extracted. Creates a traffic item object and adds it to the list of traffic items.
     *
     * @param document XML Document
     */
    public void parseXML(Document document) {
        //normalize xml document
        document.getDocumentElement().normalize();

        // List containing TRAFFIC_ITEM nodes
        NodeList trfItemList = document.getElementsByTagName("TRAFFIC_ITEM");

        // Run through traffic item node
        for (int node = 0; node < trfItemList.getLength(); node++) {

            boolean hasOpenLRCode = false;
            String tIId = null;
            String tIStatus = null;
            String tIType = null;
            String tIStart = null;
            String tIEnd = null;
            String tIOpenLR = null;
            String tIClosure = null;
            String tIShortDesc = null;
            String tILongDesc = null;

            Node trfItemNode = trfItemList.item(node);

            // Get children from specific traffic item (given by trfItemList.item(node)
            NodeList trfItemChildNodesList = trfItemNode.getChildNodes();

            //Find child node "LOCATION" to check for OpenLR Code
            for (int i = 0; i < trfItemChildNodesList.getLength(); i++) {
                if (trfItemChildNodesList.item(i).getNodeName().equals("LOCATION")) {
                    // Create node location
                    Node locationNode = trfItemChildNodesList.item(i);
                    // Create list containing all child nodes of location
                    NodeList locationChildNodesList = locationNode.getChildNodes();
                    // Find child node "TPEGOpenLRBase64"
                    for (int j = 0; j < locationChildNodesList.getLength(); j++) {
                        //check for OpenLR code tag, if not available, no traffic item object will be created
                        if (locationChildNodesList.item(j).getNodeName().equals("TPEGOpenLRBase64") && locationChildNodesList.item(j).getTextContent() != null) {
                            hasOpenLRCode = true;
                            // Get OpenLR Code
                            tIOpenLR = locationChildNodesList.item(j).getTextContent().replaceAll("\n", "");
                            // get information from different nodes
                            if (trfItemNode.getNodeType() == Node.ELEMENT_NODE) {
                                // Cast Node to Element to get elements by tag name
                                Element trfItemElement = (Element) trfItemNode;
                                //Reading out information from trafficItem Node
                                tIId = trfItemElement.getElementsByTagName("TRAFFIC_ITEM_ID").item(0).getTextContent();
                                tIStatus = trfItemElement.getElementsByTagName("TRAFFIC_ITEM_STATUS_SHORT_DESC").item(0).getTextContent();
                                tIType = trfItemElement.getElementsByTagName("TRAFFIC_ITEM_TYPE_DESC").item(0).getTextContent();
                                tIStart = trfItemElement.getElementsByTagName("START_TIME").item(0).getTextContent();
                                tIEnd = trfItemElement.getElementsByTagName("END_TIME").item(0).getTextContent();
                                // get different descriptions, same node name, different types
                                NodeList trfItemDescList = trfItemElement.getElementsByTagName("TRAFFIC_ITEM_DESCRIPTION");
                                tIShortDesc = trfItemDescList.item(0).getTextContent().replaceAll("\n", "");
                                tILongDesc = trfItemDescList.item(1).getTextContent().replaceAll("\n", "");
                            }
                        } else {
                            hasOpenLRCode = false;
                        }
                    }
                }
                // If OpenLR Code is available get node TRAFFIC_ITEM_DETAIL
                if (hasOpenLRCode) {
                    if (trfItemChildNodesList.item(i).getNodeName().equals("TRAFFIC_ITEM_DETAIL")) {
                        Node trfItemDetailNode = trfItemChildNodesList.item(i);
                        if (trfItemDetailNode.getNodeType() == Node.ELEMENT_NODE) {
                            // Cast TRAFFIC_ITEM_DETAIL node to element
                            Element trfItemDetailElement = (Element) trfItemDetailNode;
                            //Get information if road is closed
                            tIClosure = trfItemDetailElement.getElementsByTagName("ROAD_CLOSED").item(0).getTextContent();

                        }
                    }
                }
            }

            if (tIId != null)
                // Generate traffic item object and add to list of traffic items
                trafficItemToList(tIId, tIStatus, tIType, tIStart, tIEnd, tIOpenLR, tIClosure, tIShortDesc, tILongDesc);
        }
    }

    /**
     * Generates traffic item object and adds it to the list of traffic items.
     *
     * @param id        Traffic item id
     * @param status    Status of the traffic item
     * @param type      Type of traffic item
     * @param start     Start time of the traffic item
     * @param end       End time of the traffic item
     * @param openLR    OpenLR Code of the traffic item
     * @param closure   Information whether the street is closed
     * @param shortDesc Brief description of the traffic item
     * @param longDesc  Detailed description of the traffic item
     */
    private void trafficItemToList(String id, String status, String type, String start, String end, String openLR,
                                   String closure, String shortDesc, String longDesc) {
        // generate traffic Item
        TrafficItem trafficItem = new TrafficItem(id, status, type, start, end, openLR, closure, shortDesc, longDesc);

        // add TrafficItem to TrafficItemList
        CollectData.trafficItemList.add(trafficItem);
    }

}
