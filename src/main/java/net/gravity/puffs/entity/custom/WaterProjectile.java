package net.gravity.puffs.entity.custom;

import net.gravity.puffs.entity.ModEntities;
import net.gravity.puffs.entity.custom.puff.Waterpuff;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class WaterProjectile extends Projectile {

    public WaterProjectile(EntityType<? extends WaterProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public WaterProjectile(Level pLevel, Waterpuff pSpitter) {
        this(ModEntities.WATER_PROJECTILE.get(), pLevel);
        this.setOwner(pSpitter);
        this.setPos(pSpitter.getX() - (double)(pSpitter.getBbWidth() + 1.0F) * 0.5D * (double)Mth.sin(pSpitter.yBodyRot * ((float)Math.PI / 180F)), pSpitter.getEyeY() - (double)0.1F, pSpitter.getZ() + (double)(pSpitter.getBbWidth() + 1.0F) * 0.5D * (double)Mth.cos(pSpitter.yBodyRot * ((float)Math.PI / 180F)));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult))
            this.onHit(hitresult);
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();
        if (this.isInWaterOrBubble()) {
            this.discard();
        } else {
            this.setDeltaMovement(vec3.scale((double)0.99F));
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double)-0.06F, 0.0D));
            }

            this.setPos(d0, d1, d2);
        }
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = this.getOwner();
        Entity hitEntity = pResult.getEntity();
        if(level.isClientSide) {
            for(int i = 0; i < 7; ++i) {
                this.level.addParticle(ParticleTypes.SPLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
        }
        if (entity instanceof LivingEntity && hitEntity.isOnFire()) {
            hitEntity.clearFire();
            this.playEntityOnFireExtinguishedSound();
        }
        discard();
    }

    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if(level.isClientSide) {
            for(int i = 0; i < 7; ++i) {
                this.level.addParticle(ParticleTypes.SPLASH, this.getX(), this.getY(), this.getZ(), 0, 0.5, 0);
            }
        }
        if (!this.level.isClientSide) {
            this.discard();
        }

    }

    protected void defineSynchedData() {
    }

    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        double d0 = pPacket.getXa();
        double d1 = pPacket.getYa();
        double d2 = pPacket.getZa();

        this.setDeltaMovement(d0, d1, d2);
    }
}