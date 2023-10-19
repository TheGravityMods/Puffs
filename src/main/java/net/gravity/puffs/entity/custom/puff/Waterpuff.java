package net.gravity.puffs.entity.custom.puff;

import net.gravity.puffs.entity.custom.WaterProjectile;
import net.gravity.puffs.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class Waterpuff extends Puff implements RangedAttackMob {
    public Waterpuff(EntityType<? extends Waterpuff> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.LAVA, -1.0F);
    }
    @Override
    public ItemStack initializeShearItem() {
        return ModItems.WATERPUFF_ROOT.get().getDefaultInstance();
    }

    @Override
    public ItemStack initializeTameItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        return 0;
    }

    @Override
    public boolean isTamable() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.25D, 10, 15.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10 ,true, true, Entity::isOnFire));
        super.registerGoals();
    }

    private void spit(LivingEntity pTarget) {
        WaterProjectile waterProjectile = new WaterProjectile(this.level, this);
        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - waterProjectile.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2) * (double)0.2F;
        waterProjectile.shoot(d0, d1 + d3, d2, 1.5F, 10.0F);
        if (!this.isSilent()) {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_SPIT, this.getSoundSource(), 1.0F, 1.75F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        }

        this.level.addFreshEntity(waterProjectile);
        this.setTarget(null);
    }

    public void aiStep() {
        if (level.isClientSide) {
            if(this.random.nextInt(10) == 0) {
                this.level.addParticle(ParticleTypes.DRIPPING_WATER, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        }

        super.aiStep();
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        spit(pTarget);
    }

    static class WaterpuffNearestAttackableTargetGoal extends NearestAttackableTargetGoal<Player>{
        Player player = (Player) this.mob.getTarget();
        WaterpuffNearestAttackableTargetGoal(Waterpuff pMob) {
            super(pMob, Player.class, true, true);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return player != null && player.isOnFire() && super.canUse();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            if (player.isOnFire() && player != null) {
                return super.canContinueToUse();
            } else {
                this.targetMob = null;
                return false;
            }
        }
    }
}
