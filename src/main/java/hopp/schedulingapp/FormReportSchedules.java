package hopp.schedulingapp;

import database.MySQL;
import general.Appointment;
import general.DropdownList;
import general.FormNavigation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Appointments by Contact Report form
 */
public class FormReportSchedules implements Initializable {
    /**
     * Table view for the reports
     */
    public TableView tblReportSchedules;
    /**
     * Column for the appointment ID
     */
    public TableColumn colReportSchedulesApptID;
    /**
     * Column for the appointment title
     */
    public TableColumn colReportSchedulesTitle;
    /**
     * Column for the appointment type
     */
    public TableColumn colReportSchedulesType;
    /**
     * Column for the appointment description
     */
    public TableColumn colReportSchedulesDesc;
    /**
     * Column for the contact name
     */
    public TableColumn colReportSchedulesContactName;
    /**
     * Column for the customer ID
     */
    public TableColumn colReportSchedulesCustID;
    /**
     *  Column for the customer name
     */
    public TableColumn colReportSchedulesCustName;
    /**
     * Column for the appointment start datetime
     */
    public TableColumn colReportSchedulesStart;
    /**
     * Column for the appointment end datetime
     */
    public TableColumn colReportSchedulesEnd;
    /**
     * Combo box for the contact name
     */
    public ComboBox cboReportSchedulesContactName;
    /**
     * ObservableList for appointments
     */
    private ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
    /**
     * ObservableList for contacts combo box
     */
    private ObservableList<DropdownList> contacts = FXCollections.observableArrayList();

    /**
     * Initialize the form. Set fields to default, set Node IDs for pop-up messages, populate combo boxes with DB data,
     * and prep table view.
     * @param url Default url parameter for initialize method
     * @param resourceBundle Default resourceBundle parameter for initialize method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Set fields to default
        clearFields();

        // Set Node IDs for user messages
        cboReportSchedulesContactName.setId("Contact");

        //Populate combo boxes with DB data
        cboReportSchedulesContactName.setItems(MySQL.getComboBoxItems("contacts", "Contact_ID", "Contact_Name",
                contacts));

        //Prep table view
        colReportSchedulesApptID.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        colReportSchedulesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colReportSchedulesDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colReportSchedulesContactName.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colReportSchedulesType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colReportSchedulesStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colReportSchedulesEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colReportSchedulesCustID.setCellValueFactory(new PropertyValueFactory<>("custID"));
        colReportSchedulesCustName.setCellValueFactory(new PropertyValueFactory<>("customer"));


    }

    /**
     * Refresh the table view's data when an item is selected in the combo box.
     * @param actionEvent Default action event
     */
    public void reportSchedulesRefreshTbl(ActionEvent actionEvent) {
        setReportFilter();

    }

    /**
     * Populate the table view with the report based on the selected combo box's item.
     */
    private void setReportFilter(){

        tblReportSchedules.getItems().clear();
        contactAppointments.clear();

        int selectedItem = cboReportSchedulesContactName.getSelectionModel().getSelectedIndex() + 1;

        tblReportSchedules.getItems().addAll(MySQL.getAppointments('c', contactAppointments, selectedItem));
    }

    /**
     * Open the main menu form
     * @param actionEvent Default action event
     * @throws IOException Default IOException error throw
     */
    public void reportSchedulesOpenMenu(ActionEvent actionEvent) throws IOException {
        // Switch forms
        Parent root = FXMLLoader.load(getClass().getResource("frmMenu.fxml"));
        FormNavigation.changeSceneToMenu(root, actionEvent);
    }

    /**
     * Clear the current selected item from the combo box.
     */
    private void clearFields() {

        cboReportSchedulesContactName.getSelectionModel().clearSelection();

        // Clear lists
        contacts.clear();

    }

}
