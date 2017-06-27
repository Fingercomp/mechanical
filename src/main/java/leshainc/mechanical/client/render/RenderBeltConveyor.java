package leshainc.mechanical.client.render;

// import leshainc.mechanical.common.tileentity.TileEntityBeltConveyor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

@SuppressWarnings("WeakerAccess")
public class RenderBeltConveyor extends TileEntitySpecialRenderer {
    private RenderItem renderItem;

    public RenderBeltConveyor() {
        this.renderItem = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void renderTileEntityAt(TileEntity inTe, double x, double y, double z, float partialTick, int destroyStage) {
//        TileEntityBeltConveyor te = (TileEntityBeltConveyor) inTe;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        ItemStack stack = new ItemStack(net.minecraft.init.Blocks.CACTUS);  // TODO: Get ItemStack from TileEntityBeltConveyor

        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5, 0.5, 0.5);

        renderItem.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);

        GlStateManager.disableRescaleNormal();

        GlStateManager.popMatrix();
    }
}
