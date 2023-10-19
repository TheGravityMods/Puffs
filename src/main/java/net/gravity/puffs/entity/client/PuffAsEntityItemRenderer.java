package net.gravity.puffs.entity.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.jumbopuff.JumboPuff;
import net.gravity.puffs.entity.custom.puff.Flowerpuff;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.gravity.puffs.item.ModItems;
import net.gravity.puffs.item.custom.PuffDisplayItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This mod includes a small portion of code from Alex's Mobs (https://curseforge.com/minecraft/mc-mods/alexs-mobs).
 * Original code Copyright (C) 2007 Free Software Foundation, Inc. http://fsf.org.
 * Used under the GNU Lesser General Public License, version 3 (or a later version if specified by Alex's Mobs).
 *
 * The original Alex's Mobs code can be found at:
 * https://github.com/AlexModGuy/AlexsMobs
 *
 * Any modifications made to the original code from Alex's Mobs are subject to the same GNU LGPL-3.0 license terms.
 */

public class PuffAsEntityItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static int runningTicks = 0;
    private Map<String, Entity> puffRenderList = new HashMap();
    private List<EntityType> puffAsItemRenderList = new ArrayList<>();

    public static void addTick() {
        runningTicks++;
    }

    public PuffAsEntityItemRenderer() {
        super(null, null);
    }

    public static void drawEntityOnScreen(PoseStack matrixstack, float scale, boolean follow, double xRot, double yRot, double zRot, float mouseX, float mouseY, Entity entity) {
        float f = (float) Math.atan(-mouseX / 40.0F);
        float f1 = (float) Math.atan(mouseY / 40.0F);
        matrixstack.scale(scale, scale, scale);
        entity.setOnGround(false);
        float partialTicks = Minecraft.getInstance().getFrameTime();
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(20.0F);
        float partialTicksForRender = Minecraft.getInstance().isPaused() ? 0 : partialTicks;
        int tick;
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().isPaused()) {
            tick = runningTicks;
        } else {
            tick = Minecraft.getInstance().player.tickCount;
        }
        if (follow) {
            float yaw = f * 45.0F;
            entity.setYRot(yaw);
            entity.tickCount = tick;
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).yBodyRot = yaw;
                ((LivingEntity) entity).yBodyRotO = yaw;
                ((LivingEntity) entity).yHeadRot = yaw;
                ((LivingEntity) entity).yHeadRotO = yaw;
            }

            quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
            quaternion.mul(quaternion1);
        }

        matrixstack.mulPose(quaternion);
        matrixstack.mulPose(Vector3f.XP.rotationDegrees((float) (-xRot)));
        matrixstack.mulPose(Vector3f.YP.rotationDegrees((float) yRot));
        matrixstack.mulPose(Vector3f.ZP.rotationDegrees((float) zRot));
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicksForRender, matrixstack, multibuffersource$buffersource, 15728880);
        });
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        entity.setYRot(0.0F);
        entity.setXRot(0.0F);
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).yBodyRot = 0.0F;
            ((LivingEntity) entity).yHeadRotO = 0.0F;
            ((LivingEntity) entity).yHeadRot = 0.0F;
        }
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemTransforms.TransformType transformType, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        int tick;
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().isPaused()) {
            tick = runningTicks;
        } else {
            tick = Minecraft.getInstance().player.tickCount;
        }
        if (itemStackIn.getItem() == ModItems.PUFF_DISPLAY_ITEM.get()) {
            List<EntityType<? extends Puff>> puffIcons;
            if(PuffDisplayItem.IsJumbo(itemStackIn.getTag())) {
                puffIcons = PuffList.getJumboPuffEntityIcons();
            } else {
                puffIcons = PuffList.getPuffEntityIcons();
            }
            int entityIndex = (tick / 35) % (puffIcons.size());
            Entity renderEntity = null;
            float scale = 1.5F;
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                if (PuffDisplayItem.hasPuffDisplay(itemStackIn)) {
                    String index = PuffDisplayItem.getPuffDisplay(itemStackIn);
                    EntityType local = PuffDisplayItem.getEntityType(itemStackIn.getTag());
                    if (this.puffRenderList.get(index) == null && !puffAsItemRenderList.contains(local)) {
                        try {
                            Entity entity = local.create(level);
                            this.puffRenderList.put(local.getDescriptionId(), entity);
                            renderEntity = entity;
                        } catch (Exception e) {
                            puffAsItemRenderList.add(local);
                            PuffsMain.LOGGER.error("Could not render item for entity: " + local);
                        }
                    } else {
                        renderEntity = this.puffRenderList.get(local.getDescriptionId());
                    }
                } else {
                    EntityType<? extends Puff> type = puffIcons.get(entityIndex);
                    if (this.puffRenderList.get(type.getDescriptionId()) == null && !puffAsItemRenderList.contains(type)) {
                        try {
                            Entity entity = type.create(level);
                            this.puffRenderList.put(type.getDescriptionId(), entity);
                            renderEntity = entity;
                        } catch (Exception e) {
                            puffAsItemRenderList.add(type);
                            PuffsMain.LOGGER.error("Could not render item for entity: " + type);
                        }
                    } else {
                        renderEntity = this.puffRenderList.get(type.getDescriptionId());
                    }
                }
                if(renderEntity instanceof JumboPuff) {
                    scale = 0.5f;
                }
                if(renderEntity instanceof Flowerpuff flowerpuff) {
                    flowerpuff.setFlowerType(Flowerpuff.FlowerType.POPPY);
                }
                if (renderEntity != null) {
                    matrixStackIn.translate(0.5F, 0F, 0);
                    matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180F));
                    matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180F));
                    try {
                        drawEntityOnScreen(matrixStackIn, scale, true, 0, -45, 0, 230F, 85F, renderEntity);
                    } catch (Exception ignored) {

                    }
                }
            }
        }
    }
}
