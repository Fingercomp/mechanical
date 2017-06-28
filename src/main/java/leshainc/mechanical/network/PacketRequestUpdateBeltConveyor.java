package leshainc.mechanical.network;

import io.netty.buffer.ByteBuf;
import leshainc.mechanical.common.tileentity.TileEntityBeltConveyor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestUpdateBeltConveyor implements IMessage {
    public static class Handler implements IMessageHandler<PacketRequestUpdateBeltConveyor, PacketUpdateBeltConveyor> {
        @Override
        public PacketUpdateBeltConveyor onMessage(PacketRequestUpdateBeltConveyor message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(message.dimension);
            TileEntityBeltConveyor te = (TileEntityBeltConveyor) world.getTileEntity(message.pos);

            if (te != null) {
                return new PacketUpdateBeltConveyor(te);
            } else {
                return null;
            }
        }
    }

    private BlockPos pos;
    private int dimension;

    private PacketRequestUpdateBeltConveyor(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketRequestUpdateBeltConveyor(TileEntityBeltConveyor te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    @SuppressWarnings("unused")
    public PacketRequestUpdateBeltConveyor() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        dimension = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
    }
}
