public class TrafficItem {
    private String mid;
    private String id;
    private String type;
    private String openLR;
    private String shortDesc;
    private String longDesc;

    public String getOpenLR() {
        return openLR;
    }

    public void setOpenLR(String openLR) {
        this.openLR = openLR;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc1() {
        return shortDesc;
    }

    public void setDesc1(String desc1) {
        this.shortDesc = desc1;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    @Override
    public String toString()
    {
        return "TrafficItem [mid = " + mid + ", id = " + id +", type = " + type + ", OpenLR = " + openLR
                + ", shortDesc = " + shortDesc + ", longDesc = " + longDesc  + "]";
    }
}
