package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author tsamo
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection = null;

    private DatabaseConnection() {
        try {
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream("db.properties");
            properties.load(fis);

            final String FULL_DB_URL = properties.getProperty("dburl");
            final String DB_USER = properties.getProperty("user");
            final String DB_PASSWD = properties.getProperty("password");

            connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            fis.close();
        } catch (SQLException e) {
            System.out.println("Sorry, problems with the database connection!");
            System.out.println(e.toString());
            System.exit(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DatabaseConnection(boolean admin) {
        try {
            if (admin) {
                Properties properties = new Properties();
                FileInputStream fis = new FileInputStream("db.properties");
                properties.load(fis);

                final String FULL_DB_URL = properties.getProperty("dburl2");
                final String DB_USER = properties.getProperty("user2");
                final String DB_PASSWD = properties.getProperty("password2");

                connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
                fis.close();
            } else {
                Properties properties = new Properties();
                FileInputStream fis = new FileInputStream("db.properties");
                properties.load(fis);

                final String FULL_DB_URL = properties.getProperty("dburl");
                final String DB_USER = properties.getProperty("user2");
                final String DB_PASSWD = properties.getProperty("password2");

                connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
                fis.close();
            }
        } catch (SQLException e) {
            System.out.println("Sorry, problems with the database connection!");
            System.out.println(e.toString());
            System.exit(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseConnection getInstance() {
        try {
            if (instance == null) {
                instance = new DatabaseConnection();
            } else if (instance.getConnection().isClosed()) {
                instance = new DatabaseConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return instance;
    }

    public static DatabaseConnection getInstance(boolean admin) {
        try {
            if (instance == null) {
                instance = new DatabaseConnection(admin);
            } else if (instance.getConnection().isClosed()) {
                instance = new DatabaseConnection(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return instance;
    }
}


