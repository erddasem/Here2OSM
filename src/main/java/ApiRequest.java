import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiRequest {

    // Klassenattribute:
    private static String baseUrl = "https://traffic.ls.hereapi.com";
    private static String apiKey = "?apiKey=qBXOVr1c_bOSy-NICB9WnOduxAUgxTIF7Tc9svGT1qI";
    private static String bbox = "&bbox=51.057,13.744;51.053,13.751";
    private static String criticality = "&criticality=minor";
    private static String incidents = "/traffic/6.3/";
    private static String flow = "/traffic/6.2/";
    private static String resource;
    private static String format = ".xml";
    private static URL requestUrl;
    private static String answer;


    public void setRequestType(String requestType) {
        if (requestType.equals("incidents") || requestType.equals("flow"))
            resource = requestType;
    }

    private void setUrl() throws MalformedURLException {
        if (resource.equals("incidents"))
            requestUrl = new URL (baseUrl+incidents+resource+format+apiKey+bbox+criticality);
        if (resource.equals("flow"))
            requestUrl = new URL (baseUrl+flow+resource+format+apiKey+bbox+criticality);
    }

    public URL getUrl () {
        return requestUrl;
    }

    public void printUrl() {
        System.out.println(requestUrl);
    }

    public void sendRequest(String requestType) throws IOException {
        setRequestType(requestType);
        setUrl();
        printUrl();

        URL request = requestUrl;
        HttpURLConnection con = (HttpURLConnection) request.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/xml");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String readLine = null;
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            answer = response.toString();

        } else {
            System.out.println("GET Request failed.");
        }
    }

    public String getAnswer() {
        return answer;
    }

    public void printAnswer() {
        System.out.println(answer);
    }
}
