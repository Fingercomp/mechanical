package leshainc.mechanical.common;

import leshainc.mechanical.Mechanical;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@SuppressWarnings("WeakerAccess")
public final class CreativeTab {
    public static CreativeTabs instance = new CreativeTabs(Mechanical.MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.DIAMOND);
        }
    };
}
