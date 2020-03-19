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
 
   @Override
    public String toString() {
        return "HereApi.TrafficItem [id = " + id + ", status = " + status + ", type = " + type + ", start = " + start +
                ", end = " + end + ", OpenLR = " + openLR + ", closure = " + closure
                + ", shortDesc = " + shortDesc + ", longDesc = " + longDesc + "]";
    }

    /**
     * Returns list of traffic items.
     *
     * @return List of traffic items
     */

    public List<TrafficItem> getTrafficItemList() {
        return trafficItemList;
    }
}
