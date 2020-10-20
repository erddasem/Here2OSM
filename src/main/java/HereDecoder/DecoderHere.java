package HereDecoder;

import Exceptions.InvalidHereOLRException;
import Loader.OSMMapLoader;
import OpenLRImpl.MapDatabaseImpl;
import OpenLR_h2o.OpenLRMapDatabase_h2o;
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
import openlr.rawLocRef.RawLineLocRef;
import openlr.rawLocRef.RawLocationReference;
import org.apache.commons.configuration.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * HERE implementation of the TPEG-OLR standard (ISO/TS 21219-22)
 * Original C# program translated to Java.
 */

public class DecoderHere {

    /**
     * Gets the OpenLR FOW Enum depending on the given FOW integer value
     *
     * @param fow integer value Form Of Way given in the HERE Location
     * @return OpenLR FOW value
     */
    private FormOfWay getFOWEnumOpenLR(int fow) {
        return FormOfWay.values()[fow];
    }

    /**
     * Gets the OpenLR FRC Enum depending on the given FRC integer value.
     *
     * @param frc integer value Functional Road Class given in the HERE Location.
     * @return OpenLR FRC value
     */
    private FunctionalRoadClass getFRCEnumOpenLR(int frc) {
        return FunctionalRoadClass.values()[frc];
    }

    /**
     * Writes HERE Line Location Reference to Raw Line Location Reference to make it readable for the OpenLR decoder.
     *
     * @param olr OpenLocationReference
     * @return OpenLR RawLineLocation Reference
     */
    public RawLineLocRef lineLocRefHere(OpenLocationReference olr) throws InvalidHereOLRException {
        if (!olr.isValid()) {
            // Exception
            System.out.println("HERE: Invalid OpenLR Data");
            throw new InvalidHereOLRException("HERE OLR is invalid!");
        } else {
            switch (olr.getLocationReference().getType().id) {
                case OpenLocationReference.OLR_TYPE_LINEAR:
                    LinearLocationReference lr = (LinearLocationReference) olr.getLocationReference();
                    int seqNr = 0;
                    List<LocationReferencePoint> lrps = new ArrayList<>();
                    // First LRP
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
                    // Intermediate LRPs
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
                    // Last LRP
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
                    // Negative and positive offsets
                    Offsets offsets = new OffsetsBinaryImpl(lr.getPosOff(), lr.getNegOff());
                    return new RawLineLocRef("1", lrps, offsets);
                default:
                    System.out.println("Unsupported OpenLR Type");
                    break;
            }
        }
        return null;
    }

    /**
     * HERE Decoder, decodes Base64 Strings to LineLocations.
     *
     * @param openLRCode OpenLR Base64 String
     * @return location
     * @throws Exception Invalide HERE Location
     */
    public Location decodeHere(String openLRCode, OSMMapLoader osmMapLoader) throws Exception {

        // Gets Open Location Reference from Base64 String
        OpenLocationReference olr = OpenLocationReference.fromBase64TpegOlr(openLRCode);

        // Creates Raw Line Location Reference from Here Location Reference
        RawLocationReference rawLocationReference;
        try {
            rawLocationReference = lineLocRefHere(olr);
        } catch (InvalidHereOLRException e) {
            return null;
        }

        // Initialize database
        //MapDatabase mapDatabase = new OpenLRMapDatabase_h2o();


        // Initialize database
        MapDatabase mapDatabase = new MapDatabaseImpl(osmMapLoader);

        // Decoder parameter, properties for writing on map database
        FileConfiguration decoderConfig = OpenLRPropertiesReader.loadPropertiesFromFile(new File(this.getClass().getClassLoader().getResource("OpenLR-Decoder-Properties.xml").getFile()));
        OpenLRDecoderParameter params = new OpenLRDecoderParameter.Builder().with(mapDatabase).with(decoderConfig).buildParameter();

        //Initialize the OpenLR decoder
        OpenLRDecoder decoder = new openlr.decoder.OpenLRDecoder();

        //decode the location on map database
        Location location = decoder.decodeRaw(params, rawLocationReference);

        //osmMapLoader.close();
        //((OpenLRMapDatabase_h2o) mapDatabase).close();

        return location;
    }


}
