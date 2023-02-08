package general;

import database.MySQL;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static hopp.schedulingapp.Main.rb;

/**
 * Scene switching and Stage displaying.
 */
public class FormNavigation {

    /**
     * Prompt the user. If yes, close the DB conn and exit the application.
     */
    public static void exitApplication(){

        // Confirm application exit
        if (Messages.displayConfirm(rb.getString("Confirm") + " " + rb.getString("Exit"),
                rb.getString("Are") + " " + rb.getString("you") + " " + rb.getString("sure") + " " +
                        rb.getString("you") + " " +rb.getString("want") + " " +rb.getString("to") + " " +
                        rb.getString("exit") + " " +rb.getString("the") + " " +rb.getString("Inventory") + " " +
                        rb.getString("Management") + " " +rb.getString("System") + "?").get() == ButtonType.OK) {
            // Close DB conn
            MySQL.closeDBconn();
            System.exit(0);
        }
    }

    /**
     * Switch to and show the Menu scene.
     * @param root Parent for the scene
     * @param actionEvent actionEvent from calling event
     */
    public static void changeSceneToMenu(Parent root, ActionEvent actionEvent) {
        showStage(root, actionEvent, 530, 400, "Scheduling Application");
    }

    /**
     * Switch to and show the Customer Records scene.
     * @param root Parent for the scene
     * @param actionEvent actionEvent from calling event
     */
    public static void changeSceneToCustomerRecords(Parent root, ActionEvent actionEvent) {
        showStage(root, actionEvent, 937, 474, "Scheduling Application - Contact Records");
    }

    /**
     * Switch to and show the Scheduling scene.
     * @param root Parent for the scene
     * @param actionEvent actionEvent from calling event
     */
    public static void changeSceneToScheduling(Parent root, ActionEvent actionEvent) {
        showStage(root, actionEvent, 1442, 570, "Scheduling Application - Scheduling");
    }

    /**
     * Switch to and show the Local Customers Report scene.
     * @param root Parent for the scene
     * @param actionEvent actionEvent from calling event
     */
    public static void changeSceneToReportLocalCustomers(Parent root, ActionEvent actionEvent) {
        showStage(root, actionEvent, 460, 400, "Scheduling Application - Local Customers");
    }

    /**
     * Switch to and show the Customer Appointment Totals Report scene.
     * @param root Parent for the scene
     * @param actionEvent actionEvent from calling event
     */
    public static void changeSceneToReportCustomerApptTotals(Parent root, ActionEvent actionEvent) {
        showStage(root, actionEvent, 600, 400, "Scheduling Application - Customer Appointment Totals");
    }

    /**
     * Switch to and show the Contact Schedules Report scene.
     * @param root Parent for the scene
     * @param actionEvent actionEvent from calling event
     */
    public static void changeSceneToReportContactSchedules(Parent root, ActionEvent actionEvent) {
        showStage(root, actionEvent, 933, 400, "Scheduling Application - Contact Schedules");
    }

    /**
     * Create a stage, set is scene, title, dimensions, and display it in the center of the screen
     * @param root Parent for the scene
     * @param actionEvent actionEvent from calling event
     * @param width The scene's width
     * @param height The scene's height
     * @param title The scene's title
     */
    private static void showStage(Parent root, ActionEvent actionEvent, int width,
                                  int height, String title){
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, width, height);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

}
