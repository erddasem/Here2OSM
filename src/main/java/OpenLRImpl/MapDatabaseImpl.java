package OpenLRImpl;

import GeometryFunctions.*;
import Loader.OSMMapLoader;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.map.Node;
import org.geotools.filter.function.GeometryFunction;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class MapDatabaseImpl implements MapDatabase {

    OSMMapLoader osmLoader;

    public MapDatabaseImpl(OSMMapLoader osmLoader) {
        this.osmLoader = osmLoader;
    }

    private void setMapDatabaseImpl() {
        osmLoader.getAllLines().forEach(l -> l.setMdb(this));
        osmLoader.getAllNodes().forEach(n -> n.setMdb(this));
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

        //ArrayList<Double> nodesCloseBy = getNodesWithinDistance(List<NodeImpl> nodes, double latitude, double longitude, double distance);

        //function in SearchWithinDistance Class
        return null;
    }

    @Override
    public Iterator<Line> findLinesCloseByCoordinate(double longitude, double latitude, int distance) {
        //""
        return null;
    }

    @Override
    public boolean hasTurnRestrictionOnPath(List<? extends Line> path) {
        return false;
    }

    @Override
    public Iterator<Node> getAllNodes() {

        List allNodes = osmLoader.getAllNodes();
        return allNodes.iterator();
    }

    @Override
    public Iterator<Line> getAllLines() {

        List allLines = osmLoader.getAllLines();
        return allLines.iterator();
    }

    @Override
    public Rectangle2D.Double getMapBoundingBox() {

        ArrayList<Double> bbox = osmLoader.getBoundingBox();
        return new Rectangle2D.Double(bbox.get(0), bbox.get(1), bbox.get(2), bbox.get(3));
    }

    @Override
    public int getNumberOfNodes() {
       return osmLoader.numberOfNodes();
    }

    @Override
    public int getNumberOfLines() {
        return osmLoader.numberOfLines();
    }
}
