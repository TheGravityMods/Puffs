package net.gravity.puffs.block.entity;

import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PuffsMain.MOD_ID);

    public static final RegistryObject<BlockEntityType<PuffTransformerBlockEntity>> PUFF_TRANSFORMER =
            BLOCK_ENTITIES.register("puff_transformer", () ->
                    BlockEntityType.Builder.of(PuffTransformerBlockEntity::new,
                            ModBlocks.PUFF_TRANSFORMER.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
