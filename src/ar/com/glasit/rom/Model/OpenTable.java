package ar.com.glasit.rom.Model;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class OpenTable extends Table {

	private int fellowDiner;
	private String waiter;
	private List<Order> orderRequest;
	
	public OpenTable(int number, int maxCapacity) {
        super(number, maxCapacity);
        orderRequest = new Vector<Order>();
		enabled=true;
		open=true;
	}
	
	public List<Order> getOrderRequest() {
		return orderRequest;
	}

    public Order getById(String id) {
        for (Order o : orderRequest) {
            if (o.getId().equals(id))
                return o;
        }
        return null;
    }

    public void clearOrders(){
        this.orderRequest = new Vector<Order>();
    }
    public void removeOthers(List<String> uuids) {
        Iterator<Order> it = orderRequest.iterator();
        while(it.hasNext()) {
            boolean remove = true;
            for (String uuid : uuids) {
                if (uuid.equals(it.next().getId())){
                    remove = false;
                    break;
                }
            }
            if (remove) {
                it.remove();
            }
        }
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
		FreeTable closed = new FreeTable(number,maximunCapacity);
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
        OpenTable clone = new OpenTable(number, maximunCapacity);
        clone.fellowDiner = this.fellowDiner;
        clone.waiter = waiter;
        for (Order o: orderRequest) {
            clone.addOrder((Order) o.clone());
        }
        return clone;
    }
}
