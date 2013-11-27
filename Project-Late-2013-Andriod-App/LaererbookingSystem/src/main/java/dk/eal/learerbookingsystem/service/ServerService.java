package dk.eal.learerbookingsystem.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dk.eal.learerbookingsystem.activity.BaseActivity;
import dk.eal.learerbookingsystem.communication.RestCommunication;

/**
 * Created by Trine on 25-11-13.
 */
public class ServerService extends IntentService {
    URL url;
    public static final String
        TAG = ServerService.class.getSimpleName(),
        ACTION_REQUEST_PROCESSED = "action_request_processed",
        ACTION_HTTP_GET = "action_http_get",
        ACTION_HTTP_PUT ="action_http_put",
        ACTION_HTTP_POST = "action_http_post",
        ACTION_HTTP_DELETE = "action_http_delete",
        EXTRA_ERROR = "extra_error";


    public ServerService(String name){
        super(name);
    }

    public ServerService(){
        super("ServerService");
    }

    @Override
    protected  void onHandleIntent(Intent intent){
        Bundle data = intent.getExtras();
        if(data == null)
            return;

        RestCommunication rc = new RestCommunication();
        String urlString = RestCommunication.BASE_URL;
        String target = data.getString(BaseActivity.EXTRA_TARGET_CLASS);

        String action = data.getString(BaseActivity.EXTRA_HTTP_ACTION);
        Intent response = new Intent(ACTION_REQUEST_PROCESSED);
        response.putExtra((BaseActivity.EXTRA_TARGET_CLASS),(data.getString(BaseActivity.EXTRA_TARGET_CLASS)));
        response.putExtra((BaseActivity.EXTRA_HTTP_ACTION),action);

        if (data.containsKey(BaseActivity.EXTRA_URL_ITEM)) {
            String itemUrl = data.getString(BaseActivity.EXTRA_URL_ITEM);
            response.putExtra(
                BaseActivity.EXTRA_URL_ITEM, itemUrl);
            urlString = urlString + "/" + itemUrl;
        }

        if(action.equals(ACTION_HTTP_POST)){
            Log.d(TAG, "url: " + urlString);
            String json = data.getString(BaseActivity.EXTRA_JSON_DATA);
            rc.postData(url, json);
        }

        if(action.equals(ACTION_HTTP_PUT)) {
            boolean error = false;
            if(data.containsKey(BaseActivity.EXTRA_URL_ID)){
                String id = data.getString(BaseActivity.EXTRA_URL_ID);
                response.putExtra(BaseActivity.EXTRA_URL_ID, id);

                urlString = urlString + "/" + id;
                error = !setUrl(urlString);
            }

            Log.d(TAG, "url: " + urlString);

            if(error) {
                response.putExtra(EXTRA_ERROR,"Invalid url");
            }
            else {
                String json = data.getString(BaseActivity.EXTRA_JSON_DATA);
                rc.postData(url, json);
            }
        }

        if (action.equals(ACTION_HTTP_GET)) {
            boolean error = false;

            if(data.containsKey(BaseActivity.EXTRA_URL_ID)){
                String id = data.getString(BaseActivity.EXTRA_URL_ID);
                response.putExtra(BaseActivity.EXTRA_URL_ID,id);

                urlString = urlString + "/" + id;
                error = !setUrl(urlString);
            }

            List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
            for (String key : data.keySet()) {
                if (key.contains(BaseActivity.EXTRA_URL_PARAMS)) {
                    String urlKey = key.replace(BaseActivity.EXTRA_URL_PARAMS  + "_", "");
                    urlParams.add(new BasicNameValuePair(urlKey, data.getString(key)));
                }
            }

            if (urlParams != null && !urlParams.isEmpty()) {
                String paramString = URLEncodedUtils.format(urlParams, "utf-8");
                urlString += "?" + paramString;

                error = !setUrl(urlString);
            }

            Log.d(TAG, "url: " + urlString);

            if (error)
                response.putExtra(EXTRA_ERROR,"Invalid url");
            else {
                String json = rc.getData(url);

                if (json != null && !TextUtils.isEmpty(json))
                    response.putExtra(BaseActivity.EXTRA_JSON_DATA, json);
                else
                    response.putExtra(EXTRA_ERROR,"No data received");
            }
        }

        if(action.equals(ACTION_HTTP_DELETE)){
            boolean error = false;
            String id = data.getString(BaseActivity.EXTRA_URL_ID);
            response.putExtra(BaseActivity.EXTRA_URL_ID, id);

            urlString = urlString + "/" + id;
            error = !setUrl(urlString);

            Log.d(TAG, "url: " + urlString);

            if(error){
                response.putExtra(EXTRA_ERROR,"Invalid url");
            }
            else{
                rc.deleteData(url);
            }
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(response);
    }

    private boolean setUrl(String serverUrl){
        boolean result = false;
        try {
            url = new URL(serverUrl);
            result = true;
        }
        catch (MalformedURLException mue) {
            mue.printStackTrace();
        }

        return result;
    }
}