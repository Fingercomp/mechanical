package leshainc.mechanical.common.tileentity;

import leshainc.mechanical.common.inventory.InventoryBeltConveyor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

@SuppressWarnings("WeakerAccess")
public class TileEntityBeltConveyor extends TileEntity {
    public static String NAME = "tile_entity_belt_conveyor";
    public static int SIZE = 6;

    public InventoryBeltConveyor inventory;

    public TileEntityBeltConveyor() {
         inventory = new InventoryBeltConveyor(SIZE);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        super.readFromNBT(compound);
    }
}
