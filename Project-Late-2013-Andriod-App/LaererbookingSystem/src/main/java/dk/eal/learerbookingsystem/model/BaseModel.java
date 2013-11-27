package dk.eal.learerbookingsystem.model;

import dk.eal.learerbookingsystem.activity.BaseActivity;

/**
 * Created by Trine on 25-11-13.
 */
public class BaseModel <TController extends BaseActivity> {
    private static BaseModel _instance;
    protected TController _controller;

    public BaseModel() { }

    public static BaseModel getInstance() {
        if (_instance == null)
            _instance = new BaseModel();
        return _instance;
    }
}
