package ar.com.glasit.rom.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TablesGestor {

	private List<Table> allTables;
	private static TablesGestor instance = null;
	private static String myName = "pepe";
	
	private TablesGestor() {
		this.allTables = new ArrayList<Table>();
		
		//TODO:remove
		this.setMockData();
	}
	
    public static TablesGestor getInstance() {
        if (TablesGestor.instance == null) {
            TablesGestor.instance = new TablesGestor();
        }
        return TablesGestor.instance;
    }

    private void setMockData() {
		FreeTable f = new FreeTable();
		f.setMaximunCapacity(6);
		f.setNumber(2);

		allTables.add(f);
		f = new FreeTable();
		f.setMaximunCapacity(4);
		f.setNumber(5);

		allTables.add(f);
		f = new FreeTable();
		f.setMaximunCapacity(2);
		f.setNumber(7);

		allTables.add(f);
		f = new FreeTable();
		f.setMaximunCapacity(4);
		f.setNumber(9);
		allTables.add(f);

		f = new FreeTable();
		f.setMaximunCapacity(4);
		f.setNumber(10);
		f.setEnabled(false);
		allTables.add(f);
		OpenTable o = new OpenTable();
		o.setMaximunCapacity(2);
		o.setFellowDiner(3);
		o.setNumber(1);
		o.setWaiter("pepe");
		allTables.add(o);
		
		 o = new OpenTable();
		o.setMaximunCapacity(4);
		o.setFellowDiner(3);
		o.setNumber(4);
		o.setWaiter("pepe");
		allTables.add(o);
		
		 o = new OpenTable();
		o.setMaximunCapacity(2);
		o.setFellowDiner(3);
		o.setNumber(8);
		o.setWaiter("pepe");
		allTables.add(o);
		
		o = new OpenTable();
		o.setMaximunCapacity(4);
		o.setFellowDiner(3);
		o.setNumber(3);
		o.setWaiter("florencia");
		allTables.add(o);
		
		o = new OpenTable();
		o.setMaximunCapacity(2);
		o.setFellowDiner(3);
		o.setNumber(6);
		o.setWaiter("florencia");
		allTables.add(o);

		Collections.sort(allTables);
    }

	public List<Table> getAllTables() {
		return allTables;
	}
	
	public List<OpenTable> getMyTables() {
		Iterator<Table> it = allTables.iterator();
		List<OpenTable> my = new ArrayList<OpenTable>();
		while (it.hasNext()) {
			Table t = it.next();
			if (t.open)  {
				OpenTable op = (OpenTable) t;
				if (op.getWaiter().contentEquals(myName)) {
					my.add(op);
				}
			}
		}
		return my;
	}

	
	public List<FreeTable> getFreeTables() {
		Iterator<Table> it = allTables.iterator();
		List<FreeTable> free = new ArrayList<FreeTable>();
		while (it.hasNext()) {
			Table t = it.next();
			if (!t.open)  {
				FreeTable op = (FreeTable) t;
				free.add(op);
			}
		}
		return free;
	}

	
	public void closeTable(int number) {
		Iterator<Table> it = this.allTables.iterator();
		boolean found=false;
		while (it.hasNext() || !found) {
			Table t = it.next();
			if (t.getNumber() == number) {
				OpenTable ot = (OpenTable) t;
				it.remove();
				this.allTables.add(ot.close());
				found = true;
			}
		}
		Collections.sort(allTables);
	}
	
	
	public void openTable(int number, int fellowDiner) {
		
		Iterator<Table> it = this.allTables.iterator();
		boolean found=false;
		while (it.hasNext() && !found) {
			Table t = it.next();
			if (t.getNumber() == number) {
				it.remove();
				FreeTable ft = (FreeTable) t;
				this.allTables.add(ft.open(myName,fellowDiner));
				found = true;
			}
		}
		Collections.sort(allTables);

	}

	public Table getTable(int tableNumber) {
		
		Iterator<Table> it = allTables.iterator();
		boolean found=false;
		Table t = null;
		while (it.hasNext() && !found) {
			t = it.next();
			if (t.getNumber() == tableNumber) {
				found=true;
			}
		}
		if (!found) {
			return null;
		}
		else {
			return t;
		}
	}

}
