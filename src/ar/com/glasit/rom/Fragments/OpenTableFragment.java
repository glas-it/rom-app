package ar.com.glasit.rom.Fragments;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;
import ar.com.glasit.rom.Activities.MenuActivity;
import ar.com.glasit.rom.Activities.TableDetailActivity;
import ar.com.glasit.rom.Dialogs.RejectOrderDialog;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class OpenTableFragment extends SherlockFragment{

    private TextView people;
    private TextView total;

    private TableLayout orders;
    private OpenTable table;
    private static final int REQUEST_CODE = 0x1;

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

        this.table = getTable();
        TextView txtCapacity = (TextView) rootView.findViewById(ar.com.glasit.rom.R.id.capacity);
        txtCapacity.setText(Integer.toString(getTable().getMaximunCapacity()));
        TextView txtWaiter = (TextView) rootView.findViewById(ar.com.glasit.rom.R.id.mozo);
        txtWaiter.setText(getTable().getWaiter());
        TextView txtPeople = (TextView) rootView.findViewById(ar.com.glasit.rom.R.id.cubiertos);
        txtPeople.setText(Integer.toString(getTable().getFellowDiner()));
        total = (TextView) rootView.findViewById(R.id.total);
        orders = (TableLayout) rootView.findViewById(R.id.orders);
        people = (TextView) rootView.findViewById(R.id.cubiertos);
        Button newItem = (Button) rootView.findViewById(R.id.newItem);
        Button plus = (Button) rootView.findViewById(R.id.plus);
        Button less = (Button) rootView.findViewById(R.id.less);
        Button join = (Button) rootView.findViewById(R.id.join);
        if (!getStatus().equals("Cerrado")) {
            newItem.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    openMenu();
                }
            });
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
            join.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableDetailActivity t = (TableDetailActivity)getSherlockActivity();
                    JSONArray json = new JSONArray();
                    for (JoinedTable table : getTable().getJoinedTables()) {
                        JSONObject jsonTable = new JSONObject();
                        try {
                            jsonTable.put("id", table.getTableId());
                            jsonTable.put("numero", table.getTableNumber());
                            jsonTable.put("capacidad", table.getCapacity());
                        } catch (JSONException e) {
                        }
                        json.put(jsonTable);
                    }
                    t.displayTableSelector(json, getTable());
                }
            });
        } else {
            newItem.setVisibility(View.INVISIBLE);
            plus.setVisibility(View.INVISIBLE);
            less.setVisibility(View.INVISIBLE);
            join.setVisibility(View.INVISIBLE);
        }

        initOrder();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView tablesToJoin = (TextView) getView().findViewById(R.id.tablesJoined);
        tablesToJoin.setText(getTable().getJoinedTablesToString());
        TextView maxCapacity = (TextView) getView().findViewById(R.id.capacity);
        maxCapacity.setText(Integer.toString(getTable().getMaximunCapacity()));
    }

    private void openMenu() {
        Intent intent = new Intent(getSherlockActivity(), MenuActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == MenuActivity.RESULT_OK) {
            try {
                Order order = Order.buildOrder(new JSONObject(data.getStringExtra("order")));
                getTable().addOrder(order);
                addRow(order);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                    getTable().removeOrder(order);
                    total.setText("$ " + Float.toString(getTable().getPrice()));
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

            total.setText("$ " + Float.toString(getTable().getPrice()));
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
                total.setText("$ " + Float.toString(getTable().getPrice()));
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
                        ((ImageView)rowToBeModified.findViewById(R.id.image)).setImageResource(R.drawable.semaphore_item_cancelled);
                        TextView price = (TextView) rowToBeModified.findViewById(R.id.price);
                        price.setText("$ " + order.getPrice());
                        total.setText("$ " + Float.toString(getTable().getPrice()));
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
                        order.setStatus(Order.Status.DELIVERED);
                        ((ImageView)rowToBeModified.findViewById(R.id.image)).setImageResource(R.drawable.semaphore_item_delivered);
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
            } else if (order.isDelivered() && !order.isRejected()) {
                RejectOrderDialog dialog = new RejectOrderDialog(order);
                dialog.setOnCancelListener(null);
                dialog.setOnRejectListener(new RejectOrderDialog.OnRejectListener() {
                    @Override
                    public void onReject(Order order, String comment) {
                        order.setStatus(Order.Status.CANCELLED);
                        ((ImageView) rowToBeModified.findViewById(R.id.image)).setImageResource(R.drawable.semaphore_item_cancelled_rejected);
                        TextView price = (TextView) rowToBeModified.findViewById(R.id.price);
                        price.setText("$ " + order.getPrice());
                        total.setText("$ " + Float.toString(getTable().getPrice()));
                        List<NameValuePair> param = new Vector<NameValuePair>();
                        param.add(new BasicNameValuePair("uuidOrden", order.getId()));
                        param.add(new BasicNameValuePair("observaciones", comment));
                        param.add(new BasicNameValuePair("reordenar", "false"));
                        RestService.callPostService(null, WellKnownMethods.OrderRejected, param);
                    }
                });
                dialog.setOnRejectAndReorderListener(new RejectOrderDialog.OnRejectAndReorderListener() {
                    @Override
                    public void onRejectAndReorder(Order order, String comment) {
                        order.setStatus(Order.Status.PENDANT);
                        ((ImageView) rowToBeModified.findViewById(R.id.image)).setImageResource(R.drawable.semaphore_item_pendant_rejected);
                        TextView price = (TextView) rowToBeModified.findViewById(R.id.price);
                        price.setText("$ " + order.getPrice());
                        total.setText("$ " + Float.toString(getTable().getPrice()));
                        List<NameValuePair> param = new Vector<NameValuePair>();
                        param.add(new BasicNameValuePair("uuidOrden", order.getId()));
                        param.add(new BasicNameValuePair("observaciones", comment));
                        param.add(new BasicNameValuePair("reordenar", "true"));
                        RestService.callPostService(null, WellKnownMethods.OrderRejected, param);
                    }
                });
                dialog.show(getSherlockActivity().getSupportFragmentManager(), "");
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

        ImageView semaphore = (ImageView) newRow.findViewById(R.id.image);
        if (order.isRejected()) {
            semaphore.setImageResource(R.drawable.semaphore_item_rejected);
        };

        if (order.getStatus().equals(Order.Status.LOCAL)) {
            if (order.isRejected()) {
                semaphore.setImageResource(R.drawable.semaphore_item_rejected);
            } else {
                semaphore.setImageResource(R.drawable.semaphore_item);
            }
        } else if (order.getStatus().equals(Order.Status.PENDANT)) {
            if (order.isRejected()) {
                semaphore.setImageResource(R.drawable.semaphore_item_pendant_rejected);
            } else {
                semaphore.setImageResource(R.drawable.semaphore_item_pendant);
            }
        } else if (order.getStatus().equals(Order.Status.DOING)) {
            if (order.isRejected()) {
                semaphore.setImageResource(R.drawable.semaphore_item_doing_rejected);
            } else {
                semaphore.setImageResource(R.drawable.semaphore_item_doing);
            }
        } else if (order.getStatus().equals(Order.Status.DONE)) {
            if (order.isRejected()) {
                semaphore.setImageResource(R.drawable.semaphore_item_done_rejected);
            } else {
                semaphore.setImageResource(R.drawable.semaphore_item_done);
            }
        } else if (order.getStatus().equals(Order.Status.CANCELLED)) {
            if (order.isRejected()) {
                semaphore.setImageResource(R.drawable.semaphore_item_cancelled_rejected);
            } else {
                semaphore.setImageResource(R.drawable.semaphore_item_cancelled);
            }
        } else if (order.getStatus().equals(Order.Status.DELIVERED)) {
            if (order.isRejected()) {
                semaphore.setImageResource(R.drawable.semaphore_item_cancelled_rejected);
            } else {
                semaphore.setImageResource(R.drawable.semaphore_item_delivered);
            }
        }

        total.setText(Float.toString(getTable().getPrice()));
        orders.addView(newRow);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        String state = getStatus();
        if (state.equals("Cerrado")) {
            inflater.inflate(R.menu.pay_table, menu);
        } else {
            inflater.inflate(R.menu.open_table, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_order:
                ((TableManager) getSherlockActivity()).onTableOrder(table.getId(), getTable().getOrderRequest());
                break;
            case R.id.close_table:
                if (!getTable().getOrderRequest().isEmpty()) {
                    TablesGestor.getInstance().closeTable(getTable().getNumber());
                }
                ((TableManager) getSherlockActivity()).onTableClosed(getTable().getId());
                break;
            case R.id.item_wait_payment:
                getSherlockActivity().onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void initOrder() {
        for (Order o : getTable().getOrderRequest()) {
            addRow(o);
        }
    }

    private OpenTable getTable() {
        if (this.table != null)
            return this.table;
        try {
            JSONObject json = new JSONObject(getArguments().getString("table"));
            return (this.table = (OpenTable) Table.buildTable(json));
        } catch (JSONException e) {
        }
        return null;
    }

    private String getStatus() {
        try {
            JSONObject json = new JSONObject(getArguments().getString("table"));
            return json.getJSONObject("estado").getString("nombre");
        } catch (JSONException e) {
        }
        return "";
    }
}