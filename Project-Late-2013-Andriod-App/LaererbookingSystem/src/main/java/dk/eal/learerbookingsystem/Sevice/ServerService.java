package dk.eal.learerbookingsystem.Sevice;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dk.eal.learerbookingsystem.communication.RestCommunication;
import dk.eal.learerbookingsystem.controller.BaseController;

/**
 * Created by Trine on 25-11-13.
 */
public class ServerService extends IntentService {
    URL url;
    public static final String
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
        String target = data.getString(BaseController.EXTRA_TARGET_CLASS);

        String action = data.getString(BaseController.EXTRA_HTTP_ACTION);
        Intent response = new Intent("");
        response.putExtra((BaseController.EXTRA_TARGET_CLASS),(data.getString(BaseController.EXTRA_TARGET_CLASS)));
        response.putExtra((BaseController.EXTRA_HTTP_ACTION),action);

        if (data.containsKey(BaseController.EXTRA_URL_ITEM)) {
            String itemUrl = data.getString(BaseController.EXTRA_URL_ITEM);
            response.putExtra(
                BaseController.EXTRA_URL_ITEM, itemUrl);
            urlString = urlString + "/" + itemUrl;
        }

        if(action.equals(ACTION_HTTP_POST)){
            String json = data.getString(BaseController.EXTRA_JSON_DATA);
            rc.postData(url, json);
        }

        if(action.equals(ACTION_HTTP_PUT)) {
            boolean error = false;
            if(data.containsKey(BaseController.EXTRA_URL_ID)){
                String id = data.getString(BaseController.EXTRA_URL_ID);
                response.putExtra(BaseController.EXTRA_URL_ID, id);

                urlString = urlString + "/" + id;
                error = !setUrl(urlString);
            }
            if(error) {
                response.putExtra(EXTRA_ERROR,"Invalid server error");
            }
            else {
                String json = data.getString(BaseController.EXTRA_JSON_DATA);
                rc.postData(url, json);
            }
        }

        if (action.equals(ACTION_HTTP_GET)) {
            boolean error = false;

            if(data.containsKey(BaseController.EXTRA_URL_ID)){
                String id = data.getString(BaseController.EXTRA_URL_ID);
                response.putExtra(BaseController.EXTRA_URL_ID,id);

                urlString = urlString + "/" + id;
                error = !setUrl(urlString);
            }

            List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
            for (String key : data.keySet()) {
                if (key.contains(BaseController.EXTRA_URL_PARAMS))
                    urlParams.add(new BasicNameValuePair(key, data.getString(key)));
            }

            if (urlParams != null && !urlParams.isEmpty()) {
                String paramString = URLEncodedUtils.format(urlParams, "utf-8");
                urlString += "?" + paramString;

                error = !setUrl(urlString);
            }

            if (error){
                response.putExtra(EXTRA_ERROR,"Invalid server error");
            }
            else{
                String json = rc.getData(url);
                response.putExtra(BaseController.EXTRA_URL_ID, json);
            }
        }

        if(action.equals(ACTION_HTTP_DELETE)){
            boolean error = false;
            String id = data.getString(BaseController.EXTRA_URL_ID);
            response.putExtra(BaseController.EXTRA_URL_ID, id);

            urlString = urlString + "/" + id;
            error = !setUrl(urlString);

            if(error){
                response.putExtra(EXTRA_ERROR,"Invalid server error");
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