package net.gravity.puffs;

import com.mojang.logging.LogUtils;
import net.gravity.puffs.effect.ModEffects;
import net.gravity.puffs.entity.ModEntities;
import net.gravity.puffs.entity.client.JumboPuffRenderer;
import net.gravity.puffs.entity.client.PuffRenderer;
import net.gravity.puffs.entity.client.WaterProjectileRenderer;
import net.gravity.puffs.entity.custom.puff.Lavapuff;
import net.gravity.puffs.item.ModItems;
import net.gravity.puffs.sound.ModSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(PuffsMain.MOD_ID)
public class PuffsMain {
    public static final String MOD_ID = "puffs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PuffsMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModEffects.register(modEventBus);
        ModSounds.register(modEventBus);
//        ModBiomes.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(ModEntities.LAVAPUFF.get(),
                    SpawnPlacements.Type.IN_LAVA, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Lavapuff::checkLavapuffSpawnRules);
            SpawnPlacements.register(ModEntities.WATERPUFF.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Mob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntities.FLOWERPUFF.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Mob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntities.CHORUPUFF.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Mob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntities.BOMBPUFF.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Mob::checkMobSpawnRules);
        });
//        initEndBiomes();
    }
//    private void initEndBiomes(){
//        TheEndBiomes.addHighlandsBiome(ModBiomes.CHORUS_FOREST.getKey(), 0.5);
//    }
//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event) {
//        SurfaceRule.addEndSurfaceRules(event.getServer().getWorldData(), ModSurfaceRules.makeRules());
//    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.CHORUPUFF.get(), PuffRenderer::new);
            EntityRenderers.register(ModEntities.BOMBPUFF.get(), PuffRenderer::new);
            EntityRenderers.register(ModEntities.FLOWERPUFF.get(), PuffRenderer::new);
            EntityRenderers.register(ModEntities.LAVAPUFF.get(), PuffRenderer::new);
            EntityRenderers.register(ModEntities.WATERPUFF.get(), PuffRenderer::new);
            EntityRenderers.register(ModEntities.BOMB.get(), ThrownItemRenderer::new);
            EntityRenderers.register(ModEntities.WATER_PROJECTILE.get(), WaterProjectileRenderer::new);
            EntityRenderers.register(ModEntities.JUMBO_CHORUPUFF.get(), JumboPuffRenderer::new);
        }
    }
}
