package leshainc.mechanical.client.render;

import leshainc.mechanical.Mechanical;
import leshainc.mechanical.common.block.Blocks;
import leshainc.mechanical.common.block.BlockBeltConveyor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class BlockRenderRegistrar {
    public static void registerBlockRenderer() {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(Item.getItemFromBlock(Blocks.BELT_CONVEYOR), 0,
                        new ModelResourceLocation(Mechanical.MODID + ":" + BlockBeltConveyor.NAME, "inventory"));
    }
}
