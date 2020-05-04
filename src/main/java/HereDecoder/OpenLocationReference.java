package HereDecoder;

import java.util.Arrays;
import java.util.Base64;

public class OpenLocationReference extends BaseLocationReference {

    public String version;
    private String base64 = null;
    private BaseLocationReference locationReference;
    // binary decoding properties
    private int bytesRead;

    public static OpenLocationReference fromBase64TpegOlr(String base64OlrString) {
        byte[] base64EncodedBytes = Base64.getDecoder().decode(new String(base64OlrString));
        OpenLocationReference olr = OpenLocationReference.fromBinary(base64EncodedBytes);
        olr.base64 = base64OlrString;
        return olr;
    }

    public static OpenLocationReference fromBinary(byte[] bytes) {
        OpenLocationReference olr = new OpenLocationReference();
        olr.bytesRead = olr.decode(bytes);

        return olr;
    }

    public static int absGeoEq1(double coordReal) {
        int coordBinary = (int) (Math.signum(coordReal) * 0.5 + ((coordReal * Math.pow(2, 24))) / 360);
        return coordBinary;
    }

    public static double absGeoEq2(int coordBinary) {
        double coordReal = ((coordBinary - (Math.signum(coordBinary) * 0.5) * 360) / Math.pow(2, 24));
        return coordReal;
    }

    public static Geoposition fromAbsoluteCoordinates(int lat, int lon) {

        double latitude = absGeoEq2(lat);
        double longitude = absGeoEq2(lon);
        return new Geoposition(latitude, longitude);
    }

    public static Geoposition fronRelativeCoordinates(int lat, int lon, Geoposition prev) {
        double dlat = lat / 100000.0;
        double dlon = lon / 100000.0;
        return new Geoposition(dlat + prev.getLatitude(), dlon + prev.getLongitude());
    }

    public static Tuple2<Integer, Integer> absoluteToBinaryValue(Geoposition geo) {
        int ilat = absGeoEq1(geo.getLatitude());
        int ilon = absGeoEq1(geo.getLongitude());
        return new Tuple2<Integer, Integer>(ilat, ilon);
    }

    public static Tuple2<Integer, Integer> relativeToBinaryValue(Geoposition current, Geoposition previous) {
        int iRelativeLat = (int) Math.round(100000.0 * (current.getLatitude() - previous.getLatitude()));
        int iRelativeLon = (int) Math.round(100000.0 * (current.getLongitude() - previous.getLongitude()));
        return new Tuple2<Integer, Integer>(iRelativeLat, iRelativeLon);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public BaseLocationReference getLocationReference() {
        return locationReference;
    }

    public void setLocationReference(BaseLocationReference locationReference) {
        this.locationReference = locationReference;
    }

    public int getBytesRead() {
        return bytesRead;
    }

    private void setBytesRead(int bytesRead) {
        this.bytesRead = bytesRead;
    }

    public int decode(byte[] bytes) {
        bytesRead = 0;
        ComponentHeader olrHeader = new ComponentHeader();
        bytesRead += olrHeader.decode(bytes);
        isValid = olrHeader.getIsValid();

        //OPenLocationReference GCID
        if (olrHeader.getGcId() != 0x08) {
            isValid = false;
            return bytesRead;
        }

        int lengthCompSize = IntUnLoMb.encode(olrHeader.getLengthCompCH()).length;
        int totalBytesWillRead = olrHeader.getLengthCompCH() + olrHeader.getLengthAttrCH();
        int totalBytesInBuffer = bytes.length - lengthCompSize;

        if (totalBytesInBuffer != totalBytesWillRead) {
            isValid = false;
        }

        MajorMinorVersion majorMinorVersion = new MajorMinorVersion();
        bytesRead += majorMinorVersion.decode(Arrays.copyOfRange(bytes, bytesRead - 1, bytes.length - 1));
        version = majorMinorVersion.getVersion();

        if (isValid) {
            ComponentHeader locationReferenceHeader = new ComponentHeader();

            bytesRead += locationReferenceHeader.decode(Arrays.copyOfRange(bytes, bytesRead - 1, bytes.length - 1));
            if (locationReferenceHeader.getIsValid() == false) {
                isValid = false;
            }
            if (locationReferenceHeader.getGcId() == OLRType.Linear.id) {
                LinearLocationReference linearlr = new LinearLocationReference();

                int bytesread = linearlr.decode(Arrays.copyOfRange(bytes, bytesRead - 1, bytes.length - 1));

                bytesRead += bytesread;
                locationReference = linearlr;
                origin = linearlr.getLast().coordinate;
                isValid = linearlr.isValid();

            }

            if (locationReferenceHeader.getGcId() == OLRType.GeoCoordinate.id) {
                GeoCoordinateLocationReference geoCoord = new GeoCoordinateLocationReference();
                int byteread = geoCoord.decode(Arrays.copyOfRange(bytes, bytesRead - 1, bytes.length - 1));
                bytesRead += byteread;
                locationReference = geoCoord;
                origin = geoCoord.getCoordinate();
                isValid = true;
            } else {
                isValid = false;
            }
        }
        return bytesRead;
    }

    public enum OLRType {

        Linear(0),
        GeoCoordinate(1),
        PointAlongLine(2),
        Polygon(5),
        Unknown(7);

        public final int id;

        OLRType(int id) {
            this.id = id;
        }
    }

    public static final int OLR_TYPE_LINEAR = 0;
    public static final int OLR_TYPE_GEO_COORDINATE = 1;
}