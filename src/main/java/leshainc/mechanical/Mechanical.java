package leshainc.mechanical;

import leshainc.mechanical.common.CommonProxy;
import leshainc.mechanical.network.PacketUpdateBeltConveyor;
import leshainc.mechanical.network.PacketRequestUpdateBeltConveyor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(name = Mechanical.NAME, modid = Mechanical.MODID, version = Mechanical.VERSION)
public class Mechanical {
    public static final String NAME = "Mechanical";
    public static final String MODID = "mechanical";
    public static final String VERSION = "0.1";

    public static Logger log;
    public static SimpleNetworkWrapper network;


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

        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

        network.registerMessage(new PacketUpdateBeltConveyor.Handler(),
                PacketUpdateBeltConveyor.class, 0, Side.CLIENT);
        network.registerMessage(new PacketRequestUpdateBeltConveyor.Handler(),
                PacketRequestUpdateBeltConveyor.class, 1, Side.SERVER);
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
