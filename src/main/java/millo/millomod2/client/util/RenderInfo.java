package millo.millomod2.client.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public record RenderInfo(DrawContext context, float deltaTime, int mouseX, int mouseY) {
    // in seconds

    public RenderInfo(DrawContext context, float deltaTime) {
        this(context, deltaTime, -1, -1);
    }

    public float lerp(float start, float end, float delta) {
        return MathHelper.clampedLerp(delta * deltaTime, start, end);
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
