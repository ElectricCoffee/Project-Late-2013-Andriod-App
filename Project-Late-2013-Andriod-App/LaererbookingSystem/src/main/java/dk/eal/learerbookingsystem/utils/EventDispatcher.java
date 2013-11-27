package dk.eal.learerbookingsystem.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Trine on 26-11-13.
 */
public class EventDispatcher implements IDispatcher {

    private static final String TAG = EventDispatcher.class.getSimpleName();

    private HashMap<String, CopyOnWriteArrayList<IEventListener>> _listenerMap;
    private IDispatcher _target;

    public EventDispatcher() {
        this(null);
    }

    public EventDispatcher(IDispatcher target) {
        _listenerMap = new HashMap<String, CopyOnWriteArrayList<IEventListener>>();
        _target = (target != null) ? target : this;
    }

    @Override
    public void addListener(String type, IEventListener listener) {
        synchronized (_listenerMap) {
            CopyOnWriteArrayList<IEventListener> list = _listenerMap.get(type);
            if(list == null) {
                list = new CopyOnWriteArrayList<IEventListener>();
                _listenerMap.put(type, list);
            }
            list.add(listener);
        }
    }

    @Override
    public void removeListener(String type, IEventListener listener) {
        synchronized (_listenerMap) {
            CopyOnWriteArrayList<IEventListener> list = _listenerMap.get(type);
            if(list == null)
                return;
            list.remove(listener);
            if(list.isEmpty())
                _listenerMap.remove(type);
        }
    }

    @Override
    public boolean hasListener(String type, IEventListener listener) {
        synchronized (_listenerMap) {
            CopyOnWriteArrayList<IEventListener> list = _listenerMap.get(type);
            if (list == null)
                return false;
            return list.contains(listener);
        }
    }

    @Override
    public void dispatchEvent(IEvent event) {
        if(event == null) {
            Log.e(TAG, "Can not dispatch null event");
            return;
        }
        String type = event.getType();
        event.setSource(_target);
        CopyOnWriteArrayList<IEventListener> list;
        synchronized (_listenerMap) {
            list = _listenerMap.get(type);
        }
        if (list == null)
            return;
        for(IEventListener l : list)
            l.onEvent(event);
    }

    public void dispose() {
        synchronized (_listenerMap) {
            _listenerMap.clear();
        }

        _target = null;
    }
}
