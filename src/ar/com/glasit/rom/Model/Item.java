package ar.com.glasit.rom.Model;

import ar.com.glasit.rom.Service.WellKnownKeys;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Item {

    protected Long id;
    protected String description;
    protected Item parent;

    public Item(JSONObject jsonItem) throws JSONException {
        this.id = jsonItem.getLong(WellKnownKeys.ID);
        this.description = jsonItem.getString(WellKnownKeys.DESCRIPTION);
    }

    public static Item fromJson(JSONObject json, Item parent) {
        Item item = fromJson(json);
        if (item != null) {
            item.setParent(parent);
        }
        return item;
    }
    public static Item fromJson(JSONObject json) {
        Item item = null;
        try {
            String itemType = json.getString(WellKnownKeys.CLASS);
            if (itemType.toLowerCase().contains("subrubro")) {
                item = new ItemRubro(json);
            } else if (itemType.toLowerCase().contains("rubro")) {
                item = new ItemRubro(json);
            } else {
                item = new ItemProducto(json);
            }
        } catch (Exception e) {
        }
        return item;
    }

    public Long getId() {
        return id;
    }

    public abstract int getItemsCount();

    public abstract boolean hasChildren();
    public abstract Item getItem(int pos);

    public Item getParent() {
        return parent;
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return description;
    }
}
