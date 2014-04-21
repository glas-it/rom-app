package ar.com.glasit.rom.Model;

import org.apache.http.NameValuePair;

import java.util.UUID;

public class Order {

    private ItemProduct item;
    private Addition addition;
    private NameValuePair price;
    private int count;
    private String id;

    public Order(ItemProduct item, Addition addition, NameValuePair price) {
        this.price = price;
        this.item = item;
        this.addition = addition;
        this.id = UUID.randomUUID().toString();
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
}
