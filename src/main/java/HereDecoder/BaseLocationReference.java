package HereDecoder;

import java.util.Arrays;

public class BaseLocationReference {
    public boolean isValid;
    public double offset;
    public Geoposition origin;
    public OpenLocationReference.OLRType type;

    public void baseLocationReference() {

    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public Geoposition getOrigin() {
        return origin;
    }

    public void setOrigin(Geoposition origin) {
        this.origin = origin;
    }

    public OpenLocationReference.OLRType getType() {
        return OpenLocationReference.OLRType.valueOf("Unknown");
    }


    public BaseLocationReference fromBinary(byte[] bytes, int bytesRead) {
        //Check if Buffer is valid
        if (!isBufferValid(bytes)) {
            bytesRead = 0;
            return this;
        }

        BaseLocationReference retVal = new BaseLocationReference();
        int totalBytesRead = 0;

        ComponentHeader olrHeader = new ComponentHeader();

        totalBytesRead += olrHeader.decode(bytes);

        int take = olrHeader.getTotalLength();

        ComponentHeader locationTypeHeader = new ComponentHeader();
        locationTypeHeader.decode(Arrays.copyOfRange(bytes, totalBytesRead - 1, bytes.length - 1));

        if (locationTypeHeader.getGcId() == 0x08) {
            OpenLocationReference olr = new OpenLocationReference();
            int bytesReadFrom = olr.decode(Arrays.copyOfRange(bytes, totalBytesRead - 1, take - 1));
            totalBytesRead += olr.getBytesRead();

            if (olr.isValid()) {
                retVal = (BaseLocationReference) olr;
            }
        } else {
            //Undecodable Data
            totalBytesRead += take;
        }

        bytesRead = totalBytesRead;
        return retVal;
    }

    private boolean isBufferValid(byte[] byteBuffer) {
        final int min_size = 3;
        boolean retVal = true;
        int buf_size = byteBuffer.length;
        if (buf_size < min_size) {
            isValid = false;
            retVal = false;
        }
        return retVal;
    }

}