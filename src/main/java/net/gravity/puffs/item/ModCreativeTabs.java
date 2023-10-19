package net.gravity.puffs.item;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {
    public static final CreativeModeTab PUFFS_TAB = new CreativeModeTab("puffs_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.PUFF_DISPLAY_ITEM.get());
        }
    };

    public static final CreativeModeTab JUMBO_PUFFS_TAB = new CreativeModeTab("jumbo_puffs_tab") {
        @Override
        public ItemStack makeIcon() {
            ItemStack itemStack = new ItemStack(ModItems.PUFF_DISPLAY_ITEM.get());
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("JumboPuff", true);
            itemStack.setTag(tag);
            return itemStack;
        }
    };
}
