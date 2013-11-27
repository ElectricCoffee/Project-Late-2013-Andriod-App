package dk.eal.learerbookingsystem.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * Created by Trine on 27-11-13.
 */
public class BookingModel {
    @SerializedName("subject")
    private String _subject;
    @SerializedName("startDate")
    private Date _startDate;
    @SerializedName("endDate")
    private Date _endDate;
    @SerializedName("comment")
    private String _comment;

    public BookingModel(String subject, Date startDate, Date endDate, String comment) {
        _subject = subject;
        _startDate = startDate;
        _endDate = endDate;
        _comment = comment;
    }

    public String getSubject() {
        return _subject;
    }

    public void setSubject(String subject) {
        _subject = subject;
    }

    public Date getStartDate() {
        return _startDate;
    }

    public void setStartDate(Date startDate) {
        _startDate = startDate;
    }
    public Date getEndDate() {
        return _endDate;
    }

    public void setEndDate(Date endDate) {
        _endDate = endDate;
    }

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        _comment = comment;
    }
}
