package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class Name {

    private long _id;
    private String _firstname;
    private String _lastname;

    public Name() {}

    public Name(String firstname, String lastname) {
        _firstname = firstname;
        _lastname = lastname;
    }

    public String getFirstname() {
        return _firstname;
    }

    public void setFirstname(String firstname) {
       _firstname = firstname;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public String getLastname() {
        return _lastname;
    }

    public void setLastname(String lastname) {
        _lastname = lastname;
    }


}
