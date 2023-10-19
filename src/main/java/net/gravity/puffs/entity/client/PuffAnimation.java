package net.gravity.puffs.entity.client;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class PuffAnimation {

    public static final AnimationDefinition PUFF_WALK = AnimationDefinition.Builder.withLength(1f).looping()
            .addAnimation("leftleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(20f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.5f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1f, KeyframeAnimations.degreeVec(20f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("rightleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.5f, KeyframeAnimations.degreeVec(20f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("head",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 4f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, -4f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 4f),
                                    AnimationChannel.Interpolations.LINEAR))).build();

    public static final AnimationDefinition JUMBO_PUFF_WALK = AnimationDefinition.Builder.withLength(1f).looping()
            .addAnimation("rightleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.5f, KeyframeAnimations.degreeVec(17.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("leftleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(17.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.5f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1f, KeyframeAnimations.degreeVec(17.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("head",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 2f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, -2f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 2f),
                                    AnimationChannel.Interpolations.LINEAR))).build();
    public static final AnimationDefinition BLINK = AnimationDefinition.Builder.withLength(0.2916767f)
            .addAnimation("eyelid_top_left",
                    new AnimationChannel(AnimationChannel.Targets.SCALE,
                            new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 0f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.041676664f, KeyframeAnimations.scaleVec(1f, 0.5f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.125f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.20834334f, KeyframeAnimations.scaleVec(1f, 0.5f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.25f, KeyframeAnimations.scaleVec(1f, 0f, 1f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("eyelid_top_right",
                    new AnimationChannel(AnimationChannel.Targets.SCALE,
                            new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 0f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.041676664f, KeyframeAnimations.scaleVec(1f, 0.5f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.125f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.20834334f, KeyframeAnimations.scaleVec(1f, 0.5f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.25f, KeyframeAnimations.scaleVec(1f, 0f, 1f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("eyelid_bottom_left",
                    new AnimationChannel(AnimationChannel.Targets.SCALE,
                            new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 0f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.041676664f, KeyframeAnimations.scaleVec(1f, 0.5f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.125f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.20834334f, KeyframeAnimations.scaleVec(1f, 0.5f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.25f, KeyframeAnimations.scaleVec(1f, 0f, 1f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("eyelid_bottom_right",
                    new AnimationChannel(AnimationChannel.Targets.SCALE,
                            new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 0f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.041676664f, KeyframeAnimations.scaleVec(1f, 0.5f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.125f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.20834334f, KeyframeAnimations.scaleVec(1f, 0.5f, 1f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.25f, KeyframeAnimations.scaleVec(1f, 0f, 1f),
                                    AnimationChannel.Interpolations.LINEAR))).build();
}
