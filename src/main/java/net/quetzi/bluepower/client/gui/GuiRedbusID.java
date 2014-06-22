package net.quetzi.bluepower.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.client.gui.widget.BaseWidget;
import net.quetzi.bluepower.client.gui.widget.IGuiWidget;
import net.quetzi.bluepower.client.gui.widget.WidgetDipSwitch;
import net.quetzi.bluepower.containers.ContainerRedbusID;
import net.quetzi.bluepower.network.NetworkHandler;
import net.quetzi.bluepower.network.messages.MessageGuiUpdate;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier3.IRedBusWindow;

/**
 * @author fabricator77
 */
public class GuiRedbusID extends GuiBase {
private final IRedBusWindow device;

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID+":textures/gui/redbusgui.png");

    public GuiRedbusID (InventoryPlayer invPlayer, IRedBusWindow device) {
        super(new ContainerRedbusID(invPlayer, device), resLoc);
        this.device = device;

        this.xSize = 123;
        this.ySize = 81;
    }

    @Override
    public void initGui() {
    
        super.initGui();
        int busID = this.device.getRedbus_id();
        for (int i = 0; i < 8; i++) {
            WidgetDipSwitch dipSwitchWidget = new WidgetDipSwitch(i, guiLeft + 16 + 12 * i, guiTop + 25);
            //decode redbusID into binary
            if (busID % 2 == 1) {
                busID--;
                dipSwitchWidget.value = 1;
            }
            else {
            	dipSwitchWidget.value = 0;
            }
            busID = busID >> 1;
            addWidget(dipSwitchWidget);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    
        drawHorizontalAlignedString(7, 4, xSize - 14, StatCollector.translateToLocal("gui.redbusgui"), false);
        
        drawHorizontalAlignedString(7, 60, xSize - 14, StatCollector.translateToLocal("gui.redbus.id") + ":" + device.getRedbus_id(), false);
    }

    //TODO: clicking on switches toggles state and updates redbus_id
    @Override
    public void actionPerformed(IGuiWidget widget) {
    
        BaseWidget baseWidget = (BaseWidget) widget;
        if (baseWidget.getID() == 7) {
        	//top bit behaves as -127 (possibly bug in java or signed interger related)
        	// so ignore for now
        	return;
        }
        int busID = device.getRedbus_id();
        int value = 1 << baseWidget.getID();
        if (baseWidget.value > 0) {
            // value *= -1;
        }
        busID += value;
        this.device.setRedbus_id(busID);
        // not usable !
        // NetworkHandler.sendToServer(new MessageGuiUpdate((BPPart)null, widget.getID(), baseWidget.value));
    }
}
