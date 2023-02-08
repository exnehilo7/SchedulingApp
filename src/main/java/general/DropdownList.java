package general;

/**
 * Class to hold combo box items
 */
public class DropdownList {

    /**
     * The ID of the record in the DB
     */
    private int id;
    /**
     * Text value of the record in the DB
     */
    private String text;

    /**
     * Constructor
     * @param id The ID
     * @param text The text value
     */
    public DropdownList(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.getText();
    }

}
