package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import dk.eal.learerbookingsystem.model.Name;
import dk.eal.learerbookingsystem.model.User;

/**
 * Created by Trine on 29-11-13.
 */
public class UserSource {
    private Context _context;
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    private String[] _allColumns = {
        DbHelper.COLUMN_ID,
        DbHelper.COLUMN_USER_USERNAME,
        DbHelper.COLUMN_USER_PASSWORD,
        DbHelper.COLUMN_FK_NAME_ID
    };

    public UserSource(Context context) {
        _context = context;
        _dbHelper = new DbHelper(_context);
    }

    public void open() throws SQLException {
        _database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public User createUser(User user) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord

        NameSource nameSource = new NameSource(_context);

        if (!nameSource.nameExists(user.getName()))
            user.setName(nameSource.createName(user.getName()));

        values.put(DbHelper.COLUMN_USER_USERNAME, user.getUsername());
        values.put(DbHelper.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(DbHelper.COLUMN_FK_NAME_ID, user.getName().getId());

        long insertId = _database.insert(DbHelper.TABLE_USER, null,
                values);//Definere en key til at indentificere kolonnens id

        return getUserById(insertId); //Retunere den nye kommentar
    }

    public boolean userExists(User user) {
        Cursor cursor = _database.query(DbHelper.TABLE_USER, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + user.getId(), null,
            null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public User getUserById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_USER, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + id, null,
            null, null, null);
        cursor.moveToFirst();
        User user = cursorToUser(cursor);
        cursor.close();
        return user;
    }

    public User cursorToUser(Cursor cursor) {
        NameSource nameSource = new NameSource(_context);
        Name name = null;
        try {
            nameSource.open();
            name = nameSource.getNameById(cursor.getLong(3));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            nameSource.close();
        }

        User user = new User(cursor.getString(1), cursor.getString(2), name);
        user.setId(cursor.getLong(0));
        return user;
    }
}
