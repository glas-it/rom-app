package ar.com.glasit.rom.Activities;

import ar.com.glasit.rom.Fragments.OpenTableFragment;
import ar.com.glasit.rom.Model.*;
import com.actionbarsherlock.app.SherlockFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Fragments.FreeTableFragment;
import com.actionbarsherlock.view.MenuItem;

public class TableDetailActivity extends StackFragmentActivity implements TableManager {
	private final static String tittle = "Mesa "; 

	private int tableNumber;
	private int tab;

    private Table oldTable;
    private boolean cancelled;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        Intent i = getIntent();
        tableNumber = i.getIntExtra("tableNumber", 0);
        tab= i.getExtras().getInt("currentTab"); 

        cancelled = true;

        String titulo = tittle;
        titulo += tableNumber;
        setTitle(titulo);

        TablesGestor gt = TablesGestor.getInstance();
        oldTable = (Table) gt.getTable(tableNumber).clone();
        if (gt.getTable(tableNumber).isOpen() ) {
            addFragment(new OpenTableFragment(this, gt.getTable(tableNumber)));
        } else {
            addFragment(new FreeTableFragment(this, gt.getTable(tableNumber)));
        }
   
    }

	public int getTableNumber() {
		return this.tableNumber;
	}

	public int getTab() {
		return this.tab;
	}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_cancel:
                cancelled = true;
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTableOpened(OpenTable table) {
        oldTable = table;
        replaceFragment(new OpenTableFragment(this, table));
    }

    @Override
    public void onTableClosed(FreeTable table) {
        oldTable = table;
        replaceFragment(new FreeTableFragment(this, table));
        cancelled = false;
    }

    @Override
    public void onTableOrder(OpenTable table) {
        TablesGestor.getInstance().updateTable(table);
        cancelled = false;
    }

    @Override
    public void onBackPressed() {
        if (cancelled) {
            TablesGestor.getInstance().updateTable(oldTable);
        }
        super.onBackPressed();
    }
}