package net.gravity.puffs.entity.custom.puff;

import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.goals.BombpuffRunAroundLikeCrazyGoal;
import net.gravity.puffs.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Collection;

public class Bombpuff extends Puff implements PowerableMob {
    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(Bombpuff.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(Bombpuff.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(Bombpuff.class, EntityDataSerializers.BOOLEAN);
    private int oldFuseTime;
    private int fuseTime;
    private int maxFuse = 60;
    private int explosionRadius = 2;


    public Bombpuff(EntityType<? extends Bombpuff> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(pSource == DamageSource.ON_FIRE || pSource == DamageSource.IN_FIRE || pSource == DamageSource.LAVA && hasRoot()) {
            clearFire();
            ignite();
            return false;
        } else {
            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(2, new BombpuffRunAroundLikeCrazyGoal(this, 2f));
        super.registerGoals();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL_DIR, -1);
        this.entityData.define(DATA_IS_POWERED, false);
        this.entityData.define(DATA_IS_IGNITED, false);
    }

    @Override
    public ItemStack initializeShearItem() {
        return ModItems.BOMBPUFF_ROOT.get().getDefaultInstance();
    }

    @Override
    public ItemStack initializeTameItem() {
        return Items.GUNPOWDER.getDefaultInstance();
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(Items.FLINT_AND_STEEL) && hasRoot() && !isIgnited()) {
            this.level.playSound(pPlayer, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level.isClientSide) {
                this.ignite();
                itemstack.hurtAndBreak(1, pPlayer, (p_32290_) -> {
                    p_32290_.broadcastBreakEvent(pHand);
                });
            }

            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    @Override
    public ResourceLocation getTextureLocationForChangingRoot() {
        if(isIgnited()) {
            return new ResourceLocation(PuffsMain.MOD_ID, "textures/entity/puffs/bombpuff/bombpuff_ignited.png");
        } else {
            return new ResourceLocation(PuffsMain.MOD_ID, "textures/entity/puffs/bombpuff/bombpuff.png");
        }
    }

    public void tick() {
        if (this.isAlive()) {
            this.oldFuseTime = this.fuseTime;
            if (this.isIgnited() && hasRoot()) {
                this.setSwellDir(1);
            }
            if (this.isIgnited() && !hasRoot()) {
                this.setSwellDir(-1);
                this.entityData.set(DATA_IS_IGNITED, false);
            }

            int i = this.getSwellDir();
            if (i > 0 && this.fuseTime == 0) {
                this.playSound(SoundEvents.TNT_PRIMED, 1.0F, 0.37F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            this.fuseTime += i;
            if (this.fuseTime < 0) {
                this.fuseTime = 0;
            }

            if (this.fuseTime >= this.maxFuse) {
                this.fuseTime = this.maxFuse;
                this.explodeBombpuff();
            }
        }

        super.tick();
    }

    @Override
    public boolean isPowered() {
        return this.entityData.get(DATA_IS_POWERED);
    }

    public boolean doHurtTarget(Entity pEntity) {
        return true;
    }

    /**
     * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
     */
    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setSwellDir(int pState) {
        this.entityData.set(DATA_SWELL_DIR, pState);
    }

    private void explodeBombpuff() {
        if (!this.level.isClientSide) {
            Explosion.BlockInteraction explosion$blockinteraction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            float f = this.isPowered() ? 2.0F : 1.0F;
            this.dead = true;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, explosion$blockinteraction);
            this.discard();
            this.spawnLingeringCloud();
        }

    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
            areaeffectcloud.setRadius(1.7F);
            areaeffectcloud.setRadiusOnUse(-0.5F);
            areaeffectcloud.setWaitTime(10);
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());

            for(MobEffectInstance mobeffectinstance : collection) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }

            this.level.addFreshEntity(areaeffectcloud);
        }

    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.entityData.get(DATA_IS_POWERED)) {
            pCompound.putBoolean("powered", true);
        }

        pCompound.putShort("Fuse", (short)this.maxFuse);
        pCompound.putByte("ExplosionRadius", (byte)this.explosionRadius);
        pCompound.putBoolean("ignited", this.isIgnited());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(DATA_IS_POWERED, pCompound.getBoolean("powered"));
        if (pCompound.contains("Fuse", 99)) {
            this.maxFuse = pCompound.getShort("Fuse");
        }

        if (pCompound.contains("ExplosionRadius", 99)) {
            this.explosionRadius = pCompound.getByte("ExplosionRadius");
        }

        if (pCompound.getBoolean("ignited")) {
            this.ignite();
        }
    }

    public boolean isIgnited() {
        return this.entityData.get(DATA_IS_IGNITED);
    }

    public void ignite() {
        this.entityData.set(DATA_IS_IGNITED, true);
    }
}
