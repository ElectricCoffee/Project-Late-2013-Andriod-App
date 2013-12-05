package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import dk.eal.learerbookingsystem.model.Booking;
import dk.eal.learerbookingsystem.model.ConcreteBooking;
import dk.eal.learerbookingsystem.model.PossibleBooking;
import dk.eal.learerbookingsystem.model.Student;

/**
 * Created by Trine on 28-11-13.
 */
public class ConcreteBookingSource {private Context _context;
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    public static final String[] ALL_COLUMNS = {
            DbHelper.COLUMN_ID,
            DbHelper.COLUMN_CONCRETEBOOKING_TYPE,
            DbHelper.COLUMN_CONCRETEBOOKING_COMMENTS,
            DbHelper.COLUMN_CONCRETEBOOKING_STATUSCHANGED,
            DbHelper.COLUMN_FK_BOOKING_ID,
            DbHelper.COLUMN_FK_POSSIBLEBOOKING_ID,
            DbHelper.COLUMN_FK_STUDENT_ID
    };
    private SimpleDateFormat _iso8601format;

    public ConcreteBookingSource(Context context) {
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

    public ConcreteBooking createConcreteBooking(ConcreteBooking concreteBooking) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord

        BookingSource bookingSource = new BookingSource(_context);
        PossibleBookingSource possibleBookingSource = new PossibleBookingSource(_context);
        StudentSource studentSource = new StudentSource(_context);

        if (!bookingSource.bookingExists(concreteBooking.getBooking()))
            concreteBooking.setBooking(bookingSource.createBooking(concreteBooking.getBooking()));

        if(!possibleBookingSource.possibleBookingExists(concreteBooking.getPossibleBooking()))
            concreteBooking.setPossibleBooking(possibleBookingSource.createPossibleBooking(concreteBooking.getPossibleBooking()));

        if(!studentSource.studentExists(concreteBooking.getStudent()))
            concreteBooking.setStudent(studentSource.createStudent(concreteBooking.getStudent()));

        values.put(DbHelper.COLUMN_CONCRETEBOOKING_TYPE, concreteBooking.getType());
        values.put(DbHelper.COLUMN_CONCRETEBOOKING_COMMENTS, concreteBooking.getComment());
        values.put(DbHelper.COLUMN_CONCRETEBOOKING_STATUSCHANGED, concreteBooking.getStatusChanged());
        values.put(DbHelper.COLUMN_FK_BOOKING_ID, concreteBooking.getBooking().getId());
        values.put(DbHelper.COLUMN_FK_POSSIBLEBOOKING_ID, concreteBooking.getPossibleBooking().getId());
        values.put(DbHelper.COLUMN_FK_STUDENT_ID,concreteBooking.getStudent().getId());

        long insertId = _database.insert(DbHelper.TABLE_CONCRETEBOOKING, null,
                values);//Definere en key til at indentificere kolonnens id

        return getConcreteBookingById(insertId); //Retunere den nye kommentar
    }

    public boolean concreteBookingExists(ConcreteBooking concreteBooking) {
        Cursor cursor = _database.query(DbHelper.TABLE_CONCRETEBOOKING, //Opretter en cursor
                ALL_COLUMNS, DbHelper.COLUMN_ID + " = " + concreteBooking.getId(), null,
                null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public ConcreteBooking getConcreteBookingById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_CONCRETEBOOKING, //Opretter en cursor
                ALL_COLUMNS, DbHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        ConcreteBooking concreteBooking = cursorToConcreteBooking(cursor);
        cursor.close();
        return concreteBooking;
    }

    public ConcreteBooking cursorToConcreteBooking(Cursor cursor) {

        BookingSource bookingSource = new BookingSource(_context);
        Booking booking = bookingSource.getBookingById(cursor.getLong(3));

        PossibleBookingSource possibleBookingSource = new PossibleBookingSource(_context);
        PossibleBooking possibleBooking = possibleBookingSource.getPossibleBookingById(cursor.getLong(3));

        StudentSource studentSource = new StudentSource(_context);
        Student student = studentSource.getStudentById(cursor.getLong(3));

        ConcreteBooking concreteBooking = new ConcreteBooking((byte)cursor.getInt(1), cursor.getString(2), (byte) cursor.getInt(3), booking, possibleBooking, student);
        possibleBooking.setId(cursor.getLong(0));

        return concreteBooking;
    }
}
