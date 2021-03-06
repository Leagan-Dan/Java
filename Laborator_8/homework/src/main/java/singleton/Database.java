package singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "STUDENT";
    private static final String PASSWORD = "STUDENT";
    private Connection connection;

    public Database() throws ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.createConnection();
    }

    /**
     * Creates the connection with the database
     */
    private void createConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException exception) {
            System.err.println(exception);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

}