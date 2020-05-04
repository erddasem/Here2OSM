package HereDecoder;

import java.util.Arrays;
import java.util.List;

public class Program {

    public boolean runTestMode;

    public Program() {

    }

    public static void main(String[] args) {


    }

    public boolean isRunTestMode() {
        return runTestMode;
    }

    public void setRunTestMode(boolean runTestMode) {
        this.runTestMode = runTestMode;
    }

    public void run(String[] args) {
        boolean succeeded = parseCmdLineOptions(args);
        if (succeeded) {
            System.out.println("Sample Decoder");
            if (runTestMode) {
                List<String> olrString = getTestCodeStrings();

            }
        }
    }

    public void outputOlrData(OpenLocationReference olr) {
        try {
            if (!olr.isValid()) {
                System.out.println("InValid OpenLR Data");
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

    private List<String> getTestCodeStrings() {
        List<String> validBase64OlrStrings = Arrays.asList("CCoBEAAmJQTqdCRJVQAJBQQCAiYACgUEAoM/AAHDARoACQUEAgKfADCDBzA=",
                "CCkBEAAlJAbn0SU9BgAJBQQDA7UACgUEA4ETAP8z/9cACQUEAwMxADBVAA==",
                "CCgBEAAkIwepwCVIMwAJBQQEAv8ACgQDBBoA//8AFwAJBQQEAn8AMAAA",
                "CCgBEAAkIwerNyVMfAAJBQQEA98ACgQDBFEA/6sAMwAJBQQEA2EAMAAA",
                "CCoBEAAmJQeKnSUydgAJBQQDA3wACgUEA4MGAADN/r0ACQUEAwP/ADBIgiM=",
                "CGMBEABfXgmAKiVcHgAJBQQDA6YACgUEA4c7AP+dAzAACQUEAwKFAHAD+7n95QAJBQQDA6QACgUEA4E+AP8p/5MACQUEAwOkAAoFBAOBIAD/Sv+kAAkFBAMDpAAKBQQDiTEAAAA=",
                "CCkBEAAlJAl+BiVQ5gAJBQQBAeMACgUEAYElAP9uAHYACQUEAQFoADANcw==",
                "CGMBEABfXgmAKiVcHgAJBQQDA6YACgUEA4c7AP+dAzAACQUEAwKFAHAD+7n95QAJBQQDA6QACgUEA4E+AP8p/5MACQUEAwOkAAoFBAOBIAD/Sv+kAAkFBAMDpAAKBQQDiTEAAAA=",
                "CE8BEABLSgl9PyVcQgAJBQQDAokACgUEA4U4AAarAlcACQUEAwOmAHAC/5f9lAAJBQQDAoEACgQDAz4AAAH/yAAJBQQDAoAACgUEA41cAAAA",
                "CFEBEABNTAoCaCU3QAAJBQQBAb8ACgUEAfUBANfIBFcACQUEAQFAAHACrYX3WwAJBQQBAbwACgUEAYJDAP4p/+MACQUEAQG8AAoFBAG4bAAAiko=",
                "CCkBEAAlJAhjSyPx+wAJBQQBAWAACgUEAYE0AACz/44ACQUEAQHhADA4AA==",
                "CCkBEAAlJAmRzCVgcAAJBQQDA1gACgUEA4YXAAPI/ngACQUEAwPGADAAAA==",
                "CD0BEAA5OAaPjSOPLwAJBQQCA8YACgUEAo0jAPvo/b0ACQUEAgMrAHAB9xT/RgAJBQQCA7QACgUEAokmAAB1");

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
