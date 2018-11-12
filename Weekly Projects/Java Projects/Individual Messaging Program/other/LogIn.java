package other;

import database.DatabaseUser;
import users.Role;
import users.User;

import java.util.HashMap;

/**
 * @author tsamo
 */
public class LogIn {
    private String errorMessage = null;
    private User loggedInUser = null;
    private boolean isLoggedIn = false;
    private DatabaseUser dbu = new DatabaseUser();
    private final HashMap<String, Integer> eachUsersTries = new HashMap();


    public boolean LogIn(String username, String password) {
        User user = dbu.getUserByUsername(username);

        if (user != null && eachUsersTries.getOrDefault(username, 0) < 3) {
            if (dbu.checkPassword(username, password)) {
                loggedInUser = user;
                isLoggedIn = true;
                return true;
            } else {
                eachUsersTries.put(username, eachUsersTries.getOrDefault(username, 0) + 1);
                if (eachUsersTries.getOrDefault(username, 0) >= 3) {
                    System.out.println("You have made too many password errors. Now exiting...");
                    System.exit(0);
                }
                errorMessage = "Wrong password input for user " + username + ". You have " + (3 - eachUsersTries.getOrDefault(username, 0)) + " tries left.";
            }
        } else {
            errorMessage = "User " + username + " does not exist.";
        }

        return false;
    }

    public void LogOut() {
        loggedInUser = null;
        isLoggedIn = false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getUsernameLoggedInUser() {
        return loggedInUser.getUsername();
    }

    public Role getRoleLoggedInUser() {
        return loggedInUser.getRole();
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public DatabaseUser getDatabaseUser() {
        return dbu;
    }
}
