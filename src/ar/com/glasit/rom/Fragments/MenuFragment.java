package ar.com.glasit.rom.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import ar.com.glasit.rom.Adapters.MenuAdapter;
import ar.com.glasit.rom.Model.Menu;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Service.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.devspark.progressfragment.SherlockProgressListFragment;

public class MenuFragment extends SherlockProgressListFragment{

    private ListView items;

    private Menu menu;
    private MenuAdapter menuAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public MenuFragment() {
        this.menu = Menu.getInstance();
        menuAdapter = new MenuAdapter(this.menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Show indeterminate progress
        setListShown(false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setBackgroundColor(Color.TRANSPARENT);
        getListView().setCacheColorHint(Color.TRANSPARENT);
        if (menu.isEmpty()) {
            obtainData();
        } else {

        }
    }

    public void onBackPressed() {
        menu.goBack();
        menuAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                obtainData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void obtainData(){
        setListShown(false);
        RestService.callGetService(serviceListener, getString(R.string.backend_url), WellKnownMethods.GetMenu, null);
    }

    private void updateData(){
        setListAdapter(menuAdapter);
        items = getListView();
        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menu.select(position);
                menuAdapter.notifyDataSetInvalidated();
            }
        });
        setListShown(true);
    }

    ServiceListener serviceListener = new ServiceListener() {
        @Override
        public void onServiceCompleted(IServiceRequest request, ServiceResponse obj) {
            menu.update(obj.getJsonArray());
            updateData();
        }

        @Override
        public void onError(String error) {
            setListShown(false);
        }
    };
}
