package dk.eal.learerbookingsystem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import dk.eal.learerbookingsystem.R;

public class LoginView extends RelativeLayout implements IView<LoginView.ViewListener>, View.OnClickListener {
    private EditText username, password;
    private Button register, login;
    protected LoginView.ViewListener _viewListener;

    public void setViewListener(ViewListener listener) {
        _viewListener = listener;
    }

    public interface ViewListener {
        void login(String username, String password);
        void beginRegister();
    }

    public LoginView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        username = (EditText)findViewById(R.id.editTextUsername);
        password = (EditText)findViewById(R.id.editTextPassword);

        register = (Button)findViewById(R.id.buttonRegister);
        login = (Button)findViewById(R.id.buttonLogin);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                _viewListener.login(
                    username.getText().toString(),
                    password.getText().toString());
                break;
            case R.id.buttonRegister:
                _viewListener.beginRegister();
                break;
            default:
                break;
        }
    }
}
