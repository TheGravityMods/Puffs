package net.gravity.puffs.entity.custom.puff;

import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class Lavapuff extends Puff {
    public Lavapuff(EntityType<? extends Lavapuff> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.WATER, -2.0F);
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.15F).add(Attributes.ATTACK_DAMAGE, 1).build();
    }

    @Override
    public ItemStack initializeShearItem() {
        return ModItems.LAVAPUFF_ROOT.get().getDefaultInstance();
    }

    @Override
    public boolean isInvertedHealAndHarm() {
        return true;
    }

    @Override
    public ItemStack initializeTameItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isTamable() {
        return false;
    }

    @Override
    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new LavapuffAttackGoal(this));
        this.goalSelector.addGoal(2, new LavapuffLeapAtTargetGoal(this, 0.25F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Pig.class, true, (mob) -> !mob.isBaby()));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Cow.class, true, (mob) -> !mob.isBaby()));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Sheep.class, true, (mob) -> !mob.isBaby()));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Chicken.class, true, (mob) -> !mob.isBaby()));
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        pEntity.setSecondsOnFire(10);
        return super.doHurtTarget(pEntity);
    }

    public void aiStep() {
        if (level.isClientSide) {
            if(this.random.nextInt(10) == 0) {
                this.level.addParticle(ParticleTypes.DRIPPING_LAVA, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        } else {
            if (isInWaterOrRain()) {
                hurt(DamageSource.DROWN, 1);
            }
        }

        super.aiStep();
    }

    static class LavapuffAttackGoal extends MeleeAttackGoal {
        public LavapuffAttackGoal(Lavapuff lavapuff) {
            super(lavapuff, 1.0D, true);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle() && !this.mob.isBaby();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            if (this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return 0.3F + pAttackTarget.getBbWidth();
        }
    }

    static class LavapuffLeapAtTargetGoal extends Goal {
        private final Mob mob;
        private LivingEntity target;
        private final float yd;

        public LavapuffLeapAtTargetGoal(Mob pMob, float pYd) {
            this.mob = pMob;
            this.yd = pYd;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (mob.isInWaterRainOrBubble()) {
                PuffsMain.LOGGER.debug("in water");
                return false;
            }
            if (this.mob.isVehicle()) {
                return false;
            } else {
                this.target = this.mob.getTarget();
                if (this.target == null) {
                    return false;
                } else {
                    double d0 = this.mob.distanceToSqr(this.target);
                    if (!(d0 < 4.0D) && !(d0 > 16.0D)) {
                        if (!this.mob.isOnGround()) {
                            return false;
                        } else {
                            return this.mob.getRandom().nextInt(reducedTickDelay(5)) == 0;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return !this.mob.isOnGround();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            Vec3 vec3 = this.mob.getDeltaMovement();
            Vec3 vec31 = new Vec3(this.target.getX() - this.mob.getX(), 0.0D, this.target.getZ() - this.mob.getZ());
            if (vec31.lengthSqr() > 1.0E-7D) {
                vec31 = vec31.normalize().scale(0.4D).add(vec3.scale(0.2D));
            }

            this.mob.setDeltaMovement(vec31.x, (double) this.yd, vec31.z);
        }
    }

}
