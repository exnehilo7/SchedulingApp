package database;

import general.Appointment;
import general.Customer;
import general.DropdownList;
import general.Messages;
import javafx.collections.ObservableList;

import java.sql.*;
import java.sql.DriverManager;
import java.time.LocalDateTime;

import static hopp.schedulingapp.Main.DTF;
import static hopp.schedulingapp.Main.USER_ID;

/**
 * Class for all database queries.
 */
public class MySQL {

    /**
     * DB URL
     */
    private static final String DB_URL = "jdbc:mysql://localhost/client_schedule?connectionTimeZone = SERVER";
    /**
     * Java driver for DB
     */
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    /**
     * DB username
     */
    private static final String USER_NAME = "sqlUser";  //sqlUser
    /**
     * DB password
     */
    private static final String PASSWORD = "Passw0rd!"; //Passw0rd!
    /**
     * Variable for DB connection
     */
    public static Connection conn;


    /* CONNECTIONS AND LOGINS ---------------------------------------------------------------------------------------*/

    /**
     * Connect to the DB
     */
    public static void connectToDB(){
        try {
            // Connect to DB
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            System.out.println("Connected!");

        }
        catch (Exception e) {
            e.printStackTrace();
            general.Messages.displayError(Messages.getMethodName(), e.getMessage());
        }
    }

    /**
     * Close the DB connection
     */
    public static void closeDBconn(){
        try {
            // Close the DB conn
            conn.close();
            System.out.println("Closed!");
        }
        catch (Exception e) {
            general.Messages.displayError(Messages.getMethodName(), e.getMessage());
        }
    }

    /**
     * LAMBDA NOTES: Use a reusable lambda expression with varargs to create the prepared statement.
     * Query the DB with UN and PW values from the Login form. If the result is more than 0 row, the login is valid.
     * Auto-close the prepared statement and the result set with a try-with-resources.
     * @param username The username from the Login form.
     * @param password  The password from the Login form.
     * @return Return true if successful. False if not.
     */
    public static boolean isAppLoginValid(String username, String password) {

        String qry = "SELECT User_ID FROM users WHERE User_Name = ? AND Password = ?";

        PrpStmtInterface lambStmt = (String strQry, String... strArg) ->
        {
            PreparedStatement stmt = conn.prepareStatement(strQry);
            stmt.setString(1, strArg[0]);
            stmt.setString(2, strArg[1]);
            return stmt;
        };

        try (PreparedStatement pStmt = lambStmt.ps(qry, username, password);
             ResultSet rs = pStmt.executeQuery()
        )
        {
            if (rs.isBeforeFirst() && rs.getRow() == 0){
                rs.next();
                USER_ID = rs.getInt("User_ID");
                return true;
            }
        }
        catch (SQLException se) {
            general.Messages.displayError(Messages.getMethodName(), se.getMessage());
        }
        return false;
    }

    /**
     * LAMBDA NOTES: Use a reusable lambda expression with varargs to create the prepared statement.
     * Query the DB with values from the Login form. If the result is less than 1 row, the username is invalid.
     * Auto-close the prepared statement and the result set with a try-with-resources.
     * @param username The username from the Login form.
     * @return Return false if invalid. True if it is valid.
     */
    public static boolean isUsernameInvalid(String username) {

        String qry = "SELECT User_Name FROM users WHERE User_Name = ?";

        PrpStmtInterface lambStmnt = (String strQry, String... strArg) ->
        {
            PreparedStatement stmt = conn.prepareStatement(strQry);
            stmt.setString(1, strArg[0]);
            return stmt;
        };

        try (PreparedStatement pStmt = lambStmnt.ps(qry, username);
              ResultSet rs = pStmt.executeQuery())
        {
            if (!rs.isBeforeFirst() && rs.getRow() == 0){
                    return true;
            }
            else {
                return false;}
        }
        catch (SQLException se) {
            general.Messages.displayError(Messages.getMethodName(), se.getMessage());
        }
        return false;
    }


