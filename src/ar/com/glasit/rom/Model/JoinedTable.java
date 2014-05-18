package ar.com.glasit.rom.Model;

public class JoinedTable implements Comparable<JoinedTable> {
    public JoinedTable(int id, int number, int maximunCapacity) {
        setTableId(id);
        setTableNumber(number);
        setCapacity(maximunCapacity);
    }
    private int tableId;
    private int tableNumber;
    private int capacity;

    public int getTableId() {
        return tableId;
    }
    public void setTableId(int tableId) {
        this.tableId = tableId;
    }
    public int getTableNumber() {
        return tableNumber;
    }
    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    @Override
    public int compareTo(JoinedTable another) {
        int anotherTableNumber = another.getTableNumber();
        return this.tableNumber - anotherTableNumber;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof JoinedTable && ((JoinedTable)o).tableId == this.tableId;
    }
}