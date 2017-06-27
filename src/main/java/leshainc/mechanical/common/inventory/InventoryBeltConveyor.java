package leshainc.mechanical.common.inventory;

import net.minecraftforge.items.ItemStackHandler;

public class InventoryBeltConveyor extends ItemStackHandler {
    public InventoryBeltConveyor(int size) {
        super(size);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
}
