package dk.eal.learerbookingsystem.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import dk.eal.learerbookingsystem.R;

public class BookingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mine_bookinger, menu);
        return true;
    }
    
}
