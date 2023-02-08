package general;

import database.MySQL;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static general.Messages.*;

/**
 * Cass to validate the fields on the Customer and Appointment forms.
 */
public class FormValidation {


    /**
     * Return true if all fields are not blank, combo boxes have a selection, text fields are no longer than 50
     * characters (address is 100 characters). Name, Address, Phone, Postal Code do not need format checking.
     * @param name The name text field
     * @param address The address text field
     * @param postalCd The postal code text field
     * @param phone The phone text field
     * @param country The country combo box
     * @param division The division combo box
     * @return Return true if successful. False if not.
     */
    public static boolean areCustRecordFieldsValid (TextField name, TextField address, TextField postalCd,
                                                    TextField phone, ComboBox country, ComboBox division){

        // Are all fields not blank and combo boxes have no selection?
        if (isTextFieldNotBlank(name) && isTextFieldNotBlank(address) && isTextFieldNotBlank(postalCd) &&
                isTextFieldNotBlank(phone) && isComboBoxNotBlank(country) && isComboBoxNotBlank(division)){
            // Will fields exceed DB table size limit?
            if (isStringEqOrBelowMaxLength(name, 50) && isStringEqOrBelowMaxLength(address, 100) &&
                    isStringEqOrBelowMaxLength(postalCd, 50) && isStringEqOrBelowMaxLength(phone, 50)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Return true if all fields are not blank, combo boxes have a selection, text fields have the appropriate data
     * types and no invalid characters, the start date and time is less than the end date and time, the time frame is
     * not within business hours (EST), the start datetime is greater than the current date time, and the appointment
     * is not overlapping with another. Else return false.
     * @param title The title text field
     * @param desc The description text field
     * @param location The location text field
     * @param type The type text field
     * @param contact The contact combo box
     * @param custId The customer ID combo box
     * @param UserId The User ID combo box
     * @param startTime The Start Time combo box
     * @param endTime The End Time combo box
     * @param startDate The Start Date date picker
     * @param endDate The End Date date picker
     * @return Return true if successful. False if not.
     */
    public static boolean areSchedulingFieldsValid(TextField apptId, TextField title, TextField desc, TextField location, TextField type,
                                                   ComboBox contact, ComboBox custId, ComboBox UserId,
                                                   ComboBox startTime, ComboBox endTime, DatePicker startDate,
                                                   DatePicker endDate, boolean checkTimes) {

        // Are all fields not blank, combo boxes have no selection, and date pickers have no dates?
        if (isTextFieldNotBlank(title) && isTextFieldNotBlank(desc) && isTextFieldNotBlank(location) &&
                isTextFieldNotBlank(type) && isComboBoxNotBlank(contact) && isComboBoxNotBlank(custId) &&
                isComboBoxNotBlank(UserId) && isComboBoxNotBlank(startTime) && isComboBoxNotBlank(endTime) &&
                isDtPickerNotBlank(startDate) && isDtPickerNotBlank(endDate)){

            LocalDateTime start = LocalDateTime.of(startDate.getValue(),
                    LocalTime.parse(startTime.getSelectionModel().getSelectedItem().toString()));
            LocalDateTime end = LocalDateTime.of(endDate.getValue(),
                    LocalTime.parse( endTime.getSelectionModel().getSelectedItem().toString()));

            // Will fields exceed DB table size limit?
            if (isStringEqOrBelowMaxLength(title, 50) && isStringEqOrBelowMaxLength(desc, 50) &&
                    isStringEqOrBelowMaxLength(location, 50) && isStringEqOrBelowMaxLength(type, 50)) {
                // Check times?
                if (checkTimes){
                    // Is start datetime greater than current datetime?
                    if (isDatetimeGreaterThanNow(start)) {
                        // Is start datetime less than end datetime?
                        if (isStartDtLessThanEndDt(start, end)) {
                            // Is appointment within business hours? (start datetime >= 08:00 EST and end datetime <= 22:00 EST)
                            if (isTimeWithinBusinessHours(start, end)) {
                                    // Is the timeframe clear of any appointment overlaps?
                                    if (isApptOverlapFree(apptId, start, end)) {
                                        return true;
                                    }
                            }
                        }
                    }
                }
                else {return true;}
            }
        }
        return false;
    }

    /**
     * See if the start datetime is greater than the current datetime.
     * @param start Start time.
     * @return Return true if successful. False if not.
     */
    private static boolean isDatetimeGreaterThanNow(LocalDateTime start){

        LocalDateTime now = LocalDateTime.now();
        if (start.compareTo(now) > 0 ){
            return true;
        }
        general.Messages.displayWarning("Invalid Date Range", "The start date and time cannot be less "
                + "than the current date and time!");
        return false;
    }

    /**
     * Verify an appointment does not overlap with another.
     * @param start Start time.
     * @param end End time.
     * @return Return true if successful. False if not.
     */
    private static boolean isApptOverlapFree(TextField apptID, LocalDateTime start, LocalDateTime end){

        if (MySQL.isTimeframeOverlapFree(apptID.getText(), start, end)){
            return true;
        }
        displayWarning("Date Range Overlap", "The selected date range overlaps with an existing "
         + "appointment!");
        return false;
    }


    /**
     * If the timeframe is within business hours (08:00 - 22:00 EST), return true.
     * @param start The start LocalDateTime
     * @param end The end LocalDateTime
     * @return Return true if start is less than end. Else return false.
     */
    private static boolean isTimeWithinBusinessHours(LocalDateTime start, LocalDateTime end){

        // Get zone IDs
        ZoneId zidLocal = ZoneId.systemDefault();
        ZoneId zidEST = ZoneId.of("America/New_York");

        // Convert start and snd date times to ZonedDateTimes
        ZonedDateTime zonedStart = start.atZone(zidLocal);
        ZonedDateTime zonedEnd = end.atZone(zidLocal);

        // Convert ZonedDateTimes to EST
        ZonedDateTime startEST = zonedStart.withZoneSameInstant(zidEST);
        ZonedDateTime endEST = zonedEnd.withZoneSameInstant(zidEST);

        // Is start time >= 08:00 and end time <= 22:00?
        if (startEST.toLocalTime().compareTo(LocalTime.parse("08:00")) > -1
        && endEST.toLocalTime().compareTo(LocalTime.parse("22:00")) < 1 ){
            return true;
        }

        general.Messages.displayWarning("Date Range Outside of Business Hours", "The appointment cannot be" +
                " outside regular business hours! (08:00 - 22:00 EST)");
        return false;
    }

    /**
     * If the start datetime is less than end datetime, return true.
     * @param start The start LocalDateTime
     * @param end The end LocalDateTime
     * @return Return true if start is less than end. Else return false.
     */
    private static boolean isStartDtLessThanEndDt(LocalDateTime start, LocalDateTime end){

        if (start.compareTo(end) < 0 ){
            return true;
        }

        general.Messages.displayWarning("Invalid Date Range", "The start date and time cannot be equal to"
                + " or greater than the end date and time!");
        return false;
    }

    /**
     * Check a date picker to see if it's blank. Display an alert to the user if it is.
     * @param datePicker A date picker
     * @return Return true if successful. False if not.
     */
    private static boolean isDtPickerNotBlank(DatePicker datePicker) {

        // Is it empty?
        if (datePicker.getValue() == null){
            // Alert user
            displayWarning("Blank Date", datePicker.getId() + " is blank!");
            return false;
        }
        return true;
    }

    /**
     * Check a combo box to see if it's blank. Display an alert to the user if it is.
     * @param box A Combo box
     * @return Return true if successful. False if not.
     */
    private static boolean isComboBoxNotBlank(ComboBox box){

        // Is it empty?
        if (box.getSelectionModel().isEmpty()){
            // Alert user
            displayWarning("Blank Selection", box.getId() + " is blank!");
            return false;
        }
        return true;
    }

    /**
     * Check a text field to see if it's blank. Display an alert to the user if it is.
     * @param field A text field
     * @return Return true if successful. False if not.
     */
    private static boolean isTextFieldNotBlank(TextField field){

        // Is it empty?
        if (field.getText().strip().length() < 1){
            // Alert user
            displayWarning("Blank Field", field.getId() + " is blank!");
            return false;
        }
        return true;
    }

    /**
     * Check a text field to see if it exceeds a specified length. Display an alert to the user if it is.
     * @param field A text field
     * @param length The text field's maximum allowable length
     * @return Return true if successful. False if not.
     */
    private static boolean isStringEqOrBelowMaxLength(TextField field, int length){

        if (field.getText().strip().length() <= length) {
            return true;
        }
        // Alert user
        displayWarning("Text Exceeds Length", field.getId() + " cannot"
                + " be more than " + length + " characters!");
        return false;
    }

}
