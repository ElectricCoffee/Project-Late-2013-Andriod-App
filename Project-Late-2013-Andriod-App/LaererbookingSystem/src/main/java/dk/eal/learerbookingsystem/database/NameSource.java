package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import dk.eal.learerbookingsystem.model.Name;

/**
 * Created by Trine on 29-11-13.
 */
public class NameSource {
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    private String[] _allColumns = {
        DbHelper.COLUMN_ID,
        DbHelper.COLUMN_NAME_FIRSTNAME,
        DbHelper.COLUMN_NAME_LASTNAME};

    public NameSource(Context context) {
        _dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        _database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public Name createName(Name name) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord
        values.put(DbHelper.COLUMN_NAME_FIRSTNAME, name.getFirstname());
        values.put(DbHelper.COLUMN_NAME_LASTNAME, name.getLastname());
        long insertId = _database.insert(DbHelper.TABLE_NAME, null,
            values);//Definere en key til at indentificere kolonnens id

        return getNameById(insertId); /*Genlæser id*/
    }

    public boolean nameExists(Name name) {
        Cursor cursor = _database.query(DbHelper.TABLE_NAME, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + name.getId(), null,
            null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public Name getNameById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_NAME, //Opretter en cursor
            _allColumns, DbHelper.COLUMN_ID + " = " + id, null,
            null, null, null);
        cursor.moveToFirst();
        Name name = cursorToName(cursor);
        cursor.close();
        return name;
    }

    private Name cursorToName(Cursor cursor) {
        Name name = new Name(cursor.getString(1),cursor.getString(2));
        name.setId(cursor.getLong(0));
        return name;
    }
}
