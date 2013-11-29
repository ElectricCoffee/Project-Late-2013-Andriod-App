package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import dk.eal.learerbookingsystem.model.Booking;
import dk.eal.learerbookingsystem.model.Name;
import dk.eal.learerbookingsystem.model.Subject;
import dk.eal.learerbookingsystem.model.User;

/**
 * Created by Trine on 29-11-13.
 */
public class BookingSource {

    private Context _context;
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    private String[] _allColumns = {
            DbHelper.COLUMN_ID,
            DbHelper.COLUMN_BOOKING_STARTTIME,
            DbHelper.COLUMN_BOOKING_ENDTIME,
            DbHelper.COLUMN_FK_SUBJECT_ID
    };
    private SimpleDateFormat _iso8601format;

    public BookingSource(Context context) {
        _context = context;
        _dbHelper = new DbHelper(_context);
        _iso8601format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    }

    public void open() throws SQLException {
        _database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public Booking createBooking(Booking booking) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord

        SubjectSource subjectSource = new SubjectSource(_context);

        if (!subjectSource.subjectExists(booking.getSubject()))
            booking.setSubject(subjectSource.createSubject(booking.getSubject()));

        values.put(DbHelper.COLUMN_BOOKING_STARTTIME, _iso8601format.format(booking.getStartDate()));
        values.put(DbHelper.COLUMN_BOOKING_ENDTIME, _iso8601format.format(booking.getEndDate()));
        values.put(DbHelper.COLUMN_FK_SUBJECT_ID, booking.getSubject().getId());

        long insertId = _database.insert(DbHelper.TABLE_USER, null,
                values);//Definere en key til at indentificere kolonnens id

        return getBookingById(insertId); //Retunere den nye kommentar
    }

    public boolean bookingExists(Booking booking) {
        Cursor cursor = _database.query(DbHelper.TABLE_BOOKING, //Opretter en cursor
                _allColumns, DbHelper.COLUMN_ID + " = " + booking.getId(), null,
                null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public Booking getBookingById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_BOOKING, //Opretter en cursor
                _allColumns, DbHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        Booking booking = cursorToBooking(cursor);
        cursor.close();
        return booking;
    }

    public Booking cursorToBooking(Cursor cursor) {
        SubjectSource subjectSource = new SubjectSource(_context);
        Subject subject = subjectSource.getSubjectById(cursor.getLong(3));

        Booking booking = new Booking(cursor.getString(1), cursor.getString(2), subject.getName());
        booking.setId(cursor.getLong(0));
        return booking;
    }
}
