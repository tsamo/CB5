package database;

import other.Message;
import other.Utilities;
import users.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;


/**
 * @author tsamo
 */
public class DatabaseUser {
    Connection connection;

    public void connect() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public void connect(boolean admin) {
        connection = DatabaseConnection.getInstance(admin).getConnection();
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ResultSet executePrepared(String sql) {
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(sql);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isUsernameUsed(String username) {
        connect();
        boolean returnFlag = false;
        username = Utilities.escapeSQL(username);
        String sql = "SELECT COUNT(1) FROM users WHERE username='" + username + "'";
        ResultSet rs = executePrepared(sql);
        try {
            if (rs.next()) {
                returnFlag = rs.getInt(1) == 1;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public boolean userHasSentMessages(String username) {
        connect();
        boolean returnFlag = false;
        String sql = "SELECT COUNT(1) FROM messages WHERE sender='" + username + "'";
        ResultSet rs = executePrepared(sql);
        try {
            if (rs.next()) {
                returnFlag = rs.getInt(1) > 0;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public User getUserByUsername(String username) {
        connect();
        User returnedUser = null;
        String sql = "SELECT * FROM tsamoglou_dimitris_project1.users WHERE users.username='" + username + "'";
        ResultSet rs = executePrepared(sql);
        try {
            if (!rs.next()) {
                return null;
            } else {
                switch (rs.getString(3)) {
                    case "USER":
                        returnedUser = new User(rs.getString(1), rs.getString(2));
                        break;
                    case "VIEWER":
                        returnedUser = new Viewer(rs.getString(1), rs.getString(2));
                        break;
                    case "EDITOR":
                        returnedUser = new Editor(rs.getString(1), rs.getString(2));
                        break;
                    case "ADMIN":
                        returnedUser = new Admin(rs.getString(1), rs.getString(2));
                        break;
                    case "SUPERADMIN":
                        returnedUser = new SuperAdmin(rs.getString(1), rs.getString(2));
                        break;
                    default:
                        break;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return returnedUser;
    }

    public boolean checkPassword(String username, String password) {
        connect();
        ResultSet rs = null;
        Boolean returnFlag = false;
        String sql = "SELECT password FROM users WHERE username='" + username + "'";
        rs = executePrepared(sql);
        try {
            while (rs.next()) {
                if (rs.getString(1).equals(password)) {
                    returnFlag = true;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return returnFlag;
    }

    public void viewReceivedMessages(User receiver) {
        connect();
        String sql = "SELECT*FROM messages WHERE receiver='" + receiver.getUsername() + "' ORDER BY timestamp ASC";
        try {
            ResultSet rs = executePrepared(sql);
            if (rs.next()) {
                rs.beforeFirst();
                while (rs.next()) {
                    System.out.println("Message id: " + rs.getInt(1));
                    System.out.println("Sent on: " + rs.getTimestamp(5));
                    System.out.println("Sender: " + rs.getString(3));
                    if (rs.getBoolean(6) == true) {
                        System.out.println("Attachment: Yes");
                    }
                    System.out.println("\t" + rs.getString(2));
                    System.out.println("----------------------------------------------------");
                }
            } else {
                System.out.println("You have no received messages.");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public void hasUnreadMessages(User receiver) {
        connect();
        String sql = "SELECT COUNT(1) FROM messages WHERE hasBeenRead=0 AND receiver='" + receiver.getUsername() + "'";
        try {
            ResultSet rs = executePrepared(sql);
            if (rs.next() == true && rs.getInt(1) != 0) {
                System.out.println("You have " + rs.getInt(1) + " unread messages.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewUnreadMessages(User receiver) {
        connect();
        String sql = "SELECT*FROM messages WHERE receiver='" + receiver.getUsername() + "' AND hasBeenRead=false ORDER BY timestamp ASC";
        try {
            ResultSet rs = executePrepared(sql);
            while (rs.next()) {
                System.out.println("Message id: " + rs.getInt(1));
                System.out.println("Sent on: " + rs.getTimestamp(5));
                System.out.println("Sender: " + rs.getString(3));
                if (rs.getBoolean(6) == true) {
                    System.out.println("Attachment: Yes");
                }
                System.out.println("\t" + rs.getString(2));
                System.out.println("----------------------------------------------------");
            }

            if (rs.next() == false) {
                System.out.println("You have no unread messages.\n");
            }
            Thread.sleep(1000);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        disconnect();
        setOwnMessagesAsRead(receiver);
    }

    public int setOwnMessagesAsRead(User receiver) {
        connect();
        int returnFlag = 0;
        String sql = "UPDATE messages SET hasBeenRead=true" + " WHERE receiver='" + receiver.getUsername() + "'";
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

    public int setOwnMessageAsRead(User receiver, int mid) {
        connect();
        int returnFlag = 0;
        String sql = "UPDATE messages SET hasBeenRead=true" + " WHERE mid=" + mid + " AND receiver='" + receiver.getUsername() + "'";
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

    public void viewSentMessages(String sender) {
        connect();
        String sql = "SELECT*FROM messages WHERE sender='" + sender + "' ORDER BY timestamp ASC";
        try {
            ResultSet rs = executePrepared(sql);
            if (rs.next()) {
                rs.beforeFirst();
                while (rs.next()) {
                    System.out.println("Message id: " + rs.getInt(1));
                    System.out.println("Sent on: " + rs.getTimestamp(5));
                    System.out.println("Receiver: " + rs.getString(4));
                    if (rs.getBoolean(6) == true) {
                        System.out.println("Attachment: Yes");
                    }
                    System.out.println("\t" + rs.getString(2));
                    System.out.println("----------------------------------------------------");
                }
            } else {
                System.out.println("You have no sent messages.");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public void printAllAvailableUsernames() {
        connect();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT username FROM users");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("\t" + rs.getString(1));
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public void printUsersWithSentMessages() {
        connect();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT sender FROM messages");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("\t" + rs.getString(1));
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public int deleteOwnMessageById(String user, int id) {
        connect();
        int returnFlag = 0;
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM messages WHERE mid=" + id + " AND (sender='" + user + "' OR receiver='" + user + "' )");
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public int deleteAttachmentofOwnMessageById(int mid) {
        connect();
        int returnFlag = 0;
        try {
            String sql = "DELETE FROM attachments WHERE mid=" + mid;
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public int setHasAttachment(boolean hasAttachment, int mid) {
        connect();
        int returnFlag = 0;
        try {
            String sql = "UPDATE messages SET hasAttachment=" + hasAttachment + " WHERE mid=" + mid;
            PreparedStatement stmt = connection.prepareStatement(sql);
            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public boolean msgIdExists(int id) {
        connect();
        boolean returnFlag = false;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT mid FROM messages WHERE mid=" + id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                returnFlag = true;
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return returnFlag;
    }

    public boolean getMsgExists(String user, int mid) {
        connect();
        String sql = "SELECT * FROM messages WHERE messages.mid=" + mid + " AND sender='" + user + "'";
        ResultSet rs = executePrepared(sql);
        boolean returnFlag = false;
        try {
            if (rs.next()) {
                returnFlag = true;
            } else {
                returnFlag = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnFlag;
    }


    public Message getMsgById(String user, int id) {
        connect();
        String sql = "SELECT * FROM messages LEFT JOIN attachments ON messages.mid=attachments.mid WHERE messages.mid=" + id + " AND (sender='" + user + "' OR receiver='" + user + "')";
        ResultSet rs = executePrepared(sql);
        Message returnedMessage = null;

        String sender2 = null;
        String receiver = null;
        String data = null;
        boolean hasAttachment = false;
        Timestamp timestamp = null;
        Blob blob = null;
        File attachment = null;
        User receiver1 = null;
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
                    String extension = "";

                    int i = filename.lastIndexOf('.');
                    if (i > 0) {
                        extension = filename.substring(i + 1);
                    }
                    attachment = new File(filename + ".");
                    FileOutputStream out = new FileOutputStream(attachment);
                    out.write(array);
                    out.close();
                }
                User sender1 = getUserByUsername(sender2);
                receiver1 = getUserByUsername(receiver);
                if (hasAttachment) {
                    returnedMessage = new Message(sender1, receiver1, data, attachment);
                } else {
                    returnedMessage = new Message(sender1, receiver1, data);
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

        returnedMessage.setId(id);
        returnedMessage.setTimestamp(timestamp);
        if (hasAttachment) {
            attachment.deleteOnExit();
        }
        return returnedMessage;
    }

    public void openAttachmentbyId(int mid) {
        connect();
        String sql = "SELECT * FROM attachments WHERE mid=" + mid;
        ResultSet rs = executePrepared(sql);

        Blob blob;
        File attachment;
        try {
            if (rs.next()) {
                blob = rs.getBlob(2);
                String filename = rs.getString(3);

                byte[] array = blob.getBytes(1, (int) blob.length());

                int i = filename.lastIndexOf('.');
                if (i > 0) {
                    attachment = new File(filename + ".");
                    FileOutputStream out = new FileOutputStream(attachment);
                    out.write(array);
                    out.close();
                    Desktop.getDesktop().open(attachment);
                    attachment.deleteOnExit();

                } else {
                    System.out.println("Cannot determine what type of file the attachment is.");
                    System.out.println("Could not open.");
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
    }

    public int updateOwnMessageById(String username, int id, String data) {
        connect();

        int returnFlag = 0;

        try {
            String dataFormattedForSql = data.replace("'", "''");
            String sql = "UPDATE messages SET messages.data='" + dataFormattedForSql + "' WHERE messages.mid=" + id + " AND messages.sender='" + username + "'";
            PreparedStatement stmt = connection.prepareStatement(sql);

            returnFlag = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return returnFlag;
    }

    public int updateAttachmentofOwnMessageById(User user, int mid, File attachment) {
        connect();

        PreparedStatement stmt;
        int returnFlag = 0;
        boolean hasAttachment = false;
        String attachmentPath = attachment.getAbsolutePath();
        attachmentPath = attachmentPath.replaceAll("\\\\", "\\\\\\\\");
        try {
            String sql = "SELECT * FROM messages WHERE mid=" + mid;
            ResultSet rs = executePrepared(sql);
            if (rs.next()) {
                hasAttachment = rs.getBoolean(6);
            }

            if (hasAttachment) {
                String sql1 = "UPDATE attachments SET file=LOAD_FILE('" + attachmentPath + "'), filename='" + attachment.getName() + "'  WHERE mid=" + mid;
                stmt = connection.prepareStatement(sql1);
                returnFlag = stmt.executeUpdate();
            } else {
                String sql2 = "INSERT INTO attachments VALUES (NULL, LOAD_FILE('" + attachmentPath + "'), '" + attachment.getName() + "', " + mid + ")";
                stmt = connection.prepareStatement(sql2);
                returnFlag = stmt.executeUpdate();
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return returnFlag;
    }

    public int insertNewMessage(String data, String sender, String receiver, boolean hasAttachment) {
        connect();
        int mid = 0;

        String sql = "INSERT INTO messages VALUES (NULL, '" + data + "', '" + sender + "', '" + receiver + "', CURRENT_TIMESTAMP, " + hasAttachment + ", false )";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();

            ResultSet mIdrs = stmt.getGeneratedKeys();
            mid = 0;
            while (mIdrs.next()) {
                mid = mIdrs.getInt(1);
            }
            stmt.close();
            mIdrs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return mid;
    }

    public int insertNewAttachment(File attachment, int messageid) {
        connect();
        int mid = 0;
        String attachmentPath = attachment.getAbsolutePath();
        attachmentPath = attachmentPath.replaceAll("\\\\", "\\\\\\\\");
        String sql = "INSERT INTO attachments VALUES (NULL, LOAD_FILE('" + attachmentPath + "'), '" + attachment.getName() + "', " + messageid + ")";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();

            ResultSet mIdrs = stmt.getGeneratedKeys();
            mid = 0;
            while (mIdrs.next()) {
                mid = mIdrs.getInt(1);
            }
            stmt.close();
            mIdrs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return mid;
    }
}
