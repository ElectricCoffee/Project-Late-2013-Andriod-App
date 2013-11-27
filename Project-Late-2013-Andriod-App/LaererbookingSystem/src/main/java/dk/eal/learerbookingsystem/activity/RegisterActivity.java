package dk.eal.learerbookingsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import dk.eal.learerbookingsystem.R;
import dk.eal.learerbookingsystem.service.ServerService;
import dk.eal.learerbookingsystem.utils.JsonSerializer;
import dk.eal.learerbookingsystem.utils.RegisterRequest;
import dk.eal.learerbookingsystem.view.LoginView;
import dk.eal.learerbookingsystem.view.RegisterView;

public class RegisterActivity extends BaseActivity {
    public static final String
        TAG = RegisterActivity.class.getSimpleName(),
        REGISTER_URL = "register";

    private RegisterActivity _instance;
    private RegisterView _view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        _instance = this;
        // Show the Up button in the action bar.
        setupActionBar();
        _view = (RegisterView) View.inflate(this, R.layout.activity_register, null);
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
        getMenuInflater().inflate(R.menu.register, menu);
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

    private RegisterView.ViewListener viewListener = new RegisterView.ViewListener() {
        @Override
        public void cancel() {
            NavUtils.navigateUpFromSameTask(_instance);
        }

        @Override
        public void register(String firstname, String lastname, String email, String password, String repeatPassword) {
            if (!email.contains("@edu.eal.dk")) {
                Toast.makeText(
                    _instance, "Du har ikke tilladelse til at oprette dig som bruger",
                    Toast.LENGTH_LONG).show();
                return;
            }

            if (!repeatPassword.equals(password)) {
                Toast.makeText(
                    _instance, "De indtastede adgangskoder matcher ikke",
                    Toast.LENGTH_LONG).show();
                return;
            }

            Log.d(TAG, "register initiated");
            RegisterRequest request = new RegisterRequest(firstname, lastname, email, password);
            String json = JsonSerializer.Serialize(request);
            Intent intent = makeRequestIntent();
            intent.putExtra(EXTRA_HTTP_ACTION, ServerService.ACTION_HTTP_POST);
            intent.putExtra(EXTRA_URL_ITEM, REGISTER_URL);
            intent.putExtra(EXTRA_JSON_DATA, json);
            startService(intent);
            Log.d(TAG, "server started");
        }
    };
}
