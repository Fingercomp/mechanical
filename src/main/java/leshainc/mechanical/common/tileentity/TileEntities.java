package leshainc.mechanical.common.tileentity;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntities {
    public static void register() {
        GameRegistry.registerTileEntity(TileEntityBeltConveyor.class, TileEntityBeltConveyor.NAME);
    }
}
