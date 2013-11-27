package dk.eal.learerbookingsystem.utils;

import java.util.List;

import dk.eal.learerbookingsystem.model.BaseModel;

/**
 * Created by Trine on 25-11-13.
 */
public interface IServerInteraction <TModel extends BaseModel> {
    void serverResult(List<TModel> items);
}
