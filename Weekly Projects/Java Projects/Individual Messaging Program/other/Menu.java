package other;

import database.DatabaseSuperAdmin;
import users.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author tsamo
 */
public class Menu {
    private static Scanner sc;
    private LogIn login;
    private boolean terminate = false;

    public Menu() {
        sc = new Scanner(System.in);
        login = new LogIn();
    }

    public void startMenu() {
        Moptions.check();
        String selection = sc.nextLine();
        while (!selection.equals("i") && !selection.equals("s") && !selection.equals("exit")) {
            System.out.println("Not valid command. Please try again.");
            selection = sc.nextLine();
        }
        switch (selection) {
            case "i":
                informationMenu();
                break;
            case "s":
                checkMenu();
                break;
            case "exit":
                System.out.println("Exiting...");
                System.exit(0);
                break;
        }
    }

    public void informationMenu() {
        System.out.println("---Informations for the Application---\n");
        System.out.println("The parameters user2, password2 and dburl2 parameters, located inside the db.properties file that is included must be setup for your MySQL.");
        System.out.println("The above parameters must be edited to match your MySQL configuration for the first run to setup the new database and user.");
        System.out.println("The user you input is recommended to be the root user or a user that has the privileges to create new schemas and grant permissions.\n");
        System.out.println("This messaging application also supports file upload. The default size that is supported by MySQL IS 4mb.");
        System.out.println("However you can change it if you wish, by editing the my.ini file that is located in C:\\ProgramData\\MySQL\\MySQL Server (Your MySQL Edition)\\");
        System.out.println("To change the file upload you must edit the max_allowed_packet value to whichever you want.");
        System.out.println("If you change the value of max_allowed_packet, then you must restart your MySQL Service for the changes to take effect.\n");
        System.out.println("By default, the log and conversation files will be saved to the same directory with the .jar and db.properties files.\n");
        System.out.println("Also by default for security reasons, the default filepath for uploading files is C:\\ProgramData\\MySQL\\MySQL Server (Your MySQL Edition)\\Uploads");
        System.out.println("If you want to upload from anywhere in your system, find the my.ini file again and edit the secure-file-priv value.");
        System.out.println("If you want to keep your configuration secure but upload from another folder, just change the path, otherwise just leave it blank in double quotes.");
        System.out.println("Afterwards, for any change you have made to take effect, you must (as stated above) restart your MySQL Service.\n");
        Moptions.infoOptions();
        String selection = sc.nextLine();
        while (!selection.equals("b") && !selection.equals("exit")) {
            System.out.println("Not valid command. Please try again.");
            selection = sc.nextLine();
        }
        switch (selection) {
            case "b":
                startMenu();
                break;
            case "exit":
                System.out.println("Exiting...");
                System.exit(0);
                break;
        }
    }

    public void checkMenu() {
        DatabaseSuperAdmin dbs = new DatabaseSuperAdmin();
        if (dbs.checkIfDatabaseExists()) {
            normalMenu();
        } else {
            System.out.println("Cannot access database tsamoglou_dimitris_project1. Will try to create it.");
            System.out.println("Please input your credentials.");
            emergencyMenu();
        }
    }

    public void normalMenu() {
        Moptions.welcome();

        while (true) {
            if (terminate)
                break;
            System.out.print("Give \"login\" or \"exit\" : ");
            String selection = sc.nextLine();

            if (selection.equalsIgnoreCase("exit")) {
                break;
            } else if (selection.equalsIgnoreCase("login")) {
                if (isLoginSuccess()) {
                    System.out.println("Hello " + login.getUsernameLoggedInUser() + ". You are now logged in.\n");
                    mainMenu();
                } else {
                    System.out.println(login.getErrorMessage());
                }
            } else {
                System.out.println("Unknown command. Please try again.");
            }
        }
    }

