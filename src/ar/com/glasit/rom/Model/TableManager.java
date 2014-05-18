package ar.com.glasit.rom.Model;

import java.util.List;

import ar.com.glasit.rom.Model.Table.JoinedTable;

/**
 * Created by pablo on 20/04/14.
 */
public interface TableManager {

    void onTableOpened(int tableId, int fellowDiner, int[] joinedTablesId);
    void onTableClosed(int tableId);
    void onTableOrder(int tableId, List<Order> orders);
	void onTableFreeJoined(Table table, List<JoinedTable> selectedTables);
	void onTableOpenJoined(Table table, List<JoinedTable> toAdd,  List<JoinedTable> toQuit );
}
