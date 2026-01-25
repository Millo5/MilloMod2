package millo.millomod2.client.util;

import millo.millomod2.client.MilloMod;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class SoundUtil {

    public static void playSound(String name, float volume, float pitch) {
        playSound(name, SoundCategory.MASTER, volume, pitch);
    }

    public static void playSound(String name, SoundCategory category, float volume, float pitch) {
        playSound(SoundEvent.of(Identifier.of(name)), category, volume, pitch);
    }

    public static void playSound(SoundEvent soundEvent, SoundCategory category, float volume, float pitch) {
        ClientPlayerEntity player = MilloMod.player();
        if (player == null) return;

        PositionedSoundInstance soundInstance = new PositionedSoundInstance(soundEvent, category, volume, pitch, Random.create(), player.getX(), player.getY(), player.getZ());
        MilloMod.MC.getSoundManager().play(soundInstance);
    }

    public static void playSoundVariant(String soundId, float volume, float pitch, long seed) {
        var player = MilloMod.player();
        if (player == null) return;

        SoundEvent sound = SoundEvent.of(Identifier.of(soundId));

        PositionedSoundInstance soundInstance = new PositionedSoundInstance(sound, SoundCategory.MASTER, volume, pitch, Random.create(seed), player.getX(), player.getY(), player.getZ());
        MilloMod.MC.getSoundManager().play(soundInstance);
    }

}
