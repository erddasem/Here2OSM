package DataBase;

import HereApi.TrafficItem;
import OpenLR.OpenLRDecoder_h2o;
import openlr.binary.ByteArray;
import openlr.location.Location;
import openlr.map.Line;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DataCollector {


    private List<Incident> listIncidents;

    public DataCollector() {
        this.listIncidents = new ArrayList<>();
    }

    public List<Incident> getListIncidents() {
        return listIncidents;
    }

    public void collectInformation(@NotNull List<TrafficItem> trafficItemList) throws Exception {
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
        for (Line line : listLines) {
            affectedLines = affectedLines + " " + line.getID();
        }
        return affectedLines;
    }

    private void incident2list(String incidentId, String type, String status, String start, String end, String openLRCode, String shortDesc, String longDesc, boolean roadClosure, String affectedLines, int posOff, int negOff) {

        Incident incident = new Incident(incidentId, type, status, start, end, openLRCode, shortDesc, longDesc, roadClosure, affectedLines, posOff, negOff);

        this.listIncidents.add(incident);
    }
}
