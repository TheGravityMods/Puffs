package net.gravity.puffs.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.gravity.puffs.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ChorusNauseaOverlay {
    private static final ResourceLocation NAUSEA_LOCATION = new ResourceLocation("textures/misc/nausea.png");
    public static final IGuiOverlay CHORUS_NAUSEA_OVERLAY = ((gui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = gui.getMinecraft();
        Player player = minecraft.player;
//        float f = Mth.lerp(partialTick, ((LocalPlayer) player).oPortalTime, ((LocalPlayer) player).portalTime);
//        float f1 = minecraft.options.screenEffectScale().get().floatValue();

        if(player.hasEffect(ModEffects.CHORUS_NAUSEA.get())) {
            renderConfusionOverlay(1f, minecraft);
        }
    });

    private static void renderConfusionOverlay(float pScalar, Minecraft minecraft) {
        int i = minecraft.getWindow().getGuiScaledWidth();
        int j = minecraft.getWindow().getGuiScaledHeight();
        double d0 = Mth.lerp((double) pScalar, 2.0D, 1.0D);
        float f = 0.45F * pScalar;
        float f1 = 0.2F * pScalar;
        float f2 = 0.6F * pScalar;
        double d1 = (double) i * d0;
        double d2 = (double) j * d0;
        double d3 = ((double) i - d1) / 2.0D;
        double d4 = ((double) j - d2) / 2.0D;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        RenderSystem.setShaderColor(f, f1, f2, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, NAUSEA_LOCATION);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(d3, d4 + d2, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(d3 + d1, d4 + d2, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(d3 + d1, d4, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(d3, d4, -90.0D).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

}
