package net.gravity.puffs.block.entity;

import net.gravity.puffs.entity.custom.puff.Flowerpuff;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.gravity.puffs.item.custom.FlowerPuffRootItem;
import net.gravity.puffs.item.custom.PuffRootItem;
import net.gravity.puffs.screen.PuffTransformerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PuffTransformerBlockEntity extends BlockEntity implements MenuProvider {
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;

    public final Container bookAccess = new Container() {
        /**
         * Returns the number of slots in the inventory.
         */
        public int getContainerSize() {
            return 1;
        }

        public boolean isEmpty() {
            return PuffTransformerBlockEntity.this.root.isEmpty();
        }

        /**
         * Returns the stack in the given slot.
         */
        public ItemStack getItem(int p_59580_) {
            return p_59580_ == 0 ? PuffTransformerBlockEntity.this.root : ItemStack.EMPTY;
        }

        /**
         * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
         */
        public ItemStack removeItem(int p_59582_, int p_59583_) {
            if (p_59582_ == 0) {
                ItemStack itemstack = PuffTransformerBlockEntity.this.root.split(p_59583_);
                PuffTransformerBlockEntity.this.root.isEmpty();
                return itemstack;
            } else {
                return ItemStack.EMPTY;
            }
        }

        /**
         * Removes a stack from the given slot and returns it.
         */
        public ItemStack removeItemNoUpdate(int p_59590_) {
            if (p_59590_ == 0) {
                ItemStack itemstack = PuffTransformerBlockEntity.this.root;
                PuffTransformerBlockEntity.this.root = ItemStack.EMPTY;
                return itemstack;
            } else {
                return ItemStack.EMPTY;
            }
        }

        /**
         * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
         */
        public void setItem(int p_59585_, ItemStack p_59586_) {
        }

        /**
         * Returns the maximum stack size for an inventory slot. Seems to always be 64, possibly will be extended.
         */
        public int getMaxStackSize() {
            return 1;
        }

        /**
         * For block entities, ensures the chunk containing the block entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void setChanged() {
            PuffTransformerBlockEntity.this.setChanged();
        }

        /**
         * Don't rename this method to canInteractWith due to conflicts with Container
         */
        public boolean stillValid(Player p_59588_) {
            if (PuffTransformerBlockEntity.this.level.getBlockEntity(PuffTransformerBlockEntity.this.worldPosition) != PuffTransformerBlockEntity.this) {
                return false;
            } else {
                return !(p_59588_.distanceToSqr((double) PuffTransformerBlockEntity.this.worldPosition.getX() + 0.5D, (double) PuffTransformerBlockEntity.this.worldPosition.getY() + 0.5D, (double) PuffTransformerBlockEntity.this.worldPosition.getZ() + 0.5D) > 64.0D) && PuffTransformerBlockEntity.this.hasRoot();
            }
        }

        /**
         * Returns {@code true} if automation is allowed to insert the given stack (ignoring stack size) into the given
         * slot. For guis use Slot.isItemValid
         */
        public boolean canPlaceItem(int p_59592_, ItemStack p_59593_) {
            return false;
        }

        public void clearContent() {
        }
    };
    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> PuffTransformerBlockEntity.this.progress;
                case 1 -> PuffTransformerBlockEntity.this.maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> PuffTransformerBlockEntity.this.progress = value;
                case 1 -> PuffTransformerBlockEntity.this.maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    ItemStack root = ItemStack.EMPTY;

    public ItemStack getRoot() {
        return this.root;
    }

    public void setRoot(ItemStack itemStack) {
        this.root = itemStack;
        this.setChanged();
    }

    public boolean hasRoot() {
        return !this.root.isEmpty() && this.root.getItem() instanceof PuffRootItem;
    }

    public PuffTransformerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PUFF_TRANSFORMER.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> PuffTransformerBlockEntity.this.progress;
                    case 1 -> PuffTransformerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> PuffTransformerBlockEntity.this.progress = value;
                    case 1 -> PuffTransformerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Puff Transformer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new PuffTransformerMenu(pContainerId, bookAccess, data);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (!this.getRoot().isEmpty()) {
            nbt.put("Root", this.getRoot().save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("Root", 10)) {
            this.root = ItemStack.of(nbt.getCompound("Root"));
        } else {
            this.root = ItemStack.EMPTY;
        }
    }

    public void drops() {
        Containers.dropContents(this.level, this.worldPosition, bookAccess);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PuffTransformerBlockEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }

        if(hasRootInSlot(pEntity)) {
            pEntity.progress++;
            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress) {
                if(pEntity.bookAccess.getItem(0).getItem() instanceof PuffRootItem puffRootItem) {
                    Puff puff = (Puff) puffRootItem.getAssociatedPuff().spawn((ServerLevel) level, null, null, pos.above(), MobSpawnType.SPAWNER, true, true);
                    if(puff instanceof Flowerpuff flowerpuff && puffRootItem instanceof FlowerPuffRootItem flowerPuffRootItem) {
                        flowerpuff.setFlowerType(flowerPuffRootItem.getAssociatedFlowerType());
                    }
                    pEntity.resetProgress();
                    level.addFreshEntity(puff);
                    pEntity.bookAccess.removeItem(0, 1);
                }
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static boolean hasRootInSlot(PuffTransformerBlockEntity entity) {
        return entity.bookAccess.getItem(0).getItem() instanceof PuffRootItem;
    }
}
