package TomTom_OpenLR;

import Loader.OSMMapLoader;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.map.Node;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import Loader.OSMMapLoader;

public class TomTom_MapDatabase implements MapDatabase {
    @Override
    public boolean hasTurnRestrictions() {
        return false;
    }

    @Override
    public Line getLine(long id) {
        return null;
    }

    @Override
    public Node getNode(long id) {
        return null;
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
