package net.gravity.puffs.entity.client;

import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.gravity.puffs.entity.custom.puff.Waterpuff;
import net.gravity.puffs.event.ModEvents;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PuffRenderer extends MobRenderer<Puff, PuffModel<Puff>> {

    public PuffRenderer(EntityRendererProvider.Context context) {
        super(context, new PuffModel<>(context.bakeLayer(ModEvents.puffModelLayerLocation)), 0.3f);
        this.addLayer(new FlowerpuffPotLayer<>(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public ResourceLocation getTextureLocation(Puff pEntity) {
        if(pEntity.getTextureLocationForChangingRoot() == null) {
            return new ResourceLocation(PuffsMain.MOD_ID, "textures/entity/puffs/" + pEntity.getDisplayName().getString().toLowerCase().replace(" ", "_") + ".png");
        } else {
            return pEntity.getTextureLocationForChangingRoot();
        }
    }

    @Nullable
    @Override
    protected RenderType getRenderType(Puff pLivingEntity, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing) {
        if(pLivingEntity instanceof Waterpuff) {
            return RenderType.entityTranslucent(new ResourceLocation(PuffsMain.MOD_ID, "textures/entity/puffs/waterpuff.png"));
        } else {
            return super.getRenderType(pLivingEntity, pBodyVisible, pTranslucent, pGlowing);
        }
    }
}
