package HereApi;

import HereApi.TrafficItem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Incident {

    private String incidentId;
    private String type;
    private String status;
    private Timestamp start;
    private Timestamp end;
    private String openLRCode;
    private String shortDesc;
    private String longDesc;
    private boolean roadClosure;
    private int posOff;
    private int negOff;

    public Incident(String incidentId, String type, String status, Timestamp start, Timestamp end, String openLRCode, String shortDesc, String longDesc, boolean roadClosure, int posOff, int negOff) {
        this.incidentId = incidentId;
        this.type = type;
        this.status = status;
        this.start = start;
        this.end = end;
        this.openLRCode = openLRCode;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.roadClosure = roadClosure;
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

    public Timestamp getStart() {
        return start;
    }

    public Timestamp getEnd() {
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

    public boolean getRoadClosure() {
        return roadClosure;
    }

    public int getPosOff() {
        return posOff;
    }

    public int getNegOff() {
        return negOff;
    }

}