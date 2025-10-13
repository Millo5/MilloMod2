package millo.millomod2.client.rendering;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public class DonutRenderState implements SimpleGuiElementRenderState {

    private RenderPipeline pipeline;
    private Matrix3x2f pose;
    private ScreenRect bounds;

    private final int segments;
    private float centerX, centerY;
    private float innerRadius, outerRadius;
    private int color;
    private float startAngle, endAngle;

    public DonutRenderState(RenderPipeline pipeline, Matrix3x2f pose, int segments, float centerX, float centerY, float innerRadius, float outerRadius, int color, float startAngle, float endAngle) {
        this.pipeline = pipeline;
        this.pose = pose;
        this.bounds = new ScreenRect(
                (int) (centerX - outerRadius),
                (int) (centerY - outerRadius),
                (int) (outerRadius * 2),
                (int) (outerRadius * 2)
        );

        this.segments = segments;
        this.centerX = centerX;
        this.centerY = centerY;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.color = color;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    public void setCenter(float x, float y) {
        this.centerX = x;
        this.centerY = y;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setAngles(float start, float end) {
        this.startAngle = start;
        this.endAngle = end;
    }

    @Override
    public void setupVertices(VertexConsumer vertices, float depth) {

        double startAngle = this.startAngle * 2 * Math.PI;
        double endAngle = this.endAngle * 2 * Math.PI;
        double increment = (endAngle - startAngle) / segments;

        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (i * increment);
            float angle2 = (float) ((i + 1) * increment);

            float x1_inner = centerX + (float) (Math.cos(angle1) * innerRadius);
            float y1_inner = centerY + (float) (Math.sin(angle1) * innerRadius);

            float x2_inner = centerX + (float) (Math.cos(angle2) * innerRadius);
            float y2_inner = centerY + (float) (Math.sin(angle2) * innerRadius);

            float x1_outer = centerX + (float) (Math.cos(angle1) * outerRadius);
            float y1_outer = centerY + (float) (Math.sin(angle1) * outerRadius);

            float x2_outer = centerX + (float) (Math.cos(angle2) * outerRadius);
            float y2_outer = centerY + (float) (Math.sin(angle2) * outerRadius);

            vertices.vertex(pose, x1_outer, y1_outer, depth).color(color);
            vertices.vertex(pose, x2_outer, y2_outer, depth).color(color);
            vertices.vertex(pose, x2_inner, y2_inner, depth).color(color);
            vertices.vertex(pose, x1_inner, y1_inner, depth).color(color);

            vertices.vertex(pose, x1_inner, y1_inner, depth).color(color);
            vertices.vertex(pose, x2_inner, y2_inner, depth).color(color);
            vertices.vertex(pose, x2_outer, y2_outer, depth).color(color);
            vertices.vertex(pose, x1_outer, y1_outer, depth).color(color);
        }

    }

    @Override
    public RenderPipeline pipeline() {
        return this.pipeline;
    }

    @Override
    public TextureSetup textureSetup() {
        return TextureSetup.empty();
    }

    @Nullable
    @Override
    public ScreenRect scissorArea() {
        return null;
    }

    @Nullable
    @Override
    public ScreenRect bounds() {
        return bounds;
    }
}
