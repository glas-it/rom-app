package ar.com.glasit.rom.Model;

import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.R;
import org.json.JSONArray;

import java.util.List;
import java.util.Vector;

public class Menu implements IItem{

    private static Menu instance = null;

    List<IItem> items;

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
        this.items = new Vector<IItem>();
        this.update(jsonMenu);
    }

    public void update(JSONArray jsonMenu) {
        items.clear();
        try {
            for (int i = 0; i < jsonMenu.length(); i++) {
                items.add(Item.fromJson(jsonMenu.getJSONObject(i)));
            }
        } catch (Exception e) {
        }
    }

     public IItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public Long getId() {
        return 0L;
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
        return !items.isEmpty() ;
    }

    @Override
    public IItem getParent() {
        return null;
    }

    @Override
    public void setParent(IItem item) {
        throw new IllegalArgumentException();
    }

    @Override
    public String getDescription() {
        return toString();
    }

    @Override
    public String toString() {
        return ContextHelper.getContextInstance().getText(R.string.menu).toString();
    }
}
