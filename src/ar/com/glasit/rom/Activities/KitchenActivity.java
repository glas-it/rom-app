package ar.com.glasit.rom.Activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Toast;
import ar.com.glasit.rom.Fragments.KitchenFragment;
import ar.com.glasit.rom.Fragments.TablesFragment;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Model.Menu;
import ar.com.glasit.rom.Model.Order;
import ar.com.glasit.rom.Model.OrderGestor;
import ar.com.glasit.rom.Model.TablesGestor;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Service.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class KitchenActivity extends SherlockFragmentActivity implements ServiceListener{

    public static final String KEY_TAB = "KEY_TAB";

    public static int index = 0;
    private ActionBar actionBar;
    private ViewPager viewPager;
    private TabsAdapter viewPagerAdapter;
    private int currentTab = 0;
    private boolean updating = false;

    private ServiceListener notificationsListener = new ServiceListener() {
        @Override
        public void onServiceCompleted(IServiceRequest request, ServiceResponse serviceResponse) {
            try {
                JSONArray notifications = serviceResponse.getJsonArray();
                for(int i=0;i<notifications.length();i++){
                    JSONObject noti = notifications.getJSONObject(i);
                    Intent intent = new Intent();
                    PendingIntent pIntent = PendingIntent.getActivity(KitchenActivity.this, 0, intent, 0);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(KitchenActivity.this)
                            .setTicker("Novedades " + noti.getString("titulo"))
                            .setContentTitle(noti.getString("titulo"))
                            .setContentText(noti.getString("mensaje"))
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentIntent(pIntent)
                            .setAutoCancel(true);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(index++, notificationBuilder.getNotification());
                }
                updating = false;
                index %= 50;
            } catch (Exception e) {
            }
        }

        @Override
        public void onError(String error) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        actionBar = getSupportActionBar();
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(onPageChangeListener);
        if (savedInstanceState != null) {
            currentTab = savedInstanceState.getInt(KitchenActivity.KEY_TAB, 0);
        }
        addActionBarTabs();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!updating){
                                    try {
                                        for (Fragment f: viewPagerAdapter.getFragments()) {
                                            ((KitchenFragment) f).refreshOrders();
                                        }
                                    }catch (Exception e){
                                    }
                                    List<NameValuePair> params = new Vector<NameValuePair>();
                                    params.add(new BasicNameValuePair("username", BackendHelper.getLoggedUser()));
                                    RestService.callGetService(KitchenActivity.this, WellKnownMethods.GetOrders, params);
                                    params.add(new BasicNameValuePair("idRestaurant", BackendHelper.getSecretKey()));
                                    RestService.callGetService(notificationsListener, WellKnownMethods.GetNotifications, params);
                                    updating = true;
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    private ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            actionBar.setSelectedNavigationItem(position);
            try {
                for (Fragment f: viewPagerAdapter.getFragments()) {
                    ((KitchenFragment) f).refreshOrders();
                }
            }catch (Exception e){

            }
        }
    };

    private void addActionBarTabs() {
        viewPagerAdapter = new TabsAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        int[] tabs = {
                R.string.pendant,
                R.string.doing,
                R.string.done
        };
        for (int tabTitle : tabs) {
            ActionBar.Tab tab = actionBar.newTab();
            tab.setText(tabTitle);
            tab.setTabListener(tabListener);
            actionBar.addTab(tab);
        }
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setSelectedNavigationItem(currentTab);
    }

    private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TAB, getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtainData();
    }

    public static class TabsAdapter extends FragmentStatePagerAdapter {
        private Context context;
        private List<Fragment> fragments;

        public TabsAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
            this.fragments = new ArrayList<Fragment>();
            KitchenFragment mine  = new KitchenFragment();
            Bundle b = new Bundle();
            b.putInt("type", 0);
            mine.setArguments(b);
            fragments.add(mine);

            KitchenFragment free  = new KitchenFragment();
            Bundle b1 = new Bundle();
            b1.putInt("type", 1);
            free.setArguments(b1);
            fragments.add(free);

            KitchenFragment all  = new KitchenFragment();
            Bundle b2 = new Bundle();
            b2.putInt("type", 2);
            all.setArguments(b2);
            fragments.add(all);
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public List<Fragment> getFragments() {
            return this.fragments;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                List<NameValuePair> params = new Vector<NameValuePair>();
                params.add(new BasicNameValuePair("username", BackendHelper.getLoggedUser()));
                RestService.callGetService(this, WellKnownMethods.GetOrders, params);
                break;
            case R.id.close_Session:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onServiceCompleted(IServiceRequest request, ServiceResponse serviceResponse) {
        try {
            List<Order> newOrder =  new Vector<Order>();
            JSONArray orders = serviceResponse.getJsonArray();
            for(int i=0;i<orders.length();i++){
                Order order = Order.buildOrder(orders.getJSONObject(i));
                if (order != null) {
                    newOrder.add(order);
                }
            }
            OrderGestor.getInstance().updateData(newOrder);
            updating= false;
            updateUIs();
        } catch (Exception e) {
        }
    }

    @Override
    public void onError(String error) {
    }

    protected synchronized void obtainData() {
        if (!updating && TablesGestor.getInstance().getAllTables().isEmpty()) {
            updating = true;
            if (Menu.getInstance().getChildrenCount() > 0) {
                List<NameValuePair> params = new Vector<NameValuePair>();
                params.add(new BasicNameValuePair("username", BackendHelper.getLoggedUser()));
                RestService.callGetService(this, WellKnownMethods.GetOrders, params);
            } else {
                RestService.callGetService(serviceListener, WellKnownMethods.GetMenu, null);
            }
        }
    }

    ServiceListener serviceListener = new ServiceListener() {
        @Override
        public void onServiceCompleted(IServiceRequest request, ServiceResponse obj) {
            Menu.getInstance().update(obj.getJsonArray());
            List<NameValuePair> params = new Vector<NameValuePair>();
            params.add(new BasicNameValuePair("username", BackendHelper.getLoggedUser()));
            RestService.callGetService(KitchenActivity.this, WellKnownMethods.GetOrders, params);
        }

        @Override
        public void onError(String error) {
        }
    };

    public void updateUIs() {
        for (Fragment f: viewPagerAdapter.getFragments()) {
            try{
                ((KitchenFragment) f).setOrders();
            } catch (Exception e){}
        }
    }
}