package ar.com.glasit.rom.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ar.com.glasit.rom.Model.Table.JoinedTable;

public class CompositeTable extends OpenTable {

	List<JoinedTable> joinedTables;
	
	public List<JoinedTable> getJoinedTables() {
		return joinedTables;
	}

	public void setJoinedTables(List<JoinedTable> joinedTables) {
		this.joinedTables = joinedTables;
	}

	private int totalCapacity;
	
	public CompositeTable(int id, int number, int maxCapacity, List<JoinedTable> joinedTables, int joinedCapacity) {
		super(id, number, maxCapacity);
		this.isJoined=true;
		this.joinedTables = joinedTables;
		this.totalCapacity = joinedCapacity;
		
	}
	
	public CompositeTable(int id, int number, int maximunCapacity, int fellowDinner, String waiter) {
		super(id, number, maximunCapacity, fellowDinner, waiter);
		this.isJoined = true;
		this.totalCapacity = maximunCapacity;
	}

	@Override 
	public int getMaximunCapacity() {
		return totalCapacity;
	}

	public void addJoinedTable(int idJ, int numberJ, int maxCapacityJ) {
		if(this.joinedTables == null) {
			this.joinedTables = new ArrayList<JoinedTable>();
		}
		JoinedTable j = new JoinedTable(idJ, numberJ, maxCapacityJ);
		this.joinedTables.add(j);		
	}
	
	public String getJoinedTablesToString() {
    	if(this.joinedTables == null) return null;
    	else {
    		Collections.sort(this.joinedTables);
    		String tablesNumber="";
    		Iterator<JoinedTable> it = joinedTables.iterator();
    		while (it.hasNext()) {
    			JoinedTable t = it.next();
    			tablesNumber += Integer.toString(t.getTableNumber());
    			tablesNumber += " ";
    		}
    		return tablesNumber;
    	}
	}

}
