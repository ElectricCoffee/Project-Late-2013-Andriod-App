package dk.eal.learerbookingsystem.Utils;

import android.graphics.Shader;

import java.util.List;

import dk.eal.learerbookingsystem.model.BaseModel;

/**
 * Created by Trine on 25-11-13.
 */
public interface IServerInteraction <TModel extends BaseModel> {
    void serverResult(List<TModel> items);
}
