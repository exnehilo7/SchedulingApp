package general;

/**
 * Class for the customer object
 */
public class Customer {

    /**
     * Customer ID
     */
    private int id;
    /**
     * Customer name
     */
    private String name;
    /**
     * Customer address
     */
    private String address;
    /**
     * State/province (division) ID
     */
    private int divisionId;
    /**
     * Customer state/province (division)
     */
    private String division;
    /**
     * Country's ID
     */
    private int countryId;
    /**
     * Customer country
     */
    private String country;
    /**
     * Customer postal code
     */
    private String postalCode;
    /**
     * Customer phone number
     */
    private String phone;

    /**
     * Constructor
     * @param id Customer ID
     * @param name Customer name
     * @param address Customer address
     * @param divisionId State/province (division) ID
     * @param division Customer state/province (division)
     * @param countryId Country ID
     * @param country Customer country
     * @param postalCode Customer postal code
     * @param phone Customer phone number
     */
    public Customer(int id, String name, String address, int divisionId, String division, int countryId, String country,
                    String postalCode, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
        this.country = country;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
