package ar.com.glasit.rom.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import ar.com.glasit.rom.Model.OpenTable;
import ar.com.glasit.rom.Model.TablesGestor;
import ar.com.glasit.rom.Activities.TableDetailActivity;


import com.actionbarsherlock.app.SherlockFragment;;

public class OpenTableFragment extends SherlockFragment {

	private TextView txtmozo;
    private TextView txtcapacity;
    private TextView txtcomensales;
    private Button close;
    private Button edit;
    private Button menu;
    private int tableNumber;
    private int tab;
 
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
	        View rootView = inflater.inflate(ar.com.glasit.rom.R.layout.fragment_open_table, container,
	                false);
	        super.onCreate(savedInstanceState);
      
	        
	        tableNumber = ((TableDetailActivity)getActivity()).getTableNumber();
	        tab = ((TableDetailActivity)getActivity()).getTab();
	        
	        close = (Button) rootView.findViewById(ar.com.glasit.rom.R.id.closeTable);

	        close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//TODO:
				} 
	        });
	        
	        Button edit = (Button) rootView.findViewById(ar.com.glasit.rom.R.id.editTable); 
	        edit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//TODO:
			        return;
				} 
	        });
	        
	        Button menu = (Button) rootView.findViewById(ar.com.glasit.rom.R.id.menuTable); 
	        menu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//TODO:
			        return;
				} 
	        });
	        
	        TablesGestor tgGestor = TablesGestor.getInstance();
	        OpenTable t = (OpenTable)tgGestor.getTable(tableNumber);
	        
	       
	        txtcapacity = (TextView) rootView.findViewById(ar.com.glasit.rom.R.id.capacity);
	        txtcapacity.setText(Integer.toString(t.getMaximunCapacity()));
	        txtmozo = (TextView) rootView.findViewById(ar.com.glasit.rom.R.id.mozo);
	        txtmozo.setText(t.getWaiter());
	        txtcomensales = (TextView) rootView.findViewById(ar.com.glasit.rom.R.id.cubiertos);
	        txtcomensales.setText(Integer.toString(t.getFellowDiner()));
	       
	        initOrder(rootView, t);
	        return rootView;
	  }

	  private String[][] data(){
		  String[][] pedido = new String[3][3];
		  pedido[0][0]="Milanesa napolitana con fritas";
		  pedido[0][1]= "1";
		  pedido[0][2]= "$45";
		  pedido[1][0]="Agua mineral";
		  pedido[1][1]= "2";
		  pedido[1][2]= "$10";
		  pedido[2][0]="Tortilla de papa";
		  pedido[2][1]= "1";
		  pedido[2][2]= "$30";
		  return pedido;
	  }
	
	  private void initOrder(View rootView,OpenTable t) {
		
	     TableLayout tl=(TableLayout)rootView.findViewById(ar.com.glasit.rom.R.id.TableLayout01);
	     String[][] pedidos = data();
	     int i = 0;
	     while(pedidos.length != i) {
	    	
	    	LayoutInflater inflater = LayoutInflater.from(getSherlockActivity());
	     	TableRow row  = (TableRow) inflater.inflate(ar.com.glasit.rom.R.layout.order_item_table, null,false);
	     	
	     	ImageButton plus = (ImageButton) row.findViewById(ar.com.glasit.rom.R.id.plus);
	     	plus.setOnClickListener(new OnClickListener() {
		    	 
				@Override
				public void onClick(View v) {
	 
		            
		            TableRow row = (TableRow) v.getParent();
		           
		            TextView cantidad = (TextView)row.findViewById(ar.com.glasit.rom.R.id.cantidad);
		            int cant = Integer.parseInt(cantidad.getText().toString());
		            if (cant == 1) {
		            	ImageButton minus = (ImageButton) row.findViewById(ar.com.glasit.rom.R.id.minus);
		            	minus.setEnabled(true);
		            }
		            cant++;
		            cantidad.setText(Integer.toString(cant));

		            row.invalidate();

				}});
	     
	     	ImageButton minus = (ImageButton) row.findViewById(ar.com.glasit.rom.R.id.minus);
	     	
	     	minus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	            TableRow row = (TableRow) v.getParent();
		           
	            TextView cantidad = (TextView)row.findViewById(ar.com.glasit.rom.R.id.cantidad);
	            int cant = Integer.parseInt(cantidad.getText().toString());
	            if (cant == 2) {
	            	ImageButton minus = (ImageButton) row.findViewById(ar.com.glasit.rom.R.id.minus);
	            	minus.setEnabled(false);
	            }
	            cant--;
	            cantidad.setText(Integer.toString(cant));

	            row.invalidate();
			}
		});
	     
	     
	     String name = pedidos[i][0];
	     String number = pedidos[i][1]; 
	     String value = pedidos[i][2];
	     
	     TextView tvConsumitionName= (TextView) row.findViewById(ar.com.glasit.rom.R.id.nombre);
	     tvConsumitionName.setText(name);
	     TextView tvNumber= (TextView) row.findViewById(ar.com.glasit.rom.R.id.cantidad);
	     tvNumber.setText(number);
	     if (Integer.parseInt(number) == 1) {
	    	 minus.setEnabled(false);
	     }
	     TextView tvValue= (TextView) row.findViewById(ar.com.glasit.rom.R.id.precio);
	     tvValue.setText(value);
	     
	     ImageButton delete = (ImageButton) row.findViewById(ar.com.glasit.rom.R.id.delete);
	     delete.setOnClickListener(new OnClickListener() {
	    	 
					@Override
					public void onClick(View v) {
			            View row = (View) v.getParent();
			            ViewGroup container = ((ViewGroup)row.getParent());
			            container.removeView(row);
			            container.invalidate();
		 
					}});

	     tl.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
	     i++;
	     }        
	
	}
	  
}