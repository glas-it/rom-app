package ar.com.glasit.rom.Model;

import java.util.Iterator;

import org.json.JSONObject;


public class FreeTable extends Table{
	
	private int totalCapacityInJoin;
	
	public FreeTable(int id, int number, int maxCapacity) {
        super(id, number, maxCapacity);
		open=false;
		enabled=true;
		totalCapacityInJoin = maxCapacity;
	}
	
	public OpenTable open(String waiter, int fellowDiner) {
		OpenTable o = null;
		if (this.joinedTables == null) {
			o = new OpenTable(id, number, maximunCapacity, waiter);
			o.setFellowDiner(fellowDiner);
		}
		else {
			o = new CompositeTable(id, number, maximunCapacity, this.joinedTables, this.totalCapacityInJoin, waiter);
			o.setFellowDiner(fellowDiner);
		}
		return o;
	}

    @Override
    public Object clone() {
        return new FreeTable(id, number, maximunCapacity);
    }

    @Override
    public void load(JSONObject json) {

    }

    public int[] getJoinedTablesId() {
    	if(this.joinedTables == null) return null;
    	else {
    		int[] idTables = new int[joinedTables.size()];
    		int i = 0;
    		Iterator<JoinedTable> it = joinedTables.iterator();
    		while (it.hasNext()) {
    			JoinedTable t = it.next();
    			idTables[i] = t.getTableId();
    			i++;
    		}
    		return idTables;
    	}
    }

}
