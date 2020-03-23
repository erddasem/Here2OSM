package DataBase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatasourceConfig {

    private static String dbUrl = "jdbc:postgresql://localhost/test_carola";
    private static String user = "emilykast";
    private static String password = "";

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl(dbUrl);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    private DatasourceConfig() {

    }

    /*public static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(user);
        config.setPassword(password);
        config.setAutoCommit(true);
        config.setMaximumPoolSize(32);
        return new HikariDataSource(config);
    }*/

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
