package dk.eal.learerbookingsystem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.eal.learerbookingsystem.R;
import dk.eal.learerbookingsystem.model.Booking;
import dk.eal.learerbookingsystem.model.PossibleBooking;
import dk.eal.learerbookingsystem.model.Subject;

/**
 * Created by Trine on 27-11-13.
 */
public class NewBookingView extends RelativeLayout implements View.OnClickListener {

    protected NewBookingView.ViewListener _viewListener;
    private EditText comment;
    private Button cancel, create;
    private Spinner subject, date;
    private SimpleDateFormat _iso8601format;

    public void setViewListener(ViewListener listener) {
        _viewListener = listener;
    }

    public interface ViewListener {
        void cancel();
        void create(String subject, Date startDate, Date endDate, String comment);
    }

    public NewBookingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        _iso8601format = new SimpleDateFormat("yyyy-MM-dd");
    }

    protected void onFinishInflate() {
        super.onFinishInflate();

        comment = (EditText) findViewById(R.id.editTextComments);

        cancel = (Button) findViewById(R.id.buttonCancel);
        create = (Button) findViewById(R.id.buttonCreate);

        subject = (Spinner) findViewById(R.id.spinner_subject);
        date = (Spinner) findViewById(R.id.spinner_chose_date);

        cancel.setOnClickListener(this);
        create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCancel:
                _viewListener.cancel();
                break;
            case R.id.buttonCreate:
                _viewListener.create(
                    (String) subject.getSelectedItem(),
                    (Date) date.getSelectedItem(),
                    new Date(
                        ((Date) date.getSelectedItem())
                            .getTime() + 15 * 60000),
                    comment.getText().toString());
                break;
            default:
                break;
        }
    }

    public void setSubjectAdapter(Subject[] subjects) {
        List<String> subjectNames = new ArrayList<String>();

        for (Subject s : subjects)
            subjectNames.add(s.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, subjectNames);
        subject.setAdapter(adapter);
    }

    public void setDateAdapter(PossibleBooking[] possibleBookings) {
        List<Date> dates = new ArrayList<Date>();

        for (PossibleBooking pb : possibleBookings)
            dates.add(pb.getBooking().getStartDate());

        ArrayAdapter<Date> adapter = new ArrayAdapter<Date>(
            getContext(), android.R.layout.simple_list_item_1, dates);

        date.setAdapter(adapter);
    }
}
