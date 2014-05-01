package ar.com.glasit.rom.Model;

import android.app.AlertDialog;
import ar.com.glasit.rom.Service.WellKnownKeys;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class ItemSubRubro extends Item{

    private List<IItem> items;
    private List<Addition> additions;

    public ItemSubRubro(JSONObject json) throws JSONException {
        super(json);
        this.items = new Vector<IItem>();
        additions = new Vector<Addition>();
        String keyItems = null;
        if (json.has(WellKnownKeys.ITEMS)) {
            JSONArray jsonArray = json.getJSONArray(WellKnownKeys.ITEMS);
            for (int i = 0; i < jsonArray.length(); i++) {
                items.add(Item.fromJson(jsonArray.getJSONObject(i), this));
            }
        }
        if (json.has(WellKnownKeys.ADDITIONS)) {
            JSONArray jsonArray = json.getJSONArray(WellKnownKeys.ADDITIONS);
            for (int i = 0; i < jsonArray.length(); i++) {
                additions.add(Addition.fromJson(jsonArray.getJSONObject(i), this));
            }
        }
    }

    @Override
    public List<IItem> getChildren() {
        return items;
    }

    @Override
    public int getChildrenCount() {
        return items.size();
    }

    @Override
    public boolean hasChildren() {
        return !items.isEmpty();
    }

    @Override
    public IItem getItem(int pos) {
        return !items.isEmpty() ? items.get(pos) : null;
    }

    public List<Addition> getAdditions() {
        return additions;
    }
    
    public Addition getAdditionById(long id) {
        for (Addition addition : additions) {
            if (addition.getId() == id)
                return addition;
        }
        return null;
    }
}
