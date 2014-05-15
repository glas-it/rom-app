package ar.com.glasit.rom.Model;

import org.json.JSONException;
import org.json.JSONObject;

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

    public Table(int id, int number, int maximunCapacity) {
        this.id = id;
        this.number = number;
        this.maximunCapacity = maximunCapacity;
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
                    table = new OpenTable(id, number, maximunCapacity, jsonTable.getString("mozo"));
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
	public int getMaximunCapacity() {
		return maximunCapacity;
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

    @Override
    public boolean equals(Object o) {
        return (o instanceof Table && ((Table)o).number == this.number);
    }

    public abstract Object clone();
    public abstract void load(JSONObject json);
}
