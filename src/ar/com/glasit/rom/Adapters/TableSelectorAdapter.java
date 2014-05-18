package ar.com.glasit.rom.Adapters;


import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ar.com.glasit.rom.Model.JoinedTable;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Fragments.TablesSelectorFragment.CheckListener;
import ar.com.glasit.rom.Helpers.ContextHelper;

public class TableSelectorAdapter extends BaseAdapter implements Filterable {
	 List<JoinedTable> tables;
	 List<JoinedTable> filteredData;
	 List<JoinedTable> joinedTables;
	 private CheckListener checkListener;

    public TableSelectorAdapter(List<JoinedTable> tables, List<JoinedTable> joinedTables){
	    this.tables = new Vector<JoinedTable>(tables);
        this.joinedTables = joinedTables;
        for (JoinedTable joinedTable: joinedTables) {
            this.tables.add(joinedTable);
        }
        Collections.sort(this.tables);
        this.filteredData = new Vector<JoinedTable>(this.tables);
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
            rowView = inflater.inflate(R.layout.gridview_table_item, parent, false);
        }
        if (filteredData != null && filteredData.size() > 0){
            CheckBox ctv = (CheckBox) rowView.findViewById(R.id.checkBox);
            ctv.setTextColor(Color.BLACK);
            String tableWithCapacity = Integer.toString(filteredData.get(position).getTableNumber());
            tableWithCapacity += " (";
            tableWithCapacity += Integer.toString(filteredData.get(position).getCapacity());
            tableWithCapacity += "p)";
            ctv.setText(tableWithCapacity);
            ctv.setOnClickListener(new TableClickListener(ctv,(JoinedTable) getItem(position)));
            ctv.setChecked(joinedTables.contains(filteredData.get(position)));
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

    private class TableClickListener implements View.OnClickListener{

        CheckBox checkBox;
        JoinedTable table;

        TableClickListener(CheckBox checkBox, JoinedTable table) {
            this.checkBox = checkBox;
            this.table = table;
        }
        @Override
        public void onClick(View v) {
            if (checkBox.isChecked()) {
                checkListener.checked(table);
            } else {
                checkListener.unChecked(table);
            }
        }
    }
}
