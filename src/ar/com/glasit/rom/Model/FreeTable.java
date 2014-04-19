package ar.com.glasit.rom.Model;

public class FreeTable extends Table{
	
	public FreeTable(){
		open=false;
		enabled=true;
	}
	
	public OpenTable open(String waiter, int fellowDiner) {
		OpenTable o = new OpenTable();
		o.setFellowDiner(fellowDiner);
		o.setWaiter(waiter);
		o.setMaximunCapacity(this.maximunCapacity);
		o.setNumber(this.number);
		return o;
	}
}
