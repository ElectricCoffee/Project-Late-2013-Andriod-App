package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import dk.eal.learerbookingsystem.model.Name;
import dk.eal.learerbookingsystem.model.Teacher;
import dk.eal.learerbookingsystem.model.User;

/**
 * Created by Trine on 29-11-13.
 */
public class TeacherSource {
    private Context _context;
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    private String[] _allColumns = {
        DbHelper.COLUMN_ID,
        DbHelper.COLUMN_FK_USER_ID
    };

    public TeacherSource(Context context) {
        _context = context;
        _dbHelper = new DbHelper(_context);
    }

    public void open() throws SQLException {
        _database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public Teacher createTeacher(Teacher teacher) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord

        UserSource userSource = new UserSource(_context);

        User user = new User(teacher.getUsername(), teacher.getPassword(), teacher.getName());
        user.setId(teacher.getUserId());
        if (!userSource.userExists(user))
            user = userSource.createUser(user);

        values.put(DbHelper.COLUMN_FK_USER_ID, user.getId());

        long insertId = _database.insert(DbHelper.TABLE_TEACHER, null,
            values);//Definere en key til at indentificere kolonnens id

        return getTeacherById(insertId); //Retunere den nye kommentar
    }

    public boolean teacherExists(Teacher teacher) {
        Cursor cursor = _database.query(DbHelper.TABLE_TEACHER, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + teacher.getId(), null,
            null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public Teacher getTeacherById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_TEACHER, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + id, null,
            null, null, null);
        cursor.moveToFirst();
        Teacher teacher = cursorToTeacher(cursor);
        cursor.close();
        return teacher;
    }

    public Teacher cursorToTeacher(Cursor cursor) {
        UserSource userSource = new UserSource(_context);
        User user = userSource.getUserById(cursor.getLong(3));

        Teacher teacher = new Teacher(user.getUsername(), user.getPassword(), user.getName());
        teacher.setUserId(user.getId());
        return teacher;
    }
}
