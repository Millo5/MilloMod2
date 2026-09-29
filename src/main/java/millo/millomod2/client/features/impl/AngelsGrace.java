package millo.millomod2.client.features.impl;

import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.features.impl.Notifications.Notifications;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import java.util.Random;

public class AngelsGrace extends Feature implements Toggleable {

    private static final String[] messages = new String[] {
            "Saved by Millo...", "Helium consumed...", "Lowered density...",
            "+1 Pair of Wings", "Hoppoo Feather Consumed", "Walking on sunshine...",
            "Ascended to greater bounds...", "You feel lightheaded", "Schlooof screams at you for food",
            "You find yourself standing on a big pillow", "Your psychic powers activate", "I want ice cream",
            "You are now 20% more fabulous", "A big shark shaped balloon catches you"
    };

    @Override
    public String getId() {
        return "angels_grace";
    }

    @Override
    public void onTick() {
        if (!isEnabled() || player() == null) return;
        if (HypercubeAPI.getMode() != HypercubeAPI.Mode.DEV) return;

        Screen screen = MC.gui.screen();
        if (screen instanceof AbstractContainerScreen<?> || screen instanceof ChatScreen) {
            if (!player().getAbilities().flying && player().getAbilities().mayfly) {
                if (player().getDeltaMovement().y < -0.3 && !player().onGround()) {
                    player().getAbilities().flying = true;
                    player().onUpdateAbilities();
                    String message = messages[new Random().nextInt(messages.length)];
                    Notifications.notify(Component.literal(message).setStyle(Styles.VARIABLE.getStyle()));
                }
            }
        }
    }
}
