package gui.menu;

import java.io.IOException;

/**
 *
 * @author Rautu
 */
public interface MenuEvent {

    public void menuSelected(int index, int subIndex, MenuAction action) throws IOException;
}