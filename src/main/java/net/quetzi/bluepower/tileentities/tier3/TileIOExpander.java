package net.quetzi.bluepower.tileentities.tier3;

import net.quetzi.bluepower.tileentities.TileBase;

/**
 * @author fabricator77
 */
public class TileIOExpander extends TileBase implements IRedBusWindow {
    private byte redbus_id = 3;
    //TODO: half height block
    
    @Override
    public void setRedbus_id(int id) {
    
        redbus_id = (byte)id;
    }

    @Override
    public int getRedbus_id() {
    
        return redbus_id;
    }
}
