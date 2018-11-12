package database;

import other.Message;
import users.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author tsamo
 */
public class DatabaseViewer extends DatabaseUser {

    public DatabaseViewer() {
        super();
    }

    public Message getMsgById(int id) {
        connect();
        String sql = "SELECT * FROM messages LEFT JOIN attachments ON messages.mid=attachments.mid WHERE messages.mid=" + id;
        ResultSet rs = executePrepared(sql);
        Message returnedMessage;

        String sender2 = null;
        String receiver = null;
        String data = null;
        boolean hasAttachment = false;
        Timestamp timestamp = null;
        Blob blob;
        File attachment = null;
        try {
            if (rs.next()) {
                data = rs.getString(2);
                sender2 = rs.getString(3);
                receiver = rs.getString(4);
                timestamp = rs.getTimestamp(5);
                hasAttachment = rs.getBoolean(6);
                blob = rs.getBlob(9);
                String filename = rs.getString(10);

                if (hasAttachment) {
                    byte[] array = blob.getBytes(1, (int) blob.length());
                    attachment = new File(filename + ".");
                    FileOutputStream out = new FileOutputStream(attachment);
                    out.write(array);
                    out.close();
                    attachment.deleteOnExit();
                }
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        disconnect();

        User sender1;
        User receiver1;

        if (getUserByUsername(sender2) == null) {
            sender1 = new User(sender2, "temp");
        } else {
            sender1 = getUserByUsername(sender2);
        }

        if (getUserByUsername(receiver) == null) {
            receiver1 = new User(receiver, "temp");
        } else {
            receiver1 = getUserByUsername(receiver);
        }

        if (hasAttachment) {
            returnedMessage = new Message(sender1, receiver1, data, attachment);
        } else {
            returnedMessage = new Message(sender1, receiver1, data);
        }

        returnedMessage.setId(id);
        returnedMessage.setTimestamp(timestamp);

        return returnedMessage;
    }
}