package HereDecoder;

import OpenLR.OpenLRMapDatabase_h2o;
import openlr.LocationReferencePoint;
import openlr.Offsets;
import openlr.binary.impl.LocationReferencePointBinaryImpl;
import openlr.binary.impl.OffsetsBinaryImpl;
import openlr.decoder.OpenLRDecoder;
import openlr.decoder.OpenLRDecoderParameter;
import openlr.location.Location;
import openlr.map.FormOfWay;
import openlr.map.FunctionalRoadClass;
import openlr.map.MapDatabase;
import openlr.properties.OpenLRPropertiesReader;
import openlr.properties.OpenLRPropertyException;
import openlr.rawLocRef.RawLineLocRef;
import openlr.rawLocRef.RawLocationReference;
import org.apache.commons.configuration.FileConfiguration;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DecoderHere {

    private FormOfWay getFOWEnumOpenLR(int fow) {
        return FormOfWay.values()[fow];
    }

    private FunctionalRoadClass getFRCEnumOpenLR(int frc) {
        return FunctionalRoadClass.values()[frc];
    }

    public RawLineLocRef lineLocRefHere(OpenLocationReference olr) {
        try {
            if (!olr.isValid()) {
                System.out.println("Invalid OpenLR Data");
            } else {
                switch (olr.getLocationReference().getType().id) {
                    /*case (OpenLocationReference.OLR_TYPE_GEO_COORDINATE):
                        outputGeolocation((GeoCoordinateLocationReference) olr.getLocationReference());
                        break;*/
                    case OpenLocationReference.OLR_TYPE_LINEAR:
                        LinearLocationReference lr = (LinearLocationReference) olr.getLocationReference();

                        int seqNr = 0;
                        List<LocationReferencePoint> lrps = new ArrayList<>();
                        //TODO: Enum Übersetzung für FRC und FOW (Here Enum switch case TomTom Enum)
                        LocationReferencePointBinaryImpl firstRP = new LocationReferencePointBinaryImpl(
                                seqNr,
                                getFRCEnumOpenLR(lr.first.getLineProperties().frc),
                                getFOWEnumOpenLR(lr.first.getLineProperties().fow_id),
                                lr.first.coordinate.getLongitude(),
                                lr.first.coordinate.getLatitude(),
                                lr.first.lineProperties.bearing,
                                lr.first.pathProperties.dnp,
                                getFRCEnumOpenLR(lr.first.pathProperties.lfrcnp),
                                false);
                        seqNr++;
                        lrps.add(firstRP);

                        boolean empty = (lr.intermediates == null);
                        if (!empty) {
                            for (IntermediateReferencePoint intermediateRP : lr.intermediates) {

                                LocationReferencePointBinaryImpl intermediateLRP = new LocationReferencePointBinaryImpl(
                                        seqNr,
                                        getFRCEnumOpenLR(intermediateRP.getLineProperties().frc),
                                        getFOWEnumOpenLR(intermediateRP.getLineProperties().fow_id),
                                        intermediateRP.coordinate.getLongitude(),
                                        intermediateRP.coordinate.getLatitude(),
                                        intermediateRP.lineProperties.bearing,
                                        intermediateRP.pathProperties.dnp,
                                        getFRCEnumOpenLR(intermediateRP.getPathProperties().lfrcnp),
                                        false);
                                seqNr++;
                            }
                        }

                        LocationReferencePointBinaryImpl lastPoint = new LocationReferencePointBinaryImpl(
                                seqNr,
                                getFRCEnumOpenLR(lr.last.lineProperties.frc),
                                getFOWEnumOpenLR(lr.last.lineProperties.fow_id),
                                lr.last.coordinate.getLongitude(),
                                lr.last.coordinate.getLatitude(),
                                lr.last.lineProperties.bearing,
                                0,
                                getFRCEnumOpenLR(lr.first.pathProperties.lfrcnp),
                                true);
                        lrps.add(lastPoint);
                        Offsets offsets = new OffsetsBinaryImpl(lr.getPosOf(), lr.getNegOff());
                        return new RawLineLocRef("1", lrps, offsets);
                    default:
                        System.out.println("Unsupported OpenLR Type");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid OpenLR String");
        }

        return null;
    }

    public Location decodeHere(String openLRCode) throws Exception {

        OpenLocationReference olr = OpenLocationReference.fromBase64TpegOlr(openLRCode);

        RawLocationReference rawLocationReference = lineLocRefHere(olr);

        // Initialize database
        MapDatabase mapDatabase = new OpenLRMapDatabase_h2o();

        // DecoderHere parameter
        FileConfiguration decoderConfig = OpenLRPropertiesReader.loadPropertiesFromFile(new File("src/main/java/OpenLR-DecoderHere-Properties.xml"));
        OpenLRDecoderParameter params = new OpenLRDecoderParameter.Builder().with(mapDatabase).with(decoderConfig).buildParameter();

        //Initialize the decoder
        OpenLRDecoder decoder = new openlr.decoder.OpenLRDecoder();

        //decode the location on own database
        return decoder.decodeRaw(params, rawLocationReference);
    }


}
