public class TrafficItem {
    private String mid;
    private String id;
    private String type;
    private String openLR;
    private String desc1;

    public String getOpenLR() {
        return openLR;
    }

    public void setOpenLR(String openLR) {
        this.openLR = openLR;
    }

    private String desc2;
    private String desc3;

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
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }

    @Override
    public String toString()
    {
        return "TrafficItem [mid = " + mid + ", id = " + id +", type = " + type + ", OpenLR = " + openLR
                + ", desc1 = " + desc1 + ", desc2 = " + desc2 + ", desc3 = " + desc3 +"]";
    }
}
