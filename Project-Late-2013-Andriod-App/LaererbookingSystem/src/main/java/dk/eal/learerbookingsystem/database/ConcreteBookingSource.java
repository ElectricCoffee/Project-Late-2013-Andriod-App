package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import dk.eal.learerbookingsystem.model.ConcreteBooking;

/**
 * Created by Trine on 28-11-13.
 */
public class ConcreteBookingSource {
    private SQLiteDatabase _database; // ref to db
    private DbHelper _dbHelper; // ref to db helper
    private String[] _allColumns = new String[] {
        DbHelper.COLUMN_CONCRETEBOOKING_ID,
        DbHelper.COLUMN_CONCRETEBOOKING_TYPE,
        DbHelper.COLUMN_CONCRETEBOOKING_COMMENTS,
        DbHelper.COLUMN_CONCRETBOOKING_STATUSCHANGED,
        DbHelper.COLUMN_FK_BOOKING_ID,
        DbHelper.COLUMN_FK_POSSIBLEBOOKING_ID,
        DbHelper.COLUMN_FK_STUDENT_ID
    };

    private SimpleDateFormat _iso8601format;

    public ConcreteBookingSource(Context context) {
        _dbHelper = new DbHelper(context);
        _iso8601format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    }

    public void open() throws SQLException {
        _database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public ConcreteBooking createConcreteBooking (ConcreteBooking concreteBooking) {
        ContentValues values = new ContentValues();

        values.put(DbHelper.COLUMN_CONCRETEBOOKING_TYPE, concreteBooking.getType());
        values.put(DbHelper.COLUMN_CONCRETEBOOKING_COMMENTS, concreteBooking.getComment());
        values.put(DbHelper.COLUMN_CONCRETBOOKING_STATUSCHANGED, concreteBooking.getStatusChanged());

        long insertId = _database.insert(DbHelper.TABLE_CONCRETEBOOKING, null, values);

        Cursor cursor = _database.query(_dbHelper.TABLE_CONCRETEBOOKING, _allColumns, DbHelper.COLUMN_CONCRETEBOOKING_ID + "=" +
            insertId, null, null, null, null);
        cursor.moveToFirst();
        cursor.close();

        ConcreteBooking newConcreteBooking = _cursorToConcreteBooking(cursor); /*Genlæser id*/
        cursor.close();
        return newConcreteBooking;
    }
}
