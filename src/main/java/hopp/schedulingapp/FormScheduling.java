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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static hopp.schedulingapp.Main.DTF;

/**
 * Controller class for the scheduling form. Appointment times are always displayed in the context of the system's
 * default ZoneId. After deleting an appointment, a message confirms that the appointment was deleted,
 * displaying its id and type.
 */
public class FormScheduling implements Initializable {
    /**
     * Text field for the appointment ID
     */
    public TextField txtSchedulingApptID;
    /**
     * Text field for the appointment title
     */
    public TextField txtSchedulingTitle;
    /**
     * Text field for the appointment description
     */
    public TextField txtSchedulingDesc;
    /**
     * Text field for the appointment location
     */
    public TextField txtSchedulingLocation;
    /**
     * Text field for the appointment type
     */
    public TextField txtSchedulingType;
    /**
     * Combo box for the appointment contact
     */
    public ComboBox cboSchedulingContact;
    /**
     * Combo box for the customer
     */
    public ComboBox cboSchedulingCustID;
    /**
     * Combo box for the user
     */
    public ComboBox cboSchedulingUserID;
    /**
     * Date picker for the appointment start date
     */
    public DatePicker dpkSchedulingStartDate;
    /**
     * Date picker for the appointment end date
     */
    public DatePicker dpkSchedulingEndDate;
    /**
     * Combo box for the appointment start time
     */
    public ComboBox cboSchedulingStartTime;
    /**
     * Combo box for the appointment end time
     */
    public ComboBox cboSchedulingEndTime;
    /**
     * Table view for filtered schedules
     */
    public TableView tblSchedules;
    /**
     * Column for the appointment ID
     */
    public TableColumn colSchedulesApptID;
    /**
     * Column for the appointment title
     */
    public TableColumn colSchedulesTitle;
    /**
     * Column for the appointment description
     */
    public TableColumn colSchedulesDesc;
    /**
     * Column for the appointment location
     */
    public TableColumn colSchedulesLocation;
    /**
     * Column for the appointment contact
     */
    public TableColumn colSchedulesContact;
    /**
     * Column for the appointment type
     */
    public TableColumn colSchedulesType;
    /**
     * Column for the appointment start datetime
     */
    public TableColumn colSchedulesStartDate;
    /**
     * Column for the appointment end datetime
     */
    public TableColumn colSchedulesEndDate;
    /**
     * Column for the customer ID
     */
    public TableColumn colSchedulesCustID;
    /**
     * Column for the user ID
     */
    public TableColumn colSchedulesUserID;
    /**
     * Radio button for all schedules for the current week
     */
    public RadioButton rdoSchedulingByWeek;
    /**
     * Radio button for all schedules grouped by month
     */
    public RadioButton rdoSchedulingByMonth;
    /**
     * Radio button for all schedules
     */
    public RadioButton rdoSchedulingByAll;
    /**
     * Column for the customer name
     */
    public TableColumn colSchedulesCustName;
    /**
     * Column for the user's name
     */
    public TableColumn colSchedulesUserName;
    /**
     * Label for record deletion messages
     */
    public Label lblSchedulingMessage;
    /**
     * ObservableList for the contacts combo box
     */
    private ObservableList<DropdownList> contacts = FXCollections.observableArrayList();
    /**
     * ObservableList for the customers combo box
     */
    private ObservableList<DropdownList> customers = FXCollections.observableArrayList();
    /**
     * ObservableList for the users combo box
     */
    private ObservableList<DropdownList> users = FXCollections.observableArrayList();
    /**
     * ObservableList for the table view
     */
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    /**
     *  ObservableList for the time combo boxes
     */
    private final ObservableList<DropdownList> TIME_SELECTIONS = FXCollections.observableArrayList();


    /**
     * Initialize the form. Set fields to default, Sst Node IDs for the user messages, populate the combo boxes with
     * DB data, set the time combo-box ObservableList values, set the time combo box values, prep the table view,
     * populate Appointment ID with the next MySQL auto increment, and populate the table view with all Appointments.
     * @param url Default url parameter for initialize method
     * @param resourceBundle Default resourceBundle parameter for initialize method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Set fields to default
        clearFields();

        // Set Node IDs for user messages
        cboSchedulingContact.setId("Contact");
        cboSchedulingCustID.setId("Customer");
        cboSchedulingUserID.setId("User");
        cboSchedulingStartTime.setId("Start Time");
        cboSchedulingEndTime.setId("End Time");
        txtSchedulingTitle.setId("Title");
        txtSchedulingDesc.setId("Description");
        txtSchedulingLocation.setId("Location");
        txtSchedulingType.setId("Type");
        dpkSchedulingStartDate.setId("Start Date");
        dpkSchedulingEndDate.setId("End Date");

        //Populate combo boxes with DB data
        cboSchedulingContact.setItems(MySQL.getComboBoxItems("contacts", "Contact_ID", "Contact_Name",
                contacts));
        cboSchedulingCustID.setItems(MySQL.getComboBoxItems("customers", "Customer_ID", "Customer_Name",
                customers));
        cboSchedulingUserID.setItems(MySQL.getComboBoxItems("users", "User_ID", "User_Name",
                users));

        // Set time combo-box ObservableList values
        setTimeSelections();

        // Set time combo box values
        cboSchedulingStartTime.setItems(TIME_SELECTIONS);
        cboSchedulingEndTime.setItems(TIME_SELECTIONS);


        //Prep table view
        colSchedulesApptID.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        colSchedulesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colSchedulesDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colSchedulesLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colSchedulesContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colSchedulesType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colSchedulesStartDate.setCellValueFactory(new PropertyValueFactory<>("start"));
        colSchedulesEndDate.setCellValueFactory(new PropertyValueFactory<>("end"));
        colSchedulesCustID.setCellValueFactory(new PropertyValueFactory<>("custID"));
        colSchedulesCustName.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colSchedulesUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colSchedulesUserName.setCellValueFactory(new PropertyValueFactory<>("user"));

        // Populate Appointment ID with the next MySQL auto increment
        txtSchedulingApptID.setText(Integer.toString(MySQL.getAutoIncrement(
                "client_schedule", "appointments")));

        // Populate the table view with all Appointments
        setReportFilter();


    }

    // If an item in the tableview is selected, populate the fields. When using IDs from the DB table, adjust for
    // combobox's zero-indexing. GET LAMBDA NOTES FROM BELOW

    /**
     * LAMBDA NOTES: Use a reusable lambda expression to loop through an ObservableList of
     * DropdownList objects. Search by String and return its integer ID key value.
     * If an item in the tableview is selected, populate the fields. When using IDs from the DB table, adjust for a
     * combo box's zero-indexing.
     * @param mouseEvent Default mouse event
     */
    public void populateSchedFieldsWthTblSlctn(MouseEvent mouseEvent) {

        Appointment selctdAppt = (Appointment) tblSchedules.getSelectionModel().getSelectedItem();

        if (selctdAppt == null) {
        }
        else {

            // Highlight selected row
            tblSchedules.getSelectionModel().select(selctdAppt);

            txtSchedulingApptID.setText(Integer.toString(selctdAppt.getApptId()));
            txtSchedulingTitle.setText(selctdAppt.getTitle());
            txtSchedulingDesc.setText(selctdAppt.getDesc());
            txtSchedulingLocation.setText(selctdAppt.getLocation());
            txtSchedulingType.setText(selctdAppt.getType());
            dpkSchedulingStartDate.setValue(LocalDate.parse(selctdAppt.getStart(), DTF));
            dpkSchedulingEndDate.setValue(LocalDate.parse(selctdAppt.getEnd(), DTF));

            // Set the combo's selected value, not the prompt text over it
            cboSchedulingContact.getSelectionModel().select(selctdAppt.getContactId() - 1);
            cboSchedulingCustID.getSelectionModel().select(selctdAppt.getCustID() - 1);
            cboSchedulingUserID.getSelectionModel().select(selctdAppt.getUserID() - 1);

            /*
            Use a reusable lambda expression to loop through an ObservableList of DropdownList objects. Search by
            String and return its integer ID key value.
             */
            CycleDropdownList hourId = (ObservableList<DropdownList> list, String str) -> {
                for (DropdownList item : list) {
                    if (item.getText().equals(str)){
                        return item.getId();
                    }
                }
                return 0;
            };

            // Get an index value for the time to use for its combo-box update and set the box's value
            int id = hourId.id(TIME_SELECTIONS, LocalTime.parse(selctdAppt.getStart(), DTF).toString());
            cboSchedulingStartTime.getSelectionModel().select(id);
            id = hourId.id(TIME_SELECTIONS, LocalTime.parse(selctdAppt.getEnd(), DTF).toString());
            cboSchedulingEndTime.getSelectionModel().select(id);
        }

    }




    /**
     * If all editable form fields are not blank and valid, add the record to the DB, refresh the table view, and clear
     * the fields. A record cannot be added if the appointment ID already exists.
     *
     * @param actionEvent Default actionEvent parameter for event listener
     */
    public void addSchedule(ActionEvent actionEvent) {

        // If appt ID already exists, don't do anything
        ObservableList<DropdownList> temp = FXCollections.observableArrayList();

        MySQL.getComboBoxItems("appointments", "appointment_id", "title", temp,
                "appointment_id", txtSchedulingApptID.getText());

        if (temp.size() > 0){
            general.Messages.displayInformation("Record Already Exists", "This schedule ID already exists.");
            temp.clear();
        } else {
            if (areFieldsValid(true)){

                // Get Date and Time box values
                LocalDateTime start = LocalDateTime.of(dpkSchedulingStartDate.getValue(),
                        LocalTime.parse(cboSchedulingStartTime.getSelectionModel().getSelectedItem().toString()));
                LocalDateTime end = LocalDateTime.of(dpkSchedulingEndDate.getValue(),
                        LocalTime.parse(cboSchedulingEndTime.getSelectionModel().getSelectedItem().toString()));

                // Get the dropdown selection IDs
                DropdownList itemContact = (DropdownList) cboSchedulingContact.getSelectionModel().getSelectedItem();
                DropdownList itemCustomer = (DropdownList) cboSchedulingCustID.getSelectionModel().getSelectedItem();
                DropdownList itemUser = (DropdownList) cboSchedulingUserID.getSelectionModel().getSelectedItem();

                // Clear the message label
                lblSchedulingMessage.setText("");

                // Add to DB
                MySQL.appointmentsTableDML(itemCustomer.getId(), itemUser.getId(), itemContact.getId(), start, end, 'i',
                        txtSchedulingTitle.getText().strip(),
                        txtSchedulingDesc.getText().strip(),
                        txtSchedulingLocation.getText().strip(),
                        txtSchedulingType.getText().strip());

                // Refresh form
                initialize(null, null);
            }
        }
    }


    /**
     * If all form fields are not blank and valid, update the selected schedule record, refresh the table view, and
     * clear the fields. A record cannot be updated if the appointment ID does not exist.
     *
     * @param actionEvent Default actionEvent parameter for event listener
     */
    public void updateSchedule(ActionEvent actionEvent) {

        // If appt ID doesn't exist, don't do anything
        ObservableList<DropdownList> temp = FXCollections.observableArrayList();

        MySQL.getComboBoxItems("appointments", "appointment_id", "title", temp,
                "appointment_id", txtSchedulingApptID.getText());

        if (temp.size() < 1){
            general.Messages.displayInformation("Record Does Not Exist", "This schedule ID does not exist.");
            temp.clear();
        } else {

            LocalDateTime start = null;
            LocalDateTime end = null;

            boolean checkOverlap = true;

//            try {
//                // Get Date and Time box values
//                start = LocalDateTime.of(dpkSchedulingStartDate.getValue(),
//                        LocalTime.parse(cboSchedulingStartTime.getSelectionModel().getSelectedItem().toString()));
//                end = LocalDateTime.of(dpkSchedulingEndDate.getValue(),
//                        LocalTime.parse(cboSchedulingEndTime.getSelectionModel().getSelectedItem().toString()));
//            } catch (Exception e) {
//                // Do nothing
//            }
//
//
//            // If form date times = the record's date times, no need to check for schedule overlap
//            if (MySQL.areTimesTheSame(txtSchedulingApptID.getText(), start, end))
//                checkOverlap = false;
//            else
//                checkOverlap = true;


            if (areFieldsValid(true)) {

                // Get Date and Time box values
                start = LocalDateTime.of(dpkSchedulingStartDate.getValue(),
                        LocalTime.parse(cboSchedulingStartTime.getSelectionModel().getSelectedItem().toString()));
                end = LocalDateTime.of(dpkSchedulingEndDate.getValue(),
                        LocalTime.parse(cboSchedulingEndTime.getSelectionModel().getSelectedItem().toString()));

                // Get the dropdown selection IDs
                DropdownList itemContact = (DropdownList) cboSchedulingContact.getSelectionModel().getSelectedItem();
                DropdownList itemCustomer = (DropdownList) cboSchedulingCustID.getSelectionModel().getSelectedItem();
                DropdownList itemUser = (DropdownList) cboSchedulingUserID.getSelectionModel().getSelectedItem();

                // Clear the message label
                lblSchedulingMessage.setText("");

                // Update DB record
                MySQL.appointmentsTableDML(itemCustomer.getId(), itemUser.getId(), itemContact.getId(), start, end, 'u',
                        txtSchedulingTitle.getText().strip(),
                        txtSchedulingDesc.getText().strip(), txtSchedulingLocation.getText().strip(),
                        txtSchedulingType.getText().strip(), txtSchedulingApptID.getText());
                // Refresh form
                initialize(null, null);
            }
        }
    }

