import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRequest {
    public static String MyGETRequest() throws IOException, ParserConfigurationException, SAXException {
        String baseUrl = "https://traffic.ls.hereapi.com";
        String apiKey = "?apiKey=qBXOVr1c_bOSy-NICB9WnOduxAUgxTIF7Tc9svGT1qI";
        String bbox = "&bbox=51.057,13.744;51.053,13.751";
        String criticality = "&criticality=minor";
        String path = "/traffic/6.3/";
        String resource = "incidents";
        String format = ".xml";
        URL request = new URL(baseUrl+path+resource+format+apiKey+bbox+criticality);
        System.out.println(request);
        String readLine = null;
        HttpURLConnection con = (HttpURLConnection) request.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/xml");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();
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
