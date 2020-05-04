package HereDecoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class LastReferencePoint {

    public Geoposition coordinate;
    public LineProperties lineProperties;
    public boolean isValid;
    private int lat;
    private int lon;

    public int decode(byte[] buff, Geoposition prev) {
        final int sizeOfRealVal = 2;
        int totalBytesRead = 0;

        lon = LinearLocationReference.decode_relative(buff);
        totalBytesRead += sizeOfRealVal;
        lat = LinearLocationReference.decode_relative(Arrays.copyOfRange(buff, totalBytesRead - 1, buff.length - 1));
        totalBytesRead += sizeOfRealVal;

        coordinate = OpenLocationReference.fronRelativeCoordinates(lat, lon, prev);

        FixedBitArray selector = new FixedBitArray();
        totalBytesRead += selector.decode(Arrays.copyOfRange(buff, totalBytesRead - 1, buff.length - 1));

        OlrComponentHeader linePropertiesHeader = new OlrComponentHeader();
        totalBytesRead += selector.decode(Arrays.copyOfRange(buff, totalBytesRead - 1, buff.length - 1));

        isValid = linePropertiesHeader.isValid();

        if (linePropertiesHeader.getGcId() != 0x09)
            isValid = false;

        if (isValid) {
            LineProperties lineproperties = new LineProperties();
            totalBytesRead += selector.decode(Arrays.copyOfRange(buff, totalBytesRead - 1, buff.length - 1));
            lineProperties = lineproperties;
        }

        return totalBytesRead;
    }

    public byte[] encode(Geoposition prev) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();

        byte[] selector = new byte[]{0x00}; // Selector: only 0x00 is supported
        byte[] linearPropertiesHeaderBytes = OlrComponentHeader.encode(0x09, 5, 4);


        try {
            buf.write(LinearLocationReference.encode_relative(lon));
            buf.write(LinearLocationReference.encode_relative(lat));
            buf.write(selector);
            buf.write(lineProperties.encode());
            buf.write(linearPropertiesHeaderBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf.toByteArray();
    }
}