package OpenLR;

import openlr.map.Line;

import java.util.Iterator;
import java.util.List;

public class OpenLRLineIterator implements Iterator<Line> {

    private final List<Line> lineList;
    private int size;
    private int currentIndex = 0;

    public OpenLRLineIterator(List<Line> listOfLines) {
        this.lineList = listOfLines;
        this.size = listOfLines.size();
    }

    @Override
    public boolean hasNext() {

        return currentIndex < size && lineList.get(currentIndex) != null;
    }

    @Override
    public Line next() {

        return lineList.get(currentIndex++);
    }


}
