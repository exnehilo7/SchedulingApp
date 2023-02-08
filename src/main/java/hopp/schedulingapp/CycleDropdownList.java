package hopp.schedulingapp;

import general.DropdownList;
import javafx.collections.ObservableList;

/**
 * An Interface for an Observable List loop lambda abstract.
 */
public interface CycleDropdownList

{
    /**
     * An abstract function for a lambda call to loop through an ObservableList with DropdownList objects
     * @param list A DropdownList class to cycle through.
     * @param str The String value to look for.
     * @return Return the matching string's ID
     */
    int id (ObservableList<DropdownList> list, String str);

}
