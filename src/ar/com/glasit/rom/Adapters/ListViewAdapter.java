package ar.com.glasit.rom.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.Model.TablesGestor;
 
public class ListViewAdapter extends BaseAdapter {
 
    // Declare Variables
    Context context;
    String[] table;
    LayoutInflater inflater;
 
    public ListViewAdapter(Context context, String[] tables) {
        this.context = context;
        this.table = tables;
    }
 
    public int getCount() {
        return table.length;
    }
 
    public Object getItem(int position) {
        return null;
    }
 
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
 
        TextView txtrank;
 
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
 
        txtrank = (TextView) itemView.findViewById(R.id.table);

        TablesGestor tb = TablesGestor.getInstance();
        int nroTable =Integer.parseInt((table[position]));
        Table currentTable = tb.getTable(nroTable);
       
        if (!(currentTable.isEnabled())) {
        	txtrank.setEnabled(false);
        	TextView txtranklab = (TextView) itemView.findViewById(R.id.tablelabel);
        	txtranklab.setEnabled(false);
        }

        txtrank.setText(table[position]);

        return itemView;
    }
    

    
}