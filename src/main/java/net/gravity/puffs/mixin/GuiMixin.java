package net.gravity.puffs.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.gravity.puffs.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow @Final protected Minecraft minecraft;
    Gui gui = (Gui) (Object) this;
    private Player player;
    @ModifyArg(method = "render",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z"))
    private MobEffect setI(MobEffect par1) {
        if(minecraft != null && player.hasEffect(ModEffects.CHORUS_NAUSEA.get())) {
            player = minecraft.player;
            return ModEffects.CHORUS_NAUSEA.get();
        }
        return par1;
    }
}
