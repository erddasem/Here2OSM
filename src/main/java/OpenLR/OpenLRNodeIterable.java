package OpenLR;

import openlr.map.Node;

import java.util.Iterator;
import java.util.List;

public class OpenLRNodeIterable implements Iterable<Node> {
    private final OpenLRNodeIterator nodeIterator;

    public OpenLRNodeIterable(List<Node> listOfNodes) {
        nodeIterator = new OpenLRNodeIterator(listOfNodes);
    }

    @Override
    public Iterator<Node> iterator() {
        return nodeIterator;
    }
}
