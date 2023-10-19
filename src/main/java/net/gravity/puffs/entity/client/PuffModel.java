package net.gravity.puffs.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.gravity.puffs.entity.custom.jumbopuff.JumboPuff;
import net.gravity.puffs.entity.custom.puff.Bombpuff;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.gravity.puffs.entity.custom.puff.Waterpuff;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.entity.Entity;


public class PuffModel<T extends Puff> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart headtail;
    private final ModelPart head;
    private final ModelPart leftleg;
    private final ModelPart rightleg;
    private final ModelPart eyelid_top_right;
    private final ModelPart eyelid_top_left;
    private final ModelPart eyelid_bottom_right;
    private final ModelPart eyelid_bottom_left;
    private Entity entity;


    public PuffModel(ModelPart modelPart) {
        this.root = modelPart;
        this.head = root.getChild("head");
        this.headtail = head.getChild("headtail");
        this.leftleg = root.getChild("leftleg");
        this.rightleg = root.getChild("rightleg");
        this.eyelid_top_right = head.getChild("eyelid_top_right");
        this.eyelid_top_left = head.getChild("eyelid_top_left");
        this.eyelid_bottom_right = head.getChild("eyelid_bottom_right");
        this.eyelid_bottom_left = head.getChild("eyelid_bottom_left");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(8, 11).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 22.5F, 0.0F));
        partdefinition.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 22.5F, 0.0F));
        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -5.0F, -4.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 1.0F));
        head.addOrReplaceChild("headtail", CubeListBuilder.create().texOffs(0, 4).addBox(1.0F, -9.9197F, -0.8518F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -9.9197F, -0.8518F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 11).addBox(0.0F, -10.9197F, -0.8518F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.9197F, -0.6482F));

        head.addOrReplaceChild("eyelid_top_right", CubeListBuilder.create().texOffs(30, 0).addBox(-0.51F, -0.02F, 0.0F, 1.02F, 1.02F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.51F, -4.0F, -4.0001F));
        head.addOrReplaceChild("eyelid_top_left", CubeListBuilder.create().texOffs(30, 0).addBox(-0.5F, -0.02F, 0.0009F, 1.02F, 1.02F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -4.0F, -4.001F));
        head.addOrReplaceChild("eyelid_bottom_right", CubeListBuilder.create().texOffs(30, 1).addBox(-0.52F, -1.0F, 0.0F, 1.02F, 1.02F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -2.0F, -4.0001F));
        head.addOrReplaceChild("eyelid_bottom_left", CubeListBuilder.create().texOffs(30, 1).addBox(-0.5F, -1.0F, 0.0009F, 1.02F, 1.02F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -2.0F, -4.001F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        entity = pEntity;
        eyelid_top_right.visible = false;
        eyelid_top_left.visible = false;
        eyelid_bottom_right.visible = false;
        eyelid_bottom_left.visible = false;
        super.prepareMobModel(pEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
    }

    @Override
    public void renderToBuffer(PoseStack p_170625_, VertexConsumer p_170626_, int p_170627_, int p_170628_, float p_170629_, float p_170630_, float p_170631_, float p_170632_) {
        if(entity instanceof Waterpuff waterpuff) {
            int i = BiomeColors.getAverageWaterColor(waterpuff.level, waterpuff.blockPosition()) | 0xFF000000;
            float f = (float) (i >> 16 & 255) / 255.0F;
            float f1 = (float) (i >> 8 & 255) / 255.0F;
            float f2 = (float) (i & 255) / 255.0F;
            super.renderToBuffer(p_170625_, p_170626_, p_170627_, p_170628_, f, f1, f2, p_170632_);
        } else {
            super.renderToBuffer(p_170625_, p_170626_, p_170627_, p_170628_, p_170629_, p_170630_, p_170631_, p_170632_);
        }
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float p_102396_, float p_102397_) {
        root().getAllParts().forEach(ModelPart::resetPose);
        animateWalk(PuffAnimation.PUFF_WALK, pLimbSwing, pLimbSwingAmount, 5.5F, 2.5F);
        if(pEntity.getRootState() == 0) {
            headtail.y = headtail.y + 5;
        } else if(pEntity.getRootState() == 1) {
            headtail.y = headtail.y + 2;
        }  else if(pEntity.getRootState() == 2) {
            headtail.y = headtail.y + 0;
        }
        if(pEntity instanceof Bombpuff bombpuff && !bombpuff.isIgnited()) {
            animate(pEntity.blinkAnimationState, PuffAnimation.BLINK, pAgeInTicks);
        } else {
            animate(pEntity.blinkAnimationState, PuffAnimation.BLINK, pAgeInTicks);
        }
        if(pEntity.blinkAnimationState.isStarted()) {
            eyelid_top_right.visible = true;
            eyelid_top_left.visible = true;
            eyelid_bottom_right.visible = true;
            eyelid_bottom_left.visible = true;
        } else {
            eyelid_top_right.visible = false;
            eyelid_top_left.visible = false;
            eyelid_bottom_right.visible = false;
            eyelid_bottom_left.visible = false;
        }
    }

    protected void animateWalk(AnimationDefinition p_268159_, float p_268057_, float p_268347_, float p_268138_, float p_268165_) {
        Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
        long i = (long)(p_268057_ * 50.0F * p_268138_);
        float f = Math.min(p_268347_ * p_268165_, 1.0F);
        KeyframeAnimations.animate(this, p_268159_, i, f, ANIMATION_VECTOR_CACHE);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
