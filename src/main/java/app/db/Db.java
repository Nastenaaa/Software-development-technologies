package app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    private static final String URL  = "jdbc:postgresql://localhost:5432/personal_accounting";
    private static final String USER = "postgres";         // ← твій логін
    private static final String PASS = "admin";      // ← твій пароль

    static {
        try { Class.forName("org.postgresql.Driver"); }
        catch (ClassNotFoundException e) { throw new RuntimeException(e); }
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}


