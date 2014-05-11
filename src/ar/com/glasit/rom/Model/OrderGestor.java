package ar.com.glasit.rom.Model;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class OrderGestor {

	private List<Order> allOrders;
	private static OrderGestor instance = null;

	private OrderGestor() {
		this.allOrders = new Vector<Order>();
	}
	
    public static OrderGestor getInstance() {
        if (OrderGestor.instance == null) {
            OrderGestor.instance = new OrderGestor();
        }
        return OrderGestor.instance;
    }

    public void updateData(List<Order> orders) {
        this.allOrders = orders;
        for (Order o: orders){
            int index = allOrders.indexOf(o);
            if (index != -1) {
                if (o.getStatus().equals(Order.Status.CANCELLED) ||
                        o.getStatus().equals(Order.Status.DELIVERED) ||
                        o.isRejected()) {
                    allOrders.get(index).setStatus(o.getStatus());
                }
            } else {
                allOrders.add(o);
            }
        }
    }

    public void updateOrder(Order order) {
        this.allOrders.set(this.allOrders.lastIndexOf(order), order);
    }

	public List<Order> getAllOrders() {
		return allOrders;
	}

    private List<Order> getPendantOrders() {
        List<Order> orders = new Vector<Order>();
        Iterator<Order> it = allOrders.iterator();
		while (it.hasNext()) {
            Order order = it.next();
			if (order.isPendant())  {
                orders.add(order);
            }
		}
		return orders;
	}

    private List<Order> getInProgressOrders() {
        List<Order> orders = new Vector<Order>();
        Iterator<Order> it = allOrders.iterator();
        while (it.hasNext()) {
            Order order = it.next();
            if (order.isInProgress())  {
                orders.add(order);
            }
        }
        return orders;
    }

    private List<Order> getConcludedOrders() {
        List<Order> orders = new Vector<Order>();
        Iterator<Order> it = allOrders.iterator();
        while (it.hasNext()) {
            Order order = it.next();
            if (order.isConcluded())  {
                orders.add(order);
            }
        }
        return orders;
    }

    public List<Order> getOrders (Order.Status type) {
        switch (type) {
            case PENDANT:
                return getPendantOrders();
            case DOING:
                return getInProgressOrders();
            case DONE:
                return getConcludedOrders();
            default:
                return getPendantOrders();
        }
    }
}
