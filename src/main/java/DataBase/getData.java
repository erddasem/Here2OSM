package DataBase;

import Exceptions.InvalidBboxException;
import Exceptions.InvalidWGS84CoordinateException;
import HereApi.ApiRequest;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;

public class getData {
    static DSLContext ctx;

    static {
        try {
            Connection con = DatasourceConfig.getConnection();
            ctx = DSL.using(con, SQLDialect.POSTGRES);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        String sql = "select st_astext(geom) from openlr.kanten where line_id = 29";
        String sql2 = "select st_astext(ST_Reverse(geom)) from openlr.kanten where line_id = 29";
        // Fetch results using jOOQ
        Record result = ctx.fetchOne(sql);
        Record result2 = ctx.fetchOne(sql2);
        String res = result.getValue(0).toString();
        String res2 = result2.getValue(0).toString();

        System.out.println(res);
        System.out.println(res2);
    }


}
