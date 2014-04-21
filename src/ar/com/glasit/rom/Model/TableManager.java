package ar.com.glasit.rom.Model;

/**
 * Created by pablo on 20/04/14.
 */
public interface TableManager {

    void onTableOpened(OpenTable table);
    void onTableClosed(FreeTable table);
    void onTableOrder(OpenTable table);
}
