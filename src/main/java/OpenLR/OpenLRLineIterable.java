package OpenLR;

import openlr.map.Line;

import java.util.Iterator;
import java.util.List;

public class OpenLRLineIterable implements Iterable<Line> {

    private final OpenLRLineIterator lineIterator;

    public OpenLRLineIterable(List<Line> listOfLines) {
        lineIterator = new OpenLRLineIterator(listOfLines);
    }

    @Override
    public Iterator<Line> iterator() {
        return lineIterator;
    }


}
