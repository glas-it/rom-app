package ar.com.glasit.rom.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import ar.com.glasit.rom.Fragments.KitchenFragment;
import ar.com.glasit.rom.Model.*;
import ar.com.glasit.rom.Service.*;
import org.json.JSONArray;

import java.util.List;
import java.util.Vector;

/**
 * Created by pablo on 04/05/14.
 */
public class UpdateService extends Service implements ServiceListener{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if (Menu.getInstance().getChildrenCount() > 0) {
            RestService.callGetService(this, WellKnownMethods.GetOrders, null);
        }
    }

    @Override
    public void onServiceCompleted(IServiceRequest request, ServiceResponse serviceResponse) {
        try {
            List<Order> newOrder =  new Vector<Order>();
            JSONArray orders = serviceResponse.getJsonArray();
            for(int i=0;i<orders.length();i++){
                Order order = Order.buildOrder(orders.getJSONObject(i));
                OpenTable table = (OpenTable) TablesGestor.getInstance().getTable(Integer.parseInt(order.getTableNumber()));
                List<Order> tableOrders = table.getOrderRequest();
                int j = tableOrders.indexOf(order);
                if (j != -1) {
                    if (!tableOrders.get(j).getStatus().equals(Order.Status.CANCELLED) ||
                            !tableOrders.get(j).getStatus().equals(Order.Status.REJECTED)) {
                        tableOrders.get(j).setStatus(Integer.parseInt(order.getId().substring(order.getId().lastIndexOf("-"))),order.getStatus());
                    }
                } else {
                    table.addOrder(order);
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onError(String error) {

    }
}
