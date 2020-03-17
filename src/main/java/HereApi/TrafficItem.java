package HereApi;

import java.util.ArrayList;
import java.util.List;

public class TrafficItem {
    private String id;
    public static List<TrafficItem> trafficItemList = new ArrayList<>();
    private String type;
    private String status;
    private String start;
    private String openLR;
    private String end;
    private String shortDesc;
    private String longDesc;
    private String closure;

    public TrafficItem(String id, String status, String type, String start, String end, String openLR, String closure, String shortDesc, String longDesc) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.start = start;
        this.end = end;
        this.openLR = openLR;
        this.closure = closure;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getOpenLR() {
        return openLR;
    }

    public void setOpenLR(String openLR) {
        this.openLR = openLR;
    }

    public String getClosure() {
        return closure;
    }

    public void setClosure(String closure) {
        this.closure = closure;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    @Override
    public String toString() {
        return "HereApi.TrafficItem [id = " + id + ", status = " + status + ", type = " + type + ", start = " + start +
                ", end = " + end + ", OpenLR = " + openLR + ", closure = " + closure
                + ", shortDesc = " + shortDesc + ", longDesc = " + longDesc + "]";
    }

    public List<TrafficItem> getTrafficItemList() {
        return trafficItemList;
    }
}
