package ar.com.glasit.rom.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.SearchView;
import com.devspark.progressfragment.SherlockProgressListFragment;

public class ListSearcherFragment extends SherlockProgressListFragment implements SearchView.OnQueryTextListener{

    protected SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(R.string.empty);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflateMenu(menu, inflater);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (searchView != null) {
            searchView.setIconified(true);
            searchView.setOnQueryTextListener(null);
        }
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

    protected void inflateMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
    }
}
