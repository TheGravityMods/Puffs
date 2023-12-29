package net.gravity.puffs.sound;

import net.gravity.puffs.PuffsMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PuffsMain.MOD_ID);

    public static final RegistryObject<SoundEvent> PEACEFUL_WORLD = registerSoundEvent("peaceful_world");

    private static RegistryObject<SoundEvent> registerSoundEvent(String sound) {
        return SOUND_EVENTS.register(sound, () -> new SoundEvent(new ResourceLocation(PuffsMain.MOD_ID, sound)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}