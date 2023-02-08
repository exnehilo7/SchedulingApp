package hopp.schedulingapp;

import database.MySQL;
import general.Customer;
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
import java.util.ResourceBundle;

/**
 * Controller for the Customer Records form
 */
public class FormCustomerRecords implements Initializable {
    /**
     * Combo box for the customer state/province (division)
     */
    public ComboBox cboCustomerDivision;
    /**
     * Combo box for the customer country
     */
    public ComboBox cboCustomerCountry;
    /**
     * Table view for all customers
     */
    public TableView tblCustomers;
    /**
     * Column for the customer name
     */
    public TableColumn colCustomersName;
    /**
     * Column for the customer address
     */
    public TableColumn colCustomersAddress;
    /**
     * Column for the customer postal code
     */
    public TableColumn colCustomersPostalCode;
    /**
     * Column for the customer phone number
     */
    public TableColumn colCustomersPhone;
    /**
     * Column for the customer state/province (division)
     */
    public TableColumn colCustomersDivision;
    /**
     *  Column for the customer country
     */
    public TableColumn colCustomersCountry;
    /**
     * Label for record deletion messages
     */
    public Label lblCustomerMessage;
    /**
     * Text field for the customer ID
     */
    public TextField txtCustomerID;
    /**
     * Text field for the customer name
     */
    public TextField txtCustomerName;
    /**
     * Text field for the customer address
     */
    public TextField txtCustomerAddress;
    /**
     * Text field for the customer postal code
     */
    public TextField txtCustomerPostalCode;
    /**
     * Text field for the customer phone number
     */
    public TextField txtCustomerPhone;
    /**
     * Observable List for the country combo box
     */
    private ObservableList<DropdownList> countries = FXCollections.observableArrayList();
    /**
     * Observable List for the state/province (division) combo box
     */
    private ObservableList<DropdownList> divisions = FXCollections.observableArrayList();
    /**
     * Observable List for customers
     */
    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     * Initialize the form. Set Node IDs for pop-up messages, prep the table view, populate the Country combo box
     * with DB data (The Division will be based on the selected Country), populate Customer ID with the next MySQL
     * auto increment, and populate the table view with all customers
     * @param url Default url parameter for initialize method
     * @param resourceBundle Default resourceBundle parameter for initialize method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Set fields to default
        clearFields();

        // Set Node IDs for user messages
        txtCustomerName.setId("Name");
        cboCustomerDivision.setId("State/Province");
        cboCustomerCountry.setId("Country");
        txtCustomerAddress.setId("Address");
        txtCustomerPostalCode.setId("PostalCode");
        txtCustomerPhone.setId("Phone");

        // Prep table view
        colCustomersName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCustomersAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCustomersPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        colCustomersDivision.setCellValueFactory(new PropertyValueFactory<>("division"));
        colCustomersCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colCustomersPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Populate Country combo box with DB data. Division is based on the selected Country
        cboCustomerCountry.setItems(MySQL.getComboBoxItems("countries", "Country_ID", "Country",
                countries));

        // Populate Customer ID with the next MySQL auto increment
        txtCustomerID.setText(Integer.toString(MySQL.getAutoIncrement(
                "client_schedule", "customers")));

