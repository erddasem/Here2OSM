package DataBase;

import HereApi.TrafficItem;

import java.util.ArrayList;
import java.util.List;

public class Incident {

    public static List<Incident> incidentList = new ArrayList<>();

    private String incidentId;
    private String type;
    private String status;
    private String start;
    private String end;
    private String openLRCode;
    private String shortDesc;
    private String longDesc;
    private boolean roadClosure;
    private String affectedLines;
    private int posOff;
    private int negOff;

    public Incident(String incidentId, String type, String status, String start, String end, String openLRCode, String shortDesc, String longDesc, boolean roadClosure, String affectedLines, int posOff, int negOff) {
        this.incidentId = incidentId;
        this.type = type;
        this.status = status;
        this.start = start;
        this.end = end;
        this.openLRCode = openLRCode;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.roadClosure = roadClosure;
        this.affectedLines = affectedLines;
        this.posOff = posOff;
        this.negOff = negOff;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getOpenLRCode() {
        return openLRCode;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public boolean isRoadClosure() {
        return roadClosure;
    }

    public String getAffectedLines() {
        return affectedLines;
    }

    public int getPosOff() {
        return posOff;
    }

    public int getNegOff() {
        return negOff;
    }
}
