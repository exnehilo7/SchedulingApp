package hopp.schedulingapp;

import database.MySQL;
import general.Appointment;
import general.FormNavigation;
import general.Messages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ResourceBundle;

import static hopp.schedulingapp.Main.rb;

public class FormLogin implements Initializable {
    /**
     * Label for username login errors
     */
    public Label lblLoginUNError;
    /**
     * Label for password login errors
     */
    public Label lblLoginPWError;
    /**
     * Label to display the user's location
     */
    public Label lblLoginLoc;
    /**
     * Text field for the username
     */
    public TextField txtLoginUN;
    /**
     *  Text field for the password
     */
    public TextField txtLoginPW;
    /**
     * Label for login text field
     */
    public Label lblLoginUN;
    /**
     * Label for password text field
     */
    public Label lblLoginPW;
    /**
     * Button to login
     */
    public Button btnLogin;
    /**
     * Button to exit
     */
    public Button btnLoginExitApp;
    /**
     * Label for form name
     */
    public Label lblLogin;
    /**
     * Local zone ID
     */
    private ZoneId zidLocal;



    /**
     * Connect to the MySQL database. Get the user's local zone and display it on the form. Set the resource bundle to
     * the user's default locale. Display its labels and form messages in en or fr.
     * @param url Default url parameter for initialize method
     * @param resourceBundle Default resourceBundle parameter for initialize method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Connect to the DB
        MySQL.connectToDB();

        // Display local zone id
        zidLocal = ZoneId.systemDefault();
        lblLoginLoc.setText(rb.getString("Your") + " " + rb.getString("location") + ": " + zidLocal);

        // Set form labels
        lblLoginUN.setText(rb.getString("User") + " " + rb.getString("Name") + ":");
        lblLoginPW.setText(rb.getString("Password") + ":");
        btnLogin.setText(rb.getString("Login"));
        btnLoginExitApp.setText(rb.getString("Exit"));
        lblLogin.setText(rb.getString("Login"));

    }

    /**
     * If the UN and PW are not blank, login to the application. Else, display localized message feedback on the form.
     * Log to login_activity.txt all login attempts and their result along with the date and time. Save to the root
     * folder of the app. If there is an appointment within 15 minutes of the login, alert the user. If no appointments
     * within 15 minutes, inform the user.
     * @param actionEvent Default actionEvent parameter for event listener
     * @throws IOException Default IOException error throw
     */
    public void loginToApp(ActionEvent actionEvent) throws IOException{

        String username = txtLoginUN.getText().strip();
        String password = txtLoginPW.getText().strip();

        LocalDateTime ldt = LocalDateTime.now();

        if (areLoginFieldsValid(username, password)) {
            // Query Username and PW. If no match on either, display results in Login form label fields.
            if (MySQL.isAppLoginValid(username, password)) {

                // If login is valid, check if there's an appointment within 15 minutes, inclusive.
                ObservableList<Appointment> list = FXCollections.observableArrayList();
                MySQL.getUpcomingAppointment(list, ldt);

                // If appt, pop-up with the apptID, date and time (pass localtime to DB appts table. get start
                // datetime closest to user's localtime)
                if (list.size() > 0){
                    Appointment appt = list.get(0);
                    general.Messages.displayInformation(rb.getString("Upcoming") + " " + rb.getString("Appointments"),
                            rb.getString("Appointment") + " " + rb.getString("ID") + " " +
                                    appt.getApptId() + ": " + appt.getTitle() + " " + rb.getString("in") + " " +
                                    appt.getLocation() + " " +
                                    rb.getString("starts") + " " + rb.getString("within") + " 15 " +
                                    rb.getString("minutes") + ".");
                }else {
                    // Else no appt, pop up indicating there are none.
                    general.Messages.displayInformation(rb.getString("Upcoming") + " " + rb.getString("Appointments"),
                            rb.getString("There") + " " + rb.getString("are") + " " +
                                    rb.getString("no") + " " + rb.getString("upcoming") + " " +
                                    rb.getString("appointments") + ".");
                }

                // Record the successful login to login_activity.txt. Date and time. Save to root folder of the app.
                writeToLog(ldt, username, "successful");
                // Switch forms
                Parent root = FXMLLoader.load(getClass().getResource("frmMenu.fxml"));
                FormNavigation.changeSceneToMenu(root, actionEvent);

            } else {
                whichFieldsAreInvalid(username, password);
                // Record the unsuccessful login to login_activity.txt. Date and time. Save to root folder of the app.
                writeToLog(ldt, username, "unsuccessful");
            }
        }
    }

    /**
     * Create a text file if it does not exist. Record all login attempts: Username, date and time,result of login.
     * @param ldt The localtime of the login attempt
     * @param username The user's name
     * @param result The login result
     */
    private void writeToLog(LocalDateTime ldt, String username, String result){
        try {
            String login = ldt + ": Login " + result + ". User: " + username;
            File f = new File("./login_activity.txt");
            if(!f.exists()) {
                f.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(f.getName(),true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(login);
            bw.newLine();
            bw.close();
        } catch(IOException e){
            general.Messages.displayError(Messages.getMethodName(), e.getMessage());
        }
    }

    /**
     * Return true if all login form fields are not blank. If a field is blank, display a message in a label. If the
     * field is not blank, clear the label's text.
     * @param username The username text field
     * @param password The password text field
     * @return Return true if successful. False if not.
     */
    private boolean areLoginFieldsValid (String username, String password){

        Boolean result = true;

        // Is the username not blank?
        if (username.length() < 1){
            general.Messages.changeLabelText(lblLoginUNError,rb.getString("The") + " " +
                    rb.getString("username") + " " + rb.getString("cannot") + " " +
                    rb.getString("be") + " " + rb.getString("blank") + "!");
            result = false;
        }
        else{
            general.Messages.changeLabelText(lblLoginUNError,"");
        }

        // Is password not blank?
        if (password.length() < 1) {
            general.Messages.changeLabelText(lblLoginPWError,rb.getString("The") + " " +
                    rb.getString("password") + " " + rb.getString("cannot") + " " +
                    rb.getString("be") + " " + rb.getString("blank") + "!");
            result = false;
        }
        else{
            general.Messages.changeLabelText(lblLoginPWError,"");
        }

        return result;
    }

    /**
     * Determine which fields were invalid.
     * @param username The username text field
     * @param password The password text field
     */
    private void whichFieldsAreInvalid(String username, String password){
        // Is UN invalid?
        if (MySQL.isUsernameInvalid(username)) {

            general.Messages.changeLabelText(lblLoginUNError, rb.getString("The") + " " +
                    rb.getString("username") + " " + rb.getString("is") + " " +
                    rb.getString("invalid") + "!");
            general.Messages.changeLabelText(lblLoginPWError,"");
        }
        else{
            general.Messages.changeLabelText(lblLoginUNError,"");
            general.Messages.changeLabelText(lblLoginPWError, rb.getString("The") + " " +
                    rb.getString("password") + " " + rb.getString("is") + " " +
                    rb.getString("invalid") + "!");
        }
    }

    /**
     * Exit the application.
     * @param actionEvent Default actionEvent parameter for event listener
     */
    public void loginFormExitApp(ActionEvent actionEvent) {
        FormNavigation.exitApplication();
    }
}
