package net.gravity.puffs.entity.custom.puff;

import net.gravity.puffs.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class Chorupuff extends Puff {

    public Chorupuff(EntityType<? extends Chorupuff> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }


    @Override
    public ItemStack initializeShearItem() {
        return ModItems.CHORUPUFF_ROOT.get().getDefaultInstance();
    }

    @Override
    public ItemStack initializeTameItem() {
        return new ItemStack(Items.CHORUS_FRUIT);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 2f));
        super.registerGoals();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(!isNoAi()) {
            teleport();
        }
        return super.hurt(pSource, pAmount);
    }

    protected void customServerAiStep() {
        if(!isLeashed() && !(this.hurtTime >= 1)) {
            if (!this.isTame() && !shouldPanic()) {
                if (this.tickCount >= this.getRandom().nextIntBetweenInclusive(80, 120)) {
                    this.teleport();
                    tickCount = 0;
                }

                super.customServerAiStep();
            }
            if (shouldPanic()) {
                if (this.tickCount >= this.getRandom().nextIntBetweenInclusive(60, 80)) {
                    this.teleport();
                    tickCount = 0;
                }

                super.customServerAiStep();
            }
        }
    }

    public boolean shouldPanic() {
        return this.getLastHurtByMob() != null || this.isFreezing() || this.isOnFire();
    }

    protected void teleport() {
        if (!this.level.isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 16.0D;
            double d1 = this.getY() + (double)(this.random.nextInt(16) - 8);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 16.0D;
            this.teleport(d0, d1, d2);
        }
    }

    public void teleportTowards(Entity pTarget) {
        Vec3 vec3 = new Vec3(this.getX() - pTarget.getX(), this.getY(0.5D) - pTarget.getEyeY(), this.getZ() - pTarget.getZ());
        vec3 = vec3.normalize();
        double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3.x * 16.0D;
        double d2 = this.getY() + (double)(this.random.nextInt(16) - 8) - vec3.y * 16.0D;
        double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3.z * 16.0D;
        this.teleport(d1, d2, d3);
    }

    private void teleport(double pX, double pY, double pZ) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pX, pY, pZ);

        while(blockpos$mutableblockpos.getY() > this.level.getMinBuildHeight() && !this.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, pX, pY, pZ);
            if (event.isCanceled()) return;
            Vec3 vec3 = this.position();
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2) {
                this.level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
                if (!this.isSilent()) {
                    this.level.playSound((Player)null, this.xo, this.yo, this.zo, SoundEvents.CHORUS_FRUIT_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                }
            }

        }
    }
}
