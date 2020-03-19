package OpenLR;

import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryDecoder;
import openlr.*;
import openlr.binary.impl.LocationReferenceBinaryImpl;
import openlr.decoder.OpenLRDecoderParameter;
import openlr.location.Location;
import openlr.map.MapDatabase;
import openlr.properties.OpenLRPropertiesReader;
import openlr.rawLocRef.RawLocationReference;

import org.apache.commons.configuration.FileConfiguration;

import java.io.File;

import java.util.Base64;


public class OpenLRDecoder_h2o {


    /*public static ByteArray binary2array(String base64OpenLRString) throws {
        return array;
       *//* LocationReference lr = new LocationReferenceBinaryImpl("test", array);
        RawLocationReference rawLocationReference = binaryDecoder.decodeData(lr);
        System.out.println("rawLocationReference=" + rawLocationReference);
        if (rawLocationReference instanceof RawLineLocRef) {
            RawLineLocRef rawLineLocRef = (RawLineLocRef) rawLocationReference;
            System.out.println("OpenLRDecoderTest.binary2array(), centerPoint=" + rawLineLocRef.getCenterPoint());
            for (LocationReferencePoint locationReferencePoint : rawLineLocRef.getLocationReferencePoints()) {
                locationReferencePoint.getLatitudeDeg();
                locationReferencePoint.getLongitudeDeg();
                System.out.println("OpenLRDecoderTest.binary2array(), lat=" + locationReferencePoint.getLatitudeDeg() +
                        ", lon=" + locationReferencePoint.getLongitudeDeg());
            }

        }*//*
    }*/


    /**
     * Method to decode Base64 String and map extracted location references on database.
     *
     * @param base64OpenLRString String given by HERE Api, containing location references
     * @throws Exception
     */
    public static void decode(String base64OpenLRString) throws Exception {

        // Base64 String to Byte Array
        ByteArray array = new ByteArray(Base64.getDecoder().decode(base64OpenLRString));
        LocationReference lr = new LocationReferenceBinaryImpl("Traffic", array);

        // Decode Binary Array to rar location
        OpenLRBinaryDecoder binaryDecoder = new OpenLRBinaryDecoder();
        RawLocationReference rawLocationReference = binaryDecoder.decodeData(lr);

        // Initialize database
        MapDatabase mapDatabase = new OpenLRMapDatabase_h2o();

        // Decoder parameter
        FileConfiguration decoderConfig = OpenLRPropertiesReader.loadPropertiesFromFile(new File("src/main/java/OpenLR-Decoder-Properties.xml"));
        OpenLRDecoderParameter params = new OpenLRDecoderParameter.Builder().with(mapDatabase).with(decoderConfig).buildParameter();

        //Initialize the decoder
        OpenLRDecoder decoder = new openlr.decoder.OpenLRDecoder();

        //decode the location on own database
        Location location = decoder.decodeRaw(params, rawLocationReference);

        // System.out.println(decoder.decodeRaw(params, rawLocationReference));


    }


    /*public static void main(String[] args) throws Exception {

    }*/


}