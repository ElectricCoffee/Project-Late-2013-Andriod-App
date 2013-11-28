package dk.eal.learerbookingsystem.activity;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;

import dk.eal.learerbookingsystem.R;
import dk.eal.learerbookingsystem.database.DbHelper;

public class BookingActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.booking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_create:
                Intent intent = new Intent(this, NewBookingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {DbHelper.COLUMN_SUBJECT_NAME, DbHelper.COLUMN_BOOKING_STARTTIME, DbHelper.COLUMN_BOOKING_ENDTIME};
        CursorLoader cursorLoader = new CursorLoader(this,
            MyContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> cursorLoader, Cursor cursor) {
        _adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> cursorLoader) {
        _adapter.swapCursor(null);
    }
}
