package net.gravity.puffs.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.puff.Flowerpuff;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public class PuffDisplayTile extends AbstractWidget {
    EntityType<? extends Puff> puff;

    public PuffDisplayTile(EntityType<? extends Puff> puff, int pX, int pY) {
        super(pX, pY, 26, 26, puff.getDescription());
        this.puff = puff;
    }

    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        this.drawSlot(pPoseStack, minecraft.getTextureManager());
        Puff displayPuff = puff.create(Minecraft.getInstance().level);
        if(displayPuff instanceof Flowerpuff flowerpuff) {
            flowerpuff.setFlowerType(Flowerpuff.FlowerType.POPPY);
        }
        drawEntityOnScreen(pPoseStack, this.x + 13, this.y + 20, 25, false, 30, 225, 0, pMouseX, pMouseY, displayPuff);
        if (isHoveredOrFocused()) {
            this.drawSelection(pPoseStack, minecraft.getTextureManager());
        }

    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        Minecraft.getInstance().setScreen(new PuffJournalDisplayScreen(puff));
    }

    public void drawEntityOnScreen(PoseStack stackIn, int posX, int posY, float scale, boolean follow, double xRot, double yRot, double zRot, float mouseX, float mouseY, Entity entity) {
        float customYaw = posX - mouseX;
        float customPitch = posY - mouseY;
        float f = (float) Math.atan(customYaw / 40.0F);
        float f1 = (float) Math.atan(customPitch / 40.0F);

        if (follow) {
            float setX = f1 * 20.0F;
            float setY = f * 20.0F;
            entity.setXRot(setX);
            entity.setYRot(setY);
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).yBodyRot = setY;
                ((LivingEntity) entity).yBodyRotO = setY;
                ((LivingEntity) entity).yHeadRot = setY;
                ((LivingEntity) entity).yHeadRotO = setY;
            }
        } else {
            f = 0;
            f1 = 0;
        }

        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(posX, posY, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale(scale, scale, scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        quaternion.mul(Vector3f.XN.rotationDegrees((float) xRot));
        quaternion.mul(Vector3f.YP.rotationDegrees((float) yRot));
        quaternion.mul(Vector3f.ZP.rotationDegrees((float) zRot));
        posestack1.mulPose(quaternion);

        Vector3f light0 = Util.make(new Vector3f(1, -1.0F, -1.0F), Vector3f::normalize);
        Vector3f light1 = Util.make(new Vector3f(-1, -1.0F, 1.0F), Vector3f::normalize);
        RenderSystem.setShaderLights(light0, light1);
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880);
        });
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);

        entity.setYRot(0);
        entity.setXRot(0);
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).yBodyRot = 0;
            ((LivingEntity) entity).yHeadRotO = 0;
            ((LivingEntity) entity).yHeadRot = 0;
        }


        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput);
    }

    public boolean isHoveredOrFocused() {
        return super.isHoveredOrFocused();
    }

    private void drawSlot(PoseStack pPoseStack, TextureManager pTextureManager) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0,  new ResourceLocation(PuffsMain.MOD_ID, "textures/gui/puff_journal_widgets.png"));
        pPoseStack.pushPose();
        pPoseStack.translate((double) this.x, (double) this.y, 0.0D);
        blit(pPoseStack, 0, 0, 0.0F, 28.0F, 26, 26, 128, 128);
        pPoseStack.popPose();
    }

    private void drawSelection(PoseStack pPoseStack, TextureManager pTextureManager) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0,  new ResourceLocation(PuffsMain.MOD_ID, "textures/gui/puff_journal_widgets.png"));
        pPoseStack.pushPose();
        pPoseStack.translate((double) this.x, (double) this.y, 0.0D);
        blit(pPoseStack, 0, 0, 26.0F, 28.0F, 26, 26, 128, 128);
        pPoseStack.popPose();
    }
}
