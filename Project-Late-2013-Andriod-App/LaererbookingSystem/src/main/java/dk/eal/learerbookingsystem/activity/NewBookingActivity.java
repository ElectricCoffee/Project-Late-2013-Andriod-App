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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.eal.learerbookingsystem.R;
import dk.eal.learerbookingsystem.database.ConcreteBookingSource;
import dk.eal.learerbookingsystem.database.PossibleBookingSource;
import dk.eal.learerbookingsystem.database.StudentSource;
import dk.eal.learerbookingsystem.database.SubjectSource;
import dk.eal.learerbookingsystem.model.Booking;
import dk.eal.learerbookingsystem.model.BookingModel;
import dk.eal.learerbookingsystem.model.ConcreteBooking;
import dk.eal.learerbookingsystem.model.PossibleBooking;
import dk.eal.learerbookingsystem.model.Student;
import dk.eal.learerbookingsystem.model.Subject;
import dk.eal.learerbookingsystem.service.ServerService;
import dk.eal.learerbookingsystem.utils.JsonSerializer;
import dk.eal.learerbookingsystem.view.NewBookingView;

public class NewBookingActivity extends BaseActivity {
    public static final String
        TAG = NewBookingActivity.class.getSimpleName(),
        BOOKING_URL = "booking";

    private NewBookingActivity _instance;
    private NewBookingView _view;
    private List<Subject> _subjects;
    private List<PossibleBooking> _possibleBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _instance = this;
        _view = (NewBookingView) View.inflate(this, R.layout.activity_booking_new, null);
        _view.setViewListener(viewListener);

        SubjectSource subjectSource = new SubjectSource(this);
        _subjects = new ArrayList<Subject>();
        try {
            subjectSource.open();
            _subjects = subjectSource.getAll();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            subjectSource.close();
        }

        Subject[] s = new Subject[_subjects.size()];

        _view.setSubjectAdapter(_subjects.toArray(s));

        PossibleBookingSource possibleBookingSource = new PossibleBookingSource(this);
        _possibleBookings = new ArrayList<PossibleBooking>();
        try {
            possibleBookingSource.open();
            _possibleBookings = possibleBookingSource.getAll();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            possibleBookingSource.close();
        }

        PossibleBooking[] p = new PossibleBooking[_possibleBookings.size()];

        _view.setDateAdapter(_possibleBookings.toArray(p));

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

    NewBookingView.ViewListener viewListener = new NewBookingView.ViewListener() {
        @Override
        public void cancel() {
            NavUtils.navigateUpFromSameTask(_instance);
        }

        @Override
        public void create(String subjectName, Date startDate, Date endDate, String comment) {
            Log.d(TAG, "create booking initiated");

            Subject subject = new Subject();

            for (Subject sub : _subjects) {
                if (sub.getName() == subjectName)
                    subject = sub;
            }

            PossibleBooking possibleBooking = new PossibleBooking();

            for (PossibleBooking pos : _possibleBookings) {
                if (pos.getBooking().getStartDate() == startDate)
                    possibleBooking = pos;
            }

            StudentSource studentSource = new StudentSource(_context);
            Student student = null;
            try {
                studentSource.open();
                student = studentSource.getStudentById(1);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            } finally {
                studentSource.close();
            }

            Booking booking = new Booking(startDate, endDate, subject);
            ConcreteBooking concreteBooking = new ConcreteBooking(
                (byte) 0, comment, (byte) 0, booking, possibleBooking, student
            );

            localCreate(concreteBooking);
            // globalCreate(concreteBooking);
        }

        private void localCreate(ConcreteBooking concreteBooking) {
            ConcreteBookingSource concreteBookingSource = new ConcreteBookingSource(_context);
            try {
                concreteBookingSource.open();
                concreteBookingSource.createConcreteBooking(concreteBooking);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            } finally {
                concreteBookingSource.close();
            }
        }

        private void globalCreate(ConcreteBooking concreteBooking) {
            String json = JsonSerializer.Serialize(concreteBooking);
            Intent intent = makeRequestIntent();
            intent.putExtra(EXTRA_HTTP_ACTION, ServerService.ACTION_HTTP_POST);
            intent.putExtra(EXTRA_JSON_DATA, json);
            startService(intent);
            Log.d(TAG, "server started");
        }
    };
}
