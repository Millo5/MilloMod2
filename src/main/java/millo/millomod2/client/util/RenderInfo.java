package millo.millomod2.client.util;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;

public record RenderInfo(GuiGraphicsExtractor graphics, float deltaTime, int mouseX, int mouseY) {
    // in seconds

    public RenderInfo(GuiGraphicsExtractor graphics, float deltaTime) {
        this(graphics, deltaTime, -1, -1);
    }

    public float lerp(float start, float end, float delta) {
        return Mth.clampedLerp(delta * deltaTime, start, end);
    }

    @Override
    public String toString() {
        return "RenderInfo[" +
                "graphics=" + graphics + ", " +
                "deltaTime=" + deltaTime + ", " +
                "mouseX=" + mouseX + ", " +
                "mouseY=" + mouseY + ']';
    }

}
