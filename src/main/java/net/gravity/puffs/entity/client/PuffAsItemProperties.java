package net.gravity.puffs.entity.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class PuffAsItemProperties implements IClientItemExtensions {
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return new PuffAsEntityItemRenderer();
    }
}
