package dk.eal.learerbookingsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import dk.eal.learerbookingsystem.model.HomeRoomClass;

/**
 * Created by Trine on 29-11-13.
 */
public class HomeRoomSource {
    private SQLiteDatabase _database; //ref til db
    private DbHelper _dbHelper; //ref til db helper
    private String[] _allColumns = {
            DbHelper.COLUMN_ID,
            DbHelper.COLUMN_HOMEROOM_NAME
    };

    public HomeRoomSource(Context context) {
        _dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        _database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public HomeRoomClass createHomeRoom(HomeRoomClass homeRoom) { // i stedet for string comment, kunne man evt brunge en Comment comment, for at mappe det der ligger der i
        ContentValues values = new ContentValues(); //Definere values til at repræsentereindholdet for tabellens rekord
        values.put(DbHelper.COLUMN_HOMEROOM_NAME, homeRoom.getName());
        long insertId = _database.insert(DbHelper.TABLE_HOMEROOM, null,
                values);//Definere en key til at indentificere kolonnens id

        return getHomeRoomById(insertId); /*Genlæser id*/
    }

    public boolean homeRoomExists(HomeRoomClass homeRoom) {
        Cursor cursor = _database.query(DbHelper.TABLE_HOMEROOM, //Opretter en cursor
                _allColumns, DbHelper.COLUMN_ID + " = " + homeRoom.getId(), null,
                null, null, null);
        boolean result = cursor.moveToFirst(); //Går til den første i rækken
        cursor.close();
        return result;
    }

    public HomeRoomClass getHomeRoomById(long id) {
        Cursor cursor = _database.query(DbHelper.TABLE_HOMEROOM, //Opretter en cursor
                _allColumns, DbHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        HomeRoomClass homeRoom = cursorToHomeRoom(cursor);
        cursor.close();
        return homeRoom;
    }

    private HomeRoomClass cursorToHomeRoom(Cursor cursor) {
        HomeRoomClass homeRoom = new HomeRoomClass(cursor.getString(1));
        homeRoom.setId(cursor.getLong(0));
        return homeRoom;
    }
}
