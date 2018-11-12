package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author tsamo
 */
public class DatabaseAdmin extends DatabaseEditor {

    public DatabaseAdmin() {
        super();
    }

    public int deleteMessageById(int mid) {
        connect();
        int res = 0;
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM messages WHERE mid=" + mid);
            res = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return res;
    }

    public int deleteAttachmentById(int mid) {
        connect();
        int res = 0;
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM attachments WHERE mid=" + mid);
            res = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return res;
    }
}
