package dk.eal.learerbookingsystem.Utils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dk.eal.learerbookingsystem.communication.RestCommunication;

/**
 * Created by Trine on 21-11-13.
 */
public class JsonSerializer {

    public JsonSerializer(){
    }

    public static <T> String Serialize(T item) {
        Gson gson = new Gson();
        return gson.toJson(item);
    }

    public static <T> T DeSerialize(String json, Class<T> cls) {
        Gson gson = new Gson();
        T item = gson.fromJson(json, cls);
        return item;
    }

    public static <T> List<T> DeSerializeList(String json, Class<T[]> cls) {
        List<T> items = new ArrayList<T>();
        Gson gson = new Gson();
        T[] itemsArray = gson.fromJson(json, cls);
        items.addAll(Arrays.asList(itemsArray));

        return items;
    }
}
