package net.gravity.puffs.item;

import net.gravity.puffs.effect.ModEffects;
import net.gravity.puffs.entity.ModEntities;
import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.puff.Flowerpuff;
import net.gravity.puffs.item.custom.*;
import net.gravity.puffs.sound.ModSounds;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PuffsMain.MOD_ID);

    public static final RegistryObject<Item> CHORUPUFF_SPAWN_EGG = ITEMS.register("chorupuff_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.CHORUPUFF, 4661575,8149884, new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB)));
    public static final RegistryObject<Item> BOMBPUFF_SPAWN_EGG = ITEMS.register("bombpuff_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BOMBPUFF, 789516,14928699, new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB)));
    public static final RegistryObject<Item> FLOWERPUFF_SPAWN_EGG = ITEMS.register("flowerpuff_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FLOWERPUFF, 7947060,5848361 , new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB)));
    public static final RegistryObject<Item> LAVAPUFF_SPAWN_EGG = ITEMS.register("lavapuff_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LAVAPUFF, 13717260,15445830 , new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB)));
    public static final RegistryObject<Item> WATERPUFF_SPAWN_EGG = ITEMS.register("waterpuff_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.WATERPUFF, 2314188,5931763 , new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB)));
    public static final RegistryObject<Item> JUMBO_CHORUPUFF_SPAWN_EGG = ITEMS.register("jumbo_chorupuff_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.JUMBO_CHORUPUFF, 4661575,8149884, new Item.Properties().tab(ModCreativeTabs.JUMBO_PUFFS_TAB)));
    public static final RegistryObject<Item> BOMB = ITEMS.register("bomb",
            () -> new BombItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB)));
    public static final RegistryObject<Item> CHORUS_BREAD = ITEMS.register("chorus_bread",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8)
                    .saturationMod(0.68F)
                    .effect(() -> new MobEffectInstance(ModEffects.CHORUS_NAUSEA.get(), 300, 1), 0.2f).build()).tab(ModCreativeTabs.PUFFS_TAB)));
    public static final RegistryObject<Item> CHORUPUFF_ROOT = ITEMS.register("chorupuff_root",
            () -> new PuffRootItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB), ModEntities.CHORUPUFF.get()));
    public static final RegistryObject<Item> BOMBPUFF_ROOT = ITEMS.register("bombpuff_root",
            () -> new PuffRootItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB), ModEntities.BOMBPUFF.get()));
    public static final RegistryObject<Item> LAVAPUFF_ROOT = ITEMS.register("lavapuff_root",
            () -> new PuffRootItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB), ModEntities.LAVAPUFF.get()));
    public static final RegistryObject<Item> WATERPUFF_ROOT = ITEMS.register("waterpuff_root",
            () -> new PuffRootItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB), ModEntities.WATERPUFF.get()));
//    public static final RegistryObject<Item> PUFF_JOURNAL_BOOK = ITEMS.register("puff_journal_book",
//            () -> new PuffJournalBookItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB)));
    public static final RegistryObject<Item> PUFFS_WORLD = ITEMS.register("peaceful_world",
        () -> new RecordItem(7, ModSounds.PEACEFUL_WORLD.get(), (new Item.Properties()).stacksTo(1).tab(ModCreativeTabs.PUFFS_TAB).rarity(Rarity.RARE), 150));


    static {
        registerFlowerRoots();
    }


    public static void registerFlowerRoots() {
        for(Flowerpuff.FlowerType flowerType: Flowerpuff.FlowerType.values()){
            ITEMS.register("flowerpuff_" + flowerType.getName() + "_root", () -> new FlowerPuffRootItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB), flowerType));
        }
    }
    public static final RegistryObject<Item> PUFF_DISPLAY_ITEM = ITEMS.register("puff_display_item",
            () -> new PuffDisplayItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
