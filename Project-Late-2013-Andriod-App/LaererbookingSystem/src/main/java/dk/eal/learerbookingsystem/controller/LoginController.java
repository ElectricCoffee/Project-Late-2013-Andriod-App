package dk.eal.learerbookingsystem.controller;

import android.content.Context;
import android.content.Intent;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import dk.eal.learerbookingsystem.Sevice.ServerService;
import dk.eal.learerbookingsystem.Utils.IServerInteraction;
import dk.eal.learerbookingsystem.communication.RestCommunication;
import dk.eal.learerbookingsystem.model.BaseModel;
import dk.eal.learerbookingsystem.view.LoginView;

/**
 * Created by Trine on 25-11-13.
 */
public class LoginController extends BaseController<LoginView, BaseModel> {
    public static final String
        EXTRA_URL_PARAMS_USERNAME = EXTRA_URL_PARAMS + "_username",
        EXTRA_URL_PARAMS_PASSWORD = EXTRA_URL_PARAMS + "_password",
        LOGIN_URL = RestCommunication.BASE_URL + "/login";

    public LoginController(Context context) {
        super(context, LoginView.class);
    }

    public LoginController(Context context, IServerInteraction callback) {
        super(context, callback, LoginView.class);
    }

    public void login() {
        Intent intent = makeIntent();
        intent.putExtra(EXTRA_HTTP_ACTION,ServerService.ACTION_HTTP_GET);
        List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
        intent.putExtra(EXTRA_URL_ITEM, LOGIN_URL);
        intent.putExtra(EXTRA_URL_PARAMS_USERNAME, _view.getUsername());
        intent.putExtra(EXTRA_URL_PARAMS_PASSWORD, _view.getPassword());
        _context.startActivity(intent);
    }

    public void beginRegister() {

    }

    @Override
    protected void handleMessage(Intent mgs) {
        super.handleMessage(mgs);
    }
}
