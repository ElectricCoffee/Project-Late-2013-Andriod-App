package dk.eal.learerbookingsystem.model;

import dk.eal.learerbookingsystem.controller.BaseController;

/**
 * Created by Trine on 25-11-13.
 */
public class BaseModel <TController extends BaseController> {
    protected TController _controller;

    public BaseModel() { }

    public BaseModel(TController controller, Class<TController> cls) {
        try {
            _controller = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
