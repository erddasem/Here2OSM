package Loader;

import OpenLR_h2o.OpenLRLine_h2o;

public class ReversedLine2OpenLRLineConverter {

    static OpenLRLine_h2o convert(ReversedLine reversedLine) {
        OpenLRLine_h2o openLRLineH2o = new OpenLRLine_h2o(reversedLine.line_id, reversedLine.start_node, reversedLine.end_node, reversedLine.frc, reversedLine.fow, reversedLine.length, reversedLine.name);
        return openLRLineH2o;
    }
}
