package net.gravity.puffs.mixin;

import net.gravity.puffs.effect.ModEffects;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Shadow public float oPortalTime;
    @Shadow public float portalTime;
    LocalPlayer localPlayer = (LocalPlayer) (Object) this;

    @ModifyArg(method = "handleNetherPortalClient", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getEffect(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/world/effect/MobEffectInstance;"))
    private MobEffect chorusNauseaTwil(MobEffect par1) {
        if(localPlayer.hasEffect(ModEffects.CHORUS_NAUSEA.get())) {
            return ModEffects.CHORUS_NAUSEA.get();
        } else {
            return MobEffects.CONFUSION;
        }
    }

    @ModifyArg(method = "handleNetherPortalClient", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z"))
    private MobEffect chorusNauseaTwirl(MobEffect par1) {
        if(localPlayer.hasEffect(ModEffects.CHORUS_NAUSEA.get())) {
            return ModEffects.CHORUS_NAUSEA.get();
        } else {
            return MobEffects.CONFUSION;
        }
    }

    @Inject(method = "removeEffectNoUpdate", at = @At(value = "HEAD"))
    private void chorusNauseawil(MobEffect pMobEffect, CallbackInfoReturnable<MobEffectInstance> cir) {
        if(pMobEffect == ModEffects.CHORUS_NAUSEA.get()) {
            this.oPortalTime = 0.0F;
            this.portalTime = 0.0F;
        }
    }
}
