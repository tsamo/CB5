package users;

import database.DatabaseAdmin;

/**
 * @author tsamo
 */
public class Admin extends Editor {
    private DatabaseAdmin dba;

    public Admin(String username, String password) {
        super(username, password);
        this.dba = new DatabaseAdmin();
        this.setRole(Role.ADMIN);
    }

    public int deleteMessage(int mid) {
        return dba.deleteMessageById(mid);
    }

    public int deleteAttachment(int mid) {
        return dba.deleteAttachmentById(mid);
    }

}
