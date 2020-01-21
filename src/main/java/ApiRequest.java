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

    /**
     * Sets request type. Possible types are "incidents" to get traffic incidents ord "flow" to get traffic flow
     * information.
     * @param requestType type of request either incidents or flow
     */
    public void setRequestType(String requestType) {
        if (requestType.equals("incidents") || requestType.equals("flow"))
            resource = requestType;
    }

    /**
     * Sets URL depending on the given request type.
     * @throws MalformedURLException
     */
    private void setUrl() throws MalformedURLException {
        if (resource.equals("incidents"))
            requestUrl = new URL (baseUrl+incidents+resource+format+apiKey+bbox+criticality);
        if (resource.equals("flow"))
            requestUrl = new URL (baseUrl+flow+resource+format+apiKey+bbox+criticality);
    }

    /**
     * Returns request URL.
     * @return request URL
     */
    public URL getUrl () {
        return requestUrl;
    }

    /**
     * Prints generated request URL to console.
     */
    public void printUrl() {
        System.out.println(requestUrl);
    }

    /**
     * Sends request to HERE API.
     * API returns xml, xml is converted to String.
     * @param requestType can be "incidents" or "flow"
     * @throws IOException
     */
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

    /**
     * Returns XML as String.
     * @return
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Prints API answer to console.
     */
    public void printAnswer() {
        System.out.println(answer);
    }
}