    public void emergencyMenu() {
        try {
            while (true) {
                if (terminate)
                    break;
                if (isLoginAsAdminSuccess()) {
                    SuperAdmin superAdmin = new SuperAdmin("admin", "admin");
                    System.out.println("The database tsamoglou_dimitris_project1 will now be set up.");
                    System.out.println("Please wait.");

                    Properties properties = new Properties();
                    FileInputStream fis = new FileInputStream("db.properties");
                    properties.load(fis);
                    String users_table = properties.getProperty("table_users");
                    String messages_table = properties.getProperty("table_messages");
                    String attachments_table = properties.getProperty("table_attachments");

                    superAdmin.createSchema();
                    superAdmin.deleteTable("users");
                    superAdmin.deleteTable("messages");
                    superAdmin.deleteTable("attachments");
                    superAdmin.createTable(users_table);
                    superAdmin.createTable(messages_table);
                    superAdmin.createTable(attachments_table);
                    superAdmin.dbUserCreation();
                    superAdmin.grantPermissionsToUser();
                    superAdmin.grantFilePermissionsToUser();
                    superAdmin.createSuperAdmin();
                    fis.close();
                    normalMenu();
                } else {
                    System.out.println("You do not have the credentials to login as SuperAdmin and set up the database.");
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void mainMenu() {
        while (true) {
            if (login.isLoggedIn() == false) {
                break;
            }
            printMainMenu();

            System.out.print("Enter> ");
            String selection = sc.nextLine();

            if (selection.equalsIgnoreCase("exit")) {
                terminate = true;
                break;
            }
            menuOperations(selection);
        }
    }

    private void menuOperations(String selection) {
        try {
            switch (selection) {
                case "s":
                    sendMessageOperation();
                    Thread.sleep(1000);
                    break;
                case "v":
                    viewOwnSpecificMessageOperation();
                    Thread.sleep(1000);
                    break;
                case "e":
                    editOwnMessageOperation();
                    Thread.sleep(1000);
                    break;
                case "n":
                    login.getLoggedInUser().readOwnUnreadMessages();
                    Thread.sleep(1000);
                    break;
                case "i":
                    login.getLoggedInUser().readOwnReceivedMessages();
                    Thread.sleep(1000);
                    break;
                case "o":
                    login.getLoggedInUser().readOwnSentMessages();
                    Thread.sleep(1000);
                    break;
                case "d":
                    deleteOwnMessageOperation();
                    Thread.sleep(1000);
                    break;
                case "l":
                    login.LogOut();
                    break;
                case "r":
                    if (login.getLoggedInUser().getRole() == Role.USER) {
                        System.out.println("You do not have the privileges to access this command.");
                    } else {
                        readMessagesOfAUserOperation();
                    }
                    Thread.sleep(1000);
                    break;
                case "ee":
                    if (login.getLoggedInUser().getRole() == Role.USER || login.getLoggedInUser().getRole() == Role.VIEWER) {
                        System.out.println("You do not have the privileges to access this command.");
                    } else {
                        editMessagesOfAnotherUserOperation();
                    }
                    Thread.sleep(1000);
                    break;
                case "del":
                    if (login.getLoggedInUser().getRole() == Role.USER || login.getLoggedInUser().getRole() == Role.VIEWER || login.getLoggedInUser().getRole() == Role.EDITOR) {
                        System.out.println("You do not have the privileges to access this command.");
                    } else {
                        deleteMessagesOfAUserOperation();
                    }
                    Thread.sleep(1000);
                    break;
                case "cu":
                    if (login.getLoggedInUser().getRole() != Role.SUPERADMIN) {
                        System.out.println("You do not have the privileges to access this command.");
                    } else {
                        registerNewUserOperation();
                    }
                    Thread.sleep(1000);
                    break;
                case "vu":
                    if (login.getLoggedInUser().getRole() != Role.SUPERADMIN) {
                        System.out.println("You do not have the privileges to access this command.");
                    } else {
                        viewUserOperation();
                    }
                    Thread.sleep(1000);
                    break;
                case "ru":
                    if (login.getLoggedInUser().getRole() != Role.SUPERADMIN) {
                        System.out.println("You do not have the privileges to access this command.");
                    } else {
                        removeUserOperation();
                    }
                    Thread.sleep(1000);
                    break;
                case "uu":
                    if (login.getLoggedInUser().getRole() != Role.SUPERADMIN) {
                        System.out.println("You do not have the privileges to access this command.");
                    } else {
                        updateUserOperation();
                    }
                    Thread.sleep(1000);
                    break;
                default:
                    System.out.println("Unknown command.");
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printMainMenu() {
        System.out.println();
        login.getLoggedInUser().hasUnreadMessages();
        switch (login.getRoleLoggedInUser()) {
            case ADMIN:
                Moptions.adminMainMenu();
                break;
            case EDITOR:
                Moptions.editorMainMenu();
                break;
            case VIEWER:
                Moptions.viewerMainMenu();
                break;
            case SUPERADMIN:
                Moptions.superAdminMainMenu();
                break;
            case USER:
                Moptions.userMainMenu();
                break;
            default:
                break;
        }
    }

    private boolean isLoginSuccess() {
        System.out.print("Username : ");
        String username = sc.nextLine();

        System.out.print("Password : ");
        String password = sc.nextLine();

        return login.LogIn(username, password);
    }

    private boolean isLoginAsAdminSuccess() {
        System.out.print("Username : ");
        String username = sc.nextLine();

        System.out.print("Password : ");
        String password = sc.nextLine();

        if (username.equals("admin") && password.equals("admin")) {
            return true;
        } else {
            return false;
        }
    }


    public void sendMessageOperation() {
        System.out.println("---Send a new Message---");
        login.getDatabaseUser().printAllAvailableUsernames();
        System.out.println("Please check a user to send a message to, from the above.");
        System.out.print("Reciever(username):> ");
        String username = sc.nextLine();

        User receiver = login.getDatabaseUser().getUserByUsername(username);

        if (receiver != null) {
            System.out.println("You message cannot exceed 250 characters.");
            System.out.print("Your message:> ");
            String data = sc.nextLine();

            data = Utilities.checkMessageDataLength(sc, data);
            data = Utilities.formatMessageData(data);

            System.out.println("To add an attachment to your message, input the filepath of the file(By default MAX 4MB), otherwise press Enter to skip.");
            System.out.println("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\example.jpg");
            System.out.println("The filepath must be in the above format.");
            System.out.print("The file's filepath(otherwise press Enter to skip):> ");

            String filepath = sc.nextLine();
            File attachment = new File(filepath);

            if (filepath.equals("")) {
                System.out.println("Skipping attachment...");
                System.out.println(login.getLoggedInUser().newMessage(receiver, data));
                System.out.println("Message has been sent succesfully.");
            } else if (attachment.exists() && !attachment.isDirectory()) {
                System.out.println("Uploading file. Please wait.");
                System.out.println(login.getLoggedInUser().newMessage(receiver, data, attachment));
                System.out.println("Message has been sent succesfully.");
            } else {
                System.out.println("Wrong input for attachment.");
                System.out.println("Skipping attachment...");
                System.out.println(login.getLoggedInUser().newMessage(receiver, data));
                System.out.println("\nMessage has been sent succesfully.\n");
            }
        } else {
            System.out.println("Receiver was not found.");
        }
    }

    public void deleteOwnMessageOperation() {
        System.out.println("---Delete a message---");
        User user = login.getLoggedInUser();
        user.readOwnReceivedMessages();
        user.readOwnSentMessages();

        System.out.print("Id of message to delete(Input 0 to cancel):> ");

        String tempMid = sc.nextLine();
        while (!Utilities.isInteger(tempMid)) {
            System.out.println("Error, you must input a valid number.");
            System.out.print("Id of message to delete(Input 0 to cancel):> ");
            tempMid = sc.nextLine();
        }
        int mid = Integer.parseInt(tempMid);

        if (mid == 0) {
            System.out.println("Deletion cancelled.");
        } else if (!login.getDatabaseUser().msgIdExists(mid)) {
            System.out.println("Message with id: " + mid + " not found.");
        } else if (!login.getLoggedInUser().msgIsOwnMsg(login.getLoggedInUser().getUsername(), mid)) {
            System.out.println("Message with id: " + mid + " not yours to delete.");
        } else {
            System.out.print("The message with id: " + mid + " will be deleted.\n" +
                    "Are you sure? (y/n)");

            String yesno = sc.nextLine();

            if (yesno.equalsIgnoreCase("Y")) {
                if (user.getOwnMessage(user.getUsername(), mid).getHasAttachment()) {
                    user.deleteAttachmentofOwnMessage(user, user.getOwnMessage(user.getUsername(), mid));
                }
                user.deleteOwnMessage(user.getUsername(), mid);

                System.out.println("Message with id: " + mid + " deleted.");

            } else {
                System.out.println("Deletion canceled.");
            }
        }

    }

    private void editOwnMessageOperation() {
        User user = login.getLoggedInUser();
        System.out.println("---Edit a message---");

        user.readOwnSentMessages();
        System.out.print("Id of message to edit(Input 0 to cancel):> ");

        String tempMid = sc.nextLine();
        while (!Utilities.isInteger(tempMid)) {
            System.out.println("Error, you must input a valid number.");
            System.out.print("Id of message to edit(Input 0 to cancel):> ");
            tempMid = sc.nextLine();
        }
        int mid = Integer.parseInt(tempMid);

        if (mid == 0) {
            System.out.println("Editing cancelled.");
        } else if (!login.getDatabaseUser().msgIdExists(mid)) {
            System.out.println("Message with mid: " + mid + " not found.");
        } else if (!login.getLoggedInUser().msgIsOwnMsg(login.getLoggedInUser().getUsername(), mid)) {
            System.out.println("Message with id: " + mid + " not yours to edit.");
        } else {
            Message message = login.getDatabaseUser().getUserByUsername(user.getUsername()).getOwnMessage(user.getUsername(), mid);
            System.out.println(message);
            System.out.println("You message cannot exceed 250 characters.");
            System.out.print("Edit message (Press Enter to cancel):>");

            String data = sc.nextLine();

            data = Utilities.checkMessageDataLength(sc, data);
            if (!data.equals("")) {
                data = Utilities.formatMessageData(data);
            }
            data = Utilities.escapeSQL(data);
            String selection = "";
            File attachment = null;

            if (message.getHasAttachment() == true) {
                System.out.println("\nThis message has an attachment. What do you want to do with it?");
                System.out.println("Press Enter if you do not want to change the attachment.");
                System.out.println("Press d if you want to delete the attachment.");
                System.out.println("Press u if you want to replace the attachment.");
                System.out.print("Selection:>");
            } else {
                System.out.println("\nThis message does not have an attachment. What do you want to do?");
                System.out.println("Press u if you want to input an attachment.");
                System.out.println("Press Enter if you do not want to change the attachment.");
                System.out.print("Selection:>");
            }

            selection = sc.nextLine();

            if (selection.equals("")) {
                System.out.println("No changes will be made to the attachment.");
            } else if (selection.equals("d")) {
                System.out.println("The attachment will be deleted.");
            } else if (selection.equals("u")) {
                System.out.println("To add an attachment to your message, input the filepath of the file(By default MAX 4MB), otherwise press Enter to skip.");
                System.out.println("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\example.jpg");
                System.out.println("The filepath must be in the above format.");

                String filepath = sc.nextLine();
                attachment = new File(filepath);

                if (filepath.equals("")) {
                    System.out.println("Attachment updating cancelled.");
                    selection = "";
                } else if (attachment.exists() && !attachment.isDirectory()) {
                    System.out.println("Attachment updating will be carried out.");
                } else {
                    System.out.println("Wrong selection. Attachment updating cancelled.");
                    selection = "";
                }
            }

            if (data.equals("") && selection.equals("")) {
                System.out.println("Canceled editing.");
            } else if (selection.equals("")) {
                if (user.editOwnMessage(user, message, data) > 0) {
                    System.out.println("Message with mid: " + mid + " has been edited.");
                } else {
                    System.out.println("Something went wrong. Editing cancelled.");
                }
            } else if (data.equals("") && selection.equals("u")) {
                if (user.updateAttachmentofOwnMessage(user, message, attachment) > 0) {
                    System.out.println("Message with mid: " + mid + " has been edited.");
                } else {
                    System.out.println("Something went wrong. Editing cancelled.");
                }
            } else if (data.equals("") && selection.equals("d")) {
                if (user.deleteAttachmentofOwnMessage(user, message) > 0) {
                    System.out.println("Message with mid: " + mid + " has been edited.");
                } else {
                    System.out.println("Something went wrong. Editing cancelled.");
                }
            } else if (selection.equals("d")) {
                if (user.editOwnMessage(user, message, data) > 0 && user.deleteAttachmentofOwnMessage(user, message) > 0) {
                    System.out.println("Message with mid: " + mid + " has been edited.");
                } else {
                    System.out.println("Something went wrong. Editing cancelled.");
                }
            } else if (selection.equals("u")) {
                if (user.editOwnMessage(user, message, data) > 0 && user.updateAttachmentofOwnMessage(user, message, attachment) > 0) {
                    System.out.println("Message with mid: " + mid + " has been edited.");
                } else {
                    System.out.println("Something went wrong. Editing cancelled.");
                }

            }
        }
    }

    private void editMessagesOfAnotherUserOperation() {
        System.out.println("---Edit Messages---");

        login.getDatabaseUser().printUsersWithSentMessages();
        System.out.println("Please select a username from those displayed above, to view and select which to edit from that user's sent messages(Press Enter to cancel).");
        System.out.print("Username(Press Enter to cancel):> ");
        String username = sc.nextLine();

        if (username.equals("")) {
            System.out.println("Editing cancelled.");
        } else if (!login.getDatabaseUser().userHasSentMessages(username)) {
            System.out.println("Error. No message found, sent by " + username + ".");
        } else {
            Editor editor = (Editor) login.getLoggedInUser();
            editor.readSentMessagesOfUser(username);

            System.out.print("Id of message to edit(Input 0 to cancel):> ");

            String tempMid = sc.nextLine();
            while (!Utilities.isInteger(tempMid)) {
                System.out.println("Error, you must input a valid number.");
                System.out.print("Id of message to edit(Input 0 to cancel):> ");
                tempMid = sc.nextLine();
            }
            int mid = Integer.parseInt(tempMid);

            if (mid == 0) {
                System.out.println("Editing cancelled.");
            } else if (!login.getDatabaseUser().msgIdExists(mid)) {
                System.out.println("Message with mid: " + mid + " not found.");
            } else if (!login.getDatabaseUser().getMsgExists(username, mid)) {
                System.out.println("Message with id: " + mid + " was not sent by the user: " + username + ".");
            } else {
                Message message = ((Editor) login.getLoggedInUser()).getMessage(mid);
                System.out.println(message);
                System.out.println("You message cannot exceed 250 characters.");
                System.out.print("Edit message (Press Enter to cancel):>");

                String data = sc.nextLine();

                data = Utilities.checkMessageDataLength(sc, data);

                data = Utilities.formatMessageData(data);

                String selection = "";
                File attachment = null;

                if (message.getHasAttachment() == true) {
                    System.out.println("\nThis message has an attachment. What do you want to do with it?");
                    System.out.println("Press Enter if you do not want to change the attachment.");
                    System.out.println("Press d if you want to delete the attachment.");
                    System.out.println("Press u if you want to replace the attachment.");
                    System.out.print("Selection:>");
                } else {
                    System.out.println("\nThis message does not have an attachment. What do you want to do?");
                    System.out.println("Press u if you want to input an attachment.");
                    System.out.println("Press Enter if you do not want to change the attachment.");
                    System.out.print("Selection:>");
                }

                selection = sc.nextLine();

                if (selection.equals("")) {
                    System.out.println("No changes will be made to the attachment.");
                } else if (selection.equals("d")) {
                    System.out.println("The attachment will be deleted.");
                } else if (selection.equals("u")) {
                    System.out.println("To add an attachment to your message, input the filepath of the file(By default MAX 4MB), otherwise press Enter to skip.");
                    System.out.println("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\example.jpg");
                    System.out.println("The filepath must be in the above format.");

                    String filepath = sc.nextLine();
                    attachment = new File(filepath);

                    if (filepath.equals("")) {
                        System.out.println("Attachment updating cancelled.");
                        selection = "";
                    } else if (attachment.exists() && !attachment.isDirectory()) {
                        System.out.println("Attachment updating will be carried out.");
                    } else {
                        System.out.println("Wrong selection. Attachment updating cancelled.");
                        selection = "";
                    }
                }
                if (data.equals("") && selection.equals("")) {
                    System.out.println("Canceled editing.");
                } else if (data.equals("") && selection.equals("u")) {
                    if (editor.updateAttachmentofMessage(editor, message, attachment) > 0) {
                        System.out.println("Message with mid: " + mid + " has been edited.");
                    } else {
                        System.out.println("Something went wrong. Editing cancelled.");
                    }
                } else if (data.equals("") && selection.equals("d")) {
                    if (editor.deleteAttachmentofOwnMessage(editor, message) > 0) {
                        System.out.println("Message with mid: " + mid + " has been edited.");
                    } else {
                        System.out.println("Something went wrong. Editing cancelled.");
                    }
                } else if (selection.equals("")) {
                    if (editor.editMessage(editor, message, data) > 0) {
                        System.out.println("Message with mid: " + mid + " has been edited.");
                    } else {
                        System.out.println("Something went wrong. Editing cancelled.");
                    }
                } else if (selection.equals("d")) {
                    if (editor.editMessage(editor, message, data) > 0 && editor.deleteAttachmentofOwnMessage(editor, message) > 0) {
                        System.out.println("Message with mid: " + mid + " has been edited.");
                    } else {
                        System.out.println("Something went wrong. Editing cancelled.");
                    }
                } else if (selection.equals("u")) {
                    if (editor.editMessage(editor, message, data) > 0 && editor.updateAttachmentofMessage(editor, message, attachment) > 0) {
                        System.out.println("Message with mid: " + mid + " has been edited.");
                    } else {
                        System.out.println("Something went wrong. Editing cancelled.");
                    }
                }
            }
        }
    }


    private void readMessagesOfAUserOperation() {
        System.out.println("---View Messages of User---");

        login.getDatabaseUser().printUsersWithSentMessages();
        System.out.println("Please select a username from those displayed above, to view that user's sent messages(Press Enter to cancel).");
        System.out.print("Enter:> ");
        String username = sc.nextLine();

        if (username.equals("")) {
            System.out.println("Viewing cancelled.");
        } else if (!login.getDatabaseUser().userHasSentMessages(username)) {
            System.out.println("Error. User " + username + " was not found");
        } else {
            Viewer viewer = (Viewer) login.getLoggedInUser();
            viewer.readSentMessagesOfUser(username);
            System.out.println("Do you want to fully view one of these messages?");
            System.out.print("Please input yes or no (y/n):>");
            String selection = sc.nextLine();

            switch (selection) {
                case "y":
                    viewSpecificMessageOperation(username);
                    break;
                case "n":
                    break;
                default:
                    System.out.println("Wrong selection. Exiting...");
                    break;
            }
        }
    }

    private void viewSpecificMessageOperation(String username) {
        System.out.print("Please input the id of the message you want to fully view(Input 0 to cancel):> ");

        String tempMid = sc.nextLine();
        while (!Utilities.isInteger(tempMid)) {
            System.out.println("Error, you must input a valid number.");
            System.out.print("Please input the id of the message you want to fully view(Input 0 to cancel):> ");
            tempMid = sc.nextLine();
        }
        int mid = Integer.parseInt(tempMid);

        if (mid == 0) {
            System.out.println("Viewing cancelled.");
        } else if (!login.getDatabaseUser().msgIdExists(mid)) {
            System.out.println("Message with id: " + mid + " not found");
        } else if (!login.getDatabaseUser().getMsgExists(username, mid)) {
            System.out.println("Message with id: " + mid + " was not sent by the user: " + username + ".");
        } else {
            Viewer viewer = (Viewer) login.getLoggedInUser();
            Message message = viewer.getMessage(mid);
            if (message.getHasAttachment()) {
                System.out.println("This message has an attachment. The attachment is " + message.getFilename() + " Would you like to open it?");
                System.out.print("Please input (y) for yes or (n) for no.:>");
                String selection = sc.nextLine();

                switch (selection) {
                    case "y":
                        System.out.println("Attachment will now open.");
                        login.getDatabaseUser().openAttachmentbyId(mid);
                        break;
                    case "n":
                        System.out.println("Attachment will not open.");
                        break;
                    default:
                        System.out.println("Wrong selection. Attachment will not open.");
                        break;
                }
            } else {
                System.out.println("No attachment.");
            }
            System.out.println(message);
        }
    }

    private void viewOwnSpecificMessageOperation() {
        System.out.println("---View specific Message---");
        login.getDatabaseUser().viewReceivedMessages(login.getLoggedInUser());
        login.getDatabaseUser().viewSentMessages(login.getLoggedInUser().getUsername());

        System.out.println("Please input the id of the message you want to fully view(Input 0 to cancel).");
        System.out.print("Enter:> ");

        String tempMid = sc.nextLine();
        while (!Utilities.isInteger(tempMid)) {
            System.out.println("Error, you must input a valid number.");
            System.out.println("Please input the id of the message you want to fully view(Input 0 to cancel).");
            System.out.print("Enter:> ");
            tempMid = sc.nextLine();
        }
        int mid = Integer.parseInt(tempMid);

        if (mid == 0) {
            System.out.println("Viewing cancelled.");
        } else if (!login.getDatabaseUser().msgIdExists(mid)) {
            System.out.println("Message with id: " + mid + " not found.");
        } else if (!login.getLoggedInUser().msgIsOwnMsg(login.getLoggedInUser().getUsername(), mid)) {
            System.out.println("Message with id: " + mid + " not yours to view.");
        } else {
            Message message = login.getLoggedInUser().getOwnMessage(login.getUsernameLoggedInUser(), mid);
            if (message.getHasAttachment()) {
                System.out.println("This message has an attachment. The attachment is " + message.getFilename() + " Would you like to open it?");
                System.out.print("Please input (y) for yes or (n) for no.:>");
                String selection = sc.nextLine();

                switch (selection) {
                    case "y":
                        System.out.println("Attachment will now open.");
                        login.getDatabaseUser().openAttachmentbyId(mid);
                        break;
                    case "n":
                        System.out.println("Attachment will not open.");
                        break;
                    default:
                        System.out.println("Wrong selection. Attachment will not open.");
                        break;
                }
            } else {
                System.out.println("No attachment.");
            }
            login.getLoggedInUser().setOwnMessagaAsRead(login.getLoggedInUser(), mid);
            System.out.println(message);
        }
    }

    private void deleteMessagesOfAUserOperation() {
        System.out.println("---Delete Message---");

        login.getDatabaseUser().printUsersWithSentMessages();
        System.out.println("Please select a username from those displayed above, to view and select which to delete from that user's sent messages(Press Enter to cancel).");
        System.out.print("Enter:> ");
        String username = sc.nextLine();

        if (username.equals("")) {
            System.out.println("Deletion cancelled.");
        } else if (!login.getDatabaseUser().userHasSentMessages(username)) {
            System.out.println("Error. User " + username + " was not found");
        } else {
            Admin admin = (Admin) login.getLoggedInUser();
            admin.readSentMessagesOfUser(username);

            System.out.print("Please input the id of message to delete(Input 0 to cancel):> ");

            String tempMid = sc.nextLine();
            while (!Utilities.isInteger(tempMid)) {
                System.out.println("Error, you must input a valid number.");
                System.out.print("Please input the id of message to delete(Input 0 to cancel):> ");
                tempMid = sc.nextLine();
            }
            int mid = Integer.parseInt(tempMid);

            if (mid == 0) {
                System.out.println("Deletion cancelled.");
            } else if (!login.getDatabaseUser().msgIdExists(mid)) {
                System.out.println("Message with id: " + mid + " not found.");
            } else if (!login.getDatabaseUser().getMsgExists(username, mid)) {
                System.out.println("Message with id: " + mid + " was not sent by the user: " + username + ".");
            } else {
                System.out.print("The message with id: " + mid + " will be deleted.\n" +
                        "Are you sure? (y/n)");

                String yesno = sc.nextLine();

                if (yesno.equalsIgnoreCase("y")) {
                    if (admin.getMessage(mid).getHasAttachment()) {
                        if (admin.deleteAttachment(mid) > 0 && admin.deleteMessage(mid) > 0) {
                            System.out.println("Message deleted");
                        } else {
                            System.out.println("Error. Something went wrong.");
                            System.out.println("Deletion cancelled...");
                        }
                    } else {
                        if (admin.deleteMessage(mid) > 0) {
                            System.out.println("Message deleted");
                        } else {
                            System.out.println("Error. Something went wrong.");
                            System.out.println("Deletion cancelled...");
                        }
                    }
                } else {
                    System.out.println("Deletion canceled");
                }
            }
        }


    }

    private void registerNewUserOperation() {
        System.out.println("---Register New User---");
        System.out.println("Username and password must have at least 5 and at most 45 characters each.");
        System.out.print("Username :> ");
        String username = sc.nextLine();

        username = Utilities.checkUsernameOrPasswordLength(sc, username, "Username");

        if (!login.getDatabaseUser().isUsernameUsed(username)) {
            System.out.print("Password :> ");
            String password = sc.nextLine();

            password = Utilities.checkUsernameOrPasswordLength(sc, password, "Password");
            System.out.print("Role (user/viewer/editor/admin):> ");
            String roleString = sc.nextLine().toUpperCase();

            Role role = Role.USER;

            switch (roleString) {
                case "USER":
                    role = Role.USER;
                    break;
                case "VIEWER":
                    role = Role.VIEWER;
                    break;
                case "EDITOR":
                    role = Role.EDITOR;
                    break;
                case "ADMIN":
                    role = Role.ADMIN;
                    break;
                default:
                    System.out.println("Did not enter a valid role. Role is now by default USER.");
            }

            SuperAdmin superadmin = (SuperAdmin) login.getLoggedInUser();
            if (superadmin.registerUser(username, password, role) > 0) {
                System.out.println("User: " + username + " registered succesfully.");
            } else {
                System.out.println("Something went wrong. No registration was carried out.");
            }
        } else {
            System.out.println("Error. A user with username: " + username + " already exists.");
        }
    }

    private void removeUserOperation() {
        System.out.println("---Remove User---");
        login.getDatabaseUser().printAllAvailableUsernames();
        System.out.print("Select a user, from above list to remove: ");
        String username = sc.nextLine();

        if (login.getDatabaseUser().isUsernameUsed(username) && !username.equals("admin")) {
            System.out.print("Are you sure you want remove user " + username + "? (y/n): ");
            String c = sc.nextLine();

            if (c.equalsIgnoreCase("y")) {
                SuperAdmin superadmin = (SuperAdmin) login.getLoggedInUser();
                if (superadmin.removeUser(username) > 0) {
                    System.out.println("User " + username + " removed.");
                } else {
                    System.out.println("Something went wrong. Removal was cancelled.");
                }
            } else {
                System.out.println("Removal cancelled.");
            }
        } else if (username.equals("admin")) {
            System.out.println("Error. User 'admin' is the SuperAdmin and cannot be deleted.");
        } else {
            System.out.println("Error. A user with username: " + username + " does not exist.");
        }
    }

    private void viewUserOperation() {
        System.out.println("---View User---");
        login.getDatabaseUser().printAllAvailableUsernames();
        System.out.print("Select a user, from above list to view: ");
        String username = sc.nextLine();

        if (login.getDatabaseUser().isUsernameUsed(username)) {
            SuperAdmin superadmin = (SuperAdmin) login.getLoggedInUser();
            superadmin.viewUser(username);
        } else {
            System.out.println("Error. A user with username: " + username + " does not exist.");
        }
    }

    private void updateUserOperation() {
        System.out.println("---Update User---");
        login.getDatabaseUser().printAllAvailableUsernames();
        System.out.print("Select a user, from above list to view: ");
        String username = sc.nextLine();

        if (login.getDatabaseUser().isUsernameUsed(username) && !username.equals("admin")) {
            boolean changed = false;

            User usertoupdate = login.getDatabaseUser().getUserByUsername(username);
            System.out.println("Current values of user:");
            System.out.println("\t\t" + usertoupdate.toString());

            System.out.println("Username must have at least 5 and at most 45 characters.");
            System.out.print("Enter new username (Press Enter to skip.):> ");
            String newusername = sc.nextLine();

            if (newusername.equals("")) {
                newusername = usertoupdate.getUsername();
            } else if (newusername.length() > 0) {
                newusername = Utilities.checkUsernameOrPasswordLength(sc, newusername, "Username");
            } else {
                changed = true;
            }

            System.out.println("Password must have at least 5 and at most 45 characters.");
            System.out.print("Enter new password (Press Enter to skip.):> ");
            String newpassword = sc.nextLine();

            if (newpassword.equals("")) {
                newpassword = usertoupdate.getPassword();
            } else if (newpassword.length() > 0) {
                newpassword = Utilities.checkUsernameOrPasswordLength(sc, newpassword, "Password");
            } else {
                changed = true;
            }

            System.out.print("Enter new role(user/viewer/editor/admin) (Press Enter to skip.):> ");
            String newroleString = sc.nextLine().toUpperCase();
            Role newrole = usertoupdate.getRole();

            switch (newroleString) {
                case "ADMIN":
                    newrole = Role.ADMIN;
                    changed = true;
                    break;
                case "EDITOR":
                    newrole = Role.EDITOR;
                    changed = true;
                    break;
                case "VIEWER":
                    newrole = Role.VIEWER;
                    changed = true;
                    break;
                case "USER":
                    newrole = Role.USER;
                    changed = true;
                    break;
                case "":
                    newrole = usertoupdate.getRole();
                    changed = true;
                    break;
                default:
                    System.out.println("Didn't understand the role.No change to role");
            }

            if (changed) {
                SuperAdmin superadmin = (SuperAdmin) login.getLoggedInUser();
                if (superadmin.updateUser(usertoupdate.getUsername(), newusername, newpassword, newrole) > 0) {
                    System.out.println("User " + newusername + " updated succesfully");
                } else {
                    System.out.println("Something went wrong. Update was cancelled");
                }
            } else {
                System.out.println("No change was made.");
            }
        } else if (username.equals("admin")) {
            System.out.println("Error. User 'admin' is the SuperAdmin and cannot be edited.");
        } else {
            System.out.println("Error. A user with username: " + username + " does not exist.");
        }
    }
}