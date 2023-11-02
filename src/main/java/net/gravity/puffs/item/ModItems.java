package net.gravity.puffs.item;

import net.gravity.puffs.entity.ModEntities;
import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.puff.Flowerpuff;
import net.gravity.puffs.item.custom.*;
import net.minecraft.world.item.Item;
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
    public static final RegistryObject<Item> CHORUPUFF_ROOT = ITEMS.register("chorupuff_root",
            () -> new PuffRootItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB), ModEntities.CHORUPUFF.get()));
    public static final RegistryObject<Item> BOMBPUFF_ROOT = ITEMS.register("bombpuff_root",
            () -> new PuffRootItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB), ModEntities.BOMBPUFF.get()));
    public static final RegistryObject<Item> LAVAPUFF_ROOT = ITEMS.register("lavapuff_root",
            () -> new PuffRootItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB), ModEntities.LAVAPUFF.get()));
    public static final RegistryObject<Item> WATERPUFF_ROOT = ITEMS.register("waterpuff_root",
            () -> new PuffRootItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB), ModEntities.WATERPUFF.get()));
//    public static final RegistryObject<Item> BLANKPUFF_ROOT_LV_1 = ITEMS.register("blankpuff_root_lv_1",
//            () -> new PuffRootItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB)));
//    public static final RegistryObject<Item> BLANKPUFF_ROOT_LV_2 = ITEMS.register("blankpuff_root_lv_2",
//            () -> new PuffRootItem(new Item.Properties().tab(ModCreativeTabs.PUFFS_TAB)));

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
