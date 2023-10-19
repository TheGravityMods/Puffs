package net.gravity.puffs.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.jumbopuff.JumboPuff;
import net.gravity.puffs.event.ModEvents;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class JumboPuffRenderer extends MobRenderer<JumboPuff, JumboPuffModel<JumboPuff>> {
    public JumboPuffRenderer(EntityRendererProvider.Context context) {
        super(context, new JumboPuffModel<>(context.bakeLayer(ModEvents.jumboPuffModelLayerLocation)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(JumboPuff pEntity) {
        if(pEntity.getTextureLocationForChangingRoot() == null) {
            return new ResourceLocation(PuffsMain.MOD_ID, "textures/entity/puffs/" + pEntity.getDisplayName().getString().toLowerCase().replace(" ", "_") + ".png");
        } else {
            return pEntity.getTextureLocationForChangingRoot();
        }
    }

    @Override
    protected void scale(JumboPuff pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
        pMatrixStack.scale(3.0F, 3.0F, 3.0F);
    }
}
