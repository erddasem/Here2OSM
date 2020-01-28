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

    String openLRString = "CCkBEAAlJAnGZiROrAAJBQQBAnkACgUEAYQRAAB+/i4ACQUEAQIJADBdHg==";

    public void binary2array() throws PhysicalFormatException {
        OpenLRBinaryDecoder binaryDecoder = new OpenLRBinaryDecoder();
        ByteArray byteArray = new ByteArray(Base64.getDecoder().decode(openLRString));


    }





}
