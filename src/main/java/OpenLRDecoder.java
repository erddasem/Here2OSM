import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryDecoder;
import openlr.binary.decoder.*;
import openlr.*;
import openlr.binary.impl.LocationReferenceBinaryImpl;
import openlr.decoder.OpenLRDecoderParameter;
import openlr.properties.OpenLRPropertiesReader;
import openlr.rawLocRef.RawLocationReference;

import java.io.File;
import java.lang.module.Configuration;
import java.util.Base64;

public class OpenLRDecoder {
    /*String openLRString = "CCkBEAAlJAnGZiROrAAJBQQBAnkACgUEAYQRAAB+/i4ACQUEAQIJADBdHg==";

    OpenLRBinaryDecoder binaryDecoder = new OpenLRBinaryDecoder();
    ByteArray byteArray = new ByteArray(Base64.getDecoder().decode(openLRString));
    LocationReferenceBinaryImpl locationReferenceBinary;

    {
        try {
            locationReferenceBinary = new LocationReferenceBinaryImpl("Test location", byteArray);
            RawLocationReference rawLocationReference = binaryDecoder.decodeData(locationReferenceBinary);
            Configuration decoderConfig = OpenLRPropertiesReader.loadPropertiesFromFile(new File(TestMapStubTest.class.getClassLoader().getResource("OpenLR-Decoder-Properties.xml").getFile()));
            OpenLRDecoderParameter params = new OpenLRDecoderParameter.Builder().with(map).with(decoderConfig).buildParameter();

            OpenLRDecoder decoder = new openlr.decoder.OpenLRDecoder();

            Locatoin location = decoder.decodeRaw(params, rawLocationReference);
        } catch (PhysicalFormatException e) {
            e.printStackTrace();
        }
    }*/

}
