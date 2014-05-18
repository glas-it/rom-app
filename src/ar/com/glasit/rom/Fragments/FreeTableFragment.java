package ar.com.glasit.rom.Fragments;

import ar.com.glasit.rom.Model.*;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.app.SherlockFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import org.json.JSONException;
import org.json.JSONObject;

public class FreeTableFragment extends SherlockFragment{

    private int cubiertos;
    private TextView people;
    private Table table;
    private TableManager manager;

    public FreeTableFragment() {
        this.manager = (TableManager) getSherlockActivity();
        this.cubiertos = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(ar.com.glasit.rom.R.layout.fragment_free_table, container,
                false);
        super.onCreate(savedInstanceState);

        this.table = getTable();
        Button plus = (Button) rootView.findViewById(R.id.plus);
        Button less = (Button) rootView.findViewById(R.id.less);

        people = (TextView) rootView.findViewById(R.id.cubiertos);
        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cubiertos < table.getMaximunCapacity()) {
                    people.setText(Integer.toString(++cubiertos));
                }
            }
        });
        less.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cubiertos > 0) {
                    people.setText(Integer.toString(--cubiertos));
                }
            }
        });
        
        Button join = (Button) rootView.findViewById(R.id.join);
        join.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
        
        TextView maxCapacity = (TextView) rootView.findViewById(R.id.capacity);
        maxCapacity.setText(Integer.toString(table.getMaximunCapacity()));
        this.validateEnableTable(rootView);
	    return rootView;
	  }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getTable().isEnabled()) {
            inflater.inflate(R.menu.free_table, menu);
        } else {
            inflater.inflate(R.menu.cancel, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.item_open:
                TablesGestor.getInstance().openTable(getTable().getNumber(), Integer.parseInt(people.getText().toString()));
                ((TableManager) getSherlockActivity()).onTableOpened(getTable().getId(), Integer.parseInt(people.getText().toString()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void validateEnableTable(View root) {
        if(!getTable().isEnabled() ) {
            root.findViewById(R.id.plus).setClickable(false);
            root.findViewById(R.id.less).setClickable(false);
        }
    }

    private Table getTable() {
        if (this.table != null)
            return this.table;
        try {
            JSONObject json = new JSONObject(getArguments().getString("table"));
            return (this.table = Table.buildTable(json));
        } catch (JSONException e) {
        }
        return null;
    }
}
