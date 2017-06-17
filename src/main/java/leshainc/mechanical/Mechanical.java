package leshainc.mechanical;

import leshainc.mechanical.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("WeakerAccess")
@Mod(name = Mechanical.NAME, modid = Mechanical.MODID, version = Mechanical.VERSION)
public class Mechanical {
    public static final String NAME = "Mechanical";
    public static final String MODID = "mechanical";
    public static final String VERSION = "0.1";

    public static Logger log;

    @SidedProxy(
            clientSide = "leshainc.mechanical.client.ClientProxy",
            serverSide = "leshainc.mechanical.common.CommonProxy"
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();

        log.info("Pre-initialization begins...");
        proxy.preInit(event);
        log.info("Pre-initialization done.");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        log.info("Initialization begins...");
        proxy.init(event);
        log.info("Initialization done.");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        log.info("Post-initialization begins...");
        proxy.postInit(event);
        log.info("Post-initialization done.");
    }
}
