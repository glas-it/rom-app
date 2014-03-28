package ar.com.glasit.rom.Model;

import ar.com.glasit.rom.Service.WellKnownKeys;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class ItemRubro extends Item{

    private List<Item> items;

    public ItemRubro(JSONObject json) throws JSONException {
        super(json);
        this.items = new Vector<Item>();
        String keyItems = null;
        if (json.has(WellKnownKeys.SUBRUBROS)) {
            JSONArray jsonArray = json.getJSONArray(WellKnownKeys.SUBRUBROS);
            for (int i = 0; i < jsonArray.length(); i++) {
                items.add(Item.fromJson(jsonArray.getJSONObject(i), this));
            }
        }
        if (json.has(WellKnownKeys.ITEMS)) {
            JSONArray jsonArray = json.getJSONArray(WellKnownKeys.ITEMS);
            for (int i = 0; i < jsonArray.length(); i++) {
                items.add(Item.fromJson(jsonArray.getJSONObject(i), this));
            }
        }
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }

    @Override
    public boolean hasChildren() {
        return !items.isEmpty();
    }

    @Override
    public Item getItem(int pos) {
        return !items.isEmpty() ? items.get(pos) : null;
    }

}
