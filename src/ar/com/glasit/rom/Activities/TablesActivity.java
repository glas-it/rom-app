
package ar.com.glasit.rom.Activities;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Fragments.TablesFragment;

public class TablesActivity extends SherlockFragmentActivity
{

	static final int MY_TABLES_TAB = 0;
	static final int FREE_TABLES_TAB = 1;
	static final int ALL_TAB = 2;
	
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	TextView tabCenter;
	TextView tabText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mViewPager = new ViewPager(this);
		mViewPager.setId(ar.com.glasit.rom.R.id.pager);

		setContentView(mViewPager);
		final ActionBar bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mTabsAdapter = new TabsAdapter(this, mViewPager);

		mTabsAdapter.addTab(
				bar.newTab().setText("Mías"),
				TablesFragment.class, null);
		mTabsAdapter.addTab(
				bar.newTab()
						.setText("Libres"),
				TablesFragment.class, null);
		mTabsAdapter.addTab(
				bar.newTab()
						.setText("Todas"),
				TablesFragment.class, null);
			 
		 if ( getIntent().getExtras() != null) {
			 int tab = getIntent().getExtras().getInt("currentTab");
			 if (tab!= 0) {
				 tab--;
				 bar.setSelectedNavigationItem(tab);
			 }
		 }


	}
	public int getTab() {
		return mViewPager.getCurrentItem();
	}
	  @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	    }
	
	
	public static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener
	{
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo
		{
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args)
			{
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager)
		{
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args)
		{
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount()
		{
			return mTabs.size();
		}

		
		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			Fragment f = Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
			Bundle bundle = new Bundle();
			bundle.putInt("tab", position+1);
			f.setArguments(bundle);
			return f;

		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels)
		{
		}

		public void onPageSelected(int position)
		{
			mActionBar.setSelectedNavigationItem(position);
			
		}

		public void onPageScrollStateChanged(int state)
		{
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++)
			{
				if (mTabs.get(i) == tag)
				{
					mViewPager.setCurrentItem(i);
				}
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
		}
	}


}