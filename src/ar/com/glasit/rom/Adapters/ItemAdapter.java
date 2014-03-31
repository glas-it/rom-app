package ar.com.glasit.rom.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.Model.IItem;

import java.util.List;
import java.util.Vector;

public class ItemAdapter extends BaseAdapter implements Filterable {

    IItem item;
    List<IItem> filteredData;

    public ItemAdapter(IItem menu){
        this.item = menu;
        this.filteredData = menu.getChildren();
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
        return this.filteredData.get(position).getId();
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
            description.setText(filteredData.get(position).toString());
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

            List<IItem> nList;
            if (!filterString.trim().isEmpty()){
                nList = new Vector<IItem>();
                for (IItem it: item.getChildren()) {
                    if (it.toString().toLowerCase().contains(filterString)) {
                        nList.add(it);
                    }
                }
            } else {
                nList = item.getChildren();
            }
            results.values = nList;
            results.count = nList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (List<IItem>) results.values;
            notifyDataSetChanged();
        }
    };
}
