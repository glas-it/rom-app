package ar.com.glasit.rom.Model;

import ar.com.glasit.rom.Service.WellKnownKeys;
import org.json.JSONObject;

public class Addition {

    private Long id;
    private IItem parent = null;
    private String title, description;

    public static Addition fromJson(JSONObject json, Item parent) {
        Addition addition = fromJson(json);
        if (addition != null) {
            addition.setParent(parent);
        }
        return addition;
    }

    public static Addition fromJson(JSONObject json) {
        Addition addition = new Addition();
        try {
            addition.id = json.getLong("id");
            addition.title = json.getString(WellKnownKeys.TITLE);
            addition.description = json.getString(WellKnownKeys.DESCRIPTION);
        } catch (Exception e) {
        }
        return addition;
    }

    public void setParent(IItem setParent) {
        this.parent = setParent;
    }

    public IItem getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return title;
    }
}
