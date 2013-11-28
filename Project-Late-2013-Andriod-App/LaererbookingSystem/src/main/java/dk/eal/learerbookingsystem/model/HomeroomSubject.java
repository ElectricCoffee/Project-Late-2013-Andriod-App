package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class HomeroomSubject {
    private Subject _subject;
    private Homeroom _homeroom;

    public HomeroomSubject() {}

    public HomeroomSubject(Subject subject, Homeroom homeroom) {
        _subject = subject;
        _homeroom = homeroom;
    }

    public Subject getSubject() {
        return _subject;
    }

    public void setSubject(Subject subject) {
        _subject = subject;
    }

    public Homeroom getHomeroom() {
        return _homeroom;
    }

    public void setHomeroom(Homeroom homeroom) {
        _homeroom = homeroom;
    }
}
