package ar.com.glasit.rom.Model;

public class OpenTable extends Table {

	private int fellowDiner;
	private String waiter;
	private Order orderRequest;
	
	public OpenTable() {
		enabled=true;
		open=true;
	}
	
	public Order getOrderRequest() {
		return orderRequest;
	}
	
	public void setOrderRequest(Order orderRequest) {
		this.orderRequest = orderRequest;
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
		FreeTable closed = new FreeTable();
		closed.setEnabled(true);
		closed.setMaximunCapacity(this.maximunCapacity);
		closed.setNumber(this.maximunCapacity);
		return closed;
	}
	

}
