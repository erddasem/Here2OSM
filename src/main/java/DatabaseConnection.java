import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
  private final String url = "jdbc:postgresql://localhost/routing_db";
  private final String user = "emilykast";
  private final String password = "";

  /**
   * Connect to the PostgreSQL database
   *
   * @return a Connection object
   */
  public Connection connectDB() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url, user, password);
      System.out.println("Connected to the PostgreSQL server successfully.");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return conn;
  }

}
