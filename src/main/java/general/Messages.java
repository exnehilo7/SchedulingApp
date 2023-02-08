package general;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

/**
 * Class for pop up messaging
 */
public class Messages {

    /**
     * Change a label's text.
     * @param label A form's label
     * @param text The text to change the label text to
     */
    public static void changeLabelText(Label label, String text){
        label.setText(text);
    }

    /**
     * Display a warning message to the user.
     * @param title Alert title
     * @param message Alert message
     */
    public static void displayWarning(String title, String message){
        // Create alert and display. Wait for user acknowledgement.
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Display an error message to the user.
     * @param title Alert title
     * @param message Alert message
     */
    public static void displayError(String title, String message){
        // Create alert and display. Wait for user acknowledgement.
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Display an information message to the user.
     * @param title Alert title
     * @param message Alert message
     */
    public static void displayInformation(String title, String message){
        // Create alert and display. Wait for user acknowledgement.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Display a confirmation message to the user.
     * @param title Alert title
     * @param message Alert message
     * @return Return user's selection
     */
    public static Optional displayConfirm(String title, String message){
        // Create alert and display. Wait for user acknowledgement.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        Optional<ButtonType> option = alert.showAndWait();
        return option;
    }

    /**
     * Return the calling method's name from the stack.
     * @return Return the calling method's name.
     */
    public static String getMethodName(){
        String methodName = new Throwable().getStackTrace()[1].getMethodName();
        return methodName;
    }
}
