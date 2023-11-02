package net.gravity.puffs.entity.custom.puff;

import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.item.ModItems;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class Flowerpuff extends Puff {
    private static final EntityDataAccessor<Boolean> DATA_HAS_POT = SynchedEntityData.defineId(Flowerpuff.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_DISGUISED = SynchedEntityData.defineId(Flowerpuff.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_TYPE = SynchedEntityData.defineId(Flowerpuff.class, EntityDataSerializers.STRING);
    private long previousTime = 0;
    private boolean didNightHappen = false;
    private boolean didDayHappen = false;

    public Flowerpuff(EntityType<? extends Flowerpuff> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }
    @Override
    public ItemStack initializeShearItem() {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(PuffsMain.MOD_ID + ":flowerpuff_" + getCurrentFlowerType().type + "_root")).getDefaultInstance();
    }

    @Override
    public ItemStack initializeTameItem() {
        return getCurrentFlowerType().getItemStack();
    }

    @Override
    public void tick() {
        super.tick();
        long currentTime = level.getDayTime();
        if (currentTime > previousTime) {
            if (!level.isDay()) {
                if (!didNightHappen) {
                    if(hasPot()) {
                        setDisguised(true);
                    }
                    didNightHappen = true;
                }
                didDayHappen = false;
            } else {
                if (!didDayHappen) {
                    setDisguised(false);
                    setInvisible(false);
                    didDayHappen = true;
                }
                didNightHappen = false;
            }
        }

        previousTime = currentTime;
        if(isDisguised()) {
            disguise();
        }
    }

    void disguise() {
        setInvisible(true);
        setYBodyRot(0);
        moveTo(blockPosition(), 180, 0);
        if(level.getBlockState(blockPosition()).is(Blocks.GRASS) || level.getBlockState(blockPosition()).is(Blocks.TALL_GRASS) ||
                level.getBlockState(blockPosition()).is(Blocks.SNOW)) {
                level.removeBlock(blockPosition(), false);
        }
        this.setDeltaMovement(0, 0, 0);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive() && isDisguised();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(hasPot() && pSource != DamageSource.OUT_OF_WORLD) {
            if(isDisguised()) {
                setDisguised(false);
                setInvisible(false);
            }
            if(pSource.getDirectEntity() instanceof WitherSkull witherSkull && witherSkull.getOwner() instanceof WitherBoss) {
                setFlowerType(FlowerType.WITHER_ROSE);
                setShearItem(FlowerType.WITHER_ROSE.getItemStack());
                setTameItem(FlowerType.WITHER_ROSE.getItemStack());
            }
            breakPot();
            return false;
        } else {
            return super.hurt(pSource, pAmount);
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_DISGUISED, false);
        this.entityData.define(DATA_HAS_POT, true);
        this.entityData.define(DATA_TYPE, FlowerType.randomFlower().type);
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(Items.FLOWER_POT) && !hasPot()) {
            this.repairPot();
            if (!this.level.isClientSide) {
                if(pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    public ResourceLocation getDefaultLootTable() {
        if (!this.hasRoot()) {
            return this.getType().getDefaultLootTable();
        } else {
            return switch (this.getCurrentFlowerType()) {
                default -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/poppy");
                case DANDELION -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/dandelion");
                case BLUE_ORCHID -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/blue_orchid");
                case ALLIUM -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/allium");
                case AZURE_BLUET -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/azure_bluet");
                case RED_TULIP -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/red_tulip");
                case ORANGE_TULIP -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/orange_tulip");
                case WHITE_TULIP -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/white_tulip");
                case PINK_TULIP -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/pink_tulip");
                case OXEYE_DAISY -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/oxeye_daisy");
                case CORNFLOWER -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/cornflower");
                case LILY_OF_THE_VALLEY -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/lily_of_the_valley");
                case WITHER_ROSE -> new ResourceLocation(PuffsMain.MOD_ID, "entities/flowerpuff/wither_rose");
            };
        }
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if(!pCompound.contains("Disguised")) {
            pCompound.putBoolean("Disguised", this.entityData.get(DATA_IS_DISGUISED));
        }
        if(!pCompound.contains("Pot")) {
            pCompound.putBoolean("Pot", this.entityData.get(DATA_HAS_POT));
        }
        if(!pCompound.contains("Type")) {
            pCompound.putString("Type", this.entityData.get(DATA_TYPE));
        }
    }


    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if(pCompound.contains("Disguised")) {
            this.entityData.set(DATA_IS_DISGUISED, pCompound.getBoolean("Disguised"));
        }
        if(pCompound.contains("Pot")) {
            this.entityData.set(DATA_HAS_POT, pCompound.getBoolean("Pot"));
        }
        if(pCompound.contains("Type")) {
            this.entityData.set(DATA_TYPE, pCompound.getString("Type"));
        }
    }

    public boolean hasPot() {
        return this.entityData.get(DATA_HAS_POT);
    }
    public boolean isDisguised() {
        return this.entityData.get(DATA_IS_DISGUISED);
    }
    public void setDisguised(boolean disguised) {
        this.entityData.set(DATA_IS_DISGUISED, disguised);
    }
    public void repairPot() {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
        if(!level.isClientSide) {
            this.entityData.set(DATA_HAS_POT, true);
        }
    }

    @Override
    public ResourceLocation getTextureLocationForChangingRoot() {
        if(hasPot()) {
            return new ResourceLocation(PuffsMain.MOD_ID, "textures/entity/puffs/flowerpuff/pot/flowerpuff_pot_" + getCurrentFlowerType().getName() + ".png");
        } else {
            return new ResourceLocation(PuffsMain.MOD_ID, "textures/entity/puffs/flowerpuff/no_pot/flowerpuff_no_pot_" + getCurrentFlowerType().getName() + ".png");
        }
    }

    public void breakPot() {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F);
        if (this.level instanceof ServerLevel) {
            ((ServerLevel) this.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.FLOWER_POT.defaultBlockState()),
                    this.getX(), this.getY(0.6666666666666666D), this.getZ(), 10, (double) (this.getBbWidth() / 4.0F),
                    (double) (this.getBbHeight() / 4.0F), (double) (this.getBbWidth() / 4.0F), 0.05D);
        }
        if(!level.isClientSide) {
            this.entityData.set(DATA_HAS_POT, false);
        }
    }

    public void setFlowerType(Flowerpuff.FlowerType pType) {
        this.entityData.set(DATA_TYPE, pType.type);
    }

    public Flowerpuff.FlowerType getCurrentFlowerType() {
        return FlowerType.byType(this.entityData.get(DATA_TYPE));
    }

    public enum FlowerType {
        POPPY("poppy", Items.POPPY.getDefaultInstance()),
        DANDELION("dandelion", Items.DANDELION.getDefaultInstance()),
        BLUE_ORCHID("blue_orchid", Items.BLUE_ORCHID.getDefaultInstance()),
        ALLIUM("allium", Items.ALLIUM.getDefaultInstance()),
        AZURE_BLUET("azure_bluet", Items.AZURE_BLUET.getDefaultInstance()),
        RED_TULIP("red_tulip", Items.RED_TULIP.getDefaultInstance()),
        ORANGE_TULIP("orange_tulip", Items.ORANGE_TULIP.getDefaultInstance()),
        WHITE_TULIP("white_tulip", Items.WHITE_TULIP.getDefaultInstance()),
        PINK_TULIP("pink_tulip", Items.PINK_TULIP.getDefaultInstance()),
        OXEYE_DAISY("oxeye_daisy", Items.OXEYE_DAISY.getDefaultInstance()),
        CORNFLOWER("cornflower", Items.CORNFLOWER.getDefaultInstance()),
        LILY_OF_THE_VALLEY("lily_of_the_valley", Items.LILY_OF_THE_VALLEY.getDefaultInstance()),
        WITHER_ROSE("wither_rose", Items.WITHER_ROSE.getDefaultInstance());

        final String type;
        private final ItemStack itemStack;

        private static final Random random = new Random();

        public static FlowerType randomFlower()  {
            FlowerType[] flowers = values();
            return flowers[random.nextInt(flowers.length - 1)];
        }

        FlowerType(String type, ItemStack itemStack) {
            this.type = type;
            this.itemStack = itemStack;
        }

        /**
         * A block state that is rendered on the back of the mooshroom.
         */
        public ItemStack getItemStack() {
            return this.itemStack;
        }

        public String getName() {
            return this.type;
        }

        static Flowerpuff.FlowerType byType(String pName) {
            for(Flowerpuff.FlowerType flowerpuff$flowertype : values()) {
                if (flowerpuff$flowertype.type.equals(pName)) {
                    return flowerpuff$flowertype;
                }
            }

            return POPPY;
        }
    }
}
