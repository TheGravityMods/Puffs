package net.gravity.puffs.item.custom;

import net.gravity.puffs.entity.ModEntities;
import net.gravity.puffs.entity.custom.puff.Flowerpuff;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class FlowerPuffRootItem extends PuffRootItem {
    final Flowerpuff.FlowerType flowerType;
    public FlowerPuffRootItem(Properties pProperties, Flowerpuff.FlowerType flowerType) {
        super(pProperties, ModEntities.FLOWERPUFF.get());
        this.flowerType = flowerType;
    }

    public Flowerpuff.FlowerType getAssociatedFlowerType() {
        return flowerType;
    }
}
