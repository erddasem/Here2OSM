package HereApi;

import java.util.List;

public class CollectData {
    //TODO: Funktion zum sammeln der Daten

    public void collectInformation(List<TrafficItem> trafficItemList) {

        String incidentId;
        String type;
        String status;
        String start;
        String end;
        String openLRCode;
        String shortDesc;
        String longDesc;
        boolean roadClosure;
        String lines;
        int posOff;
        int negOff;


        System.out.println(trafficItemList.size());
        System.out.println(trafficItemList.get(0));


    }
}
