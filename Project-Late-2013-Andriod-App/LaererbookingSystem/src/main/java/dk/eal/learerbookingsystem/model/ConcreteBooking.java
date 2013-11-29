package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class ConcreteBooking {
    private long _id;
    private String _comment;
    private byte _type;
    private byte _statusChanged;
    private Booking _booking;
    private PossibleBooking _possibleBooking;
    private Student _student;

    public ConcreteBooking() {}

    public ConcreteBooking(String comment, byte type, byte statusChanged, Booking booking, PossibleBooking possibleBooking, Student student) {
        _comment = comment;
        _type = type;
        _statusChanged = statusChanged;
        _booking = booking;
        _possibleBooking = possibleBooking;
        _student = student;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
       _comment = comment;
    }

    public byte getType() {
        return _type;
    }

    public void setType(byte type) {
        _type = type;
    }

    public byte getStatusChanged() {
        return _statusChanged;
    }

    public void setStatusChanges(byte statusChanged) {
        _statusChanged = statusChanged;
    }

    public Booking getBooking() {
        return _booking;
    }

    public void setBooking(Booking booking) {
        _booking = booking;
    }

    public PossibleBooking getPossibleBooking() {
        return _possibleBooking;
    }

    public void setPossibleBooking(PossibleBooking possibleBooking) {
        _possibleBooking = possibleBooking;
    }

    public Student getStudent() {
        return _student;
    }

    public void setStudent(Student student) {
        _student = student;
    }
}
