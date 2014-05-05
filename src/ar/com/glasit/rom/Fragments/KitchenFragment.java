package ar.com.glasit.rom.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filterable;
import android.widget.Toast;
import ar.com.glasit.rom.Activities.TableDetailActivity;
import ar.com.glasit.rom.Adapters.OrderAdapter;
import ar.com.glasit.rom.Adapters.TableAdapter;
import ar.com.glasit.rom.Model.Order;
import ar.com.glasit.rom.Model.OrderGestor;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Service.RestService;
import ar.com.glasit.rom.Service.WellKnownMethods;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import java.util.List;
import java.util.Vector;

public class KitchenFragment extends ListSearcherFragment{

    public enum Type {
        PENDANT, DOING, DONE;

        Order.Status getOrderStatus() {
            switch (this) {
                case PENDANT:
                    return Order.Status.PENDANT;
                case DOING:
                    return Order.Status.DOING;
                case DONE:
                    return Order.Status.DONE;
                default:
                    return null;
            }
        }
    }

    protected List<Order> orders = null;
    protected boolean isVisible = true, searching = false;
    protected Type type;

    private GestureDetector gestureDetector;

    public KitchenFragment(Type type){
        this(new Vector<Order>(),type);
    }

    public KitchenFragment(List<Order>orders, Type type){
        super();
        this.type = type;
        this.orders = orders;
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        setOrders();
    }

    @Override
    public void onStop() {
        super.onStop();
        isVisible = false;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setBackgroundColor(Color.TRANSPARENT);
        getListView().setCacheColorHint(Color.TRANSPARENT);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        REL_SWIPE_MIN_DISTANCE = (int)(120.0f * dm.densityDpi / 160.0f + 0.5);
        REL_SWIPE_MAX_OFF_PATH = (int)(250.0f * dm.densityDpi / 160.0f + 0.5);
        REL_SWIPE_THRESHOLD_VELOCITY = (int)(200.0f * dm.densityDpi / 160.0f + 0.5);
        gestureDetector = new GestureDetector(getSherlockActivity(), new MyGestureDetector());
        getListView().setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(R.string.empty_orders);
        setListShown(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            ((Filterable) getListAdapter()).getFilter().filter(query);
        } catch (NullPointerException ex) {}
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            ((Filterable) getListAdapter()).getFilter().filter(newText);
        } catch (NullPointerException ex) {}
        return false;
    }

    public void setOrders(){
        setListShown(true);
        this.orders = OrderGestor.getInstance().getOrders(type.getOrderStatus());
        setListAdapter(new OrderAdapter(orders));
    }

    public void refreshOrders(){
        this.orders = OrderGestor.getInstance().getOrders(type.getOrderStatus());
        setListAdapter(new OrderAdapter(orders));
    }

    @Override
    protected void inflateMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
    }

    public void showMessage(String msg) {
        if (isVisible) {
            Toast.makeText(getSherlockActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void myOnItemClick(int position) {
    }

    private void onLTRFling(int position) {
        try {
            Order o = (Order) getListAdapter().getItem(position);
            o.stageCompleted();
            setListShown(false);
        } catch (ArrayIndexOutOfBoundsException e){}
    }

    private void onRTLFling(int position) {
        if (this.type == Type.PENDANT) {
            final Order o = (Order) getListAdapter().getItem(position);
            AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
            alert.setMessage(R.string.cancelOrderConfirmation);
            alert.setPositiveButton("Ok", new CancelOrder(o));
            alert.setNegativeButton("Cancel", null);
            alert.show();
        }
    }

    public class CancelOrder implements DialogInterface.OnClickListener {
        Order o;

        CancelOrder(Order o) {
            this.o = o;
        }
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            this.o.cancel();
            setListShown(false);
            setOrders();
        }
    }
    private int REL_SWIPE_MIN_DISTANCE;
    private int REL_SWIPE_MAX_OFF_PATH;
    private int REL_SWIPE_THRESHOLD_VELOCITY;

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int pos = getListView().pointToPosition((int)e.getX(), (int)e.getY());
            myOnItemClick(pos);
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH)
                return false;
            int position = getListView().pointToPosition(Math.round(e1.getX()), Math.round(e1.getY()));
            if(e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE &&
                    Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {
                onRTLFling(position);
            }  else if (e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE &&
                    Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {
                onLTRFling(position);
            }
            return false;
        }
    }
}
