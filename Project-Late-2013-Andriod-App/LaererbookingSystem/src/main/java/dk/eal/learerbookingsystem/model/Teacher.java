package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class Teacher {
    private int _id;
    private User _user;

    public Teacher() {}

    public Teacher(User user) {
        _user = user;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public User getUser() {
        return _user;
    }

    public void setUser(User user){
        _user = user;
    }
}
