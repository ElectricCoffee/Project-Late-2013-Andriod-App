package dk.eal.learerbookingsystem.communication;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Trine on 21-11-13.
 */
public interface ICommunication {
    String readStream(InputStream in);
    void putData(URL url, String json);
    void postData(URL url, String json);
    String getData(URL url);
    void deleteData(URL url);
}

