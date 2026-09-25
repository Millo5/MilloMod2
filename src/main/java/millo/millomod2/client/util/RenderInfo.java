package millo.millomod2.client.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public record RenderInfo(GuiGraphics context, float deltaTime, int mouseX, int mouseY) {
    // in seconds

    public RenderInfo(GuiGraphics context, float deltaTime) {
        this(context, deltaTime, -1, -1);
    }

    public float lerp(float start, float end, float delta) {
        return Mth.clampedLerp(delta * deltaTime, start, end);
    }

    @Override
    public String toString() {
        return "RenderInfo[" +
                "context=" + context + ", " +
                "deltaTime=" + deltaTime + ", " +
                "mouseX=" + mouseX + ", " +
                "mouseY=" + mouseY + ']';
    }

}
