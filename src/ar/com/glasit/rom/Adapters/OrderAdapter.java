package ar.com.glasit.rom.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.Model.Order;
import ar.com.glasit.rom.R;

import java.util.List;
import java.util.Vector;

public class OrderAdapter extends BaseAdapter implements Filterable {

    List<Order> Orders;
    List<Order> filteredData;

    public OrderAdapter(List<Order> Orders){
        this.Orders = Orders;
        this.filteredData = Orders;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) ContextHelper.getContextInstance()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.listview_order_item, parent, false);
        }

        if (filteredData != null && filteredData.size() > 0){
            TextView table = (TextView) rowView.findViewById(R.id.text1);
            table.setTextColor(Color.BLACK);
            table.setText(filteredData.get(position).getTableNumber());
            TextView description = (TextView) rowView.findViewById(R.id.text2);
            description.setTextColor(Color.BLACK);
            description.setText(filteredData.get(position).toString());
            TextView time = (TextView) rowView.findViewById(R.id.text3);
            time.setTextColor(Color.BLACK);
            time.setText(filteredData.get(position).getTime());
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

            List<Order> nList;
            if (!filterString.trim().isEmpty()){
                nList = new Vector<Order>();
                for (Order it: Orders) {
                    if (it.toString().toLowerCase().contains(filterString)) {
                        nList.add(it);
                    }
                }
            } else {
                nList = Orders;
            }
            results.values = nList;
            results.count = nList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (List<Order>) results.values;
            notifyDataSetChanged();
        }
    };
}
