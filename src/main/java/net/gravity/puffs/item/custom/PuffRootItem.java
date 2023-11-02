package net.gravity.puffs.item.custom;

import net.gravity.puffs.entity.custom.puff.Puff;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class PuffRootItem extends Item {
    final EntityType<? extends Puff> associatedPuff;
    public PuffRootItem(Properties pProperties, EntityType<? extends Puff> associatedPuff) {
        super(pProperties);
        this.associatedPuff = associatedPuff;
    }

    public EntityType<? extends Puff> getAssociatedPuff() {
        return associatedPuff;
    }
}
