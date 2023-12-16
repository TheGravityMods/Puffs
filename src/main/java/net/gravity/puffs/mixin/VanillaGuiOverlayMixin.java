package net.gravity.puffs.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.gravity.puffs.effect.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VanillaGuiOverlay.class)
public class VanillaGuiOverlayMixin {
    VanillaGuiOverlay vanillaGuiOverlay = (VanillaGuiOverlay) (Object) this;
    private static Player player;
    @ModifyArg(method = "lambda$static$4",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z"))
    private static MobEffect setI(MobEffect par1) {
        if(player.hasEffect(ModEffects.CHORUS_NAUSEA.get())) {
            return ModEffects.CHORUS_NAUSEA.get();
        } else {
            return MobEffects.CONFUSION;
        }
    }

    @Inject(method = "lambda$static$4",at = @At(value = "HEAD"))
    private static void setI(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight, CallbackInfo ci) {
        player = gui.getMinecraft().player;
    }
}
