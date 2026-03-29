package millo.millomod2.client.config;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.FeaturePosition;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class PositionalElement extends ClickableElement<PositionalElement> {

    private boolean dragging = false;
    private double dragStartX, dragStartY;
    private int originalX, originalY;

    private final Feature feature;

    public PositionalElement(FeaturePosition pos, Feature feature) {
        super(pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), MilloMod.translatable("feature", feature.getId()));
        this.feature = feature;

        border(new Border().full(0xff000000));
        background(0x80000000);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!this.isMouseOver(click.x(), click.y())) return false;

        dragging = true;
        dragStartX = click.x();
        dragStartY = click.y();
        originalX = feature.getPosition().getX();
        originalY = feature.getPosition().getY();
        return true;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (!dragging) return false;

        double offX = click.x() - dragStartX;
        double offY = click.y() - dragStartY;

        int newX = (int) (originalX + offX);
        int newY = (int) (originalY + offY);

        feature.getPosition().setScreenX(newX);
        feature.getPosition().setScreenY(newY);

        setPosition(feature.getPosition().getX(), feature.getPosition().getY());

        for (FeaturePosition.Anchor anchor : FeaturePosition.Anchor.values()) {
            int anchorX = (int) (anchor.x / 2d * MilloMod.MC.getWindow().getScaledWidth());
            int anchorY = (int) (anchor.y / 2d * MilloMod.MC.getWindow().getScaledHeight());

            if (Math.abs(click.x() - anchorX) < 10 && Math.abs(click.y() - anchorY) < 10) {
                feature.getPosition().setAnchor(anchor);
                break;
            }
        }

        return true;
    }

    @Override
    public boolean mouseReleased(Click click) {
        dragging = false;
        return true;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.fill(getX(), getY(), getRight(), getBottom(), 0x80000000);
        context.drawStrokedRectangle(getX(), getY(), getWidth(), getHeight(), 0xff00ffff);

        if (dragging || isMouseOver(mouseX, mouseY)) {
            int anchorX = (int) (feature.getPosition().getAnchor().x / 2d * MilloMod.MC.getWindow().getScaledWidth());
            int anchorY = (int) (feature.getPosition().getAnchor().y / 2d * MilloMod.MC.getWindow().getScaledHeight());

            double dx = anchorX - (getX() + getWidth() / 2d);
            double dy = anchorY - (getY() + getHeight() / 2d);
            double lineX = anchorX;
            double lineY = anchorY;

            double dist = Math.sqrt(dx*dx + dy*dy);
            dx /= -dist;
            dy /= -dist;

            for (int i = 0; i < 20; i++) {
                int color = new Color(1f, 1f, 1f, 1f - i/20f).hashCode();

                context.fill((int) lineX, (int) lineY, (int) lineX + 1, (int) lineY + 1, color);
                lineX += dx;
                lineY += dy;
            }

            lineX = getX() + getWidth() / 2d;
            lineY = getY() + getHeight() / 2d;
            for (int i = 0; i < 20; i++) {
                int color = new Color(1f, 1f, 1f, 1f - i/20f).hashCode();

                context.fill((int) lineX, (int) lineY, (int) lineX + 1, (int) lineY + 1, color);
                lineX -= dx;
                lineY -= dy;
            }

            for (FeaturePosition.Anchor anchor : FeaturePosition.Anchor.values()) {
                if (anchor == feature.getPosition().getAnchor()) continue;
                int anchorPosX = (int) (anchor.x / 2d * MilloMod.MC.getWindow().getScaledWidth());
                int anchorPosY = (int) (anchor.y / 2d * MilloMod.MC.getWindow().getScaledHeight());
                context.drawStrokedRectangle(anchorPosX - 3, anchorPosY - 3, 5,5, 0xffffffff);
            }

            context.fill(anchorX - 4, anchorY - 4, anchorX + 3, anchorY + 3, 0xffffffff);
            context.drawTextWithShadow(getTextRenderer(), message, getX() + 2, getY() + 2, 0xffffffff);
        }


        super.renderWidget(context, mouseX, mouseY, deltaTicks);
    }
}
