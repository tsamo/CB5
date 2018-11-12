package other;

/**
 * @author tsamo
 */
public class Moptions {

    public Moptions() {
    }

    public static void check() {
        System.out.println("Welcome!!");
        System.out.println("If this is your first time running this application it is HIGHLY recommended to visit the info page.");
        System.out.println("s     -> Start the application.");
        System.out.println("i     -> Information for this application.");
        System.out.println("exit  -> Exit this application.");
        System.out.print("Enter:> ");
    }

    public static void welcome() {
        System.out.println("--Prototype Messaging Application--");
        System.out.println("Welcome to the application");
        System.out.println("Please log in to continue\n");
    }

    public static void infoOptions(){
        System.out.println("b     -> Go back to the previous screen.");
        System.out.println("exit  -> Exit");
        System.out.print("Enter:> ");
    }

    private static void userOptions() {

        System.out.println("s     -> Send a message.");
        System.out.println("n     -> View all of your unread messages.");
        System.out.println("o     -> View all of your sent messages.");
        System.out.println("i     -> View all of your received messages.");
        System.out.println("v     -> View a specific message from you sent and received messages.");
        System.out.println("e     -> Edit one of your sent messages");
        System.out.println("d     -> Delete one of your messages");
        System.out.println("l     -> Logout");
        System.out.println("exit  -> Exit");
        System.out.println("------------------------\n");
    }

    private static void viewerOptions() {
        System.out.println("r     -> Read another user's sent messages");
    }

    private static void editorOptions() {
        System.out.println("ee    -> Edit another user's sent messages");
    }

    private static void adminOptions() {
        System.out.println("del   -> Delete another user's sent messages");
    }

    private static void superAdminOptions() {
        System.out.println("cu    -> Register new user");
        System.out.println("vu    -> View user");
        System.out.println("ru    -> Remove user");
        System.out.println("uu    -> Update user");
    }

    static void userMainMenu() {
        System.out.println("------User menu------");
        userOptions();
    }

    static void viewerMainMenu() {
        System.out.println("------Viewer menu------");
        viewerOptions();
        userOptions();
    }

    static void editorMainMenu() {
        System.out.println("------Editor menu------");
        editorOptions();
        viewerOptions();
        userOptions();
    }

    static void adminMainMenu() {
        System.out.println("------Admin menu------");
        adminOptions();
        editorOptions();
        viewerOptions();
        userOptions();
    }

    static void superAdminMainMenu() {
        System.out.println("------Super Admin menu------");
        superAdminOptions();
        adminOptions();
        editorOptions();
        viewerOptions();
        userOptions();
    }
}