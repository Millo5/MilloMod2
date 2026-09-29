package millo.millomod2.client.features.impl.CommandWheel;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class CommandWheelEntry {
    protected final String name;
    protected final String command;
    protected final Component text;

    public CommandWheelEntry(String name, String command) {
        this.name = name;
        this.command = command;
        this.text = Component.literal(name);
    }

    private boolean selected = false;
    private float hover = 0f;
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean isSelected() {
        return selected;
    }

    public void draw(RenderInfo info, int x, int y, Font textRenderer, float shown) {
        hover = info.lerp(hover, isSelected() ? 1f : 0f, 1f);
        GuiGraphicsExtractor context = info.graphics();

        if (selected) drawMouseLine(context, x, y);

        context.pose().pushMatrix();
        context.pose().translate(x, y);
        context.pose().scale(shown, shown);
        context.pose().scale(hover*0.2f+1f, hover*0.2f+1f);

        int color = new Color(0f, 0f, 0f, 0.2f + hover * 0.3f).hashCode();
        int borderCol = new Color(1f-hover, 1f, 1f, 1f).hashCode();
        context.fill(-20, -20, 20, 20, color);
        context.outline(-20, -20, 40, 40, borderCol);

        int w = textRenderer.width(text);
        context.text(textRenderer, text, -w / 2, -5, Color.WHITE.hashCode(), true);

        context.pose().popMatrix();
    }

    private void drawMouseLine(GuiGraphicsExtractor graphics, int x, int y) {
        var window = MilloMod.MC.getWindow();
        double mouseX = MilloMod.MC.mouseHandler.xpos() / window.getScreenWidth() * window.getGuiScaledWidth();
        double mouseY = MilloMod.MC.mouseHandler.ypos() / window.getScreenHeight() * window.getGuiScaledHeight();

        double dx = (x - mouseX);
        double dy = (y - mouseY);

        double dist = Math.sqrt(dx*dx + dy*dy);
        dx /= dist;
        dy /= dist;

        for (int j = 0; j < 20; j++) {
            mouseX += dx;
            mouseY += dy;

            int color = new Color(1f, 1f, 1f, (1f - j/20f) * hover).hashCode();
            graphics.fill((int) mouseX, (int) mouseY, (int) (mouseX+1), (int) (mouseY+1), color);
        }
    }

    public void execute() {
        PlayerUtil.sendCommand(command);
    }

}
