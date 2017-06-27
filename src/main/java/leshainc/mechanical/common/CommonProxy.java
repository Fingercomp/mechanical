package leshainc.mechanical.common;

import leshainc.mechanical.common.block.Blocks;
import leshainc.mechanical.common.tileentity.TileEntities;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Blocks.register();
    }

    public void init(FMLInitializationEvent event) {
        TileEntities.register();
    }

    public void postInit(FMLPostInitializationEvent event) {
        Blocks.setCreativeTab(CreativeTab.instance);
    }
}