    /**
     * If all form fields are not blank and valid, update the selected schedule record, refresh the table view, and
     * clear the fields. A record cannot be updated if the appointment ID does not exist. The date range checks are
     * not needed.
     * @param actionEvent Default actionEvent parameter for event listener
     */
    public void deleteSchedule(ActionEvent actionEvent) {

        // If appt ID doesn't exist, don't do anything
        ObservableList<DropdownList> temp = FXCollections.observableArrayList();

        MySQL.getComboBoxItems("appointments", "appointment_id", "title", temp,
                "appointment_id", txtSchedulingApptID.getText());

        if (temp.size() < 1){
            general.Messages.displayInformation("Record Does Not Exist", "This schedule ID does not exist.");
            temp.clear();
        } else {
            // Confirm deletion
            if (general.Messages.displayConfirm("Confirm Delete", "Are you sure you want to delete the " +
                    "selected record?").get() == ButtonType.OK) {

                // Date range checks are not needed
                if (areFieldsValid(false)) {

                    // Get Date and Time box values
                    LocalDateTime start = LocalDateTime.of(dpkSchedulingStartDate.getValue(),
                            LocalTime.parse(cboSchedulingStartTime.getSelectionModel().getSelectedItem().toString()));
                    LocalDateTime end = LocalDateTime.of(dpkSchedulingEndDate.getValue(),
                            LocalTime.parse(cboSchedulingEndTime.getSelectionModel().getSelectedItem().toString()));

                    // Get the dropdown selection IDs
                    DropdownList itemContact = (DropdownList) cboSchedulingContact.getSelectionModel().getSelectedItem();
                    DropdownList itemCustomer = (DropdownList) cboSchedulingCustID.getSelectionModel().getSelectedItem();
                    DropdownList itemUser = (DropdownList) cboSchedulingUserID.getSelectionModel().getSelectedItem();

                    // Populate message label with deletion message (Ideally this should file if the DB call returns successful)
                    lblSchedulingMessage.setText("The " + txtSchedulingType.getText().strip() +
                            " appointment with ID " + txtSchedulingApptID.getText() + " has been canceled.");

                    // Delete from DB
                    MySQL.appointmentsTableDML(itemCustomer.getId(), itemUser.getId(), itemContact.getId(), start, end, 'd',
                            txtSchedulingApptID.getText());

                    // Refresh form
                    initialize(null, null);

                }
            }
        }

    }

    /**
     * Populate the table view per the selected radio button.
     */
    private void setReportFilter(){

        tblSchedules.getItems().clear();
        allAppointments.clear();

        if(rdoSchedulingByAll.isSelected()){
            // Populate the table view with all Appointments
            tblSchedules.getItems().addAll(MySQL.getAppointments('a', allAppointments, 0));
        } else if (rdoSchedulingByWeek.isSelected()) {
            tblSchedules.getItems().addAll(MySQL.getAppointments('w', allAppointments, 0));
        } else if (rdoSchedulingByMonth.isSelected()) {
            tblSchedules.getItems().addAll(MySQL.getAppointments('m', allAppointments, 0));
        }
    }

