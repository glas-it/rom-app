package ar.com.glasit.rom.Fragments;

import ar.com.glasit.rom.Model.OpenTable;
import ar.com.glasit.rom.Model.TableManager;
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
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.Model.TablesGestor;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class FreeTableFragment extends SherlockFragment{

    private int cubiertos;
    private TextView people;
    private Table table;
    private TableManager manager;

    public FreeTableFragment(TableManager manager, Table table) {
        this.table = table;
        this.manager = manager;
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

        TextView maxCapacity = (TextView) rootView.findViewById(R.id.capacity);
        maxCapacity.setText(Integer.toString(table.getMaximunCapacity()));
        this.validateEnableTable(rootView);
	    return rootView;
	  }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (table.isEnabled()) {
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
                TablesGestor tb = TablesGestor.getInstance();
                tb.openTable(table.getNumber(), Integer.parseInt(people.getText().toString()));
                manager.onTableOpened((OpenTable)tb.getTable(table.getNumber()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void validateEnableTable(View root) {
        if(!table.isEnabled() ) {
            root.findViewById(R.id.plus).setClickable(false);
            root.findViewById(R.id.less).setClickable(false);
        }
    }
}
