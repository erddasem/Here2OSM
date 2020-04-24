package HereApi;

public class AffectedLine {

    private long lineId;
    private String incidentId;
    private int posOff;
    private int negOff;

    public AffectedLine(long lineId, String incidentId) {
        this.lineId = lineId;
        this.incidentId = incidentId;
    }

    public long getLineId() {
        return lineId;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public int getPosOff() {
        return posOff;
    }

    public void setPosOff(int posOff) {
        this.posOff = posOff;
    }

    public int getNegOff() {
        return negOff;
    }

    public void setNegOff(int negOff) {
        this.negOff = negOff;
    }
}
