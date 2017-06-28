package leshainc.mechanical.client.render;

import leshainc.mechanical.common.inventory.InventoryBeltConveyor;
import leshainc.mechanical.common.tileentity.TileEntityBeltConveyor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.vecmath.Point3d;

@SuppressWarnings("WeakerAccess")
public class RenderBeltConveyor extends TileEntitySpecialRenderer {
    private static Point3d[] SLOT_POSITIONS = new Point3d[] {
            new Point3d(0.0, 0.0, 0.0)
    };

    private RenderItem renderItem;

    public RenderBeltConveyor() {
        this.renderItem = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void renderTileEntityAt(TileEntity inTe, double x, double y, double z, float partialTick, int destroyStage) {
        TileEntityBeltConveyor te = (TileEntityBeltConveyor) inTe;
        InventoryBeltConveyor inventory = te.inventory;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.scale(0.45, 0.45, 0.45);

        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                renderItemStackAt(stack, SLOT_POSITIONS[slot]);
            }
        }

        GlStateManager.popMatrix();
    }

    private void renderItemStackAt(ItemStack stack, Point3d pos) {
        renderItemStackAt(stack, pos.getX(), pos.getY(), pos.getZ());
    }

    private void renderItemStackAt(ItemStack stack, double x, double y, double z) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        renderItem.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
    }
}
