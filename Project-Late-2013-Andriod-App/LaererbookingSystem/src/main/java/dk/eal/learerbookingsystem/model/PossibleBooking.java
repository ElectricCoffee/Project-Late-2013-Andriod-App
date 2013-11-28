package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class PossibleBooking {

    private int _id;
    private Booking _booking;

    public PossibleBooking(Booking booking) {
        _booking = booking;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public Booking getBooking() {
        return _booking;
    }

    public void setBooking(Booking booking) {
        _booking = booking;
    }
}
