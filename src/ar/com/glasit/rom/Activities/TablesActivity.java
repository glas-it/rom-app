package ar.com.glasit.rom.Activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.*;
import ar.com.glasit.rom.Fragments.KitchenFragment;
import ar.com.glasit.rom.Fragments.TablesFragment;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Model.*;
import ar.com.glasit.rom.Service.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.view.ViewPager;
import android.content.Context;
import android.os.Bundle;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.view.MenuItem;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

public class TablesActivity extends SherlockFragmentActivity implements ServiceListener{

    public static final String KEY_TAB = "KEY_TAB";
    public static List<String> notif = new Vector<String>();
    private ActionBar actionBar;
    private ViewPager viewPager;
    private TabsAdapter viewPagerAdapter;
    private int currentTab = 0;
    private boolean updating = false;

    private ServiceListener serviceListenerOrders = new ServiceListener() {
        @Override
        public void onServiceCompleted(IServiceRequest request, ServiceResponse serviceResponse) {
            try {
                JSONArray orders = serviceResponse.getJsonArray();
                for(int i=0;i<orders.length();i++){
                    Order order = Order.buildOrder(orders.getJSONObject(i));
                    if (order.getStatus().equals(Order.Status.DONE) &&
                            order.getUser().equals(BackendHelper.getLoggedUser())
                            && !notif.contains(order.getId())){
                        notif.add(order.getId());
                        Intent intent = new Intent();
                        PendingIntent pIntent = PendingIntent.getActivity(TablesActivity.this, 0, intent, 0);
                        NotificationCompat.Builder noti = new NotificationCompat.Builder(TablesActivity.this)
                                .setTicker(order.toString())
                                .setContentTitle(order.getTableNumber())
                                .setContentText(order.toString())
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentIntent(pIntent);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(0, noti.getNotification());
                    }
                }
                updating = false;
            } catch (Exception e) {
            }
        }

        @Override
        public void onError(String error) {

        }
    };

    ServiceListener serviceListenerMenu = new ServiceListener() {
        @Override
        public void onServiceCompleted(IServiceRequest request, ServiceResponse obj) {
            Menu.getInstance().update(obj.getJsonArray());
            RestService.callGetService(serviceListenerOrders, WellKnownMethods.GetOrders, null);
        }

        @Override
        public void onError(String error) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
        actionBar = getSupportActionBar();
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(onPageChangeListener);
        if (savedInstanceState != null) {
            currentTab = savedInstanceState.getInt(TablesActivity.KEY_TAB, 0);
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
                                    updating = true;
                                    if (Menu.getInstance().getChildrenCount() > 0) {
                                        RestService.callGetService(serviceListenerOrders, WellKnownMethods.GetOrders, null);
                                    } else {
                                        RestService.callGetService(serviceListenerMenu, WellKnownMethods.GetMenu, null);
                                    }
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
        }
    };

    private void addActionBarTabs() {
        viewPagerAdapter = new TabsAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        int[] tabs = {
                R.string.my_tables,
                R.string.free_tables,
                R.string.all_tables
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
            fragments.add(new TablesFragment(TablesFragment.Type.MINE));
            fragments.add(new TablesFragment(TablesFragment.Type.FREE));
            fragments.add(new TablesFragment(TablesFragment.Type.ALL));
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
                RestService.callGetService(this, WellKnownMethods.GetTables, null);
                break;
            case R.id.close_Session:
                BackendHelper.setLoggedUser("");
                Intent intent = new Intent(this, StartSessionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
            List<Table> newTables =  new Vector<Table>();
            JSONArray tables = serviceResponse.getJsonArray();
            for(int i=0;i<tables.length();i++){
                Table t = Table.buildTable(tables.getJSONObject(i));
                if (t != null) {
                    newTables.add(t);
                }
            }
            updating= false;
            TablesGestor.getInstance().updateData(newTables);
            for (Fragment f: viewPagerAdapter.getFragments()) {
                TablesFragment tF = (TablesFragment) f;
                tF.setTables();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onError(String error) {
    }

    protected synchronized void obtainData() {
        if (!updating && TablesGestor.getInstance().getAllTables().isEmpty()) {
            updating = true;
            RestService.callGetService(this, WellKnownMethods.GetTables, null);
        }
    }
}