package dk.eal.learerbookingsystem.controller;

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

import dk.eal.learerbookingsystem.Sevice.ServerService;
import dk.eal.learerbookingsystem.Utils.IServerInteraction;
import dk.eal.learerbookingsystem.Utils.JsonSerializer;
import dk.eal.learerbookingsystem.model.BaseModel;
import dk.eal.learerbookingsystem.view.BaseView;
import dk.eal.learerbookingsystem.view.LoginView;

/**
 * Created by Trine on 25-11-13.
 */
public abstract class BaseController<TView extends BaseView, TModel extends BaseModel> {
    public static final String
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

    public BaseController() { }

    public BaseController(Context context, Class<TView> cls) {
        _context = context;

        Intent intent = new Intent(_context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
        //_view.setController(this);
    }

    public BaseController(Context context, IServerInteraction callback, Class<TView> cls) {
        this(context, cls);
        _callback = callback;
    }

    protected BroadcastReceiver _httpReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleMessage(intent);
        }
    };

    protected void startReceiver() {
        LocalBroadcastManager.getInstance(_view)
            .registerReceiver(_httpReceiver, new IntentFilter(ServerService.ACTION_REQUEST_PROCESSED));
    }

    protected void handleMessage(Intent mgs) {
        _items = new ArrayList<TModel>();
        Bundle data = mgs.getExtras();

        String target = data.getString(EXTRA_TARGET_CLASS);
        if(!target.equals(_view.getLocalClassName()))
            _callback.serverResult(_items);

        if(data.containsKey(ServerService.EXTRA_ERROR)) {
            String error = data.getString(ServerService.EXTRA_ERROR);
            Toast.makeText(_view,error,Toast.LENGTH_LONG).show();
            _callback.serverResult(_items);
        }

        String action = data.getString(EXTRA_HTTP_ACTION);
        if(action.equals(ServerService.ACTION_HTTP_GET)) {
            String json = data.getString(EXTRA_JSON_DATA);
            Log.d(BaseController.class.getName(), json);
                    Class < TModel[]> cls =
                (Class<TModel[]>) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[1];
            _items = JsonSerializer.DeSerializeList(json, cls);
        }
    }

    protected void stopReceiver() {
        LocalBroadcastManager.getInstance(_view).unregisterReceiver(_httpReceiver);
    }

    protected Intent makeIntent() {
        Intent intent = new Intent(_view, ServerService.class);
        intent.putExtra(EXTRA_TARGET_CLASS, _view.getLocalClassName());
        return intent;
    }
}
