package ar.com.glasit.rom.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.Model.Menu;

public class MenuAdapter extends BaseAdapter {

    Menu menu;

    public MenuAdapter(Menu menu){
        this.menu = menu;
    }

    @Override
    public int getCount() {
        return this.menu.getCountInPage();
    }

    @Override
    public Object getItem(int position) {
        return this.menu.getItemInPage(position);
    }

    @Override
    public long getItemId(int position) {
        return this.menu.getItemInPage(position).getId();
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

        TextView desciption = (TextView) rowView.findViewById(android.R.id.text1);
        desciption.setText(menu.getItemInPage(position).toString());
        return rowView;
    }
}
