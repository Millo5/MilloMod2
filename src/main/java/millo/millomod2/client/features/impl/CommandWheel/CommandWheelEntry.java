package millo.millomod2.client.features.impl.CommandWheel;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.awt.*;

public class CommandWheelEntry {
    protected final String name;
    protected final String command;
    protected final Text text;

    public CommandWheelEntry(String name, String command) {
        this.name = name;
        this.command = command;
        this.text = Text.literal(name);
    }

    private boolean selected = false;
    private float hover = 0f;
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean isSelected() {
        return selected;
    }

    public void draw(RenderInfo info, int x, int y, TextRenderer textRenderer, float shown) {
        hover = info.lerp(hover, isSelected() ? 1f : 0f, 1f);
        DrawContext context = info.context();

        if (selected) drawMouseLine(context, x, y);

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        context.getMatrices().scale(shown, shown);
        context.getMatrices().scale(hover*0.2f+1f, hover*0.2f+1f);

        int color = new Color(0f, 0f, 0f, 0.2f + hover * 0.3f).hashCode();
        int borderCol = new Color(1f-hover, 1f, 1f, 1f).hashCode();
        context.fill(-20, -20, 20, 20, color);
        context.drawStrokedRectangle(-20, -20, 40, 40, borderCol);

        int w = textRenderer.getWidth(text);
        context.drawText(textRenderer, text, -w / 2, -5, Color.WHITE.hashCode(), true);

        context.getMatrices().popMatrix();
    }

    private void drawMouseLine(DrawContext context, int x, int y) {
        var window = MilloMod.MC.getWindow();
        double mouseX = MilloMod.MC.mouse.getX() / window.getWidth() * window.getScaledWidth();
        double mouseY = MilloMod.MC.mouse.getY() / window.getHeight() * window.getScaledHeight();

        double dx = (x - mouseX);
        double dy = (y - mouseY);

        double dist = Math.sqrt(dx*dx + dy*dy);
        dx /= dist;
        dy /= dist;

        for (int j = 0; j < 20; j++) {
            mouseX += dx;
            mouseY += dy;

            int color = new Color(1f, 1f, 1f, (1f - j/20f) * hover).hashCode();
            context.fill((int) mouseX, (int) mouseY, (int) (mouseX+1), (int) (mouseY+1), color);
        }
    }

    public void execute() {
        PlayerUtil.sendCommand(command);
    }

}
