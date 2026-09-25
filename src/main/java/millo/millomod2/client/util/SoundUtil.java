package millo.millomod2.client.util;

import millo.millomod2.client.MilloMod;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class SoundUtil {

    public static void playSound(String name, float volume, float pitch) {
        playSound(name, SoundSource.MASTER, volume, pitch);
    }

    public static void playSound(String name, SoundSource category, float volume, float pitch) {
        playSound(SoundEvent.createVariableRangeEvent(Identifier.parse(name)), category, volume, pitch);
    }

    public static void playSound(SoundEvent soundEvent, SoundSource category, float volume, float pitch) {
        LocalPlayer player = MilloMod.player();
        if (player == null) return;

        SimpleSoundInstance soundInstance = new SimpleSoundInstance(soundEvent, category, volume, pitch, RandomSource.create(), player.getX(), player.getY(), player.getZ());
        MilloMod.MC.getSoundManager().play(soundInstance);
    }

    public static void playSoundVariant(String soundId, float volume, float pitch, long seed) {
        var player = MilloMod.player();
        if (player == null) return;

        SoundEvent sound = SoundEvent.createVariableRangeEvent(Identifier.parse(soundId));

        SimpleSoundInstance soundInstance = new SimpleSoundInstance(sound, SoundSource.MASTER, volume, pitch, RandomSource.create(seed), player.getX(), player.getY(), player.getZ());
        MilloMod.MC.getSoundManager().play(soundInstance);
    }

    public static void playClickSound() {
        MilloMod.MC.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.ITEM_FRAME_ADD_ITEM, 1.0F));
    }
}
