package net.gravity.puffs.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.gravity.puffs.entity.custom.puff.Flowerpuff;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FlowerpuffPotLayer<T extends Puff> extends RenderLayer<T, PuffModel<T>> {
    private final BlockRenderDispatcher blockRenderer;

    public FlowerpuffPotLayer(RenderLayerParent<T, PuffModel<T>> p_234850_, BlockRenderDispatcher p_234851_) {
        super(p_234850_);
        this.blockRenderer = p_234851_;
    }
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        BlockState blockstate;
        if (pLivingEntity instanceof Flowerpuff flowerpuff && flowerpuff.hasPot() && flowerpuff.isDisguised() && !pLivingEntity.hasEffect(MobEffects.INVISIBILITY)) {
            if(flowerpuff.hasRoot()) {
                switch (flowerpuff.getCurrentFlowerType()) {
                    default -> blockstate = Blocks.POTTED_POPPY.defaultBlockState();
                    case DANDELION -> blockstate = Blocks.POTTED_DANDELION.defaultBlockState();
                    case BLUE_ORCHID -> blockstate = Blocks.POTTED_BLUE_ORCHID.defaultBlockState();
                    case ALLIUM -> blockstate = Blocks.POTTED_ALLIUM.defaultBlockState();
                    case AZURE_BLUET -> blockstate = Blocks.POTTED_AZURE_BLUET.defaultBlockState();
                    case RED_TULIP -> blockstate = Blocks.POTTED_RED_TULIP.defaultBlockState();
                    case ORANGE_TULIP -> blockstate = Blocks.POTTED_ORANGE_TULIP.defaultBlockState();
                    case WHITE_TULIP -> blockstate = Blocks.POTTED_WHITE_TULIP.defaultBlockState();
                    case PINK_TULIP -> blockstate = Blocks.POTTED_PINK_TULIP.defaultBlockState();
                    case OXEYE_DAISY -> blockstate = Blocks.POTTED_OXEYE_DAISY.defaultBlockState();
                    case CORNFLOWER -> blockstate = Blocks.POTTED_CORNFLOWER.defaultBlockState();
                    case LILY_OF_THE_VALLEY -> blockstate = Blocks.POTTED_LILY_OF_THE_VALLEY.defaultBlockState();
                    case WITHER_ROSE -> blockstate = Blocks.POTTED_WITHER_ROSE.defaultBlockState();
                }
            } else {
                blockstate = Blocks.FLOWER_POT.defaultBlockState();
            }
            int i = LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F);
            BakedModel bakedmodel = this.blockRenderer.getBlockModel(blockstate);
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.0D, 1D, 0.0D);
            pMatrixStack.scale(-1F, -1F, 1F);
            pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(-180.0F));
            pMatrixStack.translate(-0.5D, -0.5D, -0.5D);
            this.renderPotBlock(pMatrixStack, pBuffer, pPackedLight, blockstate, i, bakedmodel);
            pMatrixStack.popPose();
        }
    }

    private void renderPotBlock(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, BlockState pState, int pPackedOverlay, BakedModel pModel) {
        this.blockRenderer.renderSingleBlock(pState, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
    }
}