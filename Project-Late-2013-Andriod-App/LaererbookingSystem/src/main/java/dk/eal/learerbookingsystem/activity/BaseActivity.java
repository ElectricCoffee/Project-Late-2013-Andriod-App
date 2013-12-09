package dk.eal.learerbookingsystem.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import dk.eal.learerbookingsystem.service.ServerService;
import dk.eal.learerbookingsystem.utils.IServerInteraction;
import dk.eal.learerbookingsystem.utils.JsonSerializer;
import dk.eal.learerbookingsystem.model.BaseModel;
import dk.eal.learerbookingsystem.view.IView;

/**
 * Created by Trine on 26-11-13.
 */
public class BaseActivity <TView extends IView, TModel extends BaseModel> extends Activity {
    public static final String
        TAG = BaseActivity.class.getSimpleName(),
        EXTRA_TARGET_CLASS = "extra_target",
        EXTRA_URL_ITEM = "extra_url_item",
        EXTRA_URL_ID = "extra_url_id",
        EXTRA_URL_PARAMS = "extra_url_params",
        EXTRA_JSON_DATA = "extra_data",
        EXTRA_HTTP_ACTION = "extra_http_action";

    protected Context _context;
    protected TView _view;
    protected List<TModel> _items;
    protected IServerInteraction<TModel> _callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = this;
        startReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopReceiver();
    }

    protected BroadcastReceiver _httpReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleMessage(intent);
        }
    };

    protected void startReceiver() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(_httpReceiver, new IntentFilter(ServerService.ACTION_REQUEST_PROCESSED));
        Log.d(TAG, "receiver started");
    }

    protected void handleMessage(Intent mgs) {
        Log.d(TAG, "message received");

        _items = new ArrayList<TModel>();
        Bundle data = mgs.getExtras();

        String target = data.getString(EXTRA_TARGET_CLASS);
        if(!target.equals(this.getLocalClassName()))
            _callback.serverResult(_items);

        if(data.containsKey(ServerService.EXTRA_ERROR)) {
            String error = data.getString(ServerService.EXTRA_ERROR);
            Log.e(TAG, "message error: " + error);
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            _callback.serverResult(_items);
        }

        String action = data.getString(EXTRA_HTTP_ACTION);
        if(action.equals(ServerService.ACTION_HTTP_GET)) {
            String json = data.getString(EXTRA_JSON_DATA);
            Log.d(TAG, "message data: " + json);
            Class <TModel[]> cls =
                (Class<TModel[]>) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[1];
            _items = JsonSerializer.DeSerializeList(json, cls);
        }
    }

    protected void stopReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(_httpReceiver);
    }

    protected Intent makeRequestIntent() {
        Intent intent = new Intent(this, ServerService.class);
        intent.putExtra(EXTRA_TARGET_CLASS, this.getLocalClassName());
        return intent;
    }
}
