package HereDecoder;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class ComponentHeader {

    private int gcId;
    private int lengthCompCH;
    private int lengthAttrCH;
    private boolean isValid;
    private int totalLength;
    private int bytesToRead;

    public static byte[] encode(int gcId, int lengthCompValue, int lengthAttrValue) {
        return new byte[]{(byte) gcId, (byte) lengthCompValue, (byte) lengthAttrValue};
    }

    public int getBytesToRead() {
        return lengthCompCH + lengthAttrCH + 2;
    }

    public int getTotalLength() {
        return lengthCompCH + lengthAttrCH - 1;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public int getLengthAttrCH() {
        return lengthAttrCH;
    }

    public void setLengthAttrCH(int lengthAttrCH) {
        this.lengthAttrCH = lengthAttrCH;
    }

    public int getLengthCompCH() {
        return lengthCompCH;
    }

    public void setLengthCompCH(int lengthCompCH) {
        this.lengthCompCH = lengthCompCH;
    }

    public int getGcId() {
        return gcId;
    }

    public void setGcId(int gcId) {
        this.gcId = gcId;
    }

    public int decode(byte[] bytes) {
        IntUnLoMb lengthComp = new IntUnLoMb();
        IntUnLoMb lengthAttr = new IntUnLoMb();
        int totalBytesRead = 0;

        if (bytes.length < 2) {
            return 0;
        }

        //IntUnTi one byte
        gcId = bytes[0];
        totalBytesRead++;

        //IntUnLoMb one byte
        totalBytesRead += lengthAttr.decode(Arrays.copyOfRange(bytes, totalBytesRead - 1, bytes.length - 1));

        isValid = lengthComp.isValid() && lengthAttr.isValid();

        lengthCompCH = lengthComp.getValue();
        lengthAttrCH = lengthAttr.getValue();

        return totalBytesRead;
    }


}
