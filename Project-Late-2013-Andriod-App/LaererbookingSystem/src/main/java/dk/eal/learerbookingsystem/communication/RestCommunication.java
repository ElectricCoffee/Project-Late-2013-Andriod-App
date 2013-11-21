package dk.eal.learerbookingsystem.communication;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Trine on 21-11-13.
 */
public class RestCommunication implements ICommunication {
    private final String
            baseURL = "http://localhost:14781/api",
            itemURL = "item";

    public RestCommunication() {

    }

    @Override
    public String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine())!= null) {
                sb.append(line);
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            return sb.toString();
        }
    }

    @Override
    public void putData(URL url, String json) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPut put = new HttpPut(url.toURI());
            put.addHeader("Content-Type", "application/json");
            put.addHeader("Accept", "application/json");
            put.setEntity(new StringEntity(json));
            HttpResponse response = client.execute(put);
        }
        catch (Exception e) { }
    }

    @Override
    public void postData(URL url, String json) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url.toURI());
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Accept", "application/json");
            post.setEntity(new StringEntity(json));
            HttpResponse response = client.execute(post);
        }
        catch (Exception e) { }
    }

    @Override
    public String getData(URL url) {
        String data="";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url.toURI());
            get.addHeader("Content-Type", "application/json");
            get.addHeader("Accept", "application/json");
            HttpResponse response = client.execute(get);
            data = readStream(response.getEntity().getContent());
        }
        catch (Exception e) { }

        return data;
    }

    @Override
    public void deleteData(URL url) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpDelete delete = new HttpDelete(url.toURI());
            delete.addHeader("Content-Type", "application/json");
            delete.addHeader("Accept", "application/json");
            HttpResponse response = client.execute(delete);
            String s = response.toString();
        }
        catch (Exception e) { }
    }
}
