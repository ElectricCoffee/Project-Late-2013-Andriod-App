package dk.eal.learerbookingsystem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import dk.eal.learerbookingsystem.R;

/**
 * Created by Trine on 27-11-13.
 */
public class RegisterView extends RelativeLayout implements View.OnClickListener {

    private EditText firstname, lastname, email, password, repeatPassword;
    private Button cancel, register;
    protected RegisterView.ViewListener _viewListener;

    public void setViewListener(ViewListener listener) {
        _viewListener = listener;
    }

    public interface ViewListener {
        void cancel();
        void register(String firstname, String lastname, String email, String password, String repeatPassword);
    }

    public RegisterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        firstname = (EditText) findViewById(R.id.editTextFirstname);
        lastname = (EditText) findViewById(R.id.editTextLastname);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        repeatPassword = (EditText) findViewById(R.id.editTextRepeatPassword);

        cancel = (Button) findViewById(R.id.buttonCancel);
        register = (Button) findViewById(R.id.buttonRegister);

        cancel.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCancel:
                _viewListener.cancel();
                break;
            case R.id.buttonRegister:
                _viewListener.register(
                    firstname.getText().toString(),
                    lastname.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    repeatPassword.getText().toString());
                break;
            default:
                break;
        }
    }
}
