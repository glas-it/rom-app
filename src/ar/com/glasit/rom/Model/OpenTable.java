package ar.com.glasit.rom.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class OpenTable extends Table {

	private int fellowDiner;
	private String waiter;
	private List<Order> orderRequest;
	protected boolean isOpen;
	protected boolean isJoined;
	protected Coupon coupon;

    public boolean isJoined() {
		return isJoined;
	}

	public void setJoined(boolean isJoined) {
		this.isJoined = isJoined;
	}

	public OpenTable(int id, int number, int maxCapacity, String waiter) {
        super(id, number, maxCapacity);
        orderRequest = new Vector<Order>();
        enabled=true;
        open=true;
        isJoined=false;
        this.waiter = waiter;
    }

	public List<Order> getOrderRequest() {
		return orderRequest;
	}

	public void addOrder(Order order) {
		this.orderRequest.add(order);
	}

    public void addCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Coupon getCoupon() {
        return this.coupon;
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
        OpenTable clone = new OpenTable(id, number, maximunCapacity, waiter);
        clone.fellowDiner = this.fellowDiner;
        for (Order o: orderRequest) {
            clone.addOrder((Order) o.clone());
        }
        return clone;
    }

    @Override
    public void load(JSONObject json) {
        JSONArray ordenes = null;
        try {
            this.fellowDiner = json.getInt("comensales");
            try{
                this.coupon = new Coupon(json.getJSONObject("promocion"));
            } catch (Exception e) {
                this.coupon = null;
            }
            ordenes = json.getJSONArray("ordenes");
            for (int i = 0; i < ordenes.length(); i++) {
                Order order = Order.buildOrder(ordenes.getJSONObject(i));
                if (order != null) {
                    this.addOrder(order);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
