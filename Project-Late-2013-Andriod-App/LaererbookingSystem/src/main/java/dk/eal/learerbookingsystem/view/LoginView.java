package dk.eal.learerbookingsystem.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dk.eal.learerbookingsystem.R;
import dk.eal.learerbookingsystem.controller.LoginController;

public class LoginView extends BaseView<LoginController> implements View.OnClickListener {
    private EditText username, password;
    private Button register, login;

    public LoginView() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.editTextUsername);
        password = (EditText)findViewById(R.id.editTextPassword);

        register = (Button)findViewById(R.id.buttonRegister);
        login = (Button)findViewById(R.id.buttonLogin);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds _items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    public String getUsername() {
        return username.getText().toString();
    }

    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                _controller.login();
                break;
            /*case R.id.buttonRegister:
                _controller.beginRegister();
                break;*/
            default:
                break;
        }
    }
}
