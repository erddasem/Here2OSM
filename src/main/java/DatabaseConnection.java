import java.sql.*;

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


  /**
   * Get actors count
   * @return
   */
  public int getActorCount() {
    String SQL = "SELECT count(*) FROM nodes";
    int count = 0;

    try (Connection conn = connectDB();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(SQL)) {
      rs.next();
      count = rs.getInt(1);
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
  System.out.println(count);
    return count;
  }


  public void endDBConnection(Connection conn) {
    try {
      conn.close();
      System.out.println("Closed Connection to PostgreSQL server successfully.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
