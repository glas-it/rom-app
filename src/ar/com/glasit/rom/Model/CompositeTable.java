package ar.com.glasit.rom.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	
	public CompositeTable(int id, int number, int maximunCapacity) {
		super(id, number, maximunCapacity);
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

}
