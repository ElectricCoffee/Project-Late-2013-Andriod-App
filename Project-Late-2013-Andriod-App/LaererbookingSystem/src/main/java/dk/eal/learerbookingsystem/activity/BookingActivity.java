package dk.eal.learerbookingsystem.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

import dk.eal.learerbookingsystem.R;
import dk.eal.learerbookingsystem.contentprovider.ConcreteBookingContentProvider;
import dk.eal.learerbookingsystem.database.DbHelper;

public class BookingActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView _listView;

    private CursorAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        _listView = (ListView) findViewById(R.id.bookingListView);


        fillData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.booking, menu);
        return true;
    }

    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] {
            DbHelper.COLUMN_SUBJECT_NAME,
            DbHelper.COLUMN_BOOKING_STARTTIME,
            DbHelper.COLUMN_CONCRETEBOOKING_COMMENTS };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.subject, R.id.date, R.id.time };

        getSupportLoaderManager().initLoader(0, null, this);
        _adapter = new SimpleCursorAdapter(this, R.layout.row_booking, null, from, to, 0);

        _listView.setAdapter(_adapter);
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
            DbHelper.TABLE_CONCRETEBOOKING + "." + DbHelper.COLUMN_ID,
            DbHelper.COLUMN_CONCRETEBOOKING_TYPE,
            DbHelper.COLUMN_CONCRETEBOOKING_COMMENTS,
            DbHelper.COLUMN_CONCRETEBOOKING_STATUSCHANGED };

        CursorLoader cursorLoader = new CursorLoader(this,
            ConcreteBookingContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        _adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        _adapter.swapCursor(null);
    }
}
