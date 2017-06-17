package leshainc.mechanical.common.block;

import net.minecraft.creativetab.CreativeTabs;

public final class Blocks {
    public static BlockBeltConveyor BELT_CONVEYOR;

    public static void register() {
        BELT_CONVEYOR = new BlockBeltConveyor();
    }

    public static void setCreativeTab(CreativeTabs tab) {
        BELT_CONVEYOR.setCreativeTab(tab);
    }
}
