package leshainc.mechanical.client;

import leshainc.mechanical.client.render.BlockRenderRegistrar;
import leshainc.mechanical.common.CommonProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override

    public void init(FMLInitializationEvent event) {
        super.init(event);
        BlockRenderRegistrar.registerBlockRenderer();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
