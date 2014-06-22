package net.quetzi.bluepower.tileentities.tier3;

import net.quetzi.bluepower.tileentities.TileBase;

/**
 * @author fabricator77
 */
public class TileDiskDrive extends TileBase implements IRedBusWindow {
    private byte redbus_id = 2;
    /** redbus memory block
     * addr size function
     * 0x00 128 Data buffer
     * 0x80 2 Sector number
     * 0x82 1 Command
     * 
     * command modes
     * command function
     * 0x01    Read name
     * 0x02    Write name
     * 0x03    Read serial
     * 0x04    Read sector
     * 0x05    Write sector
    */

    @Override
    public void setRedbus_id(int id) {
    
        redbus_id = (byte)id;
    }

    @Override
    public int getRedbus_id() {
    
        return redbus_id;
    }
}
