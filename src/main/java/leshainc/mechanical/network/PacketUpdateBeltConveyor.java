package leshainc.mechanical.network;

import io.netty.buffer.ByteBuf;
import leshainc.mechanical.common.tileentity.TileEntityBeltConveyor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateBeltConveyor implements IMessage {
    public static class Handler implements IMessageHandler<PacketUpdateBeltConveyor, IMessage> {
        @Override
        public IMessage onMessage(PacketUpdateBeltConveyor message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityBeltConveyor te = (TileEntityBeltConveyor) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                assert(te != null);  // TODO: Remove assert

                for (int slot = 0; slot < message.stacks.length; slot++) {
                    te.inventory.setStackInSlot(slot, message.stacks[slot]);
                }
                te.lastChangeTime = message.lastChangeTime;
            });
            return null;
        }
    }

    private BlockPos pos;
    private long lastChangeTime;
    private ItemStack[] stacks;

    public PacketUpdateBeltConveyor(TileEntityBeltConveyor te) {
        ItemStack[] stacks = new ItemStack[te.inventory.getSlots()];

        for (int slot = 0; slot < te.inventory.getSlots(); slot++) {
            stacks[slot] = te.inventory.getStackInSlot(slot);
        }

        this.pos = te.getPos();
        this.lastChangeTime = te.lastChangeTime;
        this.stacks = stacks;
    }

    @SuppressWarnings("unused")
    public PacketUpdateBeltConveyor() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        lastChangeTime = buf.readLong();

        int length = buf.readInt();
        stacks = new ItemStack[length];
        for (int slot = 0; slot < length; slot++) {
            stacks[slot] = ByteBufUtils.readItemStack(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeLong(lastChangeTime);
        buf.writeInt(stacks.length);
        for (ItemStack stack : stacks) {
            ByteBufUtils.writeItemStack(buf, stack == null ? ItemStack.EMPTY : stack);
        }
    }
}
