package net.gravity.puffs.mixin;

import net.gravity.puffs.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusFruitItem.class)
public class ChorusFruitItemMixin {
    @Inject(method = "finishUsingItem", at = @At("TAIL"))
    private void chorusNauseaChance(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, CallbackInfoReturnable<ItemStack> cir) {
        if(pEntityLiving.getRandom().nextFloat() < 0.1) {
            pEntityLiving.addEffect(new MobEffectInstance(ModEffects.CHORUS_NAUSEA.get(), 200, 0));
        }
    }
}
