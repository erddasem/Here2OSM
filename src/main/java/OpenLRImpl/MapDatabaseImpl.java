package OpenLRImpl;

import Loader.OSMMapLoader;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.map.Node;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class MapDatabaseImpl implements MapDatabase {

    OSMMapLoader osmLoader;

    public MapDatabaseImpl(OSMMapLoader osmLoader) {
        this.osmLoader = osmLoader;
    }

    @Override
    public boolean hasTurnRestrictions() {
        return false;
    }

    @Override
    public Line getLine(long id) {

         Optional<LineImpl> matchingLine = osmLoader.getAllLinesList().stream()
                 .filter(l -> l.getID() == id).findFirst();
         return matchingLine.get();

    }

    @Override
    public Node getNode(long id) {

        Optional<NodeImpl> matchingNode = osmLoader.getAllNodesList().stream()
            .filter(n -> n.getID() == id).findFirst();
        return matchingNode.get();
    }

    @Override
    public Iterator<Node> findNodesCloseByCoordinate(double longitude, double latitude, int distance) {
        return null;
    }

    @Override
    public Iterator<Line> findLinesCloseByCoordinate(double longitude, double latitude, int distance) {
        return null;
    }

    @Override
    public boolean hasTurnRestrictionOnPath(List<? extends Line> path) {
        return false;
    }

    @Override
    public Iterator<Node> getAllNodes() {

        return null;
    }

    @Override
    public Iterator<Line> getAllLines() {

        return null;
    }

    @Override
    public Rectangle2D.Double getMapBoundingBox() {
        return null;
    }

    @Override
    public int getNumberOfNodes() {
        //int numberOfNodes =
        return 0;
    }

    @Override
    public int getNumberOfLines() {
        return 0;
    }
}
