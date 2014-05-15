package ar.com.glasit.rom.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.glasit.rom.Activities.TableDetailActivity;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Model.OpenTable;
import ar.com.glasit.rom.Model.Order;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Service.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.internal.view.menu.BaseMenuPresenter;
import com.devspark.progressfragment.SherlockProgressFragment;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class LoadTableFragment extends SherlockProgressFragment{
    private View mContentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_main, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(mContentView);
        setEmptyText(R.string.empty);
        obtainData();
    }

    private void obtainData() {
        // Show indeterminate progress
        setContentShown(false);

        int idTable = getArguments().getInt("tableId");
        List<NameValuePair> params = new Vector<NameValuePair>();
        params.add(new BasicNameValuePair("idRestaurant", BackendHelper.getSecretKey()));
        params.add(new BasicNameValuePair("idMesa", Integer.toString(idTable)));
        RestService.callGetService(new TableListener(), WellKnownMethods.GetTable, params);

    }

    private class TableListener implements ServiceListener {
        @Override
        public void onServiceCompleted(IServiceRequest request, ServiceResponse serviceResponse) {
            try {
                JSONObject table = serviceResponse.getJsonObject();
                if (table.has("id")) {
                    if (table.getJSONObject("mozo").getString("username").equals(BackendHelper.getLoggedUser())) {
                        ((TableDetailActivity)getSherlockActivity()).displayOpenTable(table);
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
                        alert.setMessage(R.string.tookTableConfirmation);
                        alert.setPositiveButton("Ok", new TookTableListener(table));
                        alert.setNegativeButton("Cancel", null);
                        alert.show();
                    }
                } else {
                    ((TableDetailActivity)getSherlockActivity()).displayFreeTable(table.getJSONObject("mesa"));
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void onError(String error) {

        }

        private class TookTableListener implements DialogInterface.OnClickListener {
            JSONObject table;
            public TookTableListener(JSONObject table) {
                this.table = table;
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int idTable = getArguments().getInt("tableId");
                List<NameValuePair> params = new Vector<NameValuePair>();
                params.add(new BasicNameValuePair("idRestaurant", BackendHelper.getSecretKey()));
                params.add(new BasicNameValuePair("idMesa", Integer.toString(idTable)));
                params.add(new BasicNameValuePair("username", BackendHelper.getLoggedUser()));
                RestService.callPostService(new TableListener(), WellKnownMethods.TakeTable, params);

                ((TableDetailActivity)getSherlockActivity()).displayOpenTable(table);
            }
        }
    };
}
