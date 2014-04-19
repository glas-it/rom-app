package ar.com.glasit.rom.Fragments;

import java.util.Iterator;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Activities.TableDetailActivity;
import ar.com.glasit.rom.Adapters.ListViewAdapter;
import ar.com.glasit.rom.Model.FreeTable;
import ar.com.glasit.rom.Model.OpenTable;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.Model.TablesGestor;

public class TablesFragment extends SherlockFragment {

		private String[] table;
	    private ListView list;
	    private ListViewAdapter adapter;
	    private TablesGestor tg;
	    private static int currentTab;
	    private int myTab;

	    @Override 
	    public void onResume() {
	    	super.onResume();
	    	adapter.notifyDataSetChanged();
	    }
	    
	     
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View rootView = inflater.inflate(R.layout.fragment_tables, container,
	                false);
  
	        
	       Bundle b= this.getArguments();
	       currentTab = b.getInt("tab");
	       myTab = currentTab;
	       
	        if(currentTab == 1) {
	            tg = TablesGestor.getInstance();
		        List<OpenTable> t;
		        t = tg.getMyTables();
		        table = new String [t.size()];
		        int i = 0;
		        Iterator<OpenTable> it = t.iterator();
		        while (it.hasNext()) {
		        	OpenTable f = it.next();
		        	table[i] = Integer.toString(f.getNumber());
		        	i++;
		        }
	        }
	        else {
	        	if (currentTab == 2) {
	        		tg = TablesGestor.getInstance();
	        		List<FreeTable> t;
	        		t = tg.getFreeTables();
	        		table = new String [t.size()];
	        		int i = 0;
	        		Iterator<FreeTable> it = t.iterator();
	        		while (it.hasNext()) {
	        			FreeTable f = it.next();
	        			table[i] = Integer.toString(f.getNumber());
	        			i++;
	        		}
	        	}
	        	else {
	        		tg = TablesGestor.getInstance();
	        		List<Table> t;
	        		t = tg.getAllTables();
	        		if ( t!= null ) {
	        			table = new String [t.size()];
	        			int i = 0;
	        			Iterator<Table> it = t.iterator();
	        			while (it.hasNext()) {
	        					Table f = it.next();
	        					table[i] = Integer.toString(f.getNumber());
	        					i++;
	        			}
	        		}
	        	}
	        }

	        // Locate the ListView in fragmenttab1.xml
	        list = (ListView) rootView.findViewById(R.id.listview);
	 
	        // Pass results to ListViewAdapter Class
	        adapter = new ListViewAdapter(getActivity(), table);
	        // Binds the Adapter to the ListView
	        list.setAdapter(adapter);
	        
	        // Capture clicks on ListView items
	       adapter.notifyDataSetChanged();
	        list.setOnItemClickListener(new OnItemClickListener() {
	        	 
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
	                Intent i = new Intent(getActivity(), TableDetailActivity.class);

	                String tableNumber = table[position];
	                i.putExtra("tableNumber", tableNumber);

	                i.putExtra("currentTab", myTab);

	                startActivity(i);
	                getActivity().finish();
	            }
	 
	        });

	        return rootView;
	    }
	    
 
}