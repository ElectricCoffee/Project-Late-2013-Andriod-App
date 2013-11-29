package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import dk.eal.learerbookingsystem.model.Semester;

/**
 * Created by Trine on 29-11-13.
 */
public class SemesterSource {
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    private String[] _allColumns = {
        DbHelper.COLUMN_ID,
        DbHelper.COLUMN_SEMESTER_NAME
    };

    public SemesterSource(Context context) {
        _dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        _database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public Semester createSemester(Semester semester) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord
        values.put(DbHelper.COLUMN_SEMESTER_NAME, semester.getName());
        long insertId = _database.insert(DbHelper.TABLE_SEMESTER, null,
                values);//Definere en key til at indentificere kolonnens id

        return getSemesterById(insertId); /*Genlæser id*/
    }

    public boolean semesterExists(Semester semester) {
        Cursor cursor = _database.query(DbHelper.TABLE_SEMESTER, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + semester.getId(), null,
            null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public Semester getSemesterById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_SEMESTER, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + id, null,
            null, null, null);
        cursor.moveToFirst();
        Semester semester = cursorToSemester(cursor);
        cursor.close();
        return semester;
    }

    private Semester cursorToSemester(Cursor cursor) {
        Semester semester = new Semester(cursor.getString(1));
        semester.setId(cursor.getLong(0));
        return semester;
    }
}
