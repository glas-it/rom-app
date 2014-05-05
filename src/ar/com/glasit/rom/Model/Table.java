package ar.com.glasit.rom.Model;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Table implements Comparable<Table> {

    private static final String NUMBER = "numero";
    private static final String MAX_CAPACITY = "capacidad";
    private static final String OPEN = "abierta";
    private static final String ENABLED = "activo";

	protected int number;
	protected int maximunCapacity;
	protected boolean enabled;
	protected boolean open;

    public Table(int number, int maximunCapacity) {
        this.number = number;
        this.maximunCapacity = maximunCapacity;
    }

    public static Table buildTable(JSONObject json) {
        try {
            int number = json.getInt(NUMBER);
            int maximunCapacity= json.getInt(MAX_CAPACITY);
            boolean enabled= json.getBoolean(ENABLED);
            boolean open= json.getBoolean(OPEN);
            if (enabled) {
                if (open) {
                    return new OpenTable(number, maximunCapacity, json.getString("mozo"));
                } else {
                    return new FreeTable(number, maximunCapacity);
                }
            }
        } catch (JSONException e) {
        }
        return null;
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
}