    /* POPULATE FORMS ----------------------------------------------------------------------------------------------*/
    /**
     * LAMBDA NOTES: Use a reusable lambda expression to create the prepared statement.
     * Query the DB for the next auto increment value. MySQL versions greater than 5.7 need to have the table
     * analyzed first to refresh information_schema.
     * @param dbName The name of the dB to query.
     * @param tblName The name of the table to query.
     * @return Return the query's result as an int. If no result, return 0.
     */
    public static int getAutoIncrement(String dbName, String tblName){

        String qry = "ANALYZE NO_WRITE_TO_BINLOG TABLE " + tblName;
        // Lambda for prepared statement
        PrpStmtInterface lambStmnt = (String strQry, String... strArg) ->
        {
            PreparedStatement stmt = conn.prepareStatement(strQry);
            return stmt;
        };
        // Query DB
        try (
                PreparedStatement pStmt = lambStmnt.ps(qry);
                ResultSet rs = pStmt.executeQuery()
        ) {
            if (rs.isBeforeFirst() && rs.getRow() == 0) {
                // do nothing
            }
        } catch (SQLException se) {
            general.Messages.displayError(Messages.getMethodName(), se.getMessage());
        }


        qry = "SELECT AUTO_INCREMENT " +
                "FROM information_schema.TABLES " +
                "WHERE TABLE_SCHEMA = '" + dbName + "' " +
                "AND TABLE_NAME = '" + tblName + "'";
        // Lambda for prepared statement
        lambStmnt = (String strQry, String... strArg) ->
        {
            PreparedStatement stmt = conn.prepareStatement(strQry);
            return stmt;
        };
        // Query DB
        try (
                PreparedStatement pStmt = lambStmnt.ps(qry);
                ResultSet rs = pStmt.executeQuery()
        ) {
            if (rs.isBeforeFirst() && rs.getRow() == 0) {
                rs.next();
                return rs.getInt("AUTO_INCREMENT");
            }
        } catch (SQLException se) {
            general.Messages.displayError(Messages.getMethodName(), se.getMessage());
        }
        return 0;
    }


    /**
     * LAMBDA NOTES: Use a reusable lambda expression to create the prepared statement.
     * Query the DB for combo box values. For method use on several tables, the table and column names are passable.
     * Auto-close the prepared statement and the result set with a try-with-resources.
     * @param tblName The name of the DB table.
     * @param idColName The name of the DB column containing the primary key (ID).
     * @param txtColName The name of the DB column containing the name of the item.
     * @param observableList An observable list for the query's results.
     * @param arg Vargs for foreign key column names and their values.
     * @return Return the query's result as an observable list. If no result, return the passed observable list.
     */
    public static ObservableList<DropdownList> getComboBoxItems(String tblName, String idColName, String txtColName,
                                                          ObservableList<DropdownList> observableList, String... arg) {
        String qry = "";

        // Are there no arguments?
        if (arg.length == 0) {
            qry = "SELECT " + idColName + ", " + txtColName + " FROM " + tblName +
            " order by " + idColName;
        } else if (arg.length == 2) {
            qry = "SELECT " + idColName + ", " + txtColName + " FROM " + tblName +
                    " WHERE " + arg[0] + " = " + arg[1] +
                    " order by " + idColName;
        }

        // Lambda for prepared statement
        PrpStmtInterface lambStmnt = (String strQry, String... strArg) ->
            {
                PreparedStatement stmt = conn.prepareStatement(strQry);
                return stmt;
            };

        // Query DB
        try (
                PreparedStatement pStmt = lambStmnt.ps(qry);
                ResultSet rs = pStmt.executeQuery()
        ) {
            if (rs.isBeforeFirst() && rs.getRow() == 0) {
                while (rs.next()) {
                    DropdownList item = new DropdownList(rs.getInt(idColName), rs.getString(txtColName));
                    observableList.add(item);
                }
                return observableList;
            }
        } catch (SQLException se) {
            general.Messages.displayError(Messages.getMethodName(), se.getMessage());
        }
        return observableList;
    }