    /**
     * Trigger for Week radio button
     * @param actionEvent Default action event
     */
    public void toggleTblToWeek(ActionEvent actionEvent) {
        setReportFilter();
    }

    /**
     * Trigger for Month radio button
     * @param actionEvent Default action event
     */
    public void toggleTblToMonth(ActionEvent actionEvent) {
        setReportFilter();
    }

    /**
     * Trigger for All radio button
     * @param actionEvent Default action event
     */
    public void toggleTblToAll(ActionEvent actionEvent) {
        setReportFilter();
    }

    /**
     * Open the main menu form
     * @param actionEvent Default action event
     * @throws IOException Default IOException error throw
     */
    public void schedulingOpenMenu(ActionEvent actionEvent) throws IOException {
        // Switch forms
        Parent root = FXMLLoader.load(getClass().getResource("frmMenu.fxml"));
        FormNavigation.changeSceneToMenu(root, actionEvent);
    }

    /**
     * Function to pass form elements and call validation function.
     * @return Return true if all fields are valid. Else return false.
     */
    private boolean areFieldsValid(boolean checkTimes){

        return general.FormValidation.areSchedulingFieldsValid(txtSchedulingApptID, txtSchedulingTitle, txtSchedulingDesc,
                txtSchedulingLocation,txtSchedulingType, cboSchedulingContact, cboSchedulingCustID, cboSchedulingUserID,
                cboSchedulingStartTime, cboSchedulingEndTime, dpkSchedulingStartDate, dpkSchedulingEndDate, checkTimes);

    }

    /**
     * Clear out text from the text boxes and current selected item from combo boxes.
     */
    private void clearFields() {

        cboSchedulingContact.getSelectionModel().clearSelection();
        cboSchedulingCustID.getSelectionModel().clearSelection();
        cboSchedulingUserID.getSelectionModel().clearSelection();
        cboSchedulingStartTime.getSelectionModel().clearSelection();
        cboSchedulingEndTime.getSelectionModel().clearSelection();

        txtSchedulingApptID.setText("");
        txtSchedulingTitle.setText("");
        txtSchedulingDesc.setText("");
        txtSchedulingLocation.setText("");
        txtSchedulingType.setText("");

        dpkSchedulingStartDate.setValue(null);
        dpkSchedulingEndDate.setValue(null);

        // Clear lists
        contacts.clear();
        customers.clear();
        users.clear();

    }

    /**
     * Generate values for the time combo boxes
     */
    private void setTimeSelections() {

        int hourSegmentIdx = 0, hour = 0, min = 0;
        String tempHour = "", tempMin = "";

        while (hour < 24) {

            // Add leading 0
            if (min == 0) {
                tempMin = "00";
            } else if (min == 15) {
                tempMin = "15";
            } else if (min == 30) {
                tempMin = "30";
            } else if (min == 45) {
                tempMin = "45";
            }

            if (hour == 0) {
                tempHour = "00";
            } else if (hour == 1) {
                tempHour = "01";
            } else if (hour == 2) {
                tempHour = "02";
            } else if (hour == 3) {
                tempHour = "03";
            } else if (hour == 4) {
                tempHour = "04";
            } else if (hour == 5) {
                tempHour = "05";
            } else if (hour == 6) {
                tempHour = "06";
            } else if (hour == 7) {
                tempHour = "07";
            } else if (hour == 8) {
                tempHour = "08";
            } else if (hour == 9) {
                tempHour = "09";
            } else if (hour > 9) {
                tempHour = hour + "";
            }

            // Add to ObservableList
            DropdownList item = new DropdownList(hourSegmentIdx,
                    tempHour + ":" + tempMin);
            TIME_SELECTIONS.add(item);
            hourSegmentIdx++;
            min += 15;

            // Reset min
            if (min > 45) {
                hour += 1;
                min = 0;
            }
        }
    }

}
