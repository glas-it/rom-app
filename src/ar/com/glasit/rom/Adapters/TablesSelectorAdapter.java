
package ar.com.glasit.rom.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import ar.com.glasit.rom.Fragments.TablesSelectorFragment.CheckListener;
import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.Model.Table.JoinedTable;
import ar.com.glasit.rom.R;

import com.actionbarsherlock.app.ActionBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class TablesSelectorAdapter extends BaseAdapter implements Filterable {

    private List<JoinedTable> tables;
    private List<JoinedTable> filteredData;
    private Table table;
	private CheckListener checkListener;
	private ListView listView;
    private List<String> previousSelection;

    public TablesSelectorAdapter(List<JoinedTable> tables, Table table, List<JoinedTable> previous){
        this.tables = tables;
        this.filteredData = tables;
        this.table=table;
        init(previous);
    }

    private void init(List<JoinedTable> previousSelected) {
		if ( (previousSelected == null) || (previousSelected.size()==0))
			return;
		else {
			Iterator<JoinedTable> it  = previousSelected.iterator();
			this.previousSelection = new ArrayList<String>();
			while (it.hasNext()) {
				String stringNumber = Integer.toString(it.next().getTableNumber());
				previousSelection.add(stringNumber);
			}
		}
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ContextHelper.getContextInstance()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(ar.com.glasit.rom.R.layout.tables_selector_item, parent, false);
        }

        if (filteredData != null && filteredData.size() > 0){
            TextView tableNumber = (TextView) rowView.findViewById(R.id.textViewTitle);
            tableNumber.setTextColor(Color.BLACK);
            tableNumber.setText(Integer.toString(filteredData.get(position).getTableNumber()));
            TextView tableCapacity = (TextView) rowView.findViewById(R.id.textViewSubtitle);
            tableCapacity.setTextColor(Color.BLACK);
            tableCapacity.setText(Integer.toString(filteredData.get(position).getCapacity()));
            CheckBox check = (CheckBox) rowView.findViewById(R.id.checkBox);
            check.setOnClickListener(new OnClickListener() {
            	 
          	  @Override
          	  public void onClick(View v) {
          		if (((CheckBox) v).isChecked()) {
         			 checkListener.checked((JoinedTable) getItem(position));
          		}
          		else{
          			checkListener.unChecked((JoinedTable) getItem(position));
          		} 
          	  }
          	});
            
            if (this.previousSelection != null) {
            	if (this.previousSelection.contains(Integer.toString(filteredData.get(position).getTableNumber()))) {
            		check.setChecked(true);
            	}
        }
            
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
		this.checkListener=checkListener;
		
	}

	public void setView(ListView listView) {
		this.listView = listView;
		
	}
}
