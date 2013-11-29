package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import dk.eal.learerbookingsystem.model.Semester;
import dk.eal.learerbookingsystem.model.Subject;
import dk.eal.learerbookingsystem.model.Teacher;

/**
 * Created by Trine on 29-11-13.
 */
public class SubjectSource {

    private Context _context;
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    private String[] _allColumns = {
        DbHelper.COLUMN_ID,
        DbHelper.COLUMN_SUBJECT_NAME,
        DbHelper.COLUMN_FK_SEMESTER_ID,
        DbHelper.COLUMN_FK_TEACHER_ID
    };

    public SubjectSource(Context context) {
        _context = context;
        _dbHelper = new DbHelper(_context);
    }

    public void open() throws SQLException {
        _database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public Subject createSubject(Subject subject) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord

        SemesterSource semesterSource = new SemesterSource(_context);
        TeacherSource teacherSource = new TeacherSource(_context);

        if (!semesterSource.semesterExists(subject.getSemester()))
            subject.setSemester(semesterSource.createSemester(subject.getSemester()));

        if(!teacherSource.teacherExists(subject.getTeacher()))
            subject.setTeacher(teacherSource.createTeacher(subject.getTeacher()));

        values.put(DbHelper.COLUMN_SUBJECT_NAME, subject.getName());
        values.put(DbHelper.COLUMN_FK_SEMESTER_ID, subject.getSemester().getId());
        values.put(DbHelper.COLUMN_FK_TEACHER_ID, subject.getTeacher().getId());

        long insertId = _database.insert(DbHelper.TABLE_SUBJECT, null,
                values);//Definere en key til at indentificere kolonnens id

        return getSubjectById(insertId); //Retunere den nye kommentar
    }

    public boolean subjectExists(Subject subject) {
        Cursor cursor = _database.query(DbHelper.TABLE_SUBJECT, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + subject.getId(), null,
            null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public Subject getSubjectById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_SUBJECT, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + id, null,
            null, null, null);
        cursor.moveToFirst();
        Subject subject = cursorToSubject(cursor);
        cursor.close();
        return subject;
    }

    public Subject cursorToSubject(Cursor cursor) {
        SemesterSource semesterSource = new SemesterSource(_context);
        Semester semester = semesterSource.getSemesterById(cursor.getLong(3));

        TeacherSource teacherSource = new TeacherSource(_context);
        Teacher teacher = teacherSource.getTeacherById(cursor.getLong(3));

        Subject subject = new Subject(cursor.getString(1), semester, teacher);
        subject.setId(cursor.getLong(0));
        return subject;
    }
}
