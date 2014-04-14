package ar.com.glasit.rom.Model;

import ar.com.glasit.rom.Service.WellKnownKeys;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class Item implements IItem {

    protected Long id;
    protected String title;
    protected IItem parent;

    public Item(JSONObject jsonItem) throws JSONException {
        this.id = jsonItem.getLong(WellKnownKeys.ID);
        this.title = jsonItem.getString(WellKnownKeys.TITLE);
    }

    public static IItem fromJson(JSONObject json, Item parent) {
        IItem item = fromJson(json);
        if (item != null) {
            item.setParent(parent);
        }
        return item;
    }
    public static IItem fromJson(JSONObject json) {
        Item item = null;
        try {
            String itemType = json.getString(WellKnownKeys.CLASS);
            if (itemType.toLowerCase().contains("subrubro")) {
                item = new ItemSubRubro(json);
            } else if (itemType.toLowerCase().contains("rubro")) {
                item = new ItemRubro(json);
            } else {
                item = new ItemProduct(json);
            }
        } catch (Exception e) {
        }
        return item;
    }

    public Long getId() {
        return id;
    }

    @Override
    public abstract List<IItem> getChildren();

    @Override
    public abstract int getChildrenCount();

    @Override
    public abstract boolean hasChildren();

    @Override
    public abstract IItem getItem(int pos);

    @Override
    public IItem getParent() {
        return parent;
    }

    @Override
    public void setParent(IItem parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public String getDescription() {
        return toString();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
