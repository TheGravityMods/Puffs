package net.gravity.puffs.item.custom;

import net.gravity.puffs.entity.client.PuffAsItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;

public class JumboPuffItem extends Item {
    public JumboPuffItem(Properties pProperties) {
        super(pProperties);
    }
    public static boolean hasPuffDisplay(ItemStack stack){
        return stack.getTag() != null && stack.getTag().contains("PuffDisplay");
    }

    public static String getPuffDisplay(ItemStack stack){
        return stack.getTag().getString("PuffDisplay");
    }

    @Override
    public void initializeClient(java.util.function.Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new PuffAsItemProperties());
    }

    @Nullable
    public static EntityType getEntityType(@Nullable CompoundTag tag) {
        if (tag != null && tag.contains("PuffDisplay")) {
            String entityType = tag.getString("PuffDisplay");
            return Registry.ENTITY_TYPE.getOptional(ResourceLocation.tryParse(entityType)).orElse(null);
        }
        return null;
    }
}
