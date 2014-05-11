package ar.com.glasit.rom.Model;

public class FreeTable extends Table{
	
	public FreeTable(int id, int number, int maxCapacity) {
        super(id, number, maxCapacity);
		open=false;
		enabled=true;
	}
	
	public OpenTable open(String waiter, int fellowDiner) {
		OpenTable o = new OpenTable(id, number, maximunCapacity);
		o.setFellowDiner(fellowDiner);
		o.setWaiter(waiter);
		return o;
	}

    @Override
    public Object clone() {
        return new FreeTable(id, number, maximunCapacity);
    }
}
