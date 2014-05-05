package ar.com.glasit.rom.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filterable;
import android.widget.Toast;
import ar.com.glasit.rom.Activities.TableDetailActivity;
import ar.com.glasit.rom.Adapters.TableAdapter;
import ar.com.glasit.rom.Model.OpenTable;
import ar.com.glasit.rom.Model.Order;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.Model.TablesGestor;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Service.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import org.json.JSONArray;

import java.util.List;
import java.util.Vector;

public class TablesFragment extends GridSearcherFragment{

    public enum Type {
        MINE, FREE, ALL
    }
    protected List<Table> tables = null;
    protected boolean isVisible = true, searching = false;
    protected Type type;

    public TablesFragment(Type type){
        this(new Vector<Table>(),type);
    }

    public TablesFragment(List<Table> tables, Type type){
        super();
        this.type = type;
        this.tables = tables;
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        setTables();
    }

    @Override
    public void onStop() {
        super.onStop();
        isVisible = false;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getGridView().setBackgroundColor(Color.TRANSPARENT);
        getGridView().setCacheColorHint(Color.TRANSPARENT);
        getGridView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Table table = (Table) getGridAdapter().getItem(position);
                RestService.callGetService(new TableListener(table), WellKnownMethods.GetOrders, null);
            }
        });
        getGridView().setNumColumns(4);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(R.string.empty_tables);
        setGridShown(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            ((Filterable) getGridAdapter()).getFilter().filter(query);
        } catch (NullPointerException ex) {}
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            ((Filterable) getGridAdapter()).getFilter().filter(newText);
        } catch (NullPointerException ex) {}
        return false;
    }

    public void setTables(){
        setGridShown(true);
        this.tables = TablesGestor.getInstance().getTables(type);
        setGridAdapter(new TableAdapter(tables));
    }

    @Override
    protected void inflateMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
    }

    public void showMessage(String msg) {
        if (isVisible) {
            Toast.makeText(getSherlockActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private class TableListener implements ServiceListener {
        Table table;
        TableListener(Table table) {
            this.table = table;
        }
        @Override
        public void onServiceCompleted(IServiceRequest request, ServiceResponse serviceResponse) {
            try {
                JSONArray orders = serviceResponse.getJsonArray();
                boolean clear = true;
                for(int i=0;i<orders.length();i++){
                    Order order = Order.buildOrder(orders.getJSONObject(i));
                    if (table.getNumber() == Integer.parseInt(order.getTableNumber())){
                        OpenTable openTable = (OpenTable) table;
                        if (clear) {
                            openTable.clearOrders();
                            clear = false;
                        }
                        openTable.addOrder(order);
                    }
                }
                Intent i = new Intent(getActivity(), TableDetailActivity.class);
                i.putExtra("tableNumber", table.getNumber());
                startActivity(i);
            } catch (Exception e) {
            }
        }

        @Override
        public void onError(String error) {

        }
    };
}
