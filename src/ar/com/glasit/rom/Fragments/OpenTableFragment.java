package ar.com.glasit.rom.Fragments;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;
import ar.com.glasit.rom.Activities.MenuActivity;
import ar.com.glasit.rom.Dialogs.OrderDialog;
import ar.com.glasit.rom.Model.*;

import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Service.RestService;
import ar.com.glasit.rom.Service.WellKnownMethods;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Vector;

public class OpenTableFragment extends SherlockFragment {

	private TextView txtmozo;
    private TextView txtcapacity;
    private TextView txtcomensales;
    private TextView people;
    private TextView total;

    private TableLayout orders;
    private OpenTable table;
    private TableManager manager;
    private static final int REQUEST_CODE = 0x1;

    public OpenTableFragment(TableManager manager, Table table) {
        this.manager = manager;
        this.table = (OpenTable) table;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(ar.com.glasit.rom.R.layout.fragment_open_table, container,
                false);
        super.onCreate(savedInstanceState);

        txtcapacity = (TextView) rootView.findViewById(ar.com.glasit.rom.R.id.capacity);
        txtcapacity.setText(Integer.toString(table.getMaximunCapacity()));
        txtmozo = (TextView) rootView.findViewById(ar.com.glasit.rom.R.id.mozo);
        txtmozo.setText(table.getWaiter());
        txtcomensales = (TextView) rootView.findViewById(ar.com.glasit.rom.R.id.cubiertos);
        txtcomensales.setText(Integer.toString(table.getFellowDiner()));
        total = (TextView) rootView.findViewById(R.id.total);

        orders = (TableLayout) rootView.findViewById(R.id.orders);
        Button newItem = (Button) rootView.findViewById(R.id.newItem);
        newItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openMenu();
            }
        });

        Button plus = (Button) rootView.findViewById(R.id.plus);
        Button less = (Button) rootView.findViewById(R.id.less);

        people = (TextView) rootView.findViewById(R.id.cubiertos);
        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (table.getFellowDiner() < table.getMaximunCapacity()) {
                    table.setFellowDiner(table.getFellowDiner() + 1);
                    people.setText(Integer.toString(table.getFellowDiner()));
                }
            }
        });
        less.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (table.getFellowDiner() > 1) {
                    table.setFellowDiner(table.getFellowDiner() - 1);
                    people.setText(Integer.toString(table.getFellowDiner()));
                }
            }
        });

        initOrder();
        return rootView;
    }

    private void openMenu() {
        Intent intent = new Intent(getSherlockActivity(), MenuActivity.class);
        intent.putExtra("tableNumber", table.getNumber());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == MenuActivity.RESULT_OK) {
            List<Order> list = table.getOrderRequest();
            addRow(list.get(list.size() - 1));
        }
    }

    private class RowRemover implements View.OnLongClickListener {
        private TableLayout list;
        private TableRow rowToBeRemoved;
        private Order order;

        public RowRemover(TableLayout list, TableRow row, Order o) {
            this.list = list;
            this.order = o;
            this.rowToBeRemoved = row;
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
            alert.setMessage(R.string.deleteItemConfirmation);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    list.removeView(rowToBeRemoved);
                    table.removeOrder(order);
                    total.setText("$ " + Float.toString(table.getPrice()));
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.show();
            return true;
        }
    }

    private class RowAdd implements View.OnClickListener {
        private TableLayout list;
        private Order order;
        private TableRow rowToBeModified;

        public RowAdd(TableLayout list, TableRow row, Order o) {
            this.list = list;
            this.rowToBeModified = row;
            this.order = o;
        }

        public void onClick(View view) {
            order.setCount(order.getCount() + 1);
            TextView amount = (TextView) rowToBeModified.findViewById(R.id.amount);
            amount.setText(Integer.toString(order.getCount()));
            TextView price = (TextView) rowToBeModified.findViewById(R.id.price);
            price.setText("$ " + order.getPrice());

            total.setText("$ " + Float.toString(table.getPrice()));
        }
    }

    private class RowRest implements View.OnClickListener {
        private TableLayout list;
        private Order order;
        private TableRow rowToBeModified;

        public RowRest(TableLayout list, TableRow row, Order order) {
            this.list = list;
            this.order = order;
            this.rowToBeModified = row;
        }

        public void onClick(View view) {
            if (order.getCount() > 1) {
                order.setCount(order.getCount() - 1);
                TextView amount = (TextView) rowToBeModified.findViewById(R.id.amount);
                amount.setText(Integer.toString(order.getCount()));
                TextView price = (TextView) rowToBeModified.findViewById(R.id.price);
                price.setText("$ " + order.getPrice());
                total.setText("$ " + Float.toString(table.getPrice()));
            }
        }
    }

    private class RowInfo implements View.OnLongClickListener {
        private TableLayout list;
        private TableRow rowToBeModified;
        private Order order;

        public RowInfo(TableLayout list, TableRow row, Order o) {
            this.list = list;
            this.order = o;
            this.rowToBeModified = row;
        }

        @Override
        public boolean onLongClick(View v) {
            if (order.isPendant()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
                alert.setMessage(R.string.cancelOrderConfirmation);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        order.setStatus(Order.Status.CANCELLED);
                        rowToBeModified.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                        TextView price = (TextView) rowToBeModified.findViewById(R.id.price);
                        price.setText("$ " + order.getPrice());
                        total.setText("$ " + Float.toString(table.getPrice()));
                        List<NameValuePair> param = new Vector<NameValuePair>();
                        param.add(new BasicNameValuePair("uuidOrden", order.getId()));
                        RestService.callPostService(null, WellKnownMethods.OrderCancel, param);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            } else if (order.isConcluded()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
                alert.setMessage(R.string.deliveredOrderConfirmation);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        order.setStatus(Order.Status.CANCELLED);
                        rowToBeModified.findViewById(R.id.image).setVisibility(View.VISIBLE);
                        List<NameValuePair> param = new Vector<NameValuePair>();
                        param.add(new BasicNameValuePair("uuidOrden", order.getId()));
                        RestService.callPostService(null, WellKnownMethods.OrderDelivered, param);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            } else if (order.isDelivered()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
                alert.setMessage(R.string.rejectOrderConfirmation);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        order.setStatus(Order.Status.CANCELLED);
                        rowToBeModified.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                        TextView price = (TextView) rowToBeModified.findViewById(R.id.price);
                        price.setText("$ " + order.getPrice());
                        total.setText("$ " + Float.toString(table.getPrice()));
                        List<NameValuePair> param = new Vector<NameValuePair>();
                        param.add(new BasicNameValuePair("uuidOrden", order.getId()));
                        RestService.callPostService(null, WellKnownMethods.OrderRejected, param);
                        OpenTable table = (OpenTable) TablesGestor.getInstance().getTable(Integer.parseInt(order.getTableNumber()));
                        Order o = order.clone2();
                        table.addOrder(o);
                        addRow(o);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            }
            return true;
        }
    }

    private void addRow(Order order) {
        LayoutInflater inflater = getLayoutInflater(null);
        TableRow newRow = (TableRow) inflater.inflate(R.layout.order_item_table, orders, false);

        LinearLayout add = (LinearLayout) newRow.findViewById(R.id.add);
        LinearLayout rest = (LinearLayout) newRow.findViewById(R.id.rest);
        if (order.isLocal()) {
            add.setOnClickListener(new RowAdd(orders, newRow ,order));
            add.setOnLongClickListener(new RowRemover(orders, newRow, order));
            rest.setOnClickListener(new RowRest(orders, newRow, order));
            rest.setOnLongClickListener(new RowRemover(orders, newRow, order));
        } else {
            add.setOnLongClickListener(new RowInfo(orders, newRow, order));
            rest.setOnLongClickListener(new RowInfo(orders, newRow, order));
        }
        TextView text = (TextView) newRow.findViewById(R.id.name);
        text.setText(order.toString());
        TextView amount = (TextView) newRow.findViewById(R.id.amount);
        amount.setText(Integer.toString(order.getCount()));
        TextView txtPrice = (TextView) newRow.findViewById(R.id.price);
        txtPrice .setText("$ " + order.getPrice());

        if (order.getStatus().equals(Order.Status.PENDANT)) {
            newRow.setBackgroundColor(getResources().getColor(R.color.dark_red));
        } else if (order.getStatus().equals(Order.Status.DOING)) {
            newRow.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
        } else if (order.getStatus().equals(Order.Status.DONE)) {
            newRow.setBackgroundColor(getResources().getColor(R.color.dark_green));
        } else if (order.getStatus().equals(Order.Status.CANCELLED)) {
            newRow.setBackgroundColor(getResources().getColor(R.color.dark_gray));
        } else if (order.getStatus().equals(Order.Status.DELIVERED)) {
            newRow.setBackgroundColor(getResources().getColor(R.color.dark_green));
            newRow.findViewById(R.id.image).setVisibility(View.VISIBLE);
        };

        total.setText(Float.toString(table.getPrice()));
        orders.addView(newRow);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.open_table, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_order:
                manager.onTableOrder(table);
                getSherlockActivity().onBackPressed();
                break;
            case R.id.close_table:
                TablesGestor.getInstance().closeTable(table.getNumber());
                manager.onTableClosed((FreeTable) TablesGestor.getInstance().getTable(table.getNumber()));
                getSherlockActivity().onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void initOrder() {
        for (Order o : table.getOrderRequest()) {
            addRow(o);
        }
    }
}