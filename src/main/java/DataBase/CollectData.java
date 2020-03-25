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

    public void collectInformation(@NotNull List<TrafficItem> trafficItemList) throws Exception {
        OpenLRDecoder_h2o decoder = new OpenLRDecoder_h2o();

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
            Location location = decoder.decode(byteArray);

            posOff = location.getPositiveOffset();
            negOff = location.getNegativeOffset();

            affectedLines = getAffectedLines(location);
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
}
