package hopp.schedulingapp;

import database.MySQL;
import general.FormNavigation;
import general.Messages;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;

import java.io.IOException;

/**
 * Main menu form.
 */
public class FormMenu {
    /**
     * Open the Customer Records form
     * @param actionEvent Default actionEvent parameter for event listener
     * @throws IOException Default IOException error throw
     */
    public void mainOpenCustomerRecords(ActionEvent actionEvent) throws IOException {

        // Switch forms
        Parent root = FXMLLoader.load(getClass().getResource("frmCustomerRecords.fxml"));
        FormNavigation.changeSceneToCustomerRecords(root, actionEvent);
    }

    /**
     * Open the Scheduling form
     * @param actionEvent Default actionEvent parameter for event listener
     * @throws IOException Default IOException error throw
     */
    public void mainOpenScheduling(ActionEvent actionEvent) throws IOException {
        // Switch forms
        Parent root = FXMLLoader.load(getClass().getResource("frmScheduling.fxml"));
        FormNavigation.changeSceneToScheduling(root, actionEvent);
    }

    /**
     * Open the Schedules by Contact Report form
     * @param actionEvent Default actionEvent parameter for event listener
     * @throws IOException Default IOException error throw
     */
    public void mainOpenRptContactSchedules(ActionEvent actionEvent) throws IOException {
        // Switch forms
        Parent root = FXMLLoader.load(getClass().getResource("frmReportSchedules.fxml"));
        FormNavigation.changeSceneToReportContactSchedules(root, actionEvent);
    }

    /**
     * Open the Appointment Totals by Type and Month Report form
     * @param actionEvent Default actionEvent parameter for event listener
     * @throws IOException Default IOException error throw
     */
    public void mainOpenRptApptTotals(ActionEvent actionEvent) throws IOException {
        // Switch forms
        Parent root = FXMLLoader.load(getClass().getResource("frmReportCustomerAppts.fxml"));
        FormNavigation.changeSceneToReportCustomerApptTotals(root, actionEvent);
    }

    /**
     * Open the Customers by Country Report form
     * @param actionEvent Default actionEvent parameter for event listener
     * @throws IOException Default IOException error throw
     */
    public void mainOpenRptLocalCustomers(ActionEvent actionEvent) throws IOException {
        // Switch forms
        Parent root = FXMLLoader.load(getClass().getResource("frmReportLocalCustomers.fxml"));
        FormNavigation.changeSceneToReportLocalCustomers(root, actionEvent);
    }

    /**
     * Close the current form
     * @param actionEvent Default actionEvent parameter for event listener
     */
    public void mainExitProgram(ActionEvent actionEvent)  {

        FormNavigation.exitApplication();

    }
}
