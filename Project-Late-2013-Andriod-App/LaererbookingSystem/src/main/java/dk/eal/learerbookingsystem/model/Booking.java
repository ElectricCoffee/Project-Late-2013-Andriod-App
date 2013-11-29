package dk.eal.learerbookingsystem.model;

import java.util.Date;

/**
 * Created by Trine on 28-11-13.
 */
public class Booking {
    private long _id;
    private Date _startDate;
    private Date _endDate;
    private Subject _subject;

    public Booking() {}

    public Booking (Date startDate, Date endDate, Subject subject) {
        _startDate = startDate;
        _endDate = endDate;
        _subject = subject;
    }

    public Date getEndDate() {
        return _endDate;
    }

    public void setEndDate(Date endDate) {
        _endDate = endDate;
    }

    public Date getStartDate() {
        return _startDate;
    }

    public void setStartDate(Date startDate) {
        _startDate = startDate;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public Subject getSubject() {
        return _subject;
    }

    public void setSubject(Subject subject) {
        _subject = subject;
    }
}
