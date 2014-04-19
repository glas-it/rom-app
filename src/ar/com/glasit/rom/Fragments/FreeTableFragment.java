package ar.com.glasit.rom.Fragments;

import com.actionbarsherlock.app.SherlockFragment;

import android.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import ar.com.glasit.rom.Activities.TableDetailActivity;
import ar.com.glasit.rom.Activities.TablesActivity;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.Model.TablesGestor;

public class FreeTableFragment extends SherlockFragment{

	TextView txtmozo;
    TextView txtcapacity;
    NumberPicker np;
    Button open;
    int tableNumber;
    int tab;
 
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
	        View rootView = inflater.inflate(ar.com.glasit.rom.R.layout.fragment_free_table, container,
	                false);
	        super.onCreate(savedInstanceState);
        
	        
	        tableNumber = ((TableDetailActivity)getActivity()).getTableNumber();
	        tab = ((TableDetailActivity)getActivity()).getTab();
	        
	        open = (Button) rootView.findViewById(ar.com.glasit.rom.R.id.openTable);

	        open.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
				    TablesGestor tb = TablesGestor.getInstance();
				    tb.openTable(tableNumber, np.getValue());
				    Intent intent = new Intent(getActivity().getBaseContext(), TablesActivity.class);
				    intent.putExtra("currentTab", tab);
				    startActivity(intent);
			        getActivity().finish();
			        return;
				} 
	        });
	        
	        Button cancel = (Button) rootView.findViewById(ar.com.glasit.rom.R.id.cancelFreeTable); 
	        cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
				    Intent intent = new Intent(getActivity().getBaseContext(), TablesActivity.class);
				    intent.putExtra("currentTab", tab);
				    startActivity(intent);
			        getActivity().finish();
			        return;
				} 
	        });
	        TablesGestor tgGestor = TablesGestor.getInstance();
	        Table t = tgGestor.getTable(tableNumber);
	        
	        
	        np = (NumberPicker) rootView.findViewById(ar.com.glasit.rom.R.id.numberPicker1);
	        np.setMaxValue((t.getMaximunCapacity()));
	        np.setMinValue(1);
	        np.setValue((t.getMaximunCapacity()));
	       
	        txtcapacity = (TextView) rootView.findViewById(ar.com.glasit.rom.R.id.capacity);
	        txtcapacity.setText(Integer.toString(t.getMaximunCapacity()));
	        
	        return rootView;

	  }
}
