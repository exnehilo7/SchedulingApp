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
 * Controller class for the Appointments by Customer Report form
 */
public class FormReportCustomerAppts implements Initializable {
    /**
     * Table view for the report
     */
    public TableView tblReportCustomerAppts;
    /**
     * Column for appointments by month
     */
    public TableColumn colReportCustomerApptsMonth;
    /**
     * Column for count
     */
    public TableColumn colReportCustomerApptsCount;
    /**
     * Column for appointment type
     */
    public TableColumn colReportCustomerApptsType;

    /**
     * ObservableList for report items
     */
    private ObservableList<Appointment> appointmentsByMonth = FXCollections.observableArrayList();

    /**
     * Initialize the form. Prep table view and populate with data.
     * @param url Default url parameter for initialize method
     * @param resourceBundle Default resourceBundle parameter for initialize method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Prep table view
        colReportCustomerApptsMonth.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colReportCustomerApptsCount.setCellValueFactory(new PropertyValueFactory<>("custID"));
        colReportCustomerApptsType.setCellValueFactory(new PropertyValueFactory<>("type"));

        tblReportCustomerAppts.getItems().addAll(MySQL.getAppointments('g', appointmentsByMonth, 0));

    }

    /**
     * Open the main menu form
     * @param actionEvent Default action event
     * @throws IOException Default IOException error throw
     */
    public void reportCustomerApptsOpenMain(ActionEvent actionEvent) throws IOException {
        // Switch forms
        Parent root = FXMLLoader.load(getClass().getResource("frmMenu.fxml"));
        FormNavigation.changeSceneToMenu(root, actionEvent);
    }
}
