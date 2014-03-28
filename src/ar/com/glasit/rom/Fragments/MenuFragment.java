package ar.com.glasit.rom.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import ar.com.glasit.rom.Adapters.MenuAdapter;
import ar.com.glasit.rom.Model.Menu;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.app.SherlockFragment;

public class MenuFragment extends SherlockFragment{

    private ListView items;

    private Menu menu;
    private MenuAdapter menuAdapter;

    public MenuFragment(Menu menu) {
        this.menu = menu;
        menuAdapter = new MenuAdapter(this.menu);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container,
                false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        items = (ListView) view.findViewById(R.id.items);
        items.setAdapter(menuAdapter);
        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menu.select(position);
                items.invalidateViews();
            }
        });
    }

    public void onBackPressed() {
        menu.goBack();
        items.invalidateViews();
    }
}
