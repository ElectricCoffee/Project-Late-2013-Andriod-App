package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import dk.eal.learerbookingsystem.model.Administrator;
import dk.eal.learerbookingsystem.model.User;

/**
 * Created by Trine on 29-11-13.
 */
public class AdministratorSource {
    private Context _context;
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    private String[] _allColumns = {
            DbHelper.COLUMN_ID,
            DbHelper.COLUMN_FK_USER_ID
    };

    public AdministratorSource(Context context) {
        _context = context;
        _dbHelper = new DbHelper(_context);
    }

    public void open() throws SQLException {
        _database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public Administrator createAdministrator(Administrator administrator) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord

        UserSource userSource = new UserSource(_context);

        User user = new User(administrator.getUsername(), administrator.getPassword(), administrator.getName());
        user.setId(administrator.getUserId());
        if (!userSource.userExists(user))
            user = userSource.createUser(user);

        values.put(DbHelper.COLUMN_FK_USER_ID, user.getId());

        long insertId = _database.insert(DbHelper.TABLE_USER, null,
            values);//Definere en key til at indentificere kolonnens id

        return getAdministratorById(insertId); //Retunere den nye kommentar
    }

    public boolean administratorExists(Administrator administrator) {
        Cursor cursor = _database.query(DbHelper.TABLE_ADMINISTRATOR, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + administrator.getId(), null,
            null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public Administrator getAdministratorById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_ADMINISTRATOR, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + id, null,
            null, null, null);
        cursor.moveToFirst();
        Administrator administrator = cursorToAdministrator(cursor);
        cursor.close();
        return administrator;
    }

    public Administrator cursorToAdministrator(Cursor cursor) {
        UserSource userSource = new UserSource(_context);
        User user = null;
        try {
            userSource.open();
            user = userSource.getUserById(cursor.getLong(3));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            userSource.close();
        }

        Administrator administrator = new Administrator(user.getUsername(), user.getPassword(), user.getName());
        administrator.setUserId(user.getId());
        return administrator;
    }
}
