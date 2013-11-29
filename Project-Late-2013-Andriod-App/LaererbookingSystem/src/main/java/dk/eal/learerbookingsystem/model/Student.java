package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class Student extends User {

    private long _userId;
    private byte _approved;
    private HomeRoomClass _homeRoomClass;

    public Student() {}

    public Student(String username, String password, byte approve, HomeRoomClass homeRoomClass, Name name) {
        super(username, password, name);
        _approved = approve;
        _homeRoomClass = homeRoomClass;
    }

    public byte getApproved() {
        return _approved;
    }

    public void setApproved(byte approved) {
        _approved = approved;
    }

    public HomeRoomClass getHomeRoomClass() {
        return _homeRoomClass;
    }

    public void setHomeRoomClass(HomeRoomClass homeRoomClass) {
        _homeRoomClass = homeRoomClass;
    }

    public long getUserId() {
        return _userId;
    }

    public void setUserId(long userId) {
        _userId = userId;
    }
}
