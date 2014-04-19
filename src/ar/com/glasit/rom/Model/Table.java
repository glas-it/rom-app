package ar.com.glasit.rom.Model;

public abstract class Table implements Comparable<Table> {

	protected int number;
	protected int maximunCapacity;
	protected boolean enabled;
	protected boolean open;
	
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
	public int compareTo(Table j)
	    {
	    if (this.number < j.number)
	        return -1;
	    else
	        return 1;
	    }
	
}
