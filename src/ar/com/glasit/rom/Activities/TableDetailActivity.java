package ar.com.glasit.rom.Activities;

import android.widget.Toast;
import ar.com.glasit.rom.Fragments.LoadTableFragment;
import ar.com.glasit.rom.Fragments.OpenTableFragment;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Model.*;
import ar.com.glasit.rom.Service.*;

import android.content.Intent;
import android.os.Bundle;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Fragments.FreeTableFragment;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class TableDetailActivity extends StackFragmentActivity implements TableManager {
	private final static String tittle = "Mesa "; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        Intent i = getIntent();
        setTitle(tittle + i.getIntExtra("tableNumber", 0));

        SherlockFragment fragment = new LoadTableFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tableId", i.getIntExtra("tableId", 0));
        fragment.setArguments(bundle);
        addFragment(fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_cancel:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTableOpened(int tableId, int fellowDiner) {
        List<NameValuePair> params = new Vector<NameValuePair>();
        params.add(new BasicNameValuePair("idRestaurant", BackendHelper.getSecretKey()));
        JSONArray mesas = new JSONArray();
        mesas.put(tableId);
        params.add(new BasicNameValuePair("idMesas", mesas.toString()));
        params.add(new BasicNameValuePair("usernameMozo", BackendHelper.getLoggedUser()));
        params.add(new BasicNameValuePair("comensales", Integer.toString(fellowDiner)));
        RestService.callPostService(openTableListener, WellKnownMethods.OpenTable, params);
    }

    @Override
    public void onTableClosed(int tableId) {
        List<NameValuePair> params = new Vector<NameValuePair>();
        params.add(new BasicNameValuePair("idRestaurant", BackendHelper.getSecretKey()));
        params.add(new BasicNameValuePair("idMesa", Integer.toString(tableId)));
        RestService.callPostService(null, WellKnownMethods.CloseTable, params);
        onBackPressed();
    }

    @Override
    public void onTableOrder(int tableId, List<Order> orders) {
        JSONArray json = new JSONArray();
        for (Order o : orders) {
            if (o.isLocal()){
                o.stageCompleted();
                for (int i = 0; i < o.getCount(); i++) {
                    JSONObject jsonTable = o.toJSON();
                    try {
                        jsonTable.put("id", o.getId()+"-"+i);
                    } catch (JSONException e) {
                    }
                    json.put(jsonTable);
                }
            }
        }
        List<NameValuePair> params = new Vector<NameValuePair>();
        params.add(new BasicNameValuePair("idRestaurant", BackendHelper.getSecretKey()));
        params.add(new BasicNameValuePair("idMesa", Integer.toString(tableId)));
        params.add(new BasicNameValuePair("platos", json.toString()));
        RestService.callPostService(null, WellKnownMethods.NewOrder, params);
        onBackPressed();
    }

    public void displayOpenTable(JSONObject table) {
        OpenTableFragment fragment = new OpenTableFragment();
        Bundle bundle = new Bundle();
        try {
            table.put("abierta", true);
        } catch (JSONException e) {
        }
        bundle.putString("table", table.toString());
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    public void displayFreeTable(JSONObject table) {
        FreeTableFragment fragment = new FreeTableFragment();
        Bundle bundle = new Bundle();
        bundle.putString("table", table.toString());
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    private ServiceListener openTableListener =  new ServiceListener() {
        @Override
        public void onServiceCompleted(IServiceRequest request, ServiceResponse response) {
            try {
                if (response.getSuccess()){
                    displayOpenTable(response.getJsonObject().getJSONObject("mesa"));
                }else {
                    onBackPressed();
                }
            } catch (JSONException e) {
            }

        }

        @Override
        public void onError(String error) {
            Toast.makeText(TableDetailActivity.this, error, Toast.LENGTH_SHORT).show();
        }
    };

}