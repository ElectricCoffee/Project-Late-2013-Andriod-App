package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import dk.eal.learerbookingsystem.model.Booking;
import dk.eal.learerbookingsystem.model.PossibleBooking;
import dk.eal.learerbookingsystem.model.Student;
import dk.eal.learerbookingsystem.model.Subject;

/**
 * Created by Trine on 04-12-13.
 */
public class PossibleBookingSource {
    private Context _context;
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    private String[] _allColumns = {
            DbHelper.COLUMN_ID,
            DbHelper.COLUMN_POSSIBLEBOOKING_DURATION,
            DbHelper.COLUMN_FK_BOOKING_ID
    };
    private SimpleDateFormat _iso8601format;

    public PossibleBookingSource(Context context) {
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

    public PossibleBooking createPossibleBooking(PossibleBooking possibleBooking) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord

        BookingSource bookingSource = new BookingSource(_context);

        if (!bookingSource.bookingExists(possibleBooking.getBooking()))
            possibleBooking.setBooking(bookingSource.createBooking(possibleBooking.getBooking()));

        values.put(DbHelper.COLUMN_POSSIBLEBOOKING_DURATION, possibleBooking.getDuration());
        values.put(DbHelper.COLUMN_FK_BOOKING_ID, possibleBooking.getBooking().getId());

        long insertId = _database.insert(DbHelper.TABLE_USER, null,
                values);//Definere en key til at indentificere kolonnens id

        return getPossibleBookingById(insertId); //Retunere den nye kommentar
    }

    public boolean possibleBookingExists(PossibleBooking possibleBooking) {
        Cursor cursor = _database.query(DbHelper.TABLE_POSSIBLEBOOKING, //Opretter en cursor
                _allColumns, DbHelper.COLUMN_ID + " = " + possibleBooking.getId(), null,
                null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public PossibleBooking getPossibleBookingById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_POSSIBLEBOOKING, //Opretter en cursor
                _allColumns, DbHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        PossibleBooking possibleBooking = cursorToPossibleBooking(cursor);
        cursor.close();
        return possibleBooking;
    }

    public PossibleBooking cursorToPossibleBooking(Cursor cursor) {

        BookingSource bookingSource = new BookingSource(_context);
        Booking booking = bookingSource.getBookingById(cursor.getLong(3));

        PossibleBooking possibleBooking = new PossibleBooking(cursor.getInt(1), booking);
        possibleBooking.setId(cursor.getLong(0));

        return possibleBooking;
    }
}
