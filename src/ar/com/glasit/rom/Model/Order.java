package ar.com.glasit.rom.Model;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.UUID;

public class Order {

    public enum Status {
        PENDANT, DOING, DONE, CANCELLED;

        public Status nextStatus() {
            switch (this) {
                case PENDANT:
                    return DOING;
                case DOING:
                    return DONE;
                default:
                    return null;
            }
        }
    }

    private ItemProduct item;
    private Addition addition;
    private NameValuePair price;
    private int count;
    private String id;
    private Status status;

    private Order() {}

    public static Order buildOrder(JSONObject json) {
        try{
            Order orden = new Order();
            orden.id = json.getString("id");
            Menu menu = Menu.getInstance();
            long rubroId = json.getLong("rubroId");
            long subRubroId = json.getLong("subSubroId");
            long productId = json.getLong("productId");
            orden.item = menu.getProductById(rubroId, subRubroId, productId);
            try{
                long additionId = json.getLong("additionId");
                orden.addition = menu.getAdditionById(rubroId, subRubroId, additionId);
            } catch (Exception e) {
                orden.addition = null;
            }
            JSONObject price = json.getJSONObject("price");
            Iterator<String> it = price.keys();
            while (it.hasNext()) {
                String priceTag = it.next();
                orden.price = new BasicNameValuePair(priceTag, price.getString(priceTag));
            }
            String status = json.getString("status");
            if (status.equals("pendant")) {
                orden.status = Status.PENDANT;
            } else if (status.equals("doing")) {
                orden.status = Status.DOING;
            } else if (status.equals("done")) {
                orden.status = Status.DONE;
            } else if (status.equals("pendant")) {
                orden.status = Status.CANCELLED;
            }
            return orden;
        } catch (Exception e) {

        }
        return null;
    }

    public Order(ItemProduct item, Addition addition, NameValuePair price) {
        this.price = price;
        this.item = item;
        this.addition = addition;
        this.id = UUID.randomUUID().toString();
        this.status = Status.PENDANT;
    }

    public String getId() {
        return id;
    }
    public float getPrice() {
        return (addition != null) ?  count * (Float.parseFloat(price.getValue()) + addition.getPrice()) : count * Float.parseFloat(price.getValue());
    }

    @Override
    public String toString() {
        return item.toString() + " (" + price.getName() + ") " + ((addition != null) ? " con " + addition.toString() : "");
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        return o.getClass() == this.getClass() && ((Order)o).id.equals(this.id);
    }

    @Override
    public Object clone() {
        Order clone = new Order(item, addition, price);
        clone.id = this.id;
        clone.count = this.count;
        return clone;
    }

    public void stageCompleted() {
        this.status = status.nextStatus();
    }

    public void cancel() {
        this.status = Status.CANCELLED;
    }

    public boolean isPendant() {
        return this.status == Status.PENDANT;
    }

    public boolean isInProgress() {
        return this.status == Status.DOING;
    }

    public boolean isConcluded() {
        return this.status == Status.DONE;
    }
    
    public boolean isCancelled() {
        return this.status == Status.CANCELLED;
    }
    
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", this.id);
            json.put("rubroId", item.getParent().getParent().getId());
            json.put("subRubroId", item.getParent().getId());
            json.put("productId", item.getId());
            if (addition != null)  {
                json.put("additionId", addition.getId());
            }
            switch (this.status) {
                case PENDANT:
                    json.put("status", "pendant");
                    break;
                case DOING:
                    json.put("status", "doing");
                    break;
                case DONE:
                    json.put("status", "done");
                    break;
                default:
                    json.put("status", "cancelled");
                    break;
            }
            JSONObject price = new JSONObject();
            price.put(this.price.getName(), this.price.getValue());
            json.put("price", price);
        } catch (JSONException e) {
        }
        return json;
    }

}
