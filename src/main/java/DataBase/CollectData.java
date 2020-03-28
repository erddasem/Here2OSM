package DataBase;

import HereApi.TrafficItem;
import OpenLR.OpenLRDecoder_h2o;
import openlr.binary.ByteArray;
import openlr.location.Location;
import openlr.map.Line;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CollectData {
    //TODO: Funktion zum sammeln der Daten
    public static List<TrafficItem> trafficItemList = new ArrayList<>();


    public void collectInformation(@NotNull List<TrafficItem> trafficItemList) throws Exception {
        //TDDO: Fall abfangen, wenn traffic item list leer ist
        OpenLRDecoder_h2o decoder = new OpenLRDecoder_h2o();

        for (TrafficItem trafficItemObject : trafficItemList) {

            String incidentId = trafficItemObject.getId();
            String type = trafficItemObject.getType();
            String status = trafficItemObject.getStatus();
            String start = trafficItemObject.getStart();
            String end = trafficItemObject.getEnd();
            String openLRCode = trafficItemObject.getOpenLR();
            String shortDesc = trafficItemObject.getShortDesc();
            String longDesc = trafficItemObject.getLongDesc();
            boolean roadClosure = Boolean.parseBoolean(trafficItemObject.getClosure());

            // Decoding OpenLR Code and extracting location
            ByteArray byteArray = decoder.openLR2byteArray(openLRCode);
            Location location = decoder.decode(byteArray);

            int posOff = location.getPositiveOffset();
            int negOff = location.getNegativeOffset();

            String affectedLines = getAffectedLines(location);

            incident2list(incidentId, type, status, start, end, openLRCode, shortDesc, longDesc, roadClosure, affectedLines, posOff, negOff);
            /*System.out.print(incidentId + "\n" + type + "\n" + status + "\n" + start + "\n" + end + "\n" + openLRCode +
                    "\n" + shortDesc + "\n" + longDesc + "\n" + roadClosure + "\n" + decodedLocation);*/
        }


    }

    private String getAffectedLines(Location location) {
        List<Line> listLines = location.getLocationLines();
        String affectedLines = "";
        for (Line line : listLines)
            affectedLines = new StringBuilder().append(affectedLines).append(" ").append(line.getID()).toString();
        return affectedLines;
    }

    private void incident2list(String incidentId, String type, String status, String start, String end, String openLRCode, String shortDesc, String longDesc, boolean roadClosure, String affectedLines, int posOff, int negOff) {

        Incident incident = new Incident(incidentId, type, status, start, end, openLRCode, shortDesc, longDesc, roadClosure, affectedLines, posOff, negOff);

        Incident.incidentList.add(incident);
    }
}
