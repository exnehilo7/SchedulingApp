package hopp.schedulingapp;

import database.MySQL;
import general.Appointment;
import general.FormNavigation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Customers by Country Report form.
 */
public class FormReportLocalCustomers implements Initializable {
    /**
     * Table view for the report
     */
    public TableView tblReportLocalCustomers;
    /**
     * Column for the customer ID
     */
    public TableColumn colReportLocalCustomersCustID;
    /**
     * Column for the customer name
     */
    public TableColumn colReportLocalCustomersName;
    /**
     * Column for the customer country
     */
    public TableColumn colReportLocalCustomersCountry;

    /**
     * ObservableList for the report
     */
    private ObservableList<Appointment> customerByCountry = FXCollections.observableArrayList();

    /**
     * Initialize the form. Prep table view and populate with data.
     * @param url Default url parameter for initialize method
     * @param resourceBundle Default resourceBundle parameter for initialize method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    //Prep table view
        colReportLocalCustomersName.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colReportLocalCustomersCustID.setCellValueFactory(new PropertyValueFactory<>("custID"));
        colReportLocalCustomersCountry.setCellValueFactory(new PropertyValueFactory<>("type"));

        tblReportLocalCustomers.getItems().addAll(MySQL.getAppointments('y', customerByCountry, 0));

    }

    /**
     * Open the main menu form
     * @param actionEvent Default action event
     * @throws IOException Default IOException error throw
     */
    public void reportLocalCustomersOpenMain(ActionEvent actionEvent) throws IOException {
        // Switch forms
        Parent root = FXMLLoader.load(getClass().getResource("frmMenu.fxml"));
        FormNavigation.changeSceneToMenu(root, actionEvent);
    }
}
