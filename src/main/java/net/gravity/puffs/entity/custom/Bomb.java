package net.gravity.puffs.entity.custom;

import net.gravity.puffs.entity.ModEntities;
import net.gravity.puffs.item.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Bomb extends ThrowableItemProjectile {
    public Bomb(EntityType<Bomb> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public Bomb(Level pLevel, LivingEntity pShooter) {
        super(ModEntities.BOMB.get(), pShooter, pLevel);
    }

    public Bomb(Level pLevel, double pX, double pY, double pZ) {
        super(ModEntities.BOMB.get(), pX, pY, pZ, pLevel);
    }

    protected Item getDefaultItem() {
        return ModItems.BOMB.get();
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            this.discard();
            level.explode(null, this.getX(), this.getY(), this.getZ(), 2, Explosion.BlockInteraction.BREAK);
        }
    }
}
