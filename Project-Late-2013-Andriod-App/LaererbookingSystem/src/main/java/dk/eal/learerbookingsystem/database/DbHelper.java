package dk.eal.learerbookingsystem.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Trine on 28-11-13.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bookingSystem.db";
    private static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID = "_id";

    //Table name
    public static final String
            TABLE_NAME = "Navn",
            COLUMN_NAME_FIRSTNAME = "Fornavn",
            COLUMN_NAME_LASTNAME = "Efternavn";

    //Table homeroom
    public static final String
            TABLE_HOMEROOM = "Hold",
            COLUMN_HOMEROOM_NAME = "Navn";

    //Table user
    public static final String
            TABLE_USER = "Bruger",
            COLUMN_USER_USERNAME = "Brugernavn",
            COLUMN_USER_PASSWORD = "Password";

    //Table student
    public static final String
            TABLE_STUDENT = "Studerende",
            COLUMN_STUDENT_APPROVED = "Godkendt";

    //Table teacher
    public static final String TABLE_TEACHER = "Lærer";

    //Table administrator
    public static final String
            TABLE_ADMINISTRATOR = "Administrator";

    //Table semester
    public static final String
            TABLE_SEMESTER = "Semester",
            COLUMN_SEMESTER_NAME = "Navn";

    //Table sebject
    public static final String
            TABLE_SUBJECT = "Fag",
            COLUMN_SUBJECT_NAME = "Navn";

    //Table homeroomsubject
    public static final String
            TABLE_HOMEROOMSUBJECT = "HoldFag";

    //Table booking
    public static final String
            TABLE_BOOKING = "Booking",
            COLUMN_BOOKING_STARTTIME = "Start Tid",
            COLUMN_BOOKING_ENDTIME = "Slut Tid";

    //Table possiblebooking
    public static final String
            TABLE_POSSIBLEBOOKING = "Mulig Booking",
            COLUMN_POSSIBLEBOOKING_DURATION = "Varighed";

    //Table concrete booking
    public static final String
            TABLE_CONCRETEBOOKING = "Konkret Booking",
            COLUMN_CONCRETEBOOKING_TYPE = "Type",
            COLUMN_CONCRETEBOOKING_COMMENTS = "Kommentar",
            COLUMN_CONCRETBOOKING_STATUSCHANGED = "Ændret Status";

    //All forging keys
    public static final String
        COLUMN_FK_HOMEROOM_ID = "Hold" + COLUMN_ID,
        COLUMN_FK_NAME_ID = "Navn" + COLUMN_ID,
        COLUMN_FK_USER_ID = "Bruger" + COLUMN_ID,
        COLUMN_FK_SEMESTER_ID = "Semester" + COLUMN_ID,
        COLUMN_FK_TEACHER_ID = "Lærer" + COLUMN_ID,
        COLUMN_FK_SUBJECT_ID = "Fag" + COLUMN_ID,
        COLUMN_FK_BOOKING_ID = "Booking" + COLUMN_ID,
        COLUMN_FK_POSSIBLEBOOKING_ID = "Mulig Booking" + COLUMN_ID,
        COLUMN_FK_STUDENT_ID = "Studerende" + COLUMN_ID;

    //Creating all tables
    private static final String NAME_CREATE = "create table "
            + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME_FIRSTNAME + " varchar(50) not null,"
            + COLUMN_NAME_LASTNAME + " varchar(50) not null);";

    private static final String HOMEROOM_CREATE = "create table "
            + TABLE_HOMEROOM + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_HOMEROOM_NAME + " varchar(50) not null);";

    private static final String USER_CREATE = "create table "
            + TABLE_USER + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USER_USERNAME + " varchar(50) not null,"
            + COLUMN_USER_PASSWORD + " varchar(50) not null,"
            + COLUMN_FK_NAME_ID + " integer not null,"
            + "foreign key(" + COLUMN_FK_NAME_ID + ") references " + TABLE_NAME + "(" + COLUMN_ID + "));";

    private static final String STUDENT_CREATE = "create table "
            + TABLE_STUDENT + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_STUDENT_APPROVED + " tinyint not null,"
            + COLUMN_FK_USER_ID + " integer not null,"
            + COLUMN_FK_HOMEROOM_ID + " integer not null,"
            + "foreign key(" + COLUMN_FK_USER_ID + ") references " + TABLE_USER + "(" + COLUMN_ID + "),"
            + "foreign key(" + COLUMN_FK_HOMEROOM_ID + ") references " + TABLE_HOMEROOM + "(" + COLUMN_ID + "));";

    private static final String TEACHER_CREATE = "create table "
            + TABLE_TEACHER + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_FK_USER_ID + " integer not null,"
            + "foreign key(" + COLUMN_FK_USER_ID+ ") references " + TABLE_USER + "(" + COLUMN_ID + "));";

    private static final String ADMINISTRATOR_CREATE = "create table "
            + TABLE_ADMINISTRATOR + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_FK_USER_ID + " integer not null,"
            + "foreign key(" + COLUMN_FK_USER_ID+ ") references " + TABLE_USER + "(" + COLUMN_ID + "));";

    private static final String SEMESTER_CREATE = "create table "
            + TABLE_SEMESTER + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SEMESTER_NAME + " varchar(50) not null)";

    private static final String SUBJECT_CREATE = "create table "
            + TABLE_SUBJECT + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SUBJECT_NAME + " varchar(50) not null,"
            + COLUMN_FK_SUBJECT_ID + " integer not null,"
            + COLUMN_FK_TEACHER_ID + " integer not null,"
            + "foreign key(" + COLUMN_FK_SEMESTER_ID + ") references " + TABLE_SEMESTER + "(" + COLUMN_ID + "),"
            + "foreign key(" + COLUMN_FK_TEACHER_ID + ") references " + TABLE_TEACHER + "(" + COLUMN_ID + "));";

    private static final String HOMEROOMSUBJECT_CREATE = "create table "
            + TABLE_HOMEROOMSUBJECT + "(" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_FK_HOMEROOM_ID + " integer not null,"
            + COLUMN_FK_SUBJECT_ID + " integer not null,"
            + "foreign key(" + COLUMN_FK_HOMEROOM_ID + ") references " + TABLE_HOMEROOM + "(" + COLUMN_ID + "),"
            + "foreign key(" + COLUMN_FK_SUBJECT_ID + ") references " + TABLE_SUBJECT + "(" + COLUMN_ID + "));";

    private static final String BOOKING_CREATE = "create table "
            + TABLE_BOOKING + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BOOKING_STARTTIME + " date not null,"
            + COLUMN_BOOKING_ENDTIME + " date not null,"
            + COLUMN_FK_SUBJECT_ID + " integer not null,"
            + "foreign key(" + COLUMN_FK_SUBJECT_ID + ") references " + TABLE_SUBJECT + "(" + COLUMN_ID + "));";

    private static final String POSSIBLEBOOKING_CREATE = "create table "
            + TABLE_POSSIBLEBOOKING + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_POSSIBLEBOOKING_DURATION + " int not null, "
            + COLUMN_FK_BOOKING_ID + " integer not null,"
            + "foreign key(" + COLUMN_FK_BOOKING_ID + ") references " + TABLE_BOOKING + "(" + COLUMN_ID + "));";

    private static final String CONCRETEBOOKING_CREATE = "create table "
            + TABLE_CONCRETEBOOKING + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CONCRETEBOOKING_TYPE + " tinyint not null,"
            + COLUMN_CONCRETEBOOKING_COMMENTS + " varchar(150) not null,"
            + COLUMN_CONCRETBOOKING_STATUSCHANGED + " tinyint not null,"
            + COLUMN_FK_BOOKING_ID + " integer not null,"
            + COLUMN_FK_POSSIBLEBOOKING_ID + " integer not null,"
            + COLUMN_FK_STUDENT_ID + " integer not null,"
            + "foreign key(" + COLUMN_FK_BOOKING_ID + ") references " + TABLE_BOOKING + "(" + COLUMN_ID + "),"
            + "foreign key(" + COLUMN_FK_POSSIBLEBOOKING_ID + ") references " + TABLE_POSSIBLEBOOKING + "(" + COLUMN_ID + "));"
            + "foreign key(" + COLUMN_FK_STUDENT_ID + ") references " + TABLE_STUDENT + "(" + COLUMN_ID + "));";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NAME_CREATE);
        db.execSQL(USER_CREATE);
        db.execSQL(HOMEROOM_CREATE);
        db.execSQL(SEMESTER_CREATE);
        db.execSQL(STUDENT_CREATE);
        db.execSQL(TEACHER_CREATE);
        db.execSQL(ADMINISTRATOR_CREATE);
        db.execSQL(SUBJECT_CREATE);
        db.execSQL(HOMEROOMSUBJECT_CREATE);
        db.execSQL(BOOKING_CREATE);
        db.execSQL(POSSIBLEBOOKING_CREATE);
        db.execSQL(CONCRETEBOOKING_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(DbHelper.class.getName(), "Upgrading database from version" + oldVersion + "to" + newVersion + "which will destroy all old data");
        db.execSQL("Drop table if exist" + TABLE_CONCRETEBOOKING + TABLE_POSSIBLEBOOKING + TABLE_BOOKING + TABLE_HOMEROOMSUBJECT
                + TABLE_SUBJECT + TABLE_ADMINISTRATOR + TABLE_TEACHER + TABLE_STUDENT + TABLE_SEMESTER
                + TABLE_HOMEROOM + TABLE_USER  + TABLE_NAME
        );

    }
}
