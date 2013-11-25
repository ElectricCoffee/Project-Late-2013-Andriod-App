package dk.eal.learerbookingsystem.view;

import android.app.Activity;
import android.os.Bundle;

import dk.eal.learerbookingsystem.R;
import dk.eal.learerbookingsystem.controller.BaseController;

/**
 * Created by Trine on 25-11-13.
 */
public abstract class BaseView <TController extends BaseController> extends Activity {
    protected TController _controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void setController(TController controller) {
        _controller = controller;
    }
}
