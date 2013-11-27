package dk.eal.learerbookingsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

import java.util.Date;

import dk.eal.learerbookingsystem.R;
import dk.eal.learerbookingsystem.model.BookingModel;
import dk.eal.learerbookingsystem.service.ServerService;
import dk.eal.learerbookingsystem.utils.JsonSerializer;
import dk.eal.learerbookingsystem.utils.RegisterRequest;
import dk.eal.learerbookingsystem.view.BookingView;
import dk.eal.learerbookingsystem.view.RegisterView;

public class NewBookingActivity extends BaseActivity {
    public static final String
        TAG = NewBookingActivity.class.getSimpleName(),
        BOOKING_URL = "booking";

    private NewBookingActivity _instance;
    private BookingView _view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _instance = this;
        _view = (BookingView) View.inflate(this, R.layout.activity_booking_new, null);
        _view.setViewListener(viewListener);
        setContentView(_view);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_booking, menu);
        return true;
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    BookingView.ViewListener viewListener = new BookingView.ViewListener() {
        @Override
        public void cancel() {
            NavUtils.navigateUpFromSameTask(_instance);
        }

        @Override
        public void create(String subject, Date startDate, Date endDate, String comment) {
            Log.d(TAG, "register initiated");
            BookingModel request = new BookingModel(subject, startDate, endDate, comment);
            String json = JsonSerializer.Serialize(request);
            Intent intent = makeRequestIntent();
            intent.putExtra(EXTRA_HTTP_ACTION, ServerService.ACTION_HTTP_POST);
            intent.putExtra(EXTRA_JSON_DATA, json);
            startService(intent);
            Log.d(TAG, "server started");
        }
    };
}
