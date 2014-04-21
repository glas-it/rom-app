package ar.com.glasit.rom.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.Toast;
import ar.com.glasit.rom.Activities.TableDetailActivity;
import ar.com.glasit.rom.Adapters.TableAdapter;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.Model.TablesGestor;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import java.util.List;
import java.util.Vector;

public class TableGridFragment extends GridSearcherFragment{

    public enum Type {
        MINE, FREE, ALL
    }
    protected List<Table> tables = null;
    protected boolean isVisible = true, searching = false;
    protected Type type;

    public TableGridFragment(Type type){
        this(new Vector<Table>(),type);
    }

    public TableGridFragment(List<Table> tables, Type type){
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
                Intent i = new Intent(getActivity(), TableDetailActivity.class);
                Table table = (Table) getGridAdapter().getItem(position);
                i.putExtra("tableNumber", table.getNumber());
                startActivity(i);
            }
        });
        getGridView().setNumColumns(4);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(R.string.empty);
        obtainData();
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

    protected void obtainData() {
        searching = true;
        if (!TablesGestor.getInstance().getAllTables().isEmpty()) {
            setTables();
        }
    }

}
