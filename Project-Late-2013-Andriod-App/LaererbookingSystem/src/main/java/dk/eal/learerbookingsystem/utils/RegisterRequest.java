package dk.eal.learerbookingsystem.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trine on 27-11-13.
 */
public class RegisterRequest {
    @SerializedName("firstname")
    private String _firstname;
    @SerializedName("lastname")
    private String _lastname;
    @SerializedName("email")
    private String _email;
    @SerializedName("password")
    private String _password;

    public RegisterRequest(String firstname, String lastname, String email, String password) {
        _firstname = firstname;
        _lastname = lastname;
        _email = email;
        _password = password;
    }

    public String getFirstname() {
        return _firstname;
    }

    public void setFirstname(String firstname) {
        _firstname = _firstname;
    }

    public String getLastname() {
        return _lastname;
    }

    public void setLastname(String lastname) {
        _lastname = lastname;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }
}
