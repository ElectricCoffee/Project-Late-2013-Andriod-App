package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dk.eal.learerbookingsystem.contentprovider.ConcreteBookingContentProvider;
import dk.eal.learerbookingsystem.model.Booking;
import dk.eal.learerbookingsystem.model.PossibleBooking;

/**
 * Created by Trine on 04-12-13.
 */
public class PossibleBookingSource {
    private static final String TAG = PossibleBookingSource.class.getName();
    private Context _context;
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    public static String[] ALL_COLUMNS = {
        DbHelper.TABLE_POSSIBLEBOOKING + "." + DbHelper.COLUMN_ID,
        DbHelper.COLUMN_POSSIBLEBOOKING_DURATION,
        DbHelper.COLUMN_FK_BOOKING_ID,
        DbHelper.COLUMN_BOOKING_STARTTIME
    };
    private SimpleDateFormat _iso8601format;

    public PossibleBookingSource(Context context) {
        _context = context;
        _dbHelper = new DbHelper(_context);
        _iso8601format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    public List<PossibleBooking> getAll() {
        List<PossibleBooking> possibleBookings = new ArrayList<PossibleBooking>(); //Opretter en liste af kommentar

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String tables = String.format("%1$s join %3$s on %3$s.%4$s = %1$s.%2$s",
            DbHelper.TABLE_POSSIBLEBOOKING, DbHelper.COLUMN_FK_BOOKING_ID,
            DbHelper.TABLE_BOOKING, DbHelper.COLUMN_ID);

        queryBuilder.setTables(tables);
        Cursor cursor = queryBuilder.query(_database, //null = select*
            ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) { //Når man ikke er nået til slutningen af query resultaterne
            PossibleBooking possibleBooking = cursorToPossibleBooking(cursor);
            possibleBookings.add(possibleBooking);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return possibleBookings;
    }

    public List<PossibleBooking> getAllBySubjectName(String subjectName) {
        List<PossibleBooking> possibleBookings = new ArrayList<PossibleBooking>(); //Opretter en liste af kommentar

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String tables = String.format("%1$s join %3$s on %3$s.%4$s = %1$s.%2$s join %6$s on %6$s.%4$s = %3$s.%5$s",
            DbHelper.TABLE_POSSIBLEBOOKING, DbHelper.COLUMN_FK_BOOKING_ID,
            DbHelper.TABLE_BOOKING, DbHelper.COLUMN_ID, DbHelper.COLUMN_FK_SUBJECT_ID,
            DbHelper.TABLE_SUBJECT);
        Log.d(TAG, "tables: " + tables);

        queryBuilder.setTables(tables);
        queryBuilder.appendWhere(DbHelper.COLUMN_SUBJECT_NAME + " = " + subjectName);

        Cursor cursor = queryBuilder.query(_database, //null = select*
            ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) { //Når man ikke er nået til slutningen af query resultaterne
            PossibleBooking possibleBooking = cursorToPossibleBooking(cursor);
            possibleBookings.add(possibleBooking);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return possibleBookings;
    }

    public boolean possibleBookingExists(PossibleBooking possibleBooking) {
        Cursor cursor = _database.query(DbHelper.TABLE_POSSIBLEBOOKING, //Opretter en cursor
                ALL_COLUMNS, DbHelper.COLUMN_ID + " = " + possibleBooking.getId(), null,
                null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public PossibleBooking getPossibleBookingById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_POSSIBLEBOOKING, //Opretter en cursor
                ALL_COLUMNS, DbHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        PossibleBooking possibleBooking = cursorToPossibleBooking(cursor);
        cursor.close();
        return possibleBooking;
    }

    public PossibleBooking cursorToPossibleBooking(Cursor cursor) {

        BookingSource bookingSource = new BookingSource(_context);
        Booking booking = null;
        try {
            bookingSource.open();
            booking = bookingSource.getBookingById(cursor.getLong(2));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            bookingSource.close();
        }

        PossibleBooking possibleBooking = new PossibleBooking(cursor.getInt(1), booking);
        possibleBooking.setId(cursor.getLong(0));

        return possibleBooking;
    }
}
