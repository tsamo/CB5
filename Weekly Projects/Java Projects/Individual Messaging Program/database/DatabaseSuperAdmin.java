package database;

import other.Utilities;
import users.Role;
import users.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author tsamo
 */
public class DatabaseSuperAdmin extends DatabaseAdmin {
    public DatabaseSuperAdmin() {
        super();
    }

    public int insertNewUser(String username, String password, String role) {
        username= Utilities.escapeSQL(username);
        password=Utilities.escapeSQL(password);
        connect();
        int returnFlag = 0;
        String sql = "INSERT INTO users VALUES ( '" + username + "', '" + password + "', '" + role + "')";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public User selectUser(String username) {
        username=Utilities.escapeSQL(username);
        connect();
        String sql = "SELECT * FROM users WHERE users.username='" + username + "'";

        ResultSet rs = executePrepared(sql);
        User user = null;

        String username1 = null;
        String password1 = null;
        Role role1 = null;

        try {
            while (rs.next()) {
                username1 = rs.getString(1);
                password1 = rs.getString(2);
                switch (rs.getString(3)) {
                    case "USER":
                        role1 = Role.USER;
                        break;
                    case "VIEWER":
                        role1 = Role.VIEWER;
                        break;
                    case "EDITOR":
                        role1 = Role.EDITOR;
                        break;
                    case "ADMIN":
                        role1 = Role.ADMIN;
                        break;
                    case "SUPERADMIN":
                        role1 = Role.SUPERADMIN;
                        break;
                    default:
                        System.out.println("Something went wrong, assigning USER role.");
                        role1 = Role.USER;
                        break;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();

        user = new User(username1, password1);
        user.setRole(role1);

        return user;
    }

    public int removeUserByUsername(String username) {
        username=Utilities.escapeSQL(username);
        connect();
        int returnFlag = 0;

        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE username='" + username + "'");
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public int updateUser(String oldusername, String username, String password, String role) {
        oldusername=Utilities.escapeSQL(oldusername);
        username=Utilities.escapeSQL(username);
        password=Utilities.escapeSQL(password);
        connect();

        int returnFlag = 0;

        try {
            String sql = "UPDATE users SET users.username='" + username + "', users.password='" + password + "', users.role='" + role + "' WHERE users.username='" + oldusername + "'";
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public int dropTable(String table) {
        connect(false);
        int returnFlag = 0;
        try {
            String sql = "DROP TABLE IF EXISTS `" + table + "`;";
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public int databaseCreation() {
        connect(true);
        int returnFlag = 0;
        try {
            String sql = "CREATE DATABASE  IF NOT EXISTS `tsamoglou_dimitris_project1`;";
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public int databaseUserCreation() {
        connect(false);
        int returnFlag = 0;

        try {
            String sql = "CREATE USER 'tsamo'@'localhost' IDENTIFIED BY 'tsamo';";
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public int userPermissionsGrant() {
        connect(false);
        int returnFlag = 0;

        try {
            String sql = "GRANT SELECT, INSERT,UPDATE, DELETE, FILE, EXECUTE ON tsamoglou_dimitris_project1.* TO 'tsamo'@'localhost';";
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public int userFilePermissionsGrant() {
        connect(false);
        int returnFlag = 0;

        try {
            String sql = "GRANT FILE ON *.* TO 'tsamo'@'localhost';";
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public int insertSuperAdmin() {
        connect(false);
        int returnFlag = 0;

        try {
            String sql = "INSERT INTO users VALUES ( 'admin', 'admin', 'SUPERADMIN')";
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public int databaseTableInsert(String sql) {
        connect(false);
        int returnFlag = 0;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public boolean checkIfDatabaseExists() {
        connect(true);
        boolean returnFlag = false;
        try {
            String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'tsamoglou_dimitris_project1';";
            ResultSet rs = executePrepared(sql);
            if (rs.next()) {
                returnFlag = true;
            } else {
                returnFlag = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }
}

