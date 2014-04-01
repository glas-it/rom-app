package ar.com.glasit.rom.Fragments;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ar.com.glasit.rom.Adapters.ItemAdapter;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Model.IItem;
import ar.com.glasit.rom.Model.Menu;
import ar.com.glasit.rom.Model.OnSelectItemListener;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Service.*;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.devspark.progressfragment.SherlockProgressListFragment;

import java.util.List;
import java.util.Vector;

public class MenuFragment extends ItemFragment{

    private ListView items;

    private Menu menu;

    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListShown(false);
    }

    public MenuFragment() {
        this(new Vector<OnSelectItemListener>());
    }

    public MenuFragment(OnSelectItemListener onSelectItemListener) {
        super(Menu.getInstance(), onSelectItemListener);
        this.menu = Menu.getInstance();
    }

    public MenuFragment(List<OnSelectItemListener> onSelectItemListeners) {
        super(Menu.getInstance(), onSelectItemListeners);
        this.menu = Menu.getInstance();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!menu.hasChildren()) {
            obtainData();
        }
    }

    @Override
    protected void inflateMenu(com.actionbarsherlock.view.Menu menu, MenuInflater inflater){
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
        RestService.callGetService(serviceListener, WellKnownMethods.GetMenu, null);
    }

    ServiceListener serviceListener = new ServiceListener() {
        @Override
        public void onServiceCompleted(IServiceRequest request, ServiceResponse obj) {
            menu.update(obj.getJsonArray());
            populateList();
        }

        @Override
        public void onError(String error) {
            Toast.makeText(getSherlockActivity(), getSherlockActivity().getText(R.string.connectivity_error), Toast.LENGTH_SHORT).show();
            setListShown(true);
        }
    };
}
