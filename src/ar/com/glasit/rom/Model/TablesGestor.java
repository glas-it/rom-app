package ar.com.glasit.rom.Model;

import ar.com.glasit.rom.Fragments.TablesFragment;
import ar.com.glasit.rom.Helpers.BackendHelper;

import java.util.*;

public class TablesGestor {

	private List<Table> allTables;
	private static TablesGestor instance = null;

	private TablesGestor() {
		this.allTables = new Vector<Table>();
	}
	
    public static TablesGestor getInstance() {
        if (TablesGestor.instance == null) {
            TablesGestor.instance = new TablesGestor();
        }
        return TablesGestor.instance;
    }

    public void updateData(List<Table> tables) {
        this.allTables = tables;
    }

    public void updateTable(Table table) {
        this.allTables.set(this.allTables.lastIndexOf(table), table);
    }

	public List<Table> getAllTables() {
		return allTables;
	}
	
	public List<Table> getMyTables() {
		Iterator<Table> it = allTables.iterator();
		List<Table> my = new Vector<Table>();
		while (it.hasNext()) {
			Table t = it.next();
			if (t.open)  {
				OpenTable op = (OpenTable) t;
				if (op.getWaiter() != null && op.getWaiter().contentEquals(BackendHelper.getLoggedUserName())) {
					my.add(op);
				}
			}
		}
		return my;
	}

	
	public List<Table> getFreeTables() {
		Iterator<Table> it = allTables.iterator();
		List<Table> free = new Vector<Table>();
		while (it.hasNext()) {
			Table t = it.next();
			if (!t.open && t.isEnabled())  {
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
                break;
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
				this.allTables.add(ft.open(BackendHelper.getLoggedUserName(),fellowDiner));
				found = true;
                break;
			}
		}
		Collections.sort(allTables);

	}

    public List<Table> getTables (TablesFragment.Type type) {
        switch (type) {
            case MINE:
                return getMyTables();
            case FREE:
                return getFreeTables();
            case ALL:
                return getAllTables();
            default:
                return getMyTables();
        }
    }
	public Table getTable(int tableNumber) {
		
		Iterator<Table> it = allTables.iterator();
		boolean found=false;
		Table t = null;
		while (it.hasNext() && !found) {
			t = it.next();
			if (t.getNumber() == tableNumber) {
				found=true;
                break;
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
