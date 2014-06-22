package net.quetzi.bluepower.tileentities.tier3;

public interface IRedBusWindow {
    byte[] redBus_memory = new byte[256];
    //TODO: read/write update status flags/events
    abstract public void setRedbus_id(int id);
    abstract public int getRedbus_id();
}