        // Populate the table view with all customers
        tblCustomers.getItems().clear();
        allCustomers.clear();
        tblCustomers.setItems(MySQL.getAllCustomers(allCustomers));


    }

    /**
     * Populate the form fields with the table view's selection. When using IDs from the DB table, adjust for a
     * combo box's zero-indexing. Because the division IDs are not in sequential order, use a temporary fix until the
     * combo box and form field population sub systems can be improved.
     * @param mouseEvent Default mouse event
     */
    public void populateCustFieldsWthTblSlctn(MouseEvent mouseEvent) {
        Customer selctdCust = (Customer) tblCustomers.getSelectionModel().getSelectedItem();

        if (selctdCust == null) {
        }
        else {

            // Highlight selected row
            tblCustomers.getSelectionModel().select(selctdCust);

            txtCustomerID.setText(Integer.toString(selctdCust.getId()));
            txtCustomerName.setText(selctdCust.getName());
            txtCustomerAddress.setText(selctdCust.getAddress());
            txtCustomerPostalCode.setText(selctdCust.getPostalCode());
            txtCustomerPhone.setText(selctdCust.getPhone());

            // Set the combo's selected value, not the prompt text over it
            cboCustomerCountry.getSelectionModel().select(selctdCust.getCountryId() - 1);

            // populate division
            selectCountry(null);
            ObservableList item = cboCustomerDivision.getItems();
            int count = 0;
            for (Object i : item) {
                count++;
            }

            int divisionId = selctdCust.getDivisionId();

            // Messy Division ID fix. DB table IDs are all over the place.
            if (count == 13){ // CAD
                divisionId -= 60;
            }else if (count == 4){ // UK
                divisionId -= 101;
            }

            // Set the combo's selected value, not the prompt text over it
            cboCustomerDivision.getSelectionModel().select(divisionId);


        }
    }


    /**
     * Update the Division box according to the selected Country
     * @param actionEvent Default action event
     */
    public void selectCountry(ActionEvent actionEvent) {

        // Get combo box's selected ID
        DropdownList item = (DropdownList) cboCustomerCountry.getSelectionModel().getSelectedItem();

        if (item != null) {
            // Clear out cboCustomerDivision
            cboCustomerDivision.getItems().clear();

            // Populate cboCustomerDivision
            cboCustomerDivision.setItems(MySQL.getComboBoxItems("first_level_divisions", "Division_ID",
                    "Division", divisions, "COUNTRY_ID",
                    Integer.toString(item.getId())));
        }

    }

    /**
     * If all editable form fields are not blank and are valid, add the record to the DB,
     * refresh the table view, and clear the fields. A record cannot be added if the customer ID already exists.
     *
     * @param actionEvent Default actionEvent parameter for event listener
     */
    public void addCustomer(ActionEvent actionEvent) {

        // If appt ID already exists, don't do anything
        ObservableList<DropdownList> temp = FXCollections.observableArrayList();

        MySQL.getComboBoxItems("customers", "customer_id", "phone", temp,
                "customer_id", txtCustomerID.getText());
        if (temp.size() > 0){
            general.Messages.displayInformation("Record Already Exists", "This customer ID already exists.");
            temp.clear();
        } else {
            if (areFieldsValid()) {

                // Get the dropdown selection IDs
                DropdownList itemDivision = (DropdownList) cboCustomerDivision.getSelectionModel().getSelectedItem();

                // Clear the message label
                lblCustomerMessage.setText("");

                // Add to DB
                MySQL.customersTableDML(itemDivision.getId(), 'i',
                        txtCustomerName.getText().strip(),
                        txtCustomerAddress.getText().strip(),
                        txtCustomerPostalCode.getText().strip(),
                        txtCustomerPhone.getText().strip());

                // Refresh form
                initialize(null, null);
            }
        }
    }

    /**
     * If all form fields are not blank and are valid, update the selected customer's record, refresh the table view, and
     * clear the fields. A record cannot be updated if the customer ID does not exist.
     *
     * @param actionEvent Default actionEvent parameter for event listener
     */
    public void updateCustomer(ActionEvent actionEvent) {

        // If appt ID doesn't exist, don't do anything
        ObservableList<DropdownList> temp = FXCollections.observableArrayList();

        MySQL.getComboBoxItems("customers", "customer_id", "phone", temp,
                "customer_id", txtCustomerID.getText());
        if (temp.size() < 1){
            general.Messages.displayInformation("Record Does Not Exist", "This customer ID does not exist.");
            temp.clear();
        } else {
            if (areFieldsValid()) {
                // Get the dropdown selection IDs
                DropdownList itemDivision = (DropdownList) cboCustomerDivision.getSelectionModel().getSelectedItem();

                // Clear the message label
                lblCustomerMessage.setText("");

                // Update to DB
                MySQL.customersTableDML(itemDivision.getId(), 'u',
                        txtCustomerName.getText().strip(),
                        txtCustomerAddress.getText().strip(),
                        txtCustomerPostalCode.getText().strip(),
                        txtCustomerPhone.getText().strip(),
                        txtCustomerID.getText().strip());

                // Refresh form
                initialize(null, null);
            }
        }
    }

    /**
     * If the customer doesn't have any appointments, delete the selected record. Prompt the user before deletion.
     * A record cannot be deleted if the customer ID does not exist.
     *
     * @param actionEvent Default actionEvent parameter for event listener
     */
    public void deleteCustomer(ActionEvent actionEvent) {

// If appt ID doesn't exist, don't do anything
        ObservableList<DropdownList> temp = FXCollections.observableArrayList();

        String custID = txtCustomerID.getText();

        MySQL.getComboBoxItems("customers", "customer_id", "phone", temp,
                "customer_id", custID);

        if (temp.size() < 1){
            general.Messages.displayInformation("Record Does Not Exist", "This customer ID does not exist.");
            temp.clear();
        } else {
            // Confirm deletion
            if (general.Messages.displayConfirm("Confirm Delete", "Are you sure you want to delete the " +
                    "selected record?").get() == ButtonType.OK) {

                // Make sure no appointments exist for the customer
                ObservableList<DropdownList>  appts = FXCollections.observableArrayList();
                MySQL.getComboBoxItems("appointments", "appointment_id", "type", appts,
                        "customer_id", custID);
                if (appts.size() > 0){
                    general.Messages.displayWarning("Active Appointments", "The customer's appointments" +
                            " must be canceled first!");
                    appts.clear();
                } else{
                    if (areFieldsValid()) {

                        // Get the dropdown selection IDs
                        DropdownList itemDivision = (DropdownList) cboCustomerDivision.getSelectionModel().getSelectedItem();

                        // Clear the message label
                        lblCustomerMessage.setText("");

                        // Populate message label with deletion message (Ideally this should file if the DB call returns successful)
                        lblCustomerMessage.setText("Customer " + txtCustomerName.getText().strip() +
                                " has been deleted.");

                        // Delete from DB
                        MySQL.customersTableDML(itemDivision.getId(), 'd',
                                txtCustomerID.getText().strip());

                        // Refresh form
                        initialize(null, null);
                    }
                }
            }
        }

    }

    /**
     * Close the form and open the main menu
     *
     * @param actionEvent Default actionEvent parameter for event listener
     * @throws IOException Default IOException error throw
     */
    public void customerOpenMenu(ActionEvent actionEvent) throws IOException {
        // Switch forms
        Parent root = FXMLLoader.load(getClass().getResource("frmMenu.fxml"));
        FormNavigation.changeSceneToMenu(root, actionEvent);
    }

    /**
     * Function to pass form elements and call validation function.
     * @return Return true if all fields are valid. Else return false.
     */
    private boolean areFieldsValid(){

        return general.FormValidation.areCustRecordFieldsValid(txtCustomerName, txtCustomerAddress,
                txtCustomerPostalCode,txtCustomerPhone, cboCustomerCountry, cboCustomerDivision);
    }

    /**
     * Clear out text from the text boxes and current selected item from combo boxes. Reset Division's values.
     */
    private void clearFields() {
        cboCustomerDivision.getItems().clear();
        cboCustomerDivision.setPromptText("-");
        cboCustomerCountry.setPromptText("-");
        txtCustomerName.setText("");
        txtCustomerAddress.setText("");
        txtCustomerPostalCode.setText("");
        txtCustomerPhone.setText("");

        // Clear Lists
        countries.clear();
        divisions.clear();

    }

}
