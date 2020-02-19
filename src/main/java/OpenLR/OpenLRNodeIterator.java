package OpenLR;

import openlr.map.Node;

import java.util.Iterator;
import java.util.List;

public class OpenLRNodeIterator implements Iterator<Node> {

    private final List<Node> nodeList;
    private int size;
    private int currentIndex = 0;

    public OpenLRNodeIterator(List<Node> listOfNodes) {
        this.nodeList = listOfNodes;
        this.size = listOfNodes.size();
    }

    @Override
    public boolean hasNext() {

        return currentIndex < size && nodeList.get(currentIndex) != null;
    }

    @Override
    public Node next() {

        return nodeList.get(currentIndex++);
    }
}
