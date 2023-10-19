package net.gravity.puffs.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.WaterProjectile;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class WaterProjectileRenderer extends EntityRenderer<WaterProjectile> {
    private final WaterProjectileModel<WaterProjectile> model;

    public WaterProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new WaterProjectileModel<>(context.bakeLayer(new ModelLayerLocation(new ResourceLocation(PuffsMain.MOD_ID, "water_projectile"), "main")));
    }

    public void render(WaterProjectile pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        Entity entity;
        if(pEntity.getOwner() == null) {
            entity = pEntity;
        } else {
            entity = pEntity.getOwner();
        }
        int i = BiomeColors.getAverageWaterColor(entity.level, entity.blockPosition()) | 0xFF000000;
        float f = (float) (i >> 16 & 255) / 255.0F;
        float f1 = (float) (i >> 8 & 255) / 255.0F;
        float f2 = (float) (i & 255) / 255.0F;
        pMatrixStack.translate(0.0D, (double)-1.125F, 0.0D);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(new ResourceLocation(PuffsMain.MOD_ID, "textures/entity/water_projectile.png")));
        this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, f, f1, f2, 1.0F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(WaterProjectile pEntity) {
        return new ResourceLocation(PuffsMain.MOD_ID, "textures/entity/water_projectile.png");
    }
}