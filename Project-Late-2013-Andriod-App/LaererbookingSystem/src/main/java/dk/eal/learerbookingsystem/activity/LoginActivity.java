package dk.eal.learerbookingsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import dk.eal.learerbookingsystem.R;
import dk.eal.learerbookingsystem.service.ServerService;
import dk.eal.learerbookingsystem.view.LoginView;

public class LoginActivity extends BaseActivity {
    public static final String
        TAG = LoginActivity.class.getSimpleName(),
        EXTRA_URL_PARAMS_USERNAME = EXTRA_URL_PARAMS + "_username",
        EXTRA_URL_PARAMS_PASSWORD = EXTRA_URL_PARAMS + "_password",
        LOGIN_URL = "login";

    private LoginView _view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _view = (LoginView) View.inflate(this, R.layout.activity_login, null);
        _view.setViewListener(viewListener);
        setContentView(_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    protected void handleMessage(Intent mgs) {
        super.handleMessage(mgs);
    }

    private LoginView.ViewListener viewListener = new LoginView.ViewListener() {
        @Override
        public void login(String username, String password) {
            Log.d(TAG, "login initiated");
            Intent intent = makeRequestIntent();
            intent.putExtra(EXTRA_HTTP_ACTION, ServerService.ACTION_HTTP_GET);
            intent.putExtra(EXTRA_URL_ITEM, LOGIN_URL);
            intent.putExtra(EXTRA_URL_PARAMS_USERNAME, username);
            intent.putExtra(EXTRA_URL_PARAMS_PASSWORD, password);
            startService(intent);
            Log.d(TAG, "server started");
        }

        @Override
        public void beginRegister() {
            Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registerIntent);
        }
    };
}
