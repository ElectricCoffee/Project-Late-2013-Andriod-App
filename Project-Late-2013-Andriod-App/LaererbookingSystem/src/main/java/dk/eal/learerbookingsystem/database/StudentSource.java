package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import dk.eal.learerbookingsystem.model.HomeRoomClass;
import dk.eal.learerbookingsystem.model.Semester;
import dk.eal.learerbookingsystem.model.Student;
import dk.eal.learerbookingsystem.model.Subject;
import dk.eal.learerbookingsystem.model.Teacher;
import dk.eal.learerbookingsystem.model.User;

/**
 * Created by Trine on 29-11-13.
 */
public class StudentSource {


    private Context _context;
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    private String[] _allColumns = {
            DbHelper.COLUMN_ID,
            DbHelper.COLUMN_STUDENT_APPROVED,
            DbHelper.COLUMN_FK_USER_ID,
            DbHelper.COLUMN_FK_HOMEROOM_ID
    };

    public StudentSource(Context context) {
        _context = context;
        _dbHelper = new DbHelper(_context);
    }

    public void open() throws SQLException {
        _database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public Student createStudent(Student student) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord

        UserSource userSource = new UserSource(_context);
        HomeRoomSource homeRoomSource = new HomeRoomSource(_context);

        User user = new User(student.getUsername(), student.getPassword(), student.getName());
        user.setId(student.getUserId());
        if (!userSource.userExists(user))
            user = userSource.createUser(user);

        if(!homeRoomSource.homeRoomExists(student.getHomeRoomClass()))
            student.setHomeRoomClass(homeRoomSource.createHomeRoom(student.getHomeRoomClass()));

        values.put(DbHelper.COLUMN_STUDENT_APPROVED, student.getApproved());
        values.put(DbHelper.COLUMN_FK_USER_ID, user.getId());
        values.put(DbHelper.COLUMN_FK_HOMEROOM_ID, student.getHomeRoomClass().getId());

        long insertId = _database.insert(DbHelper.TABLE_SUBJECT, null,
                values);//Definere en key til at indentificere kolonnens id

        return getStudentById(insertId); //Retunere den nye kommentar
    }

    public boolean studentExists(Student student) {
        Cursor cursor = _database.query(DbHelper.TABLE_STUDENT, //Opretter en cursor
                _allColumns, DbHelper.COLUMN_ID + " = " + student.getId(), null,
                null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public Student getStudentById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_STUDENT, //Opretter en cursor
                _allColumns, DbHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        Student student = cursorToStudent(cursor);
        cursor.close();
        return student;
    }

    public Student cursorToStudent(Cursor cursor) {
        UserSource userSource = new UserSource(_context);
        User user = userSource.getUserById(cursor.getLong(3));

        HomeRoomSource homeRoomSource = new HomeRoomSource(_context);
        HomeRoomClass homeRoomClass = homeRoomSource.getHomeRoomById(cursor.getLong(3));

        Student student = new Student(user.getUsername(), user.getPassword(),(byte)cursor.getInt(1), homeRoomClass, user.getName());
        student.setId(cursor.getLong(0));
        return student;
    }
}
