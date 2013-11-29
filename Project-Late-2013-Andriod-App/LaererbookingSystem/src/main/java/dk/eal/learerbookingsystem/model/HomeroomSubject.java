package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class HomeroomSubject {

    private long _id;
    private Subject _subject;
    private HomeRoomClass _homeroom;

    public HomeroomSubject() {}

    public HomeroomSubject(Subject subject, HomeRoomClass homeroom) {
        _subject = subject;
        _homeroom = homeroom;
    }

    public Subject getSubject() {
        return _subject;
    }

    public void setSubject(Subject subject) {
        _subject = subject;
    }

    public HomeRoomClass getHomeroom() {
        return _homeroom;
    }

    public void setHomeroom(HomeRoomClass homeroom) {
        _homeroom = homeroom;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }
}
