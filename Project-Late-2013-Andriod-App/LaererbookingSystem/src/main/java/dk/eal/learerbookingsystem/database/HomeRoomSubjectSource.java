package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import dk.eal.learerbookingsystem.model.HomeRoomClass;
import dk.eal.learerbookingsystem.model.HomeroomSubject;
import dk.eal.learerbookingsystem.model.Semester;
import dk.eal.learerbookingsystem.model.Subject;
import dk.eal.learerbookingsystem.model.Teacher;

/**
 * Created by Trine on 29-11-13.
 */
public class HomeRoomSubjectSource {

    private Context _context;
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    private String[] _allColumns = {
            DbHelper.COLUMN_ID,
            DbHelper.COLUMN_FK_HOMEROOM_ID,
            DbHelper.COLUMN_FK_SUBJECT_ID
    };

    public HomeRoomSubjectSource(Context context) {
        _context = context;
        _dbHelper = new DbHelper(_context);
    }

    public void open() throws SQLException {
        _database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public HomeroomSubject createHomeRoomSubject(HomeroomSubject homeroomSubject) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord

        HomeRoomSource homeRoomSource = new HomeRoomSource(_context);
        SubjectSource subjectSource = new SubjectSource(_context);

        if (!homeRoomSource.homeRoomExists(homeroomSubject.getHomeroom()))
            homeroomSubject.setHomeroom(homeRoomSource.createHomeRoom(homeroomSubject.getHomeroom()));

        if(!subjectSource.subjectExists(homeroomSubject.getSubject()))
            homeroomSubject.setSubject(subjectSource.createSubject(homeroomSubject.getSubject()));

        values.put(DbHelper.COLUMN_ID, homeroomSubject.getId());
        values.put(DbHelper.COLUMN_FK_HOMEROOM_ID, homeroomSubject.getId());
        values.put(DbHelper.COLUMN_FK_SUBJECT_ID, homeroomSubject.getId());

        long insertId = _database.insert(DbHelper.TABLE_HOMEROOMSUBJECT, null,
                values);//Definere en key til at indentificere kolonnens id

        return getHomeRoomSubjectById(insertId); //Retunere den nye kommentar
    }

    public boolean homeRoomSubjectExists(HomeroomSubject homeroomSubject) {
        Cursor cursor = _database.query(DbHelper.TABLE_HOMEROOMSUBJECT, //Opretter en cursor
                _allColumns, DbHelper.COLUMN_ID + " = " + homeroomSubject.getId(), null,
                null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public HomeroomSubject getHomeRoomSubjectById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_HOMEROOMSUBJECT, //Opretter en cursor
                _allColumns, DbHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        HomeroomSubject homeroomSubject = cursorToHomeRoomSubject(cursor);
        cursor.close();
        return homeroomSubject;
    }

    public HomeroomSubject cursorToHomeRoomSubject(Cursor cursor) {
        HomeRoomSource homeRoomSource = new HomeRoomSource(_context);
        HomeRoomClass homeRoom = null;
        try {
            homeRoomSource.open();
            homeRoom = homeRoomSource.getHomeRoomById(cursor.getLong(3));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            homeRoomSource.close();
        }

        SubjectSource subjectSource = new SubjectSource(_context);
        Subject subject = null;
        try {
            subjectSource.open();
            subject = subjectSource.getSubjectById(cursor.getLong(3));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            subjectSource.close();
        }

        HomeroomSubject homeroomSubject = new HomeroomSubject(subject, homeRoom);
        homeroomSubject.setId(cursor.getLong(0));
        return homeroomSubject;
    }
}
