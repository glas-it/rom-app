package ar.com.glasit.rom.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public abstract class Table implements Comparable<Table> {

    private static final String ID = "id";
    private static final String NUMBER = "numero";
    private static final String MAX_CAPACITY = "capacidad";
    private static final String OPEN = "abierta";
    private static final String ENABLED = "activo";

	protected int id;
	protected int number;
	protected int maximunCapacity;
	protected boolean enabled;
	protected boolean open;
    protected List<JoinedTable> joinedTables;

    public Table(int id, int number, int maximunCapacity) {
        this.id = id;
        this.number = number;
        this.maximunCapacity = maximunCapacity;
        this.joinedTables = new Vector<JoinedTable>();
    }

    public static Table buildTable(JSONObject json) {
        Table table = null;
        try {
            JSONObject jsonTable = null;
            try {
                jsonTable = json.getJSONObject("mesa");
            } catch (JSONException e) {
                jsonTable = json;
            }
            int id = jsonTable.getInt(ID);
            int number = jsonTable.getInt(NUMBER);
            int maximunCapacity= jsonTable.getInt(MAX_CAPACITY);
            boolean enabled= jsonTable.getBoolean(ENABLED);
            boolean open= jsonTable.getBoolean(OPEN);
            if (enabled) {
                if (open) {
                	String tableType = jsonTable.getString("class");
                    String waiter = jsonTable.getString("mozo");
                	if (tableType.contains("MesaComposite")) {
                		CompositeTable c = new CompositeTable(id, number, maximunCapacity, waiter);
                        JSONArray mesas = jsonTable.getJSONArray("mesas");
                    	for (int i = 0; i < mesas.length(); i++) {
                            JSONObject j = mesas.getJSONObject(i);
                            int numberJ = j.getInt(NUMBER);
                            int maxCapacityJ = j.getInt(MAX_CAPACITY);
                            int idJ = j.getInt(ID);
                            if (numberJ != number) {
                                c.addJoinedTable(idJ,numberJ,maxCapacityJ);
                            } else {
                                c.setMaximunCapacity(maxCapacityJ);
                            }
                        }
                    	table = c;
                	} else {
                		table = new OpenTable(id, number, maximunCapacity, waiter);
                	}
                	table.load(json);
                } else {
                    table = new FreeTable(id, number, maximunCapacity);
                    table.load(json);
                }
            }
        } catch (JSONException e) {
        }
        return table;
    }

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
    public int getOriginalCapacity() {
        return maximunCapacity;
    }
    public int getMaximunCapacity() {
        int totalCapacityInJoin = maximunCapacity;
        for (JoinedTable table : this.joinedTables) {
            totalCapacityInJoin += table.getCapacity();
        }
        return totalCapacityInJoin;
    }
    public String getJoinedTablesToString() {
        if(this.joinedTables == null) return null;
        else {
            Collections.sort(this.joinedTables);
            String tablesNumber="";
            for (JoinedTable t : this.joinedTables) {
                tablesNumber += Integer.toString(t.getTableNumber());
                tablesNumber += " ";
            }
            return tablesNumber;
        }
    }
	public void setMaximunCapacity(int maximunCapacity) {
		this.maximunCapacity = maximunCapacity;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
	public int compareTo(Table j){
        if (this.number < j.number)
            return -1;
        else
            return 1;
    }
    public void addTable(JoinedTable tableNumber) {
        this.joinedTables.add(tableNumber);
    }
    public void removeTable(Integer tableNumber) {
        this.joinedTables.remove(tableNumber);
    }
    public void setJoinedTables(List<JoinedTable> joinedTables) {
        this.joinedTables = joinedTables;
    }
    public List<JoinedTable> getJoinedTables() {
        return this.joinedTables;
    }
    @Override
    public boolean equals(Object o) {
        return (o instanceof Table && ((Table)o).number == this.number);
    }

    public abstract Object clone();
    public abstract void load(JSONObject json);
    
	public JoinedTable toJoinedTable() {
		JoinedTable j = new JoinedTable(this.id, this.number, this.maximunCapacity);
		return j;
	}
}
