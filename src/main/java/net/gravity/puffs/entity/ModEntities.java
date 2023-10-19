package net.gravity.puffs.entity;

import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.*;
import net.gravity.puffs.entity.custom.jumbopuff.JumboChorupuff;
import net.gravity.puffs.entity.custom.puff.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PuffsMain.MOD_ID);

    public static final RegistryObject<EntityType<Chorupuff>> CHORUPUFF =
            ENTITY_TYPES.register("chorupuff",
                    () -> EntityType.Builder.of(Chorupuff::new, MobCategory.CREATURE)
                            .sized(0.35F, 0.4F)
                            .clientTrackingRange(8)
                            .build(new ResourceLocation(PuffsMain.MOD_ID, "chorupuff").toString()));
    public static final RegistryObject<EntityType<Bombpuff>> BOMBPUFF =
            ENTITY_TYPES.register("bombpuff",
                    () -> EntityType.Builder.of(Bombpuff::new, MobCategory.CREATURE)
                            .sized(0.35F, 0.4F)
                            .clientTrackingRange(8)
                            .build(new ResourceLocation(PuffsMain.MOD_ID, "bombpuff").toString()));

    public static final RegistryObject<EntityType<Flowerpuff>> FLOWERPUFF =
            ENTITY_TYPES.register("flowerpuff",
                    () -> EntityType.Builder.of(Flowerpuff::new, MobCategory.CREATURE)
                            .sized(0.35F, 0.4F)
                            .clientTrackingRange(8)
                            .build(new ResourceLocation(PuffsMain.MOD_ID, "flowerpuff").toString()));

    public static final RegistryObject<EntityType<Lavapuff>> LAVAPUFF =
            ENTITY_TYPES.register("lavapuff",
                    () -> EntityType.Builder.of(Lavapuff::new, MobCategory.CREATURE)
                            .sized(0.35F, 0.4F)
                            .clientTrackingRange(8)
                            .fireImmune()
                            .build(new ResourceLocation(PuffsMain.MOD_ID, "lavapuff").toString()));

    public static final RegistryObject<EntityType<Waterpuff>> WATERPUFF =
            ENTITY_TYPES.register("waterpuff",
                    () -> EntityType.Builder.of(Waterpuff::new, MobCategory.CREATURE)
                            .sized(0.35F, 0.4F)
                            .clientTrackingRange(8)
                            .fireImmune()
                            .build(new ResourceLocation(PuffsMain.MOD_ID, "waterpuff").toString()));

    public static final RegistryObject<EntityType<Bomb>> BOMB =
            ENTITY_TYPES.register("bomb",
                    () -> EntityType.Builder.<Bomb>of(Bomb::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(new ResourceLocation(PuffsMain.MOD_ID, "bomb").toString()));

    public static final RegistryObject<EntityType<WaterProjectile>> WATER_PROJECTILE =
            ENTITY_TYPES.register("water_projectile",
                    () -> EntityType.Builder.<WaterProjectile>of(WaterProjectile::new, MobCategory.MISC)
                            .sized(0.375F, 0.375F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(new ResourceLocation(PuffsMain.MOD_ID, "water_projectile").toString()));

    public static final RegistryObject<EntityType<JumboChorupuff>> JUMBO_CHORUPUFF =
            ENTITY_TYPES.register("jumbo_chorupuff",
                    () -> EntityType.Builder.of(JumboChorupuff::new, MobCategory.CREATURE)
                            .sized(0.9F, 1.8F)
                            .clientTrackingRange(8)
                            .build(new ResourceLocation(PuffsMain.MOD_ID, "jumbo_chorupuff").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
