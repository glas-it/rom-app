package ar.com.glasit.rom.Fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import ar.com.glasit.rom.Model.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Filterable;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Adapters.TableSelectorAdapter;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Service.IServiceRequest;
import ar.com.glasit.rom.Service.RestService;
import ar.com.glasit.rom.Service.ServiceListener;
import ar.com.glasit.rom.Service.ServiceResponse;
import ar.com.glasit.rom.Service.WellKnownMethods;

public class TablesSelectorFragment extends GridSearcherFragment implements ServiceListener {
   
	private static final String ACTUAL_CAPACITY = " - Capacidad: ";
	protected List<JoinedTable> selectedTables = null;
    protected boolean isVisible = true;
    private int currentCapacity;
    private TableSelectorAdapter tableSelectorAdapter;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedTables = new Vector<JoinedTable>();
    }

    @Override
     public void onResume() {
         super.onResume();
         isVisible = true;
         obtainData();
     }

     private void obtainData() {
         List<NameValuePair> params = new Vector<NameValuePair>();
         params.add(new BasicNameValuePair("idRestaurant", BackendHelper.getSecretKey()));
         RestService.callGetService(this, WellKnownMethods.GetFreeTables, params);
	}

	@Override
     public void onStop() {
         super.onStop();
         isVisible = false;
     }
     
	 private  List<JoinedTable> toJoinedTables(List<Table> tables) {
		 List<JoinedTable> joinedTables = new ArrayList<JoinedTable>();
		 Iterator<Table> it = tables.iterator();
    	 while ( it.hasNext()) {
    		 Table t = it.next();
    		 joinedTables.add(t.toJoinedTable());
    	 }
    	 return joinedTables;
	 }

    public void setTables(List<Table> freeTables){
        List<JoinedTable> freeTablesToJoin = toJoinedTables(freeTables);
        List<JoinedTable> joinedTables = new Vector<JoinedTable>();
        JSONArray joinedTablesJSON = getJoinedTables();
        for (int i = 0; i < joinedTablesJSON.length(); i++) {
            try {
                int id = joinedTablesJSON.getJSONObject(i).getInt("id");
                int numero = joinedTablesJSON.getJSONObject(i).getInt("numero");
                int capacidad = joinedTablesJSON.getJSONObject(i).getInt("capacidad");
                JoinedTable joinedTable = new JoinedTable(id, numero, capacidad);
                joinedTables.add(joinedTable);
                selectedTables.add(joinedTable);
            } catch (JSONException e) {
            }
        }
        int tableNumber = getArguments().getInt("tableNumber");
        Iterator<JoinedTable> it = freeTablesToJoin.iterator();
        boolean found =false;
        while ( it.hasNext() && !found ) {
            JoinedTable t = it.next();
            if ( t.getTableNumber() == tableNumber) {
                it.remove();
            } else if (joinedTables.contains(t.getTableNumber())) {
                selectedTables.add(t);
            }
        }
        this.tableSelectorAdapter = new TableSelectorAdapter(freeTablesToJoin, joinedTables);
        this.tableSelectorAdapter.setCheckListener(this.checkListener);
        setGridShown(true);
        setGridAdapter(this.tableSelectorAdapter);
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
     
     @Override
     protected void inflateMenu(Menu menu, MenuInflater inflater) {
         inflater.inflate(R.menu.refresh, menu);
     }
     
     @Override
     public void onViewCreated(View view, Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);
    	 this.title = (String) getSherlockActivity().getTitle();
    	 this.currentCapacity = getArguments().getInt("capacity");
      	 drawTittle();
         getGridView().setBackgroundColor(Color.TRANSPARENT);
         getGridView().setCacheColorHint(Color.TRANSPARENT);
         getGridView().setNumColumns(3);
     }
     
     @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	 inflater.inflate(R.menu.table_selector, menu);
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case R.id.joinConfirmation:
            	 if (selectedTables != null) {
            		 ((TableManager) getSherlockActivity()).onTableJoined(selectedTables);
            	 }
             case R.id.cancelSelection:
                 this.getSherlockActivity().setTitle(title);
                 getSherlockActivity().onBackPressed();
                 return true;
             default:
                 return super.onOptionsItemSelected(item);
         }
     }
     
     private void drawTittle() {
    	 String message = title;
    	 message+= ACTUAL_CAPACITY;
    	 message+= Integer.toString(currentCapacity);
    	 this.getSherlockActivity().setTitle(message);
     }


    public JSONArray getJoinedTables() {
        try {
            return new JSONArray(getArguments().getString("joinedTables"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }
    public interface CheckListener {
     	public void checked(JoinedTable table);
     	public void unChecked(JoinedTable table);
 	}
     
    private CheckListener checkListener = new CheckListener() {

        @Override
        public void checked(JoinedTable table) {
            currentCapacity += table.getCapacity();
            if (selectedTables == null) {
                selectedTables = new ArrayList<JoinedTable>();
            }
            selectedTables.add(table);
            drawTittle();
        }

        @Override
        public void unChecked(JoinedTable table) {
            currentCapacity -= table.getCapacity();
            if (selectedTables != null) {
                selectedTables.remove(table);
            }
            drawTittle();
        };
    };

	@Override
	public void onServiceCompleted(IServiceRequest request, ServiceResponse serviceResponse) {
		 try {
	            List<Table> newTables =  new Vector<Table>();
	            JSONArray tables = serviceResponse.getJsonArray();
	            for(int i=0;i<tables.length();i++){
	                Table t = Table.buildTable(tables.getJSONObject(i));
	                if (t != null) {
	                    newTables.add(t);
	                }
	            }
                setTables(newTables);
         }
		 catch (Exception e) {}
	}

	@Override
	public void onError(String error) {
	} 

}
