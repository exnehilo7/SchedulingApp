package hopp.schedulingapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Java's Main class
 */
public class Main extends Application {

    /**
     * String format for text date displays
     */
    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

    /**
     * The ID of the logged-in user
     */
    public static int USER_ID;

    /**
     * Variable for the language resource bundle.
     */
    public static ResourceBundle rb = null;

    /**
     * The MySQL Connector Java driver version used is mysql-connector-j-8.0.31.jar.
     * The Javadoc files are located in the inventoryManagement folder. Look for a folder named JavaDoc.
     * The README and login files are located in the application's root folder.
     *
     * Main method to start the application.
     * @param args Java's default Main parameter
     */
    public static void main(String[] args) {

        // Set the localization text
        Locale loc = Locale.getDefault();
        if (loc.toString().contains("en") || loc.toString().contains("fr")) {
            rb = ResourceBundle.getBundle("LoginForm", loc);
        }
        else {
            general.Messages.displayInformation("Unlisted Language", "This application cannot display " +
                    "your machine's selected language. Please change your operating system's display language to " +
                    "English or French.");
            System.exit(0);
        }

        // Start app
        launch();
    }


    /**
     * Set and display the login form.
     * @param stage JavaFX's stage
     * @throws IOException Default IOException error throw
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("frmLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 478, 251);
        stage.setTitle(rb.getString("Scheduling") + " " + rb.getString("Application") + " " +
                rb.getString("Login"));
        stage.setScene(scene);
        stage.show();
    }


}