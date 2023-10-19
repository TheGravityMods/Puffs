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

/*
 * This mod includes a small portion of code from Alex's Mobs (https://curseforge.com/minecraft/mc-mods/alexs-mobs).
 * Original code Copyright (C) 2007 Free Software Foundation, Inc. http://fsf.org.
 * Used under the GNU Lesser General Public License, version 3 (or a later version if specified by Alex's Mobs).
 *
 * The original Alex's Mobs code can be found at:
 * https://github.com/AlexModGuy/AlexsMobs
 *
 * Any modifications made to the original code from Alex's Mobs are subject to the same GNU LGPL-3.0 license terms.
 */
public class PuffDisplayItem extends Item {
    public PuffDisplayItem(Properties pProperties) {
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

    public static boolean IsJumbo(@Nullable CompoundTag tag) {
        if (tag != null && tag.contains("JumboPuff")) {
            return tag.getBoolean("JumboPuff");
        }
        return false;
    }
}
