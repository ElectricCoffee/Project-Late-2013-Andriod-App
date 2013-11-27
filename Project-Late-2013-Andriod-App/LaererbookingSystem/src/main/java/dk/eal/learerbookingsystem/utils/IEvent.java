package dk.eal.learerbookingsystem.utils;

/**
 * Created by Trine on 26-11-13.
 */
public interface IEvent {
    public String getType();
    public Object getSource();
    public void setSource(Object source);
}
