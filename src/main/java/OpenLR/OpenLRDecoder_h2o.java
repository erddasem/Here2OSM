package OpenLR;

import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryDecoder;
import openlr.*;
import openlr.binary.impl.LocationReferenceBinaryImpl;
import openlr.decoder.OpenLRDecoderParameter;
import openlr.location.Location;
import openlr.map.MapDatabase;
import openlr.properties.OpenLRPropertiesReader;
import openlr.rawLocRef.RawLineLocRef;
import openlr.rawLocRef.RawLocationReference;
import org.apache.commons.configuration.FileConfiguration;

import java.io.File;
import java.util.Base64;


public class OpenLRDecoder_h2o {

    // TODO: Trennen Byte Array aus String und tatsächlicher Decode Vorgang
    public static void binary2array() throws PhysicalFormatException {
        OpenLRBinaryDecoder binaryDecoder = new OpenLRBinaryDecoder();
        //String openLRString = "CwnGsiRN4Qo/H/+ZAWAKbywr";
        String openLRString = "CCkBEAAlJAnGZiROrAAJBQQBAnkACgUEAYUVAAA7/b8ACQUEAQITADAAAA==";
        ByteArray byteArray = new ByteArray(openLRString);

        LocationReference lr = new LocationReferenceBinaryImpl("test", byteArray);
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

        }


    }

    public void decodeLocation() {

    }

    public static void main(String[] args) {
        try {
            binary2array();
        } catch (PhysicalFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }





}
