package ar.com.glasit.rom.Model;

import java.util.List;

/**
 * Created by pablo on 29/03/14.
 */
public interface IItem {

    public Long getId();

    public List<IItem> getChildren();

    public int getChildrenCount();

    public boolean hasChildren();

    public IItem getItem(int pos);

    public IItem getParent();

    public void setParent(IItem item);

    public String getDescription();

    public boolean isAvailable();
}
