package net.gravity.puffs.mixin;

import net.gravity.puffs.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private Minecraft minecraft;
    @ModifyVariable(method = "renderLevel", ordinal = 0, at = @At(value = "LOAD"))
    private int setI(int value) {
        if(this.minecraft.player.hasEffect(MobEffects.CONFUSION)) {
            return 20;
        } else if(this.minecraft.player.hasEffect(ModEffects.CHORUS_NAUSEA.get())) {
            return 70 + this.minecraft.player.getEffect(ModEffects.CHORUS_NAUSEA.get()).getAmplifier();
        } else {
            return 7;
        }
    }
}
