package millo.millomod2.client.features.impl;

import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.features.impl.Notifications.Notifications;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;

import java.util.Random;

public class AngelsGrace extends Feature implements Toggleable {

    private static String[] messages = new String[] {
            "Saved by Millo...", "Helium consumed...", "Lowered density...",
            "+1 Pair of Wings", "Hoppoo Feather Consumed", "Walking on sunshine...",
            "Ascended to greater bounds..."
    };

    @Override
    public String getId() {
        return "angels_grace";
    }

    @Override
    public void onTick() {
        if (!isEnabled() || player() == null) return;
        if (HypercubeAPI.getMode() != HypercubeAPI.Mode.DEV) return;

        Screen screen = MC.currentScreen;
        if (screen instanceof HandledScreen<?> || screen instanceof ChatScreen) {
            if (!player().getAbilities().flying && player().getAbilities().allowFlying) {
                if (player().getVelocity().y < -0.3 && !player().isOnGround()) {
                    player().getAbilities().flying = true;
                    player().sendAbilitiesUpdate();
                    String message = messages[new Random().nextInt(messages.length)];
                    Notifications.notify(Text.literal(message).setStyle(Styles.VARIABLE.getStyle()));
                }
            }
        }
    }
}
