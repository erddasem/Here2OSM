package HereApi;

import OpenLR.OpenLRDecoder_h2o;
import openlr.binary.ByteArray;

import java.util.ArrayList;
import java.util.List;

public class CollectData {
    //TODO: Funktion zum sammeln der Daten
    public static List<TrafficItem> trafficItemList = new ArrayList<>();

    public void collectInformation(List<TrafficItem> trafficItemList) throws Exception {

        String incidentId;
        String type;
        String status;
        String start;
        String end;
        String openLRCode;
        String shortDesc;
        String longDesc;
        boolean roadClosure;
        String affectedLines;
        int posOff;
        int negOff;

        OpenLRDecoder_h2o decoder = new OpenLRDecoder_h2o();

        System.out.println(trafficItemList.size());

        for (TrafficItem trafficItemObject : trafficItemList) {

            incidentId = trafficItemObject.getId();
            type = trafficItemObject.getType();
            status = trafficItemObject.getStatus();
            start = trafficItemObject.getStart();
            end = trafficItemObject.getEnd();
            openLRCode = trafficItemObject.getOpenLR();
            shortDesc = trafficItemObject.getShortDesc();
            longDesc = trafficItemObject.getLongDesc();
            roadClosure = Boolean.parseBoolean(trafficItemObject.getClosure());

            ByteArray byteArray = decoder.openLR2byteArray(openLRCode);
            String decodedLocation = decoder.decode(byteArray);

            System.out.print(incidentId + "\n" + type + "\n" + status + "\n" + start + "\n" + end + "\n" + openLRCode +
                    "\n" + shortDesc + "\n" + longDesc + "\n" + roadClosure + "\n" + decodedLocation);
        }


    }
}
