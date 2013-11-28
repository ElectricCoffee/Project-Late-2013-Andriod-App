package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class Student {

    private int _id;
    private byte _approve;
    private User _user;
    private Homeroom _homeroom;

    public Student() {}

    public Student(byte approve, User user, Homeroom homeroom) {
        _approve = approve;
        _user = user;
        _homeroom = homeroom;
    }


    public int getId() {
        return _id;
    }

    public void setId(int id) {
       _id = id;
    }

    public byte getApprove() {
        return _approve;
    }

    public void setApprove(byte approve) {
        _approve = approve;
    }

    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }

    public Homeroom getHomeroom() {
        return _homeroom;
    }

    public void setHomeroom(Homeroom homeroom) {
        _homeroom = homeroom;
    }


}
