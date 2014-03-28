package ar.com.glasit.rom.Model;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Vector;

public class Menu {

    List<Item> items;
    Item currentPage;

    public Menu(JSONArray jsonMenu) {
        this.items = new Vector<Item>();
        try {
            for (int i = 0; i < jsonMenu.length(); i++) {
                items.add(Item.fromJson(jsonMenu.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void select(int position){
        Item item = getItemInPage(position);
        if (item.hasChildren()) {
            currentPage = item;
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
}
