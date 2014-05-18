package ar.com.glasit.rom.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import ar.com.glasit.rom.Model.Table.JoinedTable;

public class FreeTable extends Table{
	
	private List<JoinedTable> tablesToJoin;
	
	public List<JoinedTable> getTablesToJoin() {
		return tablesToJoin;
	}

	public void setTablesToJoin(List<JoinedTable> tablesToJoin) {
		this.tablesToJoin = tablesToJoin;
	}

	private int totalCapacityInJoin;
	
	public FreeTable(int id, int number, int maxCapacity) {
        super(id, number, maxCapacity);
		open=false;
		enabled=true;
		totalCapacityInJoin = maxCapacity;
	}
	
	public OpenTable open(String waiter, int fellowDiner) {
		OpenTable o = null;
		if (this.tablesToJoin == null) {
			o = new OpenTable(id, number, maximunCapacity);
			o.setFellowDiner(fellowDiner);
			o.setWaiter(waiter);
		}
		else {
			o = new CompositeTable(id, number, maximunCapacity, this.tablesToJoin, this.totalCapacityInJoin);
			o.setFellowDiner(fellowDiner);
			o.setWaiter(waiter);
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
    
    public void addTablesToJoin(List<JoinedTable> tablesToJoin) {
    	
    	this.tablesToJoin = tablesToJoin;
		Iterator<JoinedTable> it = tablesToJoin.iterator();
		while (it.hasNext()) {
			JoinedTable t = it.next();
			totalCapacityInJoin += t.getCapacity();
		}
    }
    
    public int[] getJoinedTablesId() {
    	if(this.tablesToJoin == null) return null;
    	else {
    		int[] idTables = new int[tablesToJoin.size()];
    		int i = 0;
    		Iterator<JoinedTable> it = tablesToJoin.iterator();
    		while (it.hasNext()) {
    			JoinedTable t = it.next();
    			idTables[i] = t.getTableId();
    			i++;
    		}
    		return idTables;
    	}
    }
    
	@Override 
	public int getMaximunCapacity() {
		if (this.tablesToJoin == null)
			return maximunCapacity;
		else 
			return totalCapacityInJoin;
	}
	
	public String getJoinedTablesToString() {
    	if(this.tablesToJoin == null) return null;
    	else {
    		String tablesNumber="";
    		Iterator<JoinedTable> it = tablesToJoin.iterator();
    		while (it.hasNext()) {
    			JoinedTable t = it.next();
    			tablesNumber += Integer.toString(t.getTableNumber());
    			tablesNumber += " ";
    		}
    		return tablesNumber;
    	}
	}
}
