
package ar.com.glasit.rom.Activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.support.v4.app.*;
import ar.com.glasit.rom.Fragments.TableGridFragment;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.Model.TablesGestor;
import ar.com.glasit.rom.Service.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.view.ViewPager;
import android.content.Context;
import android.os.Bundle;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.view.MenuItem;
import org.json.JSONArray;

public class TablesActivity extends SherlockFragmentActivity implements ServiceListener{

    public static final String KEY_TAB = "KEY_TAB";

    private ActionBar actionBar;
    private ViewPager viewPager;
    private TabsAdapter viewPagerAdapter;
    private int currentTab = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
        viewPager = (ViewPager) findViewById(R.id.pager);
        if (savedInstanceState != null) {
            currentTab = savedInstanceState.getInt(TablesActivity.KEY_TAB, 0);
        }
	}

    private ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            actionBar.setSelectedNavigationItem(position);
        }
    };

    private void addActionBarTabs() {
        viewPager.setOnPageChangeListener(onPageChangeListener);
        viewPagerAdapter = new TabsAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        actionBar = getSupportActionBar();
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
            fragments.add(new TableGridFragment(TableGridFragment.Type.MINE));
            fragments.add(new TableGridFragment(TableGridFragment.Type.FREE));
            fragments.add(new TableGridFragment(TableGridFragment.Type.ALL));
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                newTables.add(Table.buildTable(tables.getJSONObject(i)));
            }
            TablesGestor.getInstance().updateData(newTables);
            if (viewPager.getAdapter() == null) {
                addActionBarTabs();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onError(String error) {
    }

    protected void obtainData() {
        if (TablesGestor.getInstance().getAllTables().isEmpty()) {
            RestService.callGetService(this, WellKnownMethods.GetTables, null);
        } else if (viewPager.getAdapter() == null) {
            addActionBarTabs();
        }
    }
}