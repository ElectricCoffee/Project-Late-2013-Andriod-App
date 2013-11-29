package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import dk.eal.learerbookingsystem.model.Booking;
import dk.eal.learerbookingsystem.model.ConcreteBooking;
import dk.eal.learerbookingsystem.model.HomeRoomClass;
import dk.eal.learerbookingsystem.model.Name;
import dk.eal.learerbookingsystem.model.PossibleBooking;
import dk.eal.learerbookingsystem.model.Semester;
import dk.eal.learerbookingsystem.model.Student;
import dk.eal.learerbookingsystem.model.Subject;
import dk.eal.learerbookingsystem.model.Teacher;
import dk.eal.learerbookingsystem.model.User;

/**
 * Created by Trine on 28-11-13.
 */
public class DataSource {
    private SQLiteDatabase database; //ref til db
    private DbHelper dbHelper; //ref til db helper
    private String[] allColumns = {
        DbHelper.COLUMN_NAME_ID, DbHelper.COLUMN_NAME_FIRSTNAME, DbHelper.COLUMN_NAME_LASTNAME,
        DbHelper.COLUMN_USER_ID, DbHelper.COLUMN_USER_USERNAME, DbHelper.COLUMN_USER_PASSWORD,
        DbHelper.COLUMN_HOMEROOM_ID, DbHelper.COLUMN_HOMEROOM_NAME,
        DbHelper.COLUMN_SEMESTER_ID, DbHelper.COLUMN_SEMESTER_NAME,
        DbHelper.COLUMN_STUDENT_ID, DbHelper.COLUMN_STUDENT_APPROVED,
        DbHelper.COLUMN_TEACHER_ID,
        DbHelper.COLUMN_ADMINISTRATOR_ID,
        DbHelper.COLUMN_SUBJECT_ID, DbHelper.COLUMN_SUBJECT_NAME,
        DbHelper.COLUMN_BOOKING_ID, DbHelper.COLUMN_BOOKING_STARTTIME, DbHelper.COLUMN_BOOKING_ENDTIME,
        DbHelper.COLUMN_POOSIBLEBOOKING_ID,
        DbHelper.COLUMN_CONCRETEBOOKING_ID, DbHelper.COLUMN_CONCRETEBOOKING_COMMENTS, DbHelper.COLUMN_CONCRETEBOOKING_TYPE
    };
    private SimpleDateFormat _iso8601format;

    public DataSource(Context context) {
        dbHelper = new DbHelper(context);
        _iso8601format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // ConceteBooking(
    //   String comment, Byte type, Byte statusChanged, PossibleBooking(
    //     Booking booking(Date startTime, Date endTime, Subject subject
    //        subject(
    //           String name, Semester semester, Teacher teacher(
    //              User user (
    //                  Name name(
    //                     String firstname, String lastname))))))
    public ConcreteBooking createConcreteBooking (ConcreteBooking concreteBooking) {

        concreteBooking.getComment();
        concreteBooking.getType();
        concreteBooking.getStatusChanged();
        concreteBooking.getBooking();
        concreteBooking.getPossibleBooking();
        concreteBooking.getStudent();

        ContentValues values = new ContentValues();

        values.put(DbHelper.COLUMN_CONCRETEBOOKING_COMMENTS, concreteBooking.getComment());
        values.put(DbHelper.COLUMN_CONCRETEBOOKING_TYPE, concreteBooking.getType());
        values.put(DbHelper.COLUMN_CONCRETBOOKING_STATUSCHANGED, concreteBooking.getStatusChanged());

        long insertId = database.insert(DbHelper.TABLE_CONCRETEBOOKING, null, values);

        Cursor cursor = database.query(dbHelper.TABLE_CONCRETEBOOKING, allColumns, DbHelper.COLUMN_CONCRETEBOOKING_ID + "=" +
            insertId, null, null, null, null);
        cursor.moveToFirst();
        cursor.close();

        ConcreteBooking newConcreteBooking = cursorToConcreteBooking(cursor); /*Genlæser id*/
        cursor.close();
        return newConcreteBooking;
    }

    private ConcreteBooking cursorToConcreteBooking(Cursor cursor) {
        cursor = database.query()
        Name teacherName = new Name(cursor.getString(1), cursor.getString(2));
        User teacherUser = new User(cursor.getString(1),cursor.getString(2),teacherName);
        Teacher teacher = new Teacher(teacherUser);
        Semester semester = new Semester(cursor.getString(1));
        Subject subject = new Subject(cursor.getString(1), semester, teacher);

        Name studentName = new Name(cursor.getString(1), cursor.getString(2));
        User studentUser = new User(cursor.getString(1),cursor.getString(2),studentName);
        HomeRoomClass homeroom = new HomeRoomClass(cursor.getString(1));
        Student student = new Student((byte) cursor.getInt(1), studentUser, homeroom);

        Booking booking;
        try {
            booking = new Booking(
                _iso8601format.parse(cursor.getString(1)),
                _iso8601format.parse(cursor.getString(2)),
                subject
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PossibleBooking possibleBooking = new PossibleBooking(booking);
        ConcreteBooking item = new ConcreteBooking(
            cursor.getString(1),
            (byte) cursor.getInt(2),
            (byte) cursor.getInt(3),
            booking,
            possibleBooking,
            student
        );
        return item;
    }
}


