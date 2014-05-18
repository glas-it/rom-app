package ar.com.glasit.rom.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeTable extends OpenTable {

	public CompositeTable(int id, int number, int maxCapacity, List<JoinedTable> joinedTables, int joinedCapacity, String waiter) {
		super(id, number, maxCapacity, waiter);
		this.isJoined=true;
		this.joinedTables = joinedTables;
	}
	
	public CompositeTable(int id, int number, int maximunCapacity, String waiter) {
		super(id, number, maximunCapacity, waiter);
		this.isJoined = true;
	}

	public void addJoinedTable(int idJ, int numberJ, int maxCapacityJ) {
		if(this.joinedTables == null) {
			this.joinedTables = new ArrayList<JoinedTable>();
		}
		JoinedTable j = new JoinedTable(idJ, numberJ, maxCapacityJ);
		this.joinedTables.add(j);		
	}

}