    /**
     * LAMBDA NOTES: Use a reusable lambda expression to create the prepared statement.
     * Query the DB for all Customer ID, name, address, division, postal code, and phone records. Concat the address
     * and division fields. Auto-close the prepared statement and the result set with a try-with-resources.
     * @param observableList An observable list for the query's results.
     * @return Return the query's result as an observable list. If no result, return the passed observable list.
     */
    public static ObservableList<Customer> getAllCustomers(ObservableList<Customer> observableList){

        String qry = "select Customer_ID, Customer_Name, Address, c.Division_ID, fld.Division, " +
                "cy.Country_ID, cy.Country, Postal_Code, Phone as Phone from customers c " +
                "join first_level_divisions fld on c.Division_ID = fld.Division_ID " +
                "join countries cy on fld.COUNTRY_ID = cy.Country_ID " +
                "order by customer_id";

        PrpStmtInterface lambStmnt = (String strQry, String... strArg) -> {
            PreparedStatement stmt = conn.prepareStatement(strQry);
            return stmt;
        };

        try (
            PreparedStatement pStmt = lambStmnt.ps(qry);
            ResultSet rs = pStmt.executeQuery()
            )
        {
            if (rs.isBeforeFirst() && rs.getRow() == 0) {
                while (rs.next()){
                    Customer item = new Customer(rs.getInt("Customer_ID"), rs.getString("Customer_Name"),
                            rs.getString("Address"),
                            rs.getInt("Division_ID"), rs.getString("Division"),
                            rs.getInt("Country_ID"), rs.getString("Country"),
                            rs.getString("Postal_Code"), rs.getString("Phone"));
                    observableList.add(item);
                }
                return observableList;
            }
        }
        catch (SQLException se) {
            general.Messages.displayError(Messages.getMethodName(), se.getMessage());
        }
        return observableList;
    }


    /**
     * LAMBDA NOTES: Use a reusable lambda expression to create the prepared statement.
     * Get list of appointments by filter.
     * @param filter a = All; w = Week; m = Month; c = Contact ID; g = Customer Appointments grouped by month;
     *               y = Customers by country.
     * @param observableList List to return query result into
     * @return  Return the query's result as an observable list. If no result, return the passed observable list.
     */
    public static ObservableList<Appointment> getAppointments(Character filter,
                                                              ObservableList<Appointment> observableList,
                                                              int contactID){
        String qry = "";

        // All
        if (filter == 'a'){
            qry = "select a.Appointment_ID, a.Title, a.Description, a.Location, ct.Contact_ID, ct.Contact_Name, a.Type, " +
                    "a.Start, a.End, a.Customer_ID, cu.Customer_Name, a.User_ID, u.User_Name " +
                    "from appointments a " +
                    "join contacts ct on a.Contact_ID = ct.Contact_ID " +
                    "join users u on a.User_ID = u.User_ID " +
                    "join customers cu on a.Customer_ID = cu.Customer_ID " +
                    "order by start";
        }
        //Current week
        else if (filter == 'w') {
            qry = "select a.Appointment_ID, a.Title, a.Description, a.Location, ct.Contact_ID, ct.Contact_Name, a.Type, " +
                    "a.Start, a.End, a.Customer_ID, cu.Customer_Name, a.User_ID, u.User_Name " +
                    "from appointments a " +
                    "join contacts ct on a.Contact_ID = ct.Contact_ID " +
                    "join users u on a.User_ID = u.User_ID " +
                    "join customers cu on a.Customer_ID = cu.Customer_ID " +
                    "WHERE YEARWEEK(a.Start) = YEARWEEK(NOW()) " +
                    "AND YEARWEEK(a.End) = YEARWEEK(NOW()) " +
                    "order by start";
        }
        // Current month
        else if (filter == 'm') {
            qry = "select a.Appointment_ID, a.Title, a.Description, a.Location, ct.Contact_ID, ct.Contact_Name, a.Type, " +
                    "a.Start, a.End, a.Customer_ID, cu.Customer_Name, a.User_ID, u.User_Name " +
                    "from appointments a " +
                    "join contacts ct on a.Contact_ID = ct.Contact_ID " +
                    "join users u on a.User_ID = u.User_ID " +
                    "join customers cu on a.Customer_ID = cu.Customer_ID " +
                    "WHERE MONTH(a.Start) = MONTH(NOW()) " +
                    "AND MONTH(a.End) = MONTH(NOW()) " +
                    "order by start";
        }
        // By contact ID
        else if (filter == 'c'){
            qry = "select a.Appointment_ID, a.Title, a.Description, a.Location, ct.Contact_ID, ct.Contact_Name, a.Type, " +
                    "a.Start, a.End, a.Customer_ID, cu.Customer_Name, a.User_ID, u.User_Name " +
                    "from appointments a " +
                    "join contacts ct on a.Contact_ID = ct.Contact_ID " +
                    "join users u on a.User_ID = u.User_ID " +
                    "join customers cu on a.Customer_ID = cu.Customer_ID " +
                    "where ct.Contact_ID = " + contactID + " " +
                    "order by a.start";
        }
        // Customer appts grouped by month
        else if (filter == 'g'){
            qry = "select date_format(Start, '%M') as month, count(Customer_ID) as total, Type " +
                    "from appointments " +
                    "group by month(Start), type " +
                    "order by start, type";

            return getSimpleGroupBy(qry, observableList, "month", "Type" , "total");
        }
        // Customers by country
        else if (filter == 'y'){
            qry = "select customer_id, customer_name, country " +
                    "from customers c " +
                    "join first_level_divisions fld on fld.Division_ID = c.Division_ID " +
                    "join countries y on fld.COUNTRY_ID = y.Country_ID " +
                    "order by country, Customer_Name";

            return getSimpleGroupBy(qry, observableList, "customer_name", "country", "customer_id");
        }


        PrpStmtInterface lambStmnt = (String strQry, String... strArg) -> {
            PreparedStatement stmt = conn.prepareStatement(strQry);
            return stmt;
        };

        try (
                PreparedStatement pStmt = lambStmnt.ps(qry);
                ResultSet rs = pStmt.executeQuery()
        )
        {
            if (rs.isBeforeFirst() && rs.getRow() == 0) {
                while (rs.next()){
                    Appointment item = new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                            rs.getString("Description"), rs.getString("Location"),
                            rs.getInt("Contact_ID"), rs.getString("Contact_Name"),
                            rs.getString("Type"),
                            DTF.format(rs.getTimestamp("Start").toLocalDateTime()),
                            DTF.format(rs.getTimestamp("End").toLocalDateTime()),
                            rs.getInt("Customer_ID"), rs.getString("Customer_Name"),
                            rs.getInt("User_ID"), rs.getString("User_Name"));
                    observableList.add(item);
                }
                return observableList;
            }
        }
        catch (SQLException se) {
            general.Messages.displayError(Messages.getMethodName(), se.getMessage());
        }
        return observableList;
    }


    /**
     * LAMBDA NOTES: Use a reusable lambda expression to create the prepared statement.
     * Subfunction for getAppointments.
     * @param qry The SQL query
     * @param observableList List to return query result into
     * @param arg The desc, type, and CustID variables of an Appointment Class to hold the return values of the query.
     *            Variable names will not reflect what the returned column values actually are.
     * @return
     */
    private static ObservableList<Appointment> getSimpleGroupBy(String qry, ObservableList<Appointment> observableList,
                                                                String... arg){

        PrpStmtInterface lambStmnt = (String strQry, String... strArg) -> {
            PreparedStatement stmt = conn.prepareStatement(strQry);
            return stmt;
        };

        try (
                PreparedStatement pStmt = lambStmnt.ps(qry);
                ResultSet rs = pStmt.executeQuery()
        )
        {
            if (rs.isBeforeFirst() && rs.getRow() == 0) {
                while (rs.next()){
                    Appointment item = new Appointment(0, null, rs.getString(arg[0]), null,
                            0, null, rs.getString(arg[1]),null,null,
                            rs.getInt(arg[2]), null,0, null);
                    observableList.add(item);
                }
                return observableList;
            }

        }
        catch (SQLException se) {
            general.Messages.displayError(Messages.getMethodName(), se.getMessage());
        }
        return observableList;
    }


    /* FORM VALIDATION ----------------------------------------------------------------------------------------------*/

    /**
     * LAMBDA NOTES: Use a reusable lambda expression to create the prepared statement.
     * See if the appointment ID's start and end datetimes overlap with any other start and end time
     * @param apptID The appointment ID
     * @param start The start datetime
     * @param end The end datetime
     * @return True if no rows are returned. False if not.
     */
    public static boolean isTimeframeOverlapFree(String apptID, LocalDateTime start, LocalDateTime end){

        // Convert java LocalDateTime to jave Timestamp (MySQL driver should be set to auto-convert to UTC)
        final Timestamp tStart = Timestamp.valueOf(start);
        final Timestamp tEnd = Timestamp.valueOf(end);

        String qry = "select count(*) from appointments " +
                "where Appointment_ID != ? " +
                // comparison lands within proposed time
                "and ((start >= ? and end <= ?) " +
                //time's end lands within comparison
                "or (? <= start and ? > start) " +
                // time's start lands within comparison
                "or (? < end and ? >= end))";
        PrpStmtInterface lambStmnt = (String strQry, String... strArg) -> {
            PreparedStatement stmt = conn.prepareStatement(strQry);
            stmt.setString(1, apptID);
            stmt.setTimestamp(2, tStart);
            stmt.setTimestamp(3, tEnd);
            stmt.setTimestamp(4, tStart);
            stmt.setTimestamp(5, tEnd);
            stmt.setTimestamp(6, tStart);
            stmt.setTimestamp(7, tEnd);
            return stmt;
        };
        try (
                PreparedStatement pStmt = lambStmnt.ps(qry);
                ResultSet rs = pStmt.executeQuery()
        )
        {
            rs.next();
            int count = rs.getInt(1);
            if (count < 1){
                return true;
            }
        }
        catch (SQLException se) {
            general.Messages.displayError(Messages.getMethodName(), se.getMessage());
        }

        return false;

    }

    /**
     * Database DML queries (insert, update, delete) for appointments table.
     * @param custId The ID from the Customer combo-box.
     * @param userId The ID from the Customer combo-box.
     * @param contactId The ID from the Customer combo-box.
     * @param start The start date from the date picker and the start time from the combo-box.
     * @param end The end date from the date picker and the end time from the combo-box.
     * @param dml Specify the DML action. i = insert, u = update, d = delete
     * @param args Vargs for field text values. Pass: title, description, location, type, [apptID]
     */
    /* DATABASE INSERT ----------------------------------------------------------------------------------------------*/
    public static void appointmentsTableDML(int custId, int userId, int contactId, LocalDateTime start,
                                            LocalDateTime end, Character dml, String... args) {

        String qry;

        // Convert java LocalDateTime to jave Timestamp (MySQL driver should be set to auto-convert to UTC)
        Timestamp tStart = Timestamp.valueOf(start);
        Timestamp tEnd = Timestamp.valueOf(end);

        // Insert
        if (dml == 'i') {
            qry = "insert into appointments (title, description, Location, type, start, end, create_date, created_by, " +
                    "last_update, Last_Updated_By, Customer_ID, User_id, contact_id) " +
                    "values (?, ?, ?, ?, ?, ?, " +
                    "now(), (select user_name from users where user_id = ?), " +
                    "now(), (select user_name from users where user_id = ?), ?, ?, ?)";

            try {
                PreparedStatement stmt = conn.prepareStatement(qry);
                stmt.setString(1, args[0]);
                stmt.setString(2, args[1]);
                stmt.setString(3, args[2]);
                stmt.setString(4, args[3]);
                stmt.setTimestamp(5, tStart);
                stmt.setTimestamp(6, tEnd);
                stmt.setInt(7, USER_ID);
                stmt.setInt(8, USER_ID);
                stmt.setInt(9, custId);
                stmt.setInt(10, userId);
                stmt.setInt(11, contactId);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException se) {
                general.Messages.displayError(Messages.getMethodName(), se.getMessage());
            }
        // Update
        } else if (dml =='u') {
            qry = "update appointments " +
                "set title = ?, description = ?, Location = ?, type = ?, start = ?, end = ?, " +
                    "last_update = now(), Last_Updated_By = ?, Customer_ID = ?, User_id = ?, contact_id = ? " +
                    "where appointment_id = ?";
            try {
                PreparedStatement stmt = conn.prepareStatement(qry);
                stmt.setString(1, args[0]);
                stmt.setString(2, args[1]);
                stmt.setString(3, args[2]);
                stmt.setString(4, args[3]);
                stmt.setTimestamp(5, tStart);
                stmt.setTimestamp(6, tEnd);
                stmt.setInt(7, USER_ID);
                stmt.setInt(8, custId);
                stmt.setInt(9, userId);
                stmt.setInt(10, contactId);
                stmt.setInt(11, (Integer.parseInt(args[4])));
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException se) {
                general.Messages.displayError(Messages.getMethodName(), se.getMessage());
            }
        // Delete
        } else if (dml == 'd') {
            qry = "delete from appointments where appointment_id = ?";
            try {
                PreparedStatement stmt = conn.prepareStatement(qry);
                stmt.setInt(1, (Integer.parseInt(args[0])));
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException se) {
                general.Messages.displayError(Messages.getMethodName(), se.getMessage());
            }
        }
    }

    /**
     * LAMBDA NOTES: Use a reusable lambda expression to create the prepared statement.
     * Database DML queries (insert, update, delete) for customers table.
     * @param divisionId The ID from the Division combo-box.
     * @param dml Specify the DML action. i = insert, u = update, d = delete
     * @param args Vargs for field text values. Pass: customer_name, address, postal_code, phone, [apptID]
     */
    public static void customersTableDML(int divisionId, char dml, String... args) {

        String qry;

        // Insert
        if (dml == 'i') {
            qry = "insert into customers (customer_name, address, postal_code, phone, create_date, created_by, " +
                    "last_update, Last_Updated_By, division_ID) " +
                    "values (?, ?, ?, ?, " +
                    "now(), (select user_name from users where user_id = ?), " +
                    "now(), (select user_name from users where user_id = ?), ?)";

            try {
                PreparedStatement stmt = conn.prepareStatement(qry);
                stmt.setString(1, args[0]);
                stmt.setString(2, args[1]);
                stmt.setString(3, args[2]);
                stmt.setString(4, args[3]);
                stmt.setInt(5, USER_ID);
                stmt.setInt(6, USER_ID);
                stmt.setInt(7, divisionId);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException se) {
                general.Messages.displayError(Messages.getMethodName(), se.getMessage());
            }
            // Update
        } else if (dml =='u') {
            qry = "update customers " +
                    "set customer_name = ?, address = ?, postal_code = ?, phone = ?, " +
                    "last_update = now(), Last_Updated_By = ?, division_ID = ? " +
                    "where customer_id = ?";
            try {
                PreparedStatement stmt = conn.prepareStatement(qry);
                stmt.setString(1, args[0]);
                stmt.setString(2, args[1]);
                stmt.setString(3, args[2]);
                stmt.setString(4, args[3]);
                stmt.setInt(5, USER_ID);
                stmt.setInt(6, divisionId);
                stmt.setInt(7, (Integer.parseInt(args[4])));
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException se) {
                general.Messages.displayError(Messages.getMethodName(), se.getMessage());
            }
            // Delete
        } else if (dml == 'd') {
            qry = "delete from customers where customer_id = ?";
            try {
                PreparedStatement stmt = conn.prepareStatement(qry);
                stmt.setInt(1, (Integer.parseInt(args[0])));
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException se) {
                general.Messages.displayError(Messages.getMethodName(), se.getMessage());
            }
        }
    }

    public static ObservableList<Appointment> getUpcomingAppointment(ObservableList<Appointment> appt, LocalDateTime now){

        String qry = "select Appointment_ID, title, location, Start " +
                "from appointments " +
                "where start between ? and date_add(?, interval 15 minute)";

        PrpStmtInterface lambStmnt = (String strQry, String... strArg) -> {
            PreparedStatement stmt = conn.prepareStatement(strQry);
            stmt.setTimestamp(1, Timestamp.valueOf(now));
            stmt.setTimestamp(2, Timestamp.valueOf(now));
            return stmt;
        };

        try (
                PreparedStatement pStmt = lambStmnt.ps(qry);
                ResultSet rs = pStmt.executeQuery();
        )
        {
            if (rs.isBeforeFirst() && rs.getRow() == 0) {
                while (rs.next()) {
                    Appointment item = new Appointment(rs.getInt("Appointment_ID"),
                            rs.getString("title"), null, rs.getString("location"), 0, null,
                            null, null, null, 0, null, 0, null);
                    appt.add(item);
                }
                return appt;
            }
        } catch (SQLException se) {
            general.Messages.displayError(Messages.getMethodName(), se.getMessage());
        }
        return appt;
    }

}
