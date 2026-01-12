package millo.millomod2.client.features.impl;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.FeaturePosition;
import millo.millomod2.client.features.addons.*;
import millo.millomod2.client.rendering.DonutRenderState;
import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import org.joml.Matrix3x2f;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LagslayerHUD extends Feature implements Toggleable, HUDRendered, Positional, Configurable {

    private final Pattern lsRegex = Pattern.compile("^CPU Usage: \\[▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮] \\((\\d+\\.\\d+)%\\)$");
    private float cpuUsage = 0f;
    private float renderedCpuUsage = 0f;
    private long lastUpdateTime = 0L;
    private float renderedAlpha = 0f;

    private int textOffsetX = 0;

    private DonutRenderState backgroundDonut;
    private DonutRenderState foregroundDonut;

    @Override
    public String getId() {
        return "lagslayer_hud";
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addChoice("display", "Radial", new String[] {"None", "Radial", "Bar"});
        config.addBoolean("show_text", true);
        config.addBoolean("interpolate", true);
    }

    @Override
    public FeaturePosition defaultPosition() {
        return new FeaturePosition(20, 20, 54, 20);
    }

    @OnReceivePacket
    public boolean onActionBar(OverlayMessageS2CPacket packet) {
        if (!isEnabled()) return false;

        String content = packet.text().getString();
        Matcher matcher = lsRegex.matcher(content);
        if (!matcher.find()) return false;

        String cpuUsageStr = matcher.group(1);
        try {
            cpuUsage = Float.parseFloat(cpuUsageStr);
            lastUpdateTime = System.currentTimeMillis();
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public void HUDRender(RenderInfo renderInfo) {
        if (!isEnabled()) return;
        FeaturePosition pos = getPosition();
        DrawContext context = renderInfo.context();

        double time = (System.currentTimeMillis() - lastUpdateTime) / 1000d;
        float alpha = (float) Math.max(Math.min(1, 5 - time), 0);
        renderedAlpha = renderInfo.lerp(renderedAlpha, alpha, 0.35f);

        if (config.getBoolean("interpolate")) {
            renderedCpuUsage = renderInfo.lerp(renderedCpuUsage, cpuUsage, 0.5f);
        } else renderedCpuUsage = cpuUsage;

        textOffsetX = 0;
        String display = config.getChoice("display");
        switch (display) {
            case "Radial" -> renderRadial(context, pos);
            case "Bar" -> renderBar(context, pos);
        }

        if (config.getBoolean("show_text")) {
            context.drawText(MilloMod.MC.textRenderer,
                    Math.round(renderedCpuUsage * 100d) / 100d + "%",
                    pos.getX() + 10 + textOffsetX, pos.getY() + 6,
                    new Color(1f, 1f, 1f, renderedAlpha).hashCode(), true
            );
        }

    }

    private void renderBar(DrawContext context, FeaturePosition pos) {
        int barWidth = 100;
        int barHeight = 10;
        int x = pos.getX();
        int y = pos.getY() + 5;

        textOffsetX = barWidth - 8;

        // Background bar
        context.fill(x, y, x + barWidth, y + barHeight,
                new Color(0.25f, 0.25f, 0.25f, renderedAlpha).hashCode());

        // Foreground bar
        int filledWidth = Math.round((renderedCpuUsage / 100f) * barWidth);
        Color color = Color.getHSBColor(Math.max(0f, (100f - renderedCpuUsage) / 360f), 1f, 1f);
        context.fill(x, y, x + filledWidth, y + barHeight,
                new Color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, renderedAlpha).hashCode());
    }

    private void renderRadial(DrawContext context, FeaturePosition pos) {
        textOffsetX = 12;
        if (backgroundDonut == null) {
            this.backgroundDonut = new DonutRenderState(
                    RenderPipelines.GUI,
                    new Matrix3x2f(context.getMatrices()),
                    20,
                    pos.getX() + 10, pos.getY() + 10,
                    4.9f, 9.9f,
                    new Color(0.25f, 0.25f, 0.25f, renderedAlpha).hashCode(),
                    0, 1
            );
            this.foregroundDonut = new DonutRenderState(
                    RenderPipelines.GUI,
                    new Matrix3x2f(context.getMatrices()),
                    20,
                    pos.getX() + 10, pos.getY() + 10,
                    5f, 10f,
                    Color.getHSBColor(0, 0, 0).hashCode(),
                    0, 1
            );
        }

        Color color = Color.getHSBColor(Math.max(0f, (100f - renderedCpuUsage) / 360f), 1f, 1f);
        foregroundDonut.setColor(new Color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, renderedAlpha).hashCode());
        foregroundDonut.setAngles(0.75f - (Math.round(renderedCpuUsage) / 100f), 0.75f);
        backgroundDonut.setColor(new Color(0.25f, 0.25f, 0.25f, renderedAlpha).hashCode());

        context.state.addSimpleElement(backgroundDonut);
        context.state.addSimpleElement(foregroundDonut);
    }


}
