package HereApi;

import OpenLR.OpenLRDecoder_h2o;
import openlr.binary.ByteArray;
import openlr.location.Location;
import openlr.map.Line;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DataCollector {


    private List<Incident> listIncidents;
    private List<AffectedLine> listAffectedLines;

    public DataCollector() {

        this.listIncidents = new ArrayList<>();
        this.listAffectedLines = new ArrayList<>();
    }

    public List<Incident> getListIncidents() {
        return listIncidents;
    }

    public List<AffectedLine> getListAffectedLines() {
        return listAffectedLines;
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

            incident2list(incidentId, type, status, start, end, openLRCode, shortDesc, longDesc, roadClosure, posOff, negOff);

            getAffectedLines(location, incidentId, posOff, negOff);
        }
    }

    private void getAffectedLines(Location location, String incidentId, int posOff, int negOff) {
        // decode location, extract list of affected lines
        List<Line> listLines = location.getLocationLines();
        AffectedLine affectedLine = null;

        if (listLines != null && !listLines.isEmpty()) {
            for (int i = 0; i < listLines.size(); i++) {
                if (i == 0) {
                    affectedLine = new AffectedLine(listLines.get(i).getID(), incidentId);
                    affectedLine.setPosOff(posOff);
                }
                if (i == listLines.size() - 1) {
                    affectedLine = new AffectedLine(listLines.get(i).getID(), incidentId);
                    affectedLine.setNegOff(negOff);
                }
                if (i != 0 && (i != listLines.size() - 1)) {
                    affectedLine = new AffectedLine(listLines.get(i).getID(), incidentId);
                }
                this.listAffectedLines.add(affectedLine);
            }
        }
    }

    private void incident2list(String incidentId, String type, String status, String start, String end, String openLRCode, String shortDesc, String longDesc, boolean roadClosure, int posOff, int negOff) {
        Incident incident = new Incident(incidentId, type, status, start, end, openLRCode, shortDesc, longDesc, roadClosure, posOff, negOff);
        this.listIncidents.add(incident);
    }

    private void affectedLine2List(long lineId, String incidentId) {
        AffectedLine affectedLine = new AffectedLine(lineId, incidentId);
        this.listAffectedLines.add(affectedLine);
    }
}
