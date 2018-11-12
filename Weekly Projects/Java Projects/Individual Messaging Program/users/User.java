package users;

import database.DatabaseUser;
import other.Message;
import other.Utilities;

import java.io.File;

/**
 * @author tsamo
 */
public class User {
    private String username;
    private String password;
    private Role role;

    private DatabaseUser dbu;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.USER;
        this.dbu = new DatabaseUser();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public DatabaseUser getDbu() {
        return dbu;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "\nUser{" +
                "\n username=" + username +
                "\n password=" + password +
                "\n role=" + role +
                "\n}";
    }

    public Message newMessage(User receiver, String data) {
        String sqldata= Utilities.escapeSQL(data);
        Message message = new Message(this, receiver, data);
        message.setId(insertMessageToDatabase(sqldata, this.getUsername(), receiver.getUsername(), false));
        message.saveToLogFile();
        message.saveToSenderReceiverFile();

        return message;
    }

    public Message newMessage(User receiver, String data, File attachment) {
        String sqldata=Utilities.escapeSQL(data);
        Message message = new Message(this, receiver, data, attachment);
        message.setId(insertMessageToDatabase(sqldata, this.getUsername(), receiver.getUsername(), true));
        message.setAttachmentid(insertOwnAttachmentToDatabase(this, attachment, message.getId()));
        message.saveToLogFile();
        message.saveToSenderReceiverFile();

        return message;
    }

    public int insertMessageToDatabase(String data, String sender, String receiver, boolean hasAttachment) {
        return dbu.insertNewMessage(data, sender, receiver, hasAttachment);
    }

    public int insertOwnAttachmentToDatabase(User user, File attachment, int mid) {
        int returnFlag = 0;
        if (user.msgIsOwnMsg(user.getUsername(), mid)) {
            returnFlag = dbu.insertNewAttachment(attachment, mid);
        }
        return returnFlag;
    }

    public void readOwnReceivedMessages() {
        dbu.viewReceivedMessages(this);
    }

    public void readOwnUnreadMessages() {
        dbu.viewUnreadMessages(this);
    }

    public int setOwnMessagaAsRead(User user, int mid){
        return dbu.setOwnMessageAsRead(user,mid);
    }

    public void hasUnreadMessages() {
        dbu.hasUnreadMessages(this);
    }

    public void readOwnSentMessages() {
        dbu.viewSentMessages(this.getUsername());
    }

    public int deleteOwnMessage(String sender, int mid) {
        return dbu.deleteOwnMessageById(sender, mid);
    }

    public Message getOwnMessage(String user, int mid) {
        return dbu.getMsgById(user, mid);
    }

    public boolean msgIsOwnMsg(String user, int mid) {
        return dbu.getMsgExists(user, mid);
    }

    public int editOwnMessage(User sender, Message message, String data) {
        int returnFlag = 0;

        if (sender.msgIsOwnMsg(sender.getUsername(), message.getId())) {
            returnFlag = dbu.updateOwnMessageById(sender.getUsername(), message.getId(), data);

            message.setData(data);
            message.setLastEditedBy(sender.getUsername());
            message.saveToLogFile();
            message.saveToSenderReceiverFile();
        }
        return returnFlag;
    }

    public int updateAttachmentofOwnMessage(User sender, Message message, File attachment) {
        int returnFlag = 0;

        if (sender.msgIsOwnMsg(sender.getUsername(), message.getId())) {
            returnFlag = dbu.updateAttachmentofOwnMessageById(sender, message.getId(), attachment);
            dbu.setHasAttachment(true, message.getId());

            message.setLastEditedBy(sender.getUsername());
            message.setHasAttachment(true);
            message.setFilename(attachment.getName());
            message.setAttachment(attachment);
            message.saveToLogFile();
            message.saveToSenderReceiverFile();
        }

        return returnFlag;
    }

    public int deleteAttachmentofOwnMessage(User sender, Message message) {
        int returnFlag = 0;

        if (sender.msgIsOwnMsg(sender.getUsername(), message.getId())) {
            returnFlag = dbu.deleteAttachmentofOwnMessageById(message.getId());
            dbu.setHasAttachment(false, message.getId());

            message.setLastEditedBy(sender.getUsername());
            message.setHasAttachment(false);
            message.setNullFilename();
            message.setNullAttachment();
            message.saveToLogFile();
            message.saveToSenderReceiverFile();
        }

        return returnFlag;
    }
}
