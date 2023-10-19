package net.gravity.puffs.event;

import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.ModEntities;
import net.gravity.puffs.entity.client.*;
import net.gravity.puffs.entity.custom.jumbopuff.JumboPuff;
import net.gravity.puffs.entity.custom.puff.Lavapuff;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.gravity.puffs.item.ModItems;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

public class ModEvents {
    public static final ModelLayerLocation puffModelLayerLocation = new ModelLayerLocation(new ResourceLocation("puffs", "puff"),"main");
    private static final Supplier<LayerDefinition> puffLayerDefinition = PuffModel::createBodyLayer;
    public static final ModelLayerLocation jumboPuffModelLayerLocation = new ModelLayerLocation(new ResourceLocation("puffs", "jumbo_puff"),"main");
    private static final Supplier<LayerDefinition> jumboPuffLayerDefinition = JumboPuffModel::createBodyLayer;
    @Mod.EventBusSubscriber(modid = PuffsMain.MOD_ID)
    public static class ForgeEvents {
        @Mod.EventBusSubscriber(modid = PuffsMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
        public static class ModEventBusEvents {
            @SubscribeEvent
            public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
                event.put(ModEntities.CHORUPUFF.get(), Puff.setAttributes());
                event.put(ModEntities.BOMBPUFF.get(), Puff.setAttributes());
                event.put(ModEntities.FLOWERPUFF.get(), Puff.setAttributes());
                event.put(ModEntities.LAVAPUFF.get(), Lavapuff.setAttributes());
                event.put(ModEntities.WATERPUFF.get(), Puff.setAttributes());
                event.put(ModEntities.JUMBO_CHORUPUFF.get(), JumboPuff.setAttributes());
            }
        }

        @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PuffsMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
        public static class ModClientEvents {
            @SubscribeEvent
            public void clientTick(TickEvent.ClientTickEvent event) {
                if (event.phase == TickEvent.Phase.START) {
                    PuffAsEntityItemRenderer.addTick();
                }
            }

            @SubscribeEvent
            static void registerItemColor(RegisterColorHandlersEvent.Item event)
            {
                event.register((itemColor, item) -> -12618012, ModItems.WATERPUFF_ROOT.get());
            }

            @SubscribeEvent
            public static void entityRegisterLayerEvent(EntityRenderersEvent.RegisterLayerDefinitions event) {
                event.registerLayerDefinition(puffModelLayerLocation, puffLayerDefinition);
                event.registerLayerDefinition(jumboPuffModelLayerLocation, jumboPuffLayerDefinition);
                event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(PuffsMain.MOD_ID, "water_projectile"), "main"), WaterProjectileModel::createBodyLayer);
            }
        }
    }
}
