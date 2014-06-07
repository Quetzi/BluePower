package net.quetzi.bluepower.tileentities.tier1;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.quetzi.bluepower.network.PacketPipeline;
import net.quetzi.bluepower.network.packets.PacketUpdateAlloyFurnaceGUI;
import net.quetzi.bluepower.tileentities.TileBase;

public class TileAlloyFurnace extends TileBase implements IInventory
{

    private boolean isActive;
    private int     currentBurnTime;
    private int     maxBurnTime;
    private boolean metaSet = false;
    private ItemStack[] inventory;
    private ItemStack   fuelInventory;
    private ItemStack   outputInventory;

    public TileAlloyFurnace()
    {
        inventory = new ItemStack[9];
    }


    /*************** BASIC TE FUNCTIONS **************/

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound)
    {
        super.readFromNBT(tCompound);
        isActive = tCompound.getBoolean("isActive");
        metaSet = false;

        for (int i = 0; i < 9; i++) {
            NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
            inventory[i] = ItemStack.loadItemStackFromNBT(tc);
        }
        fuelInventory = ItemStack.loadItemStackFromNBT(tCompound.getCompoundTag("fuelInventory"));
        outputInventory = ItemStack.loadItemStackFromNBT(tCompound.getCompoundTag("outputInventory"));
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public void writeToNBT(NBTTagCompound tCompound)
    {
        super.writeToNBT(tCompound);
        tCompound.setBoolean("isActive", isActive);

        for (int i = 0; i < 9; i++) {
            if (inventory[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                inventory[i].writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
            }
        }
        if (fuelInventory != null) {
            NBTTagCompound fuelCompound = new NBTTagCompound();
            fuelInventory.writeToNBT(fuelCompound);
            tCompound.setTag("fuelInventory", fuelCompound);
        }

        if (outputInventory != null) {
            NBTTagCompound outputCompound = new NBTTagCompound();
            outputInventory.writeToNBT(outputCompound);
            tCompound.setTag("outputInventory", outputCompound);
        }
    }

    /**
     * Function gets called every tick.
     * Do not forget to call the super method!
     */
    @Override
    public void updateEntity()
    {
        super.updateEntity();

        //Check if the meta is already set after loading the NBT.
        if (!metaSet) {
            metaSet = true;
            if (isActive) {
                int newMeta = getBlockMetadata();
                newMeta = newMeta & 7;
                newMeta |= (isActive == true ? 8 : 0);
                getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 2);
            }
        }

        boolean needsUpdate = false;
        setIsActive(currentBurnTime > 0);
        if (isActive) {
            currentBurnTime--;
            needsUpdate = true;
            //Work function, right here
        }
        if (!worldObj.isRemote) {
            //Do checks if the furnace should run, right here please
            if (currentBurnTime <= 0 && TileEntityFurnace.isItemFuel(fuelInventory)) {
                //Put new item in
                currentBurnTime = maxBurnTime = TileEntityFurnace.getItemBurnTime(fuelInventory) + 1;
                if (fuelInventory != null) {
                    fuelInventory.stackSize--;
                    if (fuelInventory.stackSize <= 0) {
                        fuelInventory = fuelInventory.getItem().getContainerItem(fuelInventory);
                    }
                    needsUpdate = true;
                }
            }

        }

        if (needsUpdate) {
            PacketPipeline.instance.sendToAllAround(new PacketUpdateAlloyFurnaceGUI(xCoord, yCoord, zCoord, maxBurnTime, currentBurnTime), getWorldObj());
        }
    }

    @Override
    protected void redstoneChanged(boolean newValue)
    {
        //setIsActive(newValue);
    }

    @SideOnly(Side.CLIENT)
    public void setBurnTicks(int _maxBurnTime, int _currentBurnTime)
    {
        maxBurnTime = _maxBurnTime;
        currentBurnTime = _currentBurnTime;
    }

    public float getBurningPercentage()
    {
        if (maxBurnTime > 0) {
            return ((float) currentBurnTime / (float) maxBurnTime);
        } else {
            return 0;
        }
    }

    /**
     * ************* ADDED FUNCTIONS *************
     */

    public boolean getIsActive()
    {
        return isActive;
    }

    public void setIsActive(boolean _isActive)
    {
        isActive = _isActive;
        int newMeta = getBlockMetadata();
        newMeta = newMeta & 7;
        newMeta |= (_isActive == true ? 8 : 0);
        getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 2);
    }

    /**
     * ************ IINVENTORY ****************
     */

    @Override
    public int getSizeInventory()
    {
        return 9 + 1 + 1; //9 inventory, 1 fuel, 1 output
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        if (var1 == 0) {
            return fuelInventory;
        } else if (var1 == 1) {
            return outputInventory;
        } else if (var1 < 11) {
            return inventory[var1 - 2];
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2)
    {
        ItemStack tInventory = getStackInSlot(var1);

        if (tInventory == null) {
            return null;
        }

        ItemStack ret = null;
        if (tInventory.stackSize < var2) {
            ret = tInventory;
            inventory = null;
        } else {
            ret = tInventory.splitStack(var2);
            if (tInventory.stackSize <= 0) {
                if (var2 == 0) {
                    fuelInventory = null;
                } else if (var2 == 1) {
                    outputInventory = null;
                } else {
                    inventory[var2 - 2] = null;
                }
            }
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

        return ret;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        return getStackInSlot(var1);
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack itemStack)
    {
        if (var1 == 0) {
            fuelInventory = itemStack;
        } else if (var1 == 1) {
            //This shouldn't happen, this is the output slot
        } else {
            inventory[var1 - 2] = itemStack;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public String getInventoryName()
    {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        //Todo: Some fancy code here that detects whether the player is far away
        return true;
    }

    @Override
    public void openInventory() { }

    @Override
    public void closeInventory() { }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack itemStack)
    {
        if (var1 == 0) {
            return TileEntityFurnace.isItemFuel(itemStack);
        } else if (var1 == 1) { //Output slot
            return false;
        } else {
            //What items do we want here? Maybe even check the recipes here.
            return true;
        }
        //return false;
    }

}
