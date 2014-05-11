package ar.com.glasit.rom.Fragments;

import ar.com.glasit.rom.Model.Table;
import ar.com.glasit.rom.Model.TableManager;

/**
 * Created by pablo on 11/05/14.
 */
public interface TableDetailFragment<E extends Table> {

    public void setParameters(TableManager manager, E table);
}
