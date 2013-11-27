package dk.eal.learerbookingsystem.model;

import dk.eal.learerbookingsystem.utils.BaseEvent;
import dk.eal.learerbookingsystem.utils.EventDispatcher;

/**
 * Created by Trine on 26-11-13.
 */
public class UserModel extends EventDispatcher {
    public static class ChangeEvent extends BaseEvent {

        public  ChangeEvent(String type) {
            super(type);
        }
    }
    private static UserModel _instance;

    public UserModel() { }

    public static UserModel getInstance() {
        if (_instance == null)
            _instance = new UserModel();
        return _instance;
    }

    public void notifyChange(String type) {
        dispatchEvent(new ChangeEvent(type));
    }
}
