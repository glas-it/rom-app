package ar.com.glasit.rom.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.app.ActionBar;

import java.util.List;
import java.util.Vector;

public class TableAdapter extends BaseAdapter implements Filterable {

    List<Table> tables;
    List<Table> filteredData;

    public TableAdapter(List<Table> tables){
        this.tables = tables;
        this.filteredData = tables;
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
        return this.filteredData.get(position).getNumber();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) ContextHelper.getContextInstance()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        if (filteredData != null && filteredData.size() > 0){
            TextView description = (TextView) rowView.findViewById(android.R.id.text1);
            description.setTextColor(Color.BLACK);
            description.setText(Integer.toString(filteredData.get(position).getNumber()));
            description.setGravity(Gravity.CENTER);
            description.setBackgroundResource(R.drawable.grid_item_borders);
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

            List<Table> nList;
            if (!filterString.trim().isEmpty()){
                nList = new Vector<Table>();
                for (Table it: tables) {
                    if (Integer.toString(it.getNumber()).toLowerCase().contains(filterString)) {
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
            filteredData = (List<Table>) results.values;
            notifyDataSetChanged();
        }
    };
}
