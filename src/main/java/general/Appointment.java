package general;

/**
 * Class for the appointment object
 */
public class Appointment {

    /**
     * Appointment ID
     */
    private int apptId;
    /**
     * Appointment title
     */
    private String title;
    /**
     * Appointment description
     */
    private String desc;
    /**
     * Appointment location
     */
    private String location;
    /**
     * Contact ID
     */
    private int contactId;
    /**
     * Contact name
     */
    private String contact;
    /**
     * Appointment type
     */
    private String type;
    /**
     * Appointment start datetime
     */
    private String start;
    /**
     * Appointment end datetime
     */
    private String end;
    /**
     * Customer name
     */
    private String customer;
    /**
     * User name
     */
    private String user;
    /**
     * Customer ID
     */
    private int custID;
    /**
     * User ID
     */
    private int userID;

    /**
     * Constructor
     * @param apptId Appointment ID
     * @param title Appointment title
     * @param desc Appointment description
     * @param location Appointment location
     * @param contactId Contact ID
     * @param contact Contact name
     * @param type Appointment type
     * @param start Appointment start datetime
     * @param end Appointment end datetime
     * @param custID Customer ID
     * @param customer Customer name
     * @param userID User ID
     * @param user User name
     */
    public Appointment(int apptId, String title, String desc, String location, int contactId, String contact,
                       String type, String start, String end, int custID, String customer, int userID, String user) {
        this.apptId = apptId;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.contactId = contactId;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.user = user;
        this.custID = custID;
        this.userID = userID;

    }

    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
