package ar.com.glasit.rom.Model;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Vector;

public class Menu {

    private static Menu instance = null;

    List<Item> items;
    Item currentPage;
    private List<OnSelectItemListener> onSelectItemListeners;

    public static Menu getInstance() {
        if (Menu.instance == null) {
            Menu.instance = new Menu();
        }
        return Menu.instance;
    }

    private Menu(){
        this(null);
    }

    private Menu(JSONArray jsonMenu) {
        this.items = new Vector<Item>();
        this.onSelectItemListeners = new Vector<OnSelectItemListener>();
        this.update(jsonMenu);
    }

    public void update(JSONArray jsonMenu) {
        items.clear();
        currentPage = null;
        try {
            for (int i = 0; i < jsonMenu.length(); i++) {
                items.add(Item.fromJson(jsonMenu.getJSONObject(i)));
            }
        } catch (Exception e) {
        }
    }

    public void select(int position){
        Item item = getItemInPage(position);
        if (item.hasChildren()) {
            currentPage = item;
        } else {
            for (OnSelectItemListener onSelectItemListener : onSelectItemListeners) {
                onSelectItemListener.selectItem(item);
            }
        }
    }

    public void goBack() {
        if (currentPage != null) {
            Item item = currentPage.getParent();
            currentPage = item;
        }
    }

    public int getCountInPage() {
        return (currentPage != null) ? currentPage.getItemsCount() : items.size();
    }

    public Item getItemInPage(int position) {
        Item item = null;
        if (currentPage == null) {
            item = items.get(position);
        } else {
            if (currentPage.hasChildren()) {
                item = currentPage.getItem(position);
            }
        }
        return item;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public void addOnSelectItemListener(OnSelectItemListener onSelectItemListener) {
        this.onSelectItemListeners.add(onSelectItemListener);
    }

    public void removeOnSelectItemListener(OnSelectItemListener onSelectItemListener){
        this.onSelectItemListeners.remove(onSelectItemListener);
    }
}
