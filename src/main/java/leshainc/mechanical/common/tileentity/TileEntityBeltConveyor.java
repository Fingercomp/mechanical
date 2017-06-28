package leshainc.mechanical.common.tileentity;

import leshainc.mechanical.Mechanical;
import leshainc.mechanical.common.inventory.InventoryBeltConveyor;
import leshainc.mechanical.network.PacketUpdateBeltConveyor;
import leshainc.mechanical.network.PacketRequestUpdateBeltConveyor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@SuppressWarnings("WeakerAccess")
public class TileEntityBeltConveyor extends TileEntity {
    public static String NAME = "tile_entity_belt_conveyor";
    public static int SIZE = 1;
    public static int UPDATE_RANGE = 64;

    public long lastChangeTime;
    public InventoryBeltConveyor inventory = new InventoryBeltConveyor(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            if (!world.isRemote) {
                lastChangeTime = world.getTotalWorldTime();
                Mechanical.network.sendToAllAround(
                        new PacketUpdateBeltConveyor(TileEntityBeltConveyor.this),
                        new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), UPDATE_RANGE));
            }
        }
    };

    @Override
    public void onLoad() {
        if (world.isRemote) {
            Mechanical.network.sendToServer(new PacketRequestUpdateBeltConveyor(this));
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setLong("lastChangeTime", lastChangeTime);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        lastChangeTime = compound.getLong("lastChangeTime");
        super.readFromNBT(compound);
    }
}
