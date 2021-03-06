package ar.com.glasit.rom.Model;

import ar.com.glasit.rom.Service.WellKnownKeys;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ItemProduct extends Item{

    private String description;
    private List<NameValuePair> prices;
    private boolean isAvailable;

    public ItemProduct(JSONObject json) throws JSONException {
        super(json);
        this.prices = new Vector<NameValuePair>();
        this.description = json.getString(WellKnownKeys.DESCRIPTION);
        if (json.has(WellKnownKeys.PRICES)) {
            JSONObject jsonPrices= json.getJSONObject(WellKnownKeys.PRICES);
            Iterator<String> it = jsonPrices.keys();
            while (it.hasNext()) {
                String next = it.next();
                prices.add(new BasicNameValuePair(next, jsonPrices.getString(next)));
            }
        }
        this.isAvailable = json.getBoolean(WellKnownKeys.AVAILABLE);
    }

    @Override
    public List<IItem> getChildren() {
        return null;
    }

    @Override
    public int getChildrenCount() {
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

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    public List<String> getPricesTags() {
        List<String> pricesTags = new ArrayList<String>(prices.size());
        for (NameValuePair nameValuePair: prices) {
            pricesTags.add(nameValuePair.getName());
        }
        return pricesTags;
    }

    public List<NameValuePair> getPrices() {
        return prices;
    }

    public float getPrice(String tag) {
        for (NameValuePair valuePair : prices) {
            if (valuePair.getName().equals(tag))
                return Float.parseFloat(valuePair.getValue());
        }
        return 0.f;
    }
}
