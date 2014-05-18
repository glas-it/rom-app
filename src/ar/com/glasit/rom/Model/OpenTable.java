package ar.com.glasit.rom.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class OpenTable extends

        Table {

	private int fellowDiner;
	protected String waiter;
	private List<Order> orderRequest;
	protected boolean isOpen;
	protected boolean isJoined;

    public boolean isJoined() {
		return isJoined;
	}

	public void setJoined(boolean isJoined) {
		this.isJoined = isJoined;
	}

	public OpenTable(int id, int number, int maxCapacity, int fellowDiner, String waiter) {
        super(id, number, maxCapacity);
        orderRequest = new Vector<Order>();
        enabled=true;
        open=true;
        isJoined=false;
        this.waiter = waiter;
        this.fellowDiner = fellowDiner;
    }
    
	public OpenTable(int id, int number, int maxCapacity) {
        super(id, number, maxCapacity);
        orderRequest = new Vector<Order>();
		enabled=true;
		open=true;
	}
	
	public List<Order> getOrderRequest() {
		return orderRequest;
	}

	public void addOrder(Order order) {
		this.orderRequest.add(order);
	}

    public void removeOrder(Order order) {
        this.orderRequest.remove(order);
    }

	public String getWaiter() {
		return waiter;
	}
	
	public void setWaiter(String waiter) {
		this.waiter = waiter;
	}
	
	public int getFellowDiner() {
		return fellowDiner;
	}
	
	public void setFellowDiner(int fellowDiner) {
		this.fellowDiner = fellowDiner;
	}

	public FreeTable close() {
		FreeTable closed = new FreeTable(id, number,maximunCapacity);
		closed.setEnabled(true);
		return closed;
	}
	
    public float getPrice() {
        float price = 0.f;
        for (Order o: orderRequest) {
            price += o.getPrice();
        }
        return price;
    }

    @Override
    public Object clone() {
        OpenTable clone = new OpenTable(id, number, maximunCapacity);
        clone.fellowDiner = this.fellowDiner;
        clone.waiter = waiter;
        for (Order o: orderRequest) {
            clone.addOrder((Order) o.clone());
        }
        return clone;
    }

    @Override
    public void load(JSONObject json) {
        JSONArray ordenes = null;
        try {
            ordenes = json.getJSONArray("ordenes");
            for (int i = 0; i < ordenes.length(); i++) {
                this.addOrder(Order.buildOrder(ordenes.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
