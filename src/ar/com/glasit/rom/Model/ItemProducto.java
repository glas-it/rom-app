package ar.com.glasit.rom.Model;

import ar.com.glasit.rom.Service.WellKnownKeys;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemProducto extends Item{

    private String title;

    public ItemProducto(JSONObject json) throws JSONException {
        super(json);
        this.title = json.getString(WellKnownKeys.TITLE);
    }

    @Override
    public int getItemsCount() {
        return 0;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public Item getItem(int pos) {
        return null;
    }
}
