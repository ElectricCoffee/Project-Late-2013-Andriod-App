package dk.eal.learerbookingsystem;

import android.app.Application;

import dk.eal.learerbookingsystem.controller.LoginController;

/**
 * Created by Trine on 25-11-13.
 */
public class BookingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoginController controller = new LoginController(this);
    }
}
