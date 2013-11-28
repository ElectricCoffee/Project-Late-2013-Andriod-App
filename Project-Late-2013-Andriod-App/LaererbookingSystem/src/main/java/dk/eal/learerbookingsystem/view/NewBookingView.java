package dk.eal.learerbookingsystem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.Date;

import dk.eal.learerbookingsystem.R;

/**
 * Created by Trine on 27-11-13.
 */
public class NewBookingView extends RelativeLayout implements View.OnClickListener {

    protected NewBookingView.ViewListener _viewListener;
    private EditText comment;
    private Button cancel, create;
    private Spinner subject, date, startTime, endTime;

    public void setViewListener(ViewListener listener) {
        _viewListener = listener;
    }

    public interface ViewListener {
        void cancel();
        void create(String subject, Date startDate, Date endDate, String comment);
    }

    public NewBookingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
    protected void onFinishInflate() {
        super.onFinishInflate();

        comment = (EditText) findViewById(R.id.editTextComments);

        cancel = (Button) findViewById(R.id.buttonCancel);
        create = (Button) findViewById(R.id.buttonCreate);

        subject = (Spinner) findViewById(R.id.spinner_subject);
        date = (Spinner) findViewById(R.id.spinner_chose_date);
        startTime = (Spinner) findViewById(R.id.spinner_chose_starttime);
        endTime = (Spinner) findViewById(R.id.spinner_chose_endtime);

        cancel.setOnClickListener(this);
        create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCancel:
                _viewListener.cancel();
                break;
            case R.id.buttonRegister:
                _viewListener.create(
                    (String) subject.getSelectedItem(),
                    new Date(), // get startDate from popup
                    new Date(), // get endDate from popup
                    comment.getText().toString());
                break;
            default:
                break;
        }
    }
}
