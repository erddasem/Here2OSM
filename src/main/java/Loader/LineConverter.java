package Loader;

import OpenLRImpl.LineImpl;

public class LineConverter {

    static LineImpl revertedLine2OpenLRLine(ReversedLine reversedLine) {
        LineImpl openLRLine = new LineImpl(reversedLine.line_id, reversedLine.start_node, reversedLine.end_node, reversedLine.frc, reversedLine.fow, reversedLine.length, reversedLine.name, reversedLine.reversed);
        return openLRLine;
    }

    static LineImpl directLine2OpenLRLine (DirectLine directLine) {
        LineImpl openLRLine = new LineImpl(directLine.line_id, directLine.start_node, directLine.end_node, directLine.frc, directLine.fow, directLine.length, directLine.name, directLine.reversed);
        return openLRLine;
    }
}
