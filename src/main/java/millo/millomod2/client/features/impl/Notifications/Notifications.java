package millo.millomod2.client.features.impl.Notifications;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.FeaturePosition;
import millo.millomod2.client.features.addons.*;
import millo.millomod2.client.util.ChatMatchRule;
import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Notifications extends Feature implements Toggleable, Positional, HUDRendered, Configurable {

    private final ArrayList<Notification> notifications = new ArrayList<>();
    private HashMap<String, ChatMatchRule> rules;

    @Override
    public String getId() {
        return "notifications";
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        rules = new HashMap<>();
        rules.put("friend_messages", new ChatMatchRule(Pattern.compile("» your friend (.+) has joined!")));
        rules.put("plot_votes", new ChatMatchRule(Pattern.compile("⭐ (.+) has voted for this plot! ⭐|⭐ (.+) voted for .+")));
        rules.put("player_joins_plot", new ChatMatchRule(Pattern.compile("▷ (.+) joined .+|○ (.+) played on .+")));
        rules.put("player_joins_node", new ChatMatchRule(Pattern.compile("\\[.+\\](.+) joined!?|(.+) joined!")));
        rules.put("plot_info_messages", new ChatMatchRule(
                "Note: This plot may hide, log, or change your chat messages. Use /gchat or /gc to bypass and talk to global chat.",
                "Note: Your whitelist bypass permission was applied.",
                "\\n» The plot's resource pack was automatically enabled due to your preference.\\n  [Disable]\\n"
        ));
        rules.put("plot_info_annoying", new ChatMatchRule(
                "Note: You can view your past 5 created templates with /templatehistory!",
                "Error: Invalid template placement."
        ));
        rules.put("fly_speed_changes", new ChatMatchRule(Pattern.compile("» Set fly speed to: (\\d+)% of default speed.")));

        config.addChoice("direction", "down", new String[]{"up", "down"});

        for (String ruleKey : rules.keySet()) {
            config.addBoolean(ruleKey, false);
        }
    }

    @OnReceivePacket
    public boolean onMessage(GameMessageS2CPacket packet) {
        if (!isEnabled()) return false;

        String message = packet.content().getString();

        for (String ruleKey : rules.keySet()) {
            ChatMatchRule rule = rules.get(ruleKey);
            if (config.getBoolean(ruleKey) && rule.matches(message)) {
                notify(packet.content());
                return true;
            }
        }

        return false;
    }

    @Override
    public FeaturePosition defaultPosition() {
        return new FeaturePosition(5, 5, 200, 150, FeaturePosition.Anchor.TOP_RIGHT);
    }

    @Override
    public void onTick() {
        if (!isEnabled()) return;

        notifications.removeIf(Notification::shouldRemove);
    }

    @Override
    public void HUDRender(RenderInfo renderInfo) {
        if (!isEnabled()) return;

        DrawContext context = renderInfo.context();
        int direction = (config.getChoice("direction").equals("up") ? -1 : 1);

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(position.getX(), position.getY());

        for (Notification notification : List.copyOf(notifications)) {
            float visible = renderInfo.lerp(notification.getVisible(), notification.isFadingOut() ? 0f : 1f, 1f);
            notification.setVisible(visible);
            if (visible > 0.9f) notification.age(renderInfo.deltaTime() * 0.05f);

            float progress = notification.getProgress();

            float xOffset = (1 - visible) * position.getWidth();
            context.getMatrices().translate(xOffset, 0);

            int height = MilloMod.MC.textRenderer.getWrappedLinesHeight(notification.getMessage(), position.getWidth());
            context.fill(-2, -2, position.getWidth(), height + 2, new Color(0, 0, 0, (int)(150 * visible)).hashCode());
            context.fill(-2, height, -2 + (int) (position.getWidth() * (1 - progress)), height + 2, new Color(255, 255, 255, (int)(150 * visible)).hashCode());
            context.drawWrappedText(MilloMod.MC.textRenderer, notification.getMessage(), 0, 0, position.getWidth(), new Color(1f, 1f, 1f, visible).hashCode(), true);

            context.getMatrices().translate(-xOffset, 0);

            float yOffset = visible * (height + 6);
            context.getMatrices().translate(0, yOffset * direction);
        }

        context.getMatrices().popMatrix();
    }

    public static void notify(Notification notification) {
        Notifications feature = FeatureHandler.get(Notifications.class);
        if (feature == null || !feature.isEnabled()) return;

        feature.notifications.add(notification);
    }

    public static void notify(Text message) {
        notify(new Notification(message));
    }


}
