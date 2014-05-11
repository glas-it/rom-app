package ar.com.glasit.rom.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import ar.com.glasit.rom.Adapters.ItemAdapter;
import ar.com.glasit.rom.Model.IItem;
import ar.com.glasit.rom.Model.OnSelectItemListener;

import java.util.List;
import java.util.Vector;

public class ItemFragment extends ListSearcherFragment{

    private ListView items;

    protected IItem item;
    protected ItemAdapter itemAdapter;
    private List<OnSelectItemListener> onSelectItemListeners;

    public ItemFragment(){
        super();
    }

    public ItemFragment(IItem item, OnSelectItemListener onSelectItemListener){
        super();
        this.item = item;
        itemAdapter = new ItemAdapter(this.item);
        this.onSelectItemListeners = new Vector<OnSelectItemListener>();
        this.onSelectItemListeners.add(onSelectItemListener);
    }

    public ItemFragment(IItem item, List<OnSelectItemListener> onSelectItemListeners) {
        super();
        this.item = item;
        itemAdapter = new ItemAdapter(this.item);
        this.onSelectItemListeners = onSelectItemListeners;
    }

    public void setParameters(IItem item, OnSelectItemListener onSelectItemListener) {
        this.item = item;
        itemAdapter = new ItemAdapter(this.item);
        this.onSelectItemListeners = new Vector<OnSelectItemListener>();
        this.onSelectItemListeners.add(onSelectItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSherlockActivity().getSupportActionBar().setTitle(item.toString());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setBackgroundColor(Color.TRANSPARENT);
        getListView().setCacheColorHint(Color.TRANSPARENT);
        populateList();
    }

    public void addOnSelectItemListener(OnSelectItemListener onSelectItemListener) {
        this.onSelectItemListeners.add(onSelectItemListener);
    }

    public void removeOnSelectItemListener(OnSelectItemListener onSelectItemListener){
        this.onSelectItemListeners.remove(onSelectItemListener);
    }

    protected void populateList() {
        if (item.hasChildren()){
            setListAdapter(itemAdapter);
            items = getListView();
            items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    IItem item = (IItem) getListAdapter().getItem(position);
                    for (OnSelectItemListener onSelectItemListener: ItemFragment.this.onSelectItemListeners) {
                        onSelectItemListener.selectItem(item);
                    }
                }
            });
            setListShown(true);
        }
    }
}
