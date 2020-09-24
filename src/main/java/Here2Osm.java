import Exceptions.InvalidBboxException;
import Exceptions.InvalidWGS84CoordinateException;
import HereApi.ApiRequest;
import Loader.OSMMapLoader;

import java.sql.SQLException;

public class Here2Osm {
    // mainMethode
    public static void main(String[] args) {

        ApiRequest request = new ApiRequest();
        try {
            request.updateIncidentData();
        } catch (InvalidBboxException | InvalidWGS84CoordinateException e) {
            e.printStackTrace();
        }
    }
}
