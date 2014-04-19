package ar.com.glasit.rom.Activities;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Fragments.FreeTableFragment;
import ar.com.glasit.rom.Fragments.OpenTableFragment;
import ar.com.glasit.rom.Model.TablesGestor;

public class TableDetailActivity extends SherlockFragmentActivity {
	private final static String tittle = "Mesa "; 

	private int tableNumber;
	private int tab;
 
    @Override
    public void onBackPressed() {
	    Intent intent = new Intent(this.getBaseContext(), TablesActivity.class);
	    intent.putExtra("currentTab", tab);
	    startActivity(intent);
        finish();
        return;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        Intent i = getIntent();
        String tableN = i.getExtras().getString("tableNumber");
        tab= i.getExtras().getInt("currentTab"); 
        
        String titulo = tittle;
        titulo += tableN;
        setTitle(titulo);
        
        tableNumber = Integer.parseInt(tableN);
      
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SherlockFragment newFragment = null; 
        TablesGestor gt = TablesGestor.getInstance();
        if (gt.getTable(tableNumber).isOpen() ) {
        	newFragment = new OpenTableFragment();
        }
        else {
        	newFragment = new FreeTableFragment();
        }
   
        fragmentTransaction.replace(android.R.id.content, newFragment);
        fragmentTransaction.commit();      

    }

	public int getTableNumber() {
		return this.tableNumber;
	}

	public int getTab() {
		return this.tab;
	}

}