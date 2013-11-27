package dk.eal.learerbookingsystem.utils;

/**
 * Created by Trine on 26-11-13.
 */
public class BaseEvent implements IEvent {

    private String _type;
    protected Object _source;

    public BaseEvent(String type) {
        _type = type;
    }

    @Override
    public String getType() {
        return _type;
    }

    public void setType(String type) {
        type = type;
    }

    @Override
    public Object getSource() {
        return _source;
    }

    @Override
    public void setSource(Object source) {
        _source = source;
    }
}
