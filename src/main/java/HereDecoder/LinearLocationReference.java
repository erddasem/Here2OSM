package HereDecoder;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class LinearLocationReference extends BaseLocationReference {

    // Required
    public FirstReferencePoint first;
    public LastReferencePoint last;
    //[0..*]
    public List<IntermediateReferencePoint> intermediates;
    // [0..1]
    public int posOf;
    public int negOff;
    public List<Geoposition> geometry;
    public List<String> links;
    public OpenLocationReference.OLRType type;

    public LinearLocationReference() {
        //intermediates = new List<IntermediateReferencePoint>();
    }

    public static int decode_relative(byte[] buf) {
        // Read first 2 bytes for longitude
        short upperVal = (short) (buf[0] << 0);
        short lowerVal = buf[1];
        int relativeVal = upperVal | lowerVal;

        return relativeVal;
    }

    public static byte[] encode_relative(int relativeVal) {
        byte upperVal = (byte) ((relativeVal >> 8) & 0xff);
        byte lowerVal = (byte) (relativeVal & 0xff);
        return new byte[]{upperVal, lowerVal};
    }

    public FirstReferencePoint getFirst() {
        return first;
    }

    public void setFirst(FirstReferencePoint first) {
        this.first = first;
    }

    public LastReferencePoint getLast() {
        return last;
    }

    public void setLast(LastReferencePoint last) {
        this.last = last;
    }

    //Custom
    // public boolean isValid;

    public List<IntermediateReferencePoint> getIntermediates() {
        return intermediates;
    }

    public void setIntermediates(List<IntermediateReferencePoint> intermediates) {
        this.intermediates = intermediates;
    }

    public int getPosOf() {
        return posOf;
    }

    public void setPosOf(int posOf) {
        this.posOf = posOf;
    }

    public int getNegOff() {
        return negOff;
    }

    public void setNegOff(int negOff) {
        this.negOff = negOff;
    }

    public List<Geoposition> getGeometry() {
        return geometry;
    }

    private void setGeometry(List<Geoposition> geometry) {
        this.geometry = geometry;
    }

    public List<String> getLinks() {
        return links;
    }

    private void setLinks(List<String> links) {
        this.links = links;
    }

    public OpenLocationReference.OLRType getType() {
        return OpenLocationReference.OLRType.Linear;
    }

    public int fcFromIndex(int index) {
        if (index > intermediates.size() + 1)
            return -1;

        if (index == 0)
            return first.lineProperties.frc;
        else {

            return intermediates.get(index - 1).getPathProperties().lfrcnp;
        }
    }

    public byte[] encode() {
        byte[] selector = buildSelector();
        ByteArrayOutputStream buf = new ByteArrayOutputStream();

        try {
            buf.write(first.encode());
            buf.write(last.encode(first.coordinate));
            buf.write(selector);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (intermediates.size() > 0) {
            try {
                buf.write(IntUnLoMb.encode(intermediates.size()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < intermediates.size(); i++) {
                IntermediateReferencePoint intern = intermediates.get(i);
                Geoposition prev = i == 0 ? first.coordinate : intermediates.get(i - 1).coordinate;
                try {
                    buf.write(intern.encode(prev));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

           /* if(posOf != null) {
                try {
                    buf.write(IntUnLoMb.encode(posOf));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(negOff == null) {
                try {
                    buf.write(IntUnLoMb.encode(negOff));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        }

        return buf.toByteArray();
    }

    private byte[] buildSelector() {
        byte selector = 0x00;
        final byte bitForIntermediates = 0x40;
        final byte bitForPosOffset = 0x20;
        final byte bitForNegOffset = 0x10;

        if (intermediates.size() > 0)
            selector |= bitForIntermediates;
        /*if(posOf != null)
            selector |= bitForPosOffset;
        if(negOff != null)
            selector |= bitForNegOffset;*/
        return new byte[]{selector};
    }

    public int decode(byte[] buff) {
        int totalOriginalBytes = buff.length;
        int totalBytesRead = 0;

        List<Geoposition> geopoints = new ArrayList<>();

        first = new FirstReferencePoint();
        totalBytesRead += first.decode(buff);
        Geoposition currentGeoPoint = first.getCoordinate();
        geopoints.add(currentGeoPoint);

        isValid = first.isValid();

        last = new LastReferencePoint();
        int lastBytesRead = totalBytesRead;

        totalBytesRead += last.decode(Arrays.copyOfRange(buff, totalBytesRead, buff.length), currentGeoPoint);

        isValid = first.isValid();

        if (isValid) {
            byte selector = Arrays.copyOfRange(buff, totalBytesRead, buff.length)[0];
            totalBytesRead++;
            final byte bitForIntermediates = 0x40;
            if ((selector & bitForIntermediates) == bitForIntermediates) {
                IntUnLoMb numIntermediates = new IntUnLoMb();
                totalBytesRead += numIntermediates.decode(Arrays.copyOfRange(buff, totalBytesRead, buff.length));

                int num_intermediates = numIntermediates.getValue();

                for (int i = 0; i < num_intermediates; i++) {
                    IntermediateReferencePoint interm = new IntermediateReferencePoint();
                    totalBytesRead += interm.decode(Arrays.copyOfRange(buff, totalBytesRead, buff.length), currentGeoPoint);
                    intermediates.add(interm);
                    currentGeoPoint = interm.coordinate;
                    geopoints.add(currentGeoPoint);
                    if (isValid) {
                        isValid = interm.isValid;

                    } else {
                        break;
                    }
                }
            }

            last.decode(Arrays.copyOfRange(buff, lastBytesRead, buff.length), currentGeoPoint);

            geopoints.add(last.coordinate);
            geometry = geopoints;

            final byte bitForPositiveOffset = 0x20;
            if ((selector & bitForPositiveOffset) == bitForPositiveOffset) {
                IntUnLoMb offset = new IntUnLoMb();
                totalBytesRead += offset.decode(Arrays.copyOfRange(buff, totalBytesRead, buff.length));
                posOf = offset.value;
            }

            final byte bitForNegativeOffset = 0x10;
            if ((selector & bitForNegativeOffset) == bitForNegativeOffset) {
                IntUnLoMb offset = new IntUnLoMb();
                totalBytesRead += offset.decode(Arrays.copyOfRange(buff, totalBytesRead, buff.length));
                negOff = offset.value;
            }
        }

        if (totalBytesRead != totalOriginalBytes)
            isValid = false;

        return totalBytesRead;
    }
}
