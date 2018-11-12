package users;

import database.DatabaseSuperAdmin;

/**
 * @author tsamo
 */
public class SuperAdmin extends Admin {

    DatabaseSuperAdmin dbs;

    public SuperAdmin(String username, String password) {
        super(username, password);
        this.setRole(Role.SUPERADMIN);
        this.dbs = new DatabaseSuperAdmin();
    }

    public int registerUser(String username, String password, Role role) {
        return dbs.insertNewUser(username, password, role.toString());
    }

    public void viewUser(String username) {
        System.out.println(dbs.selectUser(username).toString());
    }

    public int removeUser(String username) {
        return dbs.removeUserByUsername(username);
    }

    public int updateUser(String oldusername, String username, String password, Role role) {
        return dbs.updateUser(oldusername, username, password, role.name());
    }

    public int deleteTable(String table) {
        return dbs.dropTable(table);
    }

    public int createSchema() {
        return dbs.databaseCreation();
    }

    public int dbUserCreation() {
        return dbs.databaseUserCreation();
    }

    public int grantPermissionsToUser() {
        return dbs.userPermissionsGrant();
    }

    public int grantFilePermissionsToUser() {
        return dbs.userFilePermissionsGrant();
    }

    public int createTable(String sql) {
        return dbs.databaseTableInsert(sql);
    }

    public int createSuperAdmin() {
        return dbs.insertSuperAdmin();
    }
}
