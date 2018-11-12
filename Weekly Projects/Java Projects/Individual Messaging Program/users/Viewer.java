package users;

import database.DatabaseViewer;
import other.Message;

/**
 * @author tsamo
 */
public class Viewer extends User {
    private DatabaseViewer dbv;

    public Viewer(String username, String password) {
        super(username, password);
        this.dbv = new DatabaseViewer();
        this.setRole(Role.VIEWER);
    }

    public Message getMessage(int id) {
        return dbv.getMsgById(id);
    }

    public void readSentMessagesOfUser(String user) {
        dbv.viewSentMessages(user);
    }
}
