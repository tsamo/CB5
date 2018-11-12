package users;

import database.DatabaseEditor;
import other.Message;

import java.io.File;

/**
 * @author tsamo
 */

public class Editor extends Viewer {
    private DatabaseEditor dbe;

    public Editor(String username, String password) {
        super(username, password);
        this.dbe = new DatabaseEditor();
        this.setRole(Role.EDITOR);
    }



    public int editMessage(Editor editor, Message message, String data) {
        int returnFlag = dbe.updateMessageById(message.getId(), data);

        message.setData(data);
        message.setLastEditedBy(editor.getUsername());
        message.saveToLogFile();
        message.saveToSenderReceiverFile();
        return returnFlag;
    }

    public int updateAttachmentofMessage(User sender, Message message, File attachment) {
        int returnFlag = dbe.updateAttachmentofOwnMessageById(sender, message.getId(), attachment);
        dbe.setHasAttachment(true, message.getId());

        message.setLastEditedBy(sender.getUsername());
        message.setHasAttachment(true);
        message.setFilename(attachment.getName());
        message.setAttachment(attachment);
        message.saveToLogFile();
        message.saveToSenderReceiverFile();


        return returnFlag;
    }
}