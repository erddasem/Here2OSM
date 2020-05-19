package HereDecoder;

import openlr.LocationReferencePoint;
import openlr.Offsets;
import openlr.binary.impl.LocationReferencePointBinaryImpl;
import openlr.binary.impl.OffsetsBinaryImpl;
import openlr.decoder.OpenLRDecoder;
import openlr.map.FormOfWay;
import openlr.map.FunctionalRoadClass;
import openlr.rawLocRef.RawLineLocRef;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Program {

    public boolean runTestMode;

    public Program() {

    }



    public static void main(String[] args) {

        Program program = new Program();
        program.run(args);

        System.out.print("Press Any Key To Exit");
    }

    public boolean isRunTestMode() {
        return runTestMode;
    }

    public void setRunTestMode(boolean runTestMode) {
        this.runTestMode = runTestMode;
    }

    public void run(String[] args) {
        // boolean succeeded = parseCmdLineOptions(args);
//        if (succeeded) {
//            System.out.println("Sample DecoderHere");
//            if (runTestMode) {
//
//            }
//        }

        List<String> olrStrings = getTestCodeStrings();
        for (String olrString : olrStrings) {
            OpenLocationReference olr = OpenLocationReference.fromBase64TpegOlr(olrString);
            outputOlrData(olr);
            lineLocRefHere(olr);
        }
    }

    public void outputOlrData(OpenLocationReference olr) {
        try {
            if (!olr.isValid()) {
                System.out.println("Invalid OpenLR Data");
            } else {
                switch (olr.getLocationReference().getType().id) {
                    case (OpenLocationReference.OLR_TYPE_GEO_COORDINATE):
                        outputGeolocation((GeoCoordinateLocationReference) olr.getLocationReference());
                        break;
                    case OpenLocationReference.OLR_TYPE_LINEAR:
                        outputLinearLocation((LinearLocationReference) olr.getLocationReference());
                        break;
                    default:
                        System.out.println("Unsupported OpenLR Type");
                        break;
                }


            }
        } catch (Exception e) {
            System.out.println("Invalid OpenLR String");
        }
    }


    private void outputGeolocation(GeoCoordinateLocationReference geoLocation) {
        System.out.println("GeoCoordinateLocation at ");
        System.out.println(geoLocation.coordinate.getLatitude() + ", " + geoLocation.coordinate.getLatitude());
    }

    private void outputLinearLocation(LinearLocationReference linLocation) {
        System.out.println("LinearLocationReference with points: ");
        for (Geoposition point : linLocation.geometry) {
            System.out.println(point.getLatitude() + ", " + point.getLongitude());

        }
    }

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
                    case (OpenLocationReference.OLR_TYPE_GEO_COORDINATE):
                        outputGeolocation((GeoCoordinateLocationReference) olr.getLocationReference());
                        break;
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
                        RawLineLocRef rawLine = new RawLineLocRef("1", lrps, offsets);
                        return rawLine;
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

    private List<String> getTestCodeStrings() {
        List<String> validBase64OlrStrings = Arrays.asList("CCgBEAAkIwnJWyRCaAAJBQQDAwYACgQDAz8AAA4AOAAJBQQDA4cAMAAA"
//                "CCkBEAAlJAbn0SU9BgAJBQQDA7UACgUEA4ETAP8z/9cACQUEAwMxADBVAA==",
//                "CCgBEAAkIwepwCVIMwAJBQQEAv8ACgQDBBoA//8AFwAJBQQEAn8AMAAA",
//                "CCgBEAAkIwerNyVMfAAJBQQEA98ACgQDBFEA/6sAMwAJBQQEA2EAMAAA",
//                "CCoBEAAmJQeKnSUydgAJBQQDA3wACgUEA4MGAADN/r0ACQUEAwP/ADBIgiM=",
//                "CGMBEABfXgmAKiVcHgAJBQQDA6YACgUEA4c7AP+dAzAACQUEAwKFAHAD+7n95QAJBQQDA6QACgUEA4E+AP8p/5MACQUEAwOkAAoFBAOBIAD/Sv+kAAkFBAMDpAAKBQQDiTEAAAA=",
//                "CCkBEAAlJAl+BiVQ5gAJBQQBAeMACgUEAYElAP9uAHYACQUEAQFoADANcw==",
//                "CGMBEABfXgmAKiVcHgAJBQQDA6YACgUEA4c7AP+dAzAACQUEAwKFAHAD+7n95QAJBQQDA6QACgUEA4E+AP8p/5MACQUEAwOkAAoFBAOBIAD/Sv+kAAkFBAMDpAAKBQQDiTEAAAA=",
//                "CE8BEABLSgl9PyVcQgAJBQQDAokACgUEA4U4AAarAlcACQUEAwOmAHAC/5f9lAAJBQQDAoEACgQDAz4AAAH/yAAJBQQDAoAACgUEA41cAAAA",
//                "CFEBEABNTAoCaCU3QAAJBQQBAb8ACgUEAfUBANfIBFcACQUEAQFAAHACrYX3WwAJBQQBAbwACgUEAYJDAP4p/+MACQUEAQG8AAoFBAG4bAAAiko=",
//                "CCkBEAAlJAhjSyPx+wAJBQQBAWAACgUEAYE0AACz/44ACQUEAQHhADA4AA==",
//                "CCkBEAAlJAmRzCVgcAAJBQQDA1gACgUEA4YXAAPI/ngACQUEAwPGADAAAA==",
//                "CD0BEAA5OAaPjSOPLwAJBQQCA8YACgUEAo0jAPvo/b0ACQUEAgMrAHAB9xT/RgAJBQQCA7QACgUEAokmAAB1"
        );

        return validBase64OlrStrings;

    }

    private boolean parseCmdLineOptions(String[] args) {
        boolean validOptions = true;
        try {
            for (String arg : args) {
                if ((arg == "-h") ||
                        (arg == "/h") ||
                        (arg == "-?") ||
                        (arg == "/?") ||
                        (arg == "-help") ||
                        (arg == "-Help") ||
                        (arg == "/Help") ||
                        (arg == "/help")) {
                    showCompleteUsage();
                    validOptions = false;
                    break;
                }
                if (arg.startsWith("/RunTest=")) {
                    int str = "/RunTest=".length();
                    String answ = arg.substring(str - 1);
                    runTestMode = Boolean.parseBoolean(answ);
                }
            }
        } catch (Exception ex) {
            validOptions = false;
            System.out.println(ex.getMessage());
        }
        return validOptions;
    }

    public void showCompleteUsage() {
        System.out.println(" ");
        System.out.println("Usage: ");
        System.out.println("SampleOlrdecoder.exe [/Help] ");
        System.out.println("                                [/RunTest=TRUE|FALSE]");
        System.out.println(" ");

        System.exit(0);
    }

}
