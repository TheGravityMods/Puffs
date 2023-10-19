package net.gravity.puffs.entity.custom.puff;

import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.TamablePathfinderMob;
import net.gravity.puffs.entity.custom.jumbopuff.JumboPuff;
import net.gravity.puffs.entity.goals.PuffFollowOwnerGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public abstract class Puff extends TamablePathfinderMob {
    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.MOVEMENT_SPEED, 0.15F).build();
    }
    private static final EntityDataAccessor<Integer> DATA_ROOT_STATE = SynchedEntityData.defineId(Puff.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_GROW_TIME = SynchedEntityData.defineId(Puff.class, EntityDataSerializers.INT);
    public final AnimationState blinkAnimationState = new AnimationState();
    private ItemStack tameItem;
    private ItemStack shearItem;
    int growRootCounter;

    public Puff(EntityType<? extends Puff> p_32485_, Level p_32486_) {
        super(p_32485_, p_32486_);
        tameItem = initializeTameItem();
        shearItem = initializeShearItem();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new PuffGrowRootGoal(this));
        if(this instanceof JumboPuff) {
            this.goalSelector.addGoal(3, new PuffFollowOwnerGoal(this, 1.55D, 4.3F, 3F, true));
        } else {
            this.goalSelector.addGoal(3, new PuffFollowOwnerGoal(this, 1.55D, 0.5F, 0.5F, true));
        }
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public abstract ItemStack initializeShearItem();

    public abstract ItemStack initializeTameItem();

    public boolean isTamable() {
        return true;
    }


    public void setTameItem(ItemStack tameItem) {
        this.tameItem = tameItem;
    }

    public ItemStack getTameItem() {
        return tameItem;
    }

    public void setShearItem(ItemStack shearItem) {
        this.shearItem = shearItem;
    }
    public ItemStack getShearItem() {
        return shearItem;
    }

    public int getRootState() {
        return this.entityData.get(DATA_ROOT_STATE);
    }

    public void setGrowTime(int growTime) {
        this.entityData.set(DATA_GROW_TIME, growTime);
    }

    public int getGrowTime() {
        return this.entityData.get(DATA_GROW_TIME);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ROOT_STATE, 2);
        this.entityData.define(DATA_GROW_TIME, random.nextIntBetweenInclusive(2400, 3000));
    }

    private void setRootState(int rootState) {
        this.entityData.set(DATA_ROOT_STATE, rootState);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if(!pCompound.contains("rootState")) {
            pCompound.putInt("rootState", getRootState());
        }
        if(!pCompound.contains("growTime")) {
            pCompound.putInt("growTime", getGrowTime());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if(pCompound.contains("rootState")) {
            setRootState(pCompound.getInt("rootState"));
        }
        if(pCompound.contains("growTime")) {
            setGrowTime(pCompound.getInt("growTime"));
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        blinkAnimationState.start(this.tickCount);
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (this.level.isClientSide) {
            boolean flag = (itemstack.is(Items.SHEARS) && hasRoot()) || itemstack.is(tameItem.getItem()) || (isOwnedBy(pPlayer) && isTame());
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (itemstack.getItem() == Items.SHEARS) {
                if (!this.level.isClientSide && hasRoot()) {
                    this.shear(SoundSource.PLAYERS);
                    this.gameEvent(GameEvent.SHEAR, pPlayer);
                    itemstack.hurtAndBreak(1, pPlayer, (player) -> {
                        player.broadcastBreakEvent(pHand);
                    });
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.PASS;
                }
            }
            if (this.isTame()) {
                if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                    this.heal((float) itemstack.getFoodProperties(this).getNutrition());
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    this.gameEvent(GameEvent.EAT, this);
                    return InteractionResult.SUCCESS;
                }
                else {
                    InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
                    if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(pPlayer)) {
                        this.setOrderedToSit(!this.isOrderedToSit());
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget((LivingEntity) null);
                        return InteractionResult.SUCCESS;
                    }

                    return interactionresult;
                }
            } else if (itemstack.is(tameItem.getItem()) && isTamable()) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(3) == 0) {
                    this.tame(pPlayer);
                    this.navigation.stop();
                    this.setTarget((LivingEntity) null);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }

                return InteractionResult.SUCCESS;
            }
            return super.mobInteract(pPlayer, pHand);
        }
    }


    public void shear(SoundSource pCategory) {
        this.level.playSound((Player) null, this, SoundEvents.MOOSHROOM_SHEAR, pCategory, 1.0F, 1.0F);
        if (!this.level.isClientSide()) {
            removeRoot();
            ItemStack ponytail = new ItemStack(this.getShearItem().getItem());
            this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0D), this.getZ(), ponytail));
        }
    }

    public boolean isFood(ItemStack pStack) {
        return pStack.is(tameItem.getItem());
    }

    public void tick() {
        if (this.level.isClientSide()) {
            if(random.nextFloat() < 0.008) {
                blinkAnimationState.start(this.tickCount);
            }
        }

        super.tick();
    }

    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

    public boolean hasRoot() {
        return getRootState() == 2;
    }

    public void removeRoot() {
        setRootState(0);
    }

    public void growRoot() {
        if((getRootState() + 1) <= 2) {
            setRootState(getRootState() + 1);
        }
    }

    public ResourceLocation getTextureLocationForChangingRoot() {
        return null;
    }

    static class PuffGrowRootGoal extends Goal {
        private final Puff puff;
        PuffGrowRootGoal(Puff puff) {
            this.puff = puff;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return !puff.hasRoot();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !puff.hasRoot();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            super.tick();
            if(!puff.hasRoot()) {
                if (puff.growRootCounter > this.adjustedTickDelay(puff.getGrowTime())) {
                    puff.growRoot();
                    puff.playSound(SoundEvents.CHICKEN_EGG, 0.45F, 1.0F);
                    puff.growRootCounter = 0;
                }
                ++puff.growRootCounter;
            }
        }
    }
}
