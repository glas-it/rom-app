package ar.com.glasit.rom.Model;

import java.util.List;

/**
 * Created by pablo on 20/04/14.
 */
public interface TableManager {

    void onTableOpened(int tableId, int fellowDiner, List<JoinedTable> joinedTablesId);
    void onTableClosed(int tableId);
    void onTableOrder(int tableId, List<Order> orders);
    void onTableJoined(List<JoinedTable> selectedTables);
}
