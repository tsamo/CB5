package database;

import other.Utilities;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author tsamo
 */
public class DatabaseEditor extends DatabaseViewer {

    public DatabaseEditor() {
        super();
    }

    public int updateMessageById(int mid, String data) {
        data = Utilities.escapeSQL(data);
        connect();
        int returnFlag = 0;

        try {
            String sql = "UPDATE messages SET messages.data='" + data + "' WHERE messages.mid=" + mid;
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return returnFlag;
    }
}
