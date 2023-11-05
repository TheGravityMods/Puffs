package net.gravity.puffs.item.custom;

import net.gravity.puffs.block.ModBlocks;
import net.gravity.puffs.block.custom.PuffTransformerBlock;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PuffRootItem extends Item {
    final EntityType<? extends Puff> associatedPuff;
    public PuffRootItem(Properties pProperties, EntityType<? extends Puff> associatedPuff) {
        super(pProperties);
        this.associatedPuff = associatedPuff;
    }

    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.is(ModBlocks.PUFF_TRANSFORMER.get())) {
            return PuffTransformerBlock.tryPlaceBook(pContext.getPlayer(), level, blockpos, blockstate, pContext.getItemInHand()) ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public EntityType<? extends Puff> getAssociatedPuff() {
        return associatedPuff;
    }
}
