package OpenLR;

import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryDecoder;
import openlr.binary.decoder.*;
import openlr.*;
import openlr.binary.impl.LocationReferenceBinaryImpl;
import openlr.decoder.OpenLRDecoderParameter;
import openlr.location.Location;
import openlr.map.loader.MapLoadParameter;
import openlr.properties.OpenLRPropertiesReader;
import openlr.rawLocRef.RawLineLocRef;
import openlr.rawLocRef.RawLocationReference;
import openlr.decoder.LocationDatabase;
import openlr.map.Line;
import java.io.File;
import java.lang.module.Configuration;
import java.util.Base64;


public class OpenLRDecoder {


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

    public static void main(String[] args) {
        try {
            binary2array();
        } catch (PhysicalFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }





}
