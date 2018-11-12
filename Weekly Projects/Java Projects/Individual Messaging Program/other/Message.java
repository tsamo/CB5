package other;

import users.User;

import java.io.*;
import java.sql.Timestamp;


/**
 * @author tsamo
 */
public class Message {
    private User sender;
    private User receiver;
    private String data;
    private Timestamp timestamp;
    private int id;
    private String lastEditedBy = null;
    private boolean isResponse = false;
    private boolean hasAttachment = false;
    private File attachment = null;
    private String filename = null;
    private int attachmentid;

    public Message(User sender, User receiver, String data) {
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Message(User sender, User receiver, String data, File attachment) {
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
        timestamp = new Timestamp(System.currentTimeMillis());
        setAttachment(attachment);
        setFilename();
    }

    @Override
    public String toString() {
        if (this.getHasAttachment() == false) {
            return "\nMessage sent \nFrom: " + sender.getUsername() + "\t To: " + receiver.getUsername() + "\nOn: " + timestamp + "\n\t" + data + "\n";
        } else {
            return "\nMessage sent \nFrom: " + sender.getUsername() + "\t To: " + receiver.getUsername() + "\nOn: " + timestamp + "\nAttachment: " + this.getFilename() + "\n\t" + data + "\n";
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename() {
        this.filename = this.getAttachment().getName();
    }

    public void setFilename(String string) {
        this.filename = string;
    }

    public void setNullFilename() {
        this.filename = null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttachmentid() {
        return attachmentid;
    }

    public void setAttachmentid(int attachmentid) {
        this.attachmentid = attachmentid;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public File getAttachment() {
        return attachment;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
        setHasAttachment(true);
    }

    public void setNullAttachment() {
        this.attachment = null;
    }

    public boolean getHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setLastEditedBy(String lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
    }

    public void saveToLogFile() {
        writeToFile("MessagesLog.txt");
    }

    public void writeToFile(String filepath) {
        File file = new File(filepath);

        BufferedWriter bw = null;
        FileWriter fw;

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (lastEditedBy != null) {
                bw.append("\n---Edited by: " + lastEditedBy + ", at: " + timestamp + ".");
            }

            if (isResponse) {
                String identation = "\t\t\t\t\t\t";
                bw.append(identation + this.toString().replace("\n", "\n" + identation));
                bw.append("\n" + identation + "-----------------------------------------------------------------------\n");
            } else {
                bw.append(this.toString());
                bw.append("\n-----------------------------------------------------------------------\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveToSenderReceiverFile() {
        String formattedReceiver = this.receiver.getUsername().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        String formattedSender = this.sender.getUsername().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        String filepath1 = formattedReceiver + "_to_" + formattedSender + ".txt";
        String filepath2 = formattedSender + "_to_" + formattedReceiver + ".txt";
        if (new File(filepath1).exists()) {
            isResponse = true;
            writeToFile(filepath1);
        } else {
            writeToFile(filepath2);
        }
    }
}
