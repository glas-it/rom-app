package ar.com.glasit.rom.Fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.view.View;
import android.widget.Filterable;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Adapters.TablesSelectorAdapter;
import ar.com.glasit.rom.Fragments.TablesFragment.Type;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Model.CompositeTable;
import ar.com.glasit.rom.Model.FreeTable;
import ar.com.glasit.rom.Model.OpenTable;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.Model.TableManager;
import ar.com.glasit.rom.Model.TablesGestor;
import ar.com.glasit.rom.Model.Table.JoinedTable;
import ar.com.glasit.rom.Service.IServiceRequest;
import ar.com.glasit.rom.Service.RestService;
import ar.com.glasit.rom.Service.ServiceListener;
import ar.com.glasit.rom.Service.ServiceResponse;
import ar.com.glasit.rom.Service.WellKnownMethods;

public class TablesSelectorFragment extends ListSearcherFragment implements ServiceListener {
   
	private static final String ACTUAL_CAPACITY = " Capacidad : ";
	protected List<JoinedTable> selectedTables = null;
	protected List<JoinedTable> previouTablesSelected = null;
    protected boolean isVisible = true, searching = false;
    private Table table;
    private int currentCapacity;
    private TablesSelectorAdapter tableSelectorAdapter;
    private String title;
    
     public TablesSelectorFragment() {
		super();
	}
     
     @Override
     public void onResume() {
         super.onResume();
         isVisible = true;
         this.selectedTables = null;
         this.previouTablesSelected = null;
         obtainData();
     }

     private void obtainData() {
         List<NameValuePair> params = new Vector<NameValuePair>();
         params.add(new BasicNameValuePair("idRestaurant", BackendHelper.getSecretKey()));
         RestService.callGetService(this, WellKnownMethods.GetTables, params);
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
	
     public void setTables(){
         
    	 setListShown(true);
         List<Table> freeTables = TablesGestor.getInstance().getTables(Type.FREE);
         List<JoinedTable> freeTablesToJoin = toJoinedTables(freeTables);
         List<JoinedTable> previousSelection = null;
          
         if(!this.table.isOpen()) {
        	 
        	 FreeTable free = (FreeTable) table;
        	 previousSelection = free.getTablesToJoin();
        	 
        	 Iterator<JoinedTable> it = freeTablesToJoin.iterator();
        	 boolean found =false;
        	 while ( it.hasNext() && !found ) {
        		 JoinedTable t = it.next();
        		 if ( t.getTableNumber() == this.table.getNumber()) {
        			 it.remove();
        			 found = true;
        		 }
        	 }
         }
         else {
        	 OpenTable open = (OpenTable)this.table;
        	 if (open.isJoined()) {
        		 previousSelection = ((CompositeTable) open).getJoinedTables(); 		 
        	 }
         }

         if(previousSelection != null) {
        	this.previouTablesSelected = previousSelection;
 			if (selectedTables == null) {
				selectedTables = new ArrayList<JoinedTable>();
 			}
 			this.selectedTables.addAll(previouTablesSelected);
        	freeTablesToJoin.addAll(previousSelection);
        	Collections.sort(freeTablesToJoin);
         }
         this.tableSelectorAdapter = new TablesSelectorAdapter(freeTablesToJoin, table,previousSelection);
         this.tableSelectorAdapter.setCheckListener(this.checkListener);
         this.tableSelectorAdapter.setView(this.getListView());

         this.setListAdapter(this.tableSelectorAdapter);
     }


	@Override
     public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);
         setEmptyText(R.string.empty_tables);
         setListShown(false);
     }

     @Override
     public boolean onQueryTextSubmit(String query) {
         try {
             ((Filterable) getListAdapter()).getFilter().filter(query);
         } catch (NullPointerException ex) {}
         return true;
     }

     @Override
     public boolean onQueryTextChange(String newText) {
         try {
             ((Filterable) getListAdapter()).getFilter().filter(newText);
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
    	 this.table= getTable();
    	 this.currentCapacity = table.getMaximunCapacity();
      	 drawTittle();
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
            		 if (!this.table.isOpen()) {
            			 ((TableManager) getSherlockActivity()).onTableFreeJoined(table, selectedTables);
            		 }
            		 else {
            			 ((TableManager) getSherlockActivity()).onTableOpenJoined(table, this.toAdd(), this.toQuit());
            		 }
            	 }
            	 return true;
             default:
                 return super.onOptionsItemSelected(item);
         }
     }
     
     private void drawTittle() {
    	 String message = title;
    	 message+= ACTUAL_CAPACITY;
    	 message+= Integer.toString(currentCapacity);
    	 this.getActivity().setTitle(message);
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
				Iterator<JoinedTable> it  = selectedTables.iterator();
				boolean found = false;
				while (it.hasNext() && !found) {
					JoinedTable t = it.next();
					if ( t.getTableNumber() == table.getTableNumber() ) 
						it.remove();
				}
			}
			drawTittle();
		};
     };

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
	            TablesGestor.getInstance().updateData(newTables);
		 }
		 catch (Exception e) {}
		 setTables();
	}

	@Override
	public void onError(String error) {
	} 
	
	public List<JoinedTable> toQuit() {
		if (this.previouTablesSelected == null) 
			return null;
		else {
			List<JoinedTable> tablesToRemove = null;
			Iterator<JoinedTable> it = this.previouTablesSelected.iterator();
			while (it.hasNext()) {
				JoinedTable previousTable = it.next();
				Iterator<JoinedTable> it2 = this.selectedTables.iterator();
				boolean found = false;
				while (it2.hasNext() && !found) {
					if (it2.next().getTableId() == previousTable.getTableId()) {
						found=true;
					}
				}
				if (found != true ) {
					if (tablesToRemove == null) {
						tablesToRemove = new ArrayList<Table.JoinedTable>();		
					}
					tablesToRemove.add(previousTable);
				}
			}
			return tablesToRemove;
		}
	}
	
	public List<JoinedTable> toAdd() {
		if (this.selectedTables == null) 
			return this.selectedTables;
		else {
			
			if (this.previouTablesSelected == null) {
				return this.selectedTables;
			}
			else {
				List<JoinedTable> tablesToaAdd = null;
				Iterator<JoinedTable> it = this.selectedTables.iterator();
			
				while (it.hasNext()) {
					JoinedTable selectedTable = it.next();
					Iterator<JoinedTable> it2 = this.previouTablesSelected.iterator();
					boolean found = false;
					while (it2.hasNext() && !found) {
						if (it2.next().getTableId() == selectedTable.getTableId()) {
							found=true;
						}
					}
					if (found != true ) {
						if (tablesToaAdd == null) {
							tablesToaAdd = new ArrayList<Table.JoinedTable>();		
						}
						tablesToaAdd.add(selectedTable);
					}
				}
				return tablesToaAdd;
			}
		}
	}

}
