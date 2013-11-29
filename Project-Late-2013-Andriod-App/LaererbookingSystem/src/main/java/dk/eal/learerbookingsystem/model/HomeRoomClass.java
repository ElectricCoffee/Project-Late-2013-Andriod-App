package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class HomeRoomClass {
    private long _id;
    private String _name;

    public HomeRoomClass() {}

    public HomeRoomClass(String name) {
        _name = name;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }


}
