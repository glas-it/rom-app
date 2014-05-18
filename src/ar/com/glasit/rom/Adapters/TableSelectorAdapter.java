package ar.com.glasit.rom.Adapters;


import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Fragments.TablesSelectorFragment.CheckListener;
import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.Model.Table.JoinedTable;

public class TableSelectorAdapter extends BaseAdapter implements Filterable {
	 List<JoinedTable> tables;
	 List<JoinedTable> filteredData;
	 Table originalTable;
	 private CheckListener checkListener;
	 private GridView gridView;
	 
	 public TableSelectorAdapter(List<JoinedTable> tables, Table table){
	    this.tables = tables;
	    this.filteredData = tables;
	    this.originalTable = table;
	  //  this.previousSelected = originalTable.getJoinedTables();
	 //   this.initWithPreviousSelection();
	 }
	
	 @Override
	 public int getCount() {
		 return this.filteredData.size();
	
	 }
	
	 @Override
	 public Object getItem(int position) {
		 return this.filteredData.get(position);
	 }
	
	 @Override
	 public long getItemId(int position) {
		return this.filteredData.get(position).getTableNumber();
	 }
	 
	 @Override
	 public View getView( final int position, View convertView, ViewGroup parent) {
	        View rowView = convertView;

	        if (convertView == null) {
	            LayoutInflater inflater = (LayoutInflater) ContextHelper.getContextInstance()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            rowView = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
	        }

	        if (filteredData != null && filteredData.size() > 0){

	        	final CheckedTextView ctv = (CheckedTextView) rowView.findViewById(android.R.id.text1);
	            ctv.setTextColor(Color.BLACK);
	            ctv.setChecked(true);
	            String tableWithCapacity = Integer.toString(filteredData.get(position).getTableNumber());
	            tableWithCapacity += " (";
	            tableWithCapacity += Integer.toString(filteredData.get(position).getCapacity());
	            tableWithCapacity += "p)";
	            ctv.setText(tableWithCapacity);
	            ctv.setGravity(Gravity.CENTER);
	            ctv.setBackgroundResource(R.drawable.grid_item_borders);
	        	ctv.setOnClickListener(new View.OnClickListener() {
	        	        @Override
	        	        public void onClick(View v) {
	        	            if (ctv.isChecked()) {
	        	            	gridView.setItemChecked(position, false);
	        	                checkListener.unChecked((JoinedTable) getItem(position));
	        	            }
	        	            else {
	        	            	gridView.setItemChecked(position, true);
	        	                checkListener.checked((JoinedTable) getItem(position));
	        	            }
	        	        }
	        	    });	 
	        }
	        return rowView;
	 }
		
	 @Override
	 public Filter getFilter() {
		 return itemFilter;
	 }
	 
	 private Filter itemFilter = new Filter() {
	        @Override
	        protected FilterResults performFiltering(CharSequence constraint) {
	            String filterString = constraint.toString().toLowerCase();
	            FilterResults results = new FilterResults();

	            List<JoinedTable> nList;
	            if (!filterString.trim().isEmpty()){
	                nList = new Vector<JoinedTable>();
	                for (JoinedTable it: tables) {
	                    if (Integer.toString(it.getTableNumber()).toLowerCase().contains(filterString)) {
	                        nList.add(it);
	                    }
	                }
	            } else {
	                nList = tables;
	            }
	            results.values = nList;
	            results.count = nList.size();

	            return results;
	   }
	        @Override
	        protected void publishResults(CharSequence constraint, FilterResults results) {
	            filteredData = (List<JoinedTable>) results.values;
	            notifyDataSetChanged();
	        }
	 };

	public void setCheckListener(CheckListener checkListener) {
		this.checkListener= checkListener;
		
	}

	public void setView(GridView g) {
		this.gridView  = g;
	}
//	
//	public void initWithPreviousSelection() {
//         if ( (previousSelected == null) || (previousSelected.size()==0))
//        	 return;
//         else {
//        	 int i = 0;
//    		 while ( i < this.getCount()) {
//    			 Table t = (Table) getItem(i);
//    			 String stringNumber = Integer.toString(t.getNumber());
//    			 if (previousSelected.contains(stringNumber)) {
//    				 this.gridView.setItemChecked(i,true);
//    				 checkListener.checked((Table) getItem(i));
//    			 }
//				 i++;
//    		 }
//    		 this.notifyDataSetChanged();
//         }
//	}
	
	
}
