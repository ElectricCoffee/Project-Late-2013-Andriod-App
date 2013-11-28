package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class User {
    private int _id;
    private String _username;
    private String _password;
    private Name _name;

    public User () {}

    public User (String username, String password, Name name )
    {
        _username = username;
        _password = password;
        _name = name;
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public int getId() {
        return _id;
    }

    public Name getName() {
        return _name;
    }

    public void setName(Name name) {
        _name = name;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

}
