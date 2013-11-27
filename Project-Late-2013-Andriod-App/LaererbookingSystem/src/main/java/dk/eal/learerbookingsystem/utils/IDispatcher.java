package dk.eal.learerbookingsystem.utils;

/**
 * Created by Trine on 26-11-13.
 */
public interface IDispatcher {
    void addListener(String type, IEventListener listener);
    void removeListener(String type, IEventListener listener);
    boolean hasListener(String type, IEventListener listener);
    void dispatchEvent(IEvent event);
}
