package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class PossibleBooking {

    private long _id;
    private int _duration;
    private Booking _booking;

    public PossibleBooking() { }

    public PossibleBooking(int duration, Booking booking) {
       _duration = duration;
        _booking = booking;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public int getDuration() {
        return _duration;
    }

    public void setDuration(int duration) {
        _duration = duration;
    }

    public Booking getBooking() {
        return _booking;
    }

    public void setBooking(Booking booking) {
        _booking = booking;
    }
}
