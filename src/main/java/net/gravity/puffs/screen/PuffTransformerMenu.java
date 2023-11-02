package net.gravity.puffs.screen;


import net.gravity.puffs.item.custom.PuffRootItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PuffTransformerMenu extends AbstractContainerMenu {
    private final Container machine;
    private final ContainerData data;

    public PuffTransformerMenu(int pContainerId) {
        this(pContainerId, new SimpleContainer(1), new SimpleContainerData(2));
    }

    public PuffTransformerMenu(int pContainerId, Container machine, ContainerData machineData) {
        super(ModMenuTypes.PUFF_TRANSFORMER_MENU.get(), pContainerId);
        checkContainerSize(machine, 1);
        checkContainerDataCount(machineData, 1);
        this.machine = machine;
        this.data = machineData;

        this.addSlot(new Slot(machine, 0, 0, 0) {
            public void setChanged() {
                super.setChanged();
                PuffTransformerMenu.this.slotsChanged(this.container);
            }
            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.getItem() instanceof PuffRootItem;
            }
        });

        addDataSlots(data);
    }

    public PuffTransformerMenu(int i, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(i);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.machine.stillValid(player);
    }
}