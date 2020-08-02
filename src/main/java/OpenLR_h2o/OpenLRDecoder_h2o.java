package OpenLR_h2o;

import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryDecoder;
import openlr.*;
import openlr.binary.impl.LocationReferenceBinaryImpl;
import openlr.decoder.OpenLRDecoderParameter;
import openlr.location.Location;
import openlr.map.MapDatabase;
import openlr.properties.OpenLRPropertiesReader;
import openlr.rawLocRef.RawLocationReference;
import openlr.decoder.OpenLRDecoder;

import org.apache.commons.configuration.FileConfiguration;

import java.io.File;

import java.util.Base64;

/**
 * Decodes an TomTom OpenLR location.
 * Not suited to decode HERE TPEG-OLR location.
 */

public class OpenLRDecoder_h2o {

    /**
     * Method to decode base64 String to Byte Array.
     *
     * @param base64OpenLRString OpenLR String containing location references
     * @return Byte Array containing locations
     */
    public ByteArray openLR2byteArray(String base64OpenLRString) {
        // Base64 String to Byte Array
        return new ByteArray(Base64.getDecoder().decode(base64OpenLRString));
    }

    /**
     * Decodes byte array and outputs edges of the own routing network affected by the incident.
     *
     * @param byteArray Byte Array containing location references
     * @throws Exception Byte Array not valid
     */


    public Location decodeTomTom(ByteArray byteArray) throws Exception {
        // Byte array to location reference
        LocationReference lr = new LocationReferenceBinaryImpl("Incident", byteArray);

        // Decode Binary Array to raw location
        OpenLRBinaryDecoder binaryDecoder = new OpenLRBinaryDecoder();
        RawLocationReference rawLocationReference = binaryDecoder.decodeData(lr);

        // Initialize database
        MapDatabase mapDatabase = new OpenLRMapDatabase_h2o();

        // DecoderHere parameter
        FileConfiguration decoderConfig = OpenLRPropertiesReader.loadPropertiesFromFile(new File(this.getClass().getClassLoader().getResource("OpenLR-Decoder-Properties.xml").getFile()));
        OpenLRDecoderParameter params = new OpenLRDecoderParameter.Builder().with(mapDatabase).with(decoderConfig).buildParameter();

        //Initialize the decoder
        OpenLRDecoder decoder = new openlr.decoder.OpenLRDecoder();

        //decode the location on own database
        Location location = decoder.decodeRaw(params, rawLocationReference);

        //Close database connections
        ((OpenLRMapDatabase_h2o) mapDatabase).close();

        return location;
    }

}