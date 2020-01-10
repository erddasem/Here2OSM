import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiRequest {

    public static URL getURL (String requestType) throws MalformedURLException {
        URL url;
        String baseUrl = "https://traffic.ls.hereapi.com";
        String apiKey = "?apiKey=qBXOVr1c_bOSy-NICB9WnOduxAUgxTIF7Tc9svGT1qI";
        String bbox = "&bbox=51.057,13.744;51.053,13.751";
        String criticality = "&criticality=minor";
        String incidents = "/traffic/6.3/";
        String flow = "/traffic/6.2/";
        String resource;
        String format = ".xml";
        if (requestType.equals("incident") )
        {
            resource = "incidents";
            URL requestIncidents = new URL(baseUrl+incidents+resource+format+apiKey+bbox+criticality);
            return requestIncidents;
        }
        else {
            resource = "flow";
            URL requestFlow = new URL(baseUrl+flow+resource+format+apiKey+bbox+criticality);
            return requestFlow;
        }
    }
    public static String request(String requestType) throws IOException {

        URL request = getURL(requestType);
        System.out.println(request);
        String readLine = null;
        HttpURLConnection con = (HttpURLConnection) request.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/xml");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            // print result
            //System.out.println("JSON String Result " + response.toString());

            String xml = response.toString();
            return xml;

        } else {
            System.out.println("GET Request failed.");
            String ret;
            ret = "";
            return ret;
        }
    }
}
