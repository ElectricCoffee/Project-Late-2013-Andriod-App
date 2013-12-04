package dk.eal.learerbookingsystem.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import dk.eal.learerbookingsystem.database.ConcreteBookingSource;
import dk.eal.learerbookingsystem.database.DbHelper;
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
 * Created by Trine on 04-12-13.
 */
public class ConcreteBookingContentProvider extends ContentProvider {
    private DbHelper _database;
    private ConcreteBookingSource _concreteBookingSource;
    private SimpleDateFormat _iso8601format;

    // used for the UriMacher
    private static final int CONCRETEBOOKINGS = 10;
    private static final int CONCRETEBOOKING_ID = 20;

    private static final String AUTHORITY = "dk.eal.learerbookingsystem.contentprovider";

    private static final String BASE_PATH = "possibleBooking";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/possibleBooking";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/possibleBooking";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, CONCRETEBOOKINGS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CONCRETEBOOKING_ID);
    }

    @Override
    public boolean onCreate() {
        _database = new DbHelper(getContext()); //Opretter forbindelse til databasen
        _concreteBookingSource = new ConcreteBookingSource(getContext());
        _iso8601format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return false;
    }

    @Override
    //Svare til read
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(DbHelper.TABLE_CONCRETEBOOKING); //Fortæller hvilken tabel der skal selectes

        int uriType = sURIMatcher.match(uri); //Opretter en ny URI type på baggrund af URIMatcher
        switch (uriType) {
            case CONCRETEBOOKINGS:
                break;
            case CONCRETEBOOKING_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(DbHelper.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = _database.getWritableDatabase(); //Opretter databasen som en skrivebar database
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = _database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case CONCRETEBOOKINGS:
                id = sqlDB.insert(DbHelper.TABLE_CONCRETEBOOKING, null, contentValues);
                Name name = new Name(
                        contentValues.getAsString(DbHelper.COLUMN_NAME_FIRSTNAME),
                        contentValues.getAsString(DbHelper.COLUMN_NAME_LASTNAME)
                );

                String username = contentValues.getAsString(DbHelper.COLUMN_USER_USERNAME);
                String password = contentValues.getAsString(DbHelper.COLUMN_USER_PASSWORD);

                User user = new User(
                        username,
                        password,
                        name
                );

                Teacher teacher = new Teacher(
                        username,
                        password,
                        name
                );
                teacher.setUserId(user.getId());

                Semester semester = new Semester(
                        contentValues.getAsString(DbHelper.COLUMN_SEMESTER_NAME)
                );

                Subject subject = new Subject(
                        contentValues.getAsString(DbHelper.COLUMN_SUBJECT_NAME),
                        semester,
                        teacher
                );

                Booking booking = null;
                try {
                    booking = new Booking(
                            _iso8601format.parse(contentValues.getAsString(DbHelper.COLUMN_BOOKING_STARTTIME)),
                            _iso8601format.parse(contentValues.getAsString(DbHelper.COLUMN_BOOKING_ENDTIME)),
                            subject
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                PossibleBooking possibleBooking = new PossibleBooking(
                        contentValues.getAsInteger(DbHelper.COLUMN_POSSIBLEBOOKING_DURATION),
                        booking

                );

                HomeRoomClass homeRoomClass = new HomeRoomClass(
                        contentValues.getAsString(DbHelper.COLUMN_HOMEROOM_NAME)
                );

                Student student = new Student(
                        username,
                        password,
                        contentValues.getAsByte(DbHelper.COLUMN_STUDENT_APPROVED),
                        homeRoomClass,
                        name
                );

                _concreteBookingSource.createConcreteBooking(new ConcreteBooking(
                        contentValues.getAsByte(DbHelper.COLUMN_CONCRETEBOOKING_TYPE),
                        contentValues.getAsString(DbHelper.COLUMN_CONCRETEBOOKING_COMMENTS),
                        contentValues.getAsByte(DbHelper.COLUMN_CONCRETBOOKING_STATUSCHANGED),
                        booking,
                        possibleBooking,
                        student
                ));

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = _database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case CONCRETEBOOKINGS:
                rowsDeleted = sqlDB.delete(DbHelper.TABLE_CONCRETEBOOKING, selection,
                        selectionArgs);
                break;
            case CONCRETEBOOKING_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {//Tjekker for om der er en where clause med
                    rowsDeleted = sqlDB.delete(DbHelper.TABLE_CONCRETEBOOKING,
                            DbHelper.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(DbHelper.TABLE_CONCRETEBOOKING,
                            DbHelper.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = _database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case CONCRETEBOOKINGS:
                rowsUpdated = sqlDB.update(DbHelper.TABLE_CONCRETEBOOKING,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CONCRETEBOOKING_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DbHelper.TABLE_CONCRETEBOOKING,
                            values,
                            DbHelper.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(DbHelper.TABLE_CONCRETEBOOKING,
                            values,
                            DbHelper.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
    //Metode der tjekker på, at jeg ikke tjekker efter kolonner der ikke eksistere
    private void checkColumns(String[] projection) {
        String[] available = ConcreteBookingSource.ALL_COLUMNS;
        if (projection != null) {
            List<String> requestedColumns = Arrays.asList(projection);
            List<String> availableColumns = Arrays.asList(available);
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
