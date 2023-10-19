package net.gravity.puffs.entity.custom.jumbopuff;

import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public abstract class JumboPuff extends Puff implements ItemSteerable {
    private boolean isTraveling;
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(JumboPuff.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(JumboPuff.class, EntityDataSerializers.BOOLEAN);
    private final ItemBasedSteering steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.15F).build();
    }
    public JumboPuff(EntityType<? extends JumboPuff> p_32485_, Level p_32486_) {
        super(p_32485_, p_32486_);
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        super.mobInteract(pPlayer, pHand);
        if(!level.isClientSide) {
            if (!pPlayer.isShiftKeyDown() && isTame()) {
                if (!this.level.isClientSide) {
                    pPlayer.startRiding(this);
                }

                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
        return super.mobInteract(pPlayer, pHand);
    }

    public boolean isTraveling() {
        return isTraveling;
    }

    public void travel(Vec3 pTravelVector) {
        this.setSpeed(this.getMoveSpeed() / 0.4f);
        isTraveling = this.travel(this, this.steering, pTravelVector);
    }

    public double getPassengersRidingOffset() {
        float f = Math.min(0.1F, this.animationSpeed);
        float f1 = this.animationPosition;
        return (double)this.getBbHeight() - 0.19D + (double)(0.12F * Mth.cos(f1 * 1.5F) * 2.0F * f);
    }

    @Nullable
    public Entity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        return this.canBeControlledBy(entity) ? entity : null;
    }

    private boolean canBeControlledBy(Entity p_219127_) {
        return p_219127_ instanceof Player;
    }

    public Vec3 getDismountLocationForPassenger(LivingEntity pLivingEntity) {
        Direction direction = this.getMotionDirection();
        if (direction.getAxis() != Direction.Axis.Y) {
            int[][] aint = DismountHelper.offsetsForDirection(direction);
            BlockPos blockpos = this.blockPosition();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (Pose pose : pLivingEntity.getDismountPoses()) {
                AABB aabb = pLivingEntity.getLocalBoundsForPose(pose);

                for (int[] aint1 : aint) {
                    blockpos$mutableblockpos.set(blockpos.getX() + aint1[0], blockpos.getY(), blockpos.getZ() + aint1[1]);
                    double d0 = this.level.getBlockFloorHeight(blockpos$mutableblockpos);
                    if (DismountHelper.isBlockFloorValid(d0)) {
                        Vec3 vec3 = Vec3.upFromBottomCenterOf(blockpos$mutableblockpos, d0);
                        if (DismountHelper.canDismountTo(this.level, pLivingEntity, aabb.move(vec3))) {
                            pLivingEntity.setPose(pose);
                            return vec3;
                        }
                    }
                }
            }

        }
        return super.getDismountLocationForPassenger(pLivingEntity);
    }

    public float getMoveSpeed() {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    public float getSteeringSpeed() {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }

    public void travelWithInput(Vec3 pTravelVec) {
        super.travel(pTravelVec);
    }

    protected float nextStep() {
        return this.moveDist + 0.6F;
    }
}
