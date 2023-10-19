package net.gravity.puffs.entity.client;

import com.google.common.collect.Lists;
import net.gravity.puffs.entity.ModEntities;
import net.gravity.puffs.entity.custom.jumbopuff.JumboPuff;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.minecraft.Util;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class PuffList {
    private static final List<EntityType<? extends Puff>> PUFF_ENTITY_ICONS = Util.make(Lists.newArrayList(), (list) -> {
        list.add(ModEntities.CHORUPUFF.get());
        list.add(ModEntities.BOMBPUFF.get());
        list.add(ModEntities.FLOWERPUFF.get());
        list.add(ModEntities.LAVAPUFF.get());
        list.add(ModEntities.WATERPUFF.get());
    });

    private static final List<EntityType<? extends Puff>> JUMBO_PUFF_ENTITY_ICONS = Util.make(Lists.newArrayList(), (list) -> {
        list.add(ModEntities.JUMBO_CHORUPUFF.get());
    });
    public static List<EntityType<? extends Puff>> getPuffEntityIcons() {
        return PUFF_ENTITY_ICONS;
    }
    public static List<EntityType<? extends Puff>> getJumboPuffEntityIcons() {
        return JUMBO_PUFF_ENTITY_ICONS;
    }
}
