package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class Administrator extends User {
    private long _userId;

    public Administrator() {}

    public Administrator(String username, String password, Name name) {
        super(username, password, name);
    }

    public long getUserId() {
        return _userId;
    }

    public void setUserId(long userId) {
        _userId = userId;
    }
}
