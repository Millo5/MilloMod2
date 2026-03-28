package millo.millomod2.client.config;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.FeaturePosition;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;

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

            if (Math.abs(newX - anchorX) < 10 && Math.abs(newY - anchorY) < 10) {
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

        if (dragging || isMouseOver(mouseX, mouseY)) {
            context.drawTextWithShadow(getTextRenderer(), message, getX(), getY(), 0xffffffff);
            int anchorX = (int) (feature.getPosition().getAnchor().x / 2d * MilloMod.MC.getWindow().getScaledWidth());
            int anchorY = (int) (feature.getPosition().getAnchor().y / 2d * MilloMod.MC.getWindow().getScaledHeight());

            int anchorLineX = anchorX;
            int anchorLineY = anchorY;
            for (int i = 0; i < 5; i++) {
                context.fill(anchorLineX - 2, anchorLineY - 2, anchorLineX + 2, anchorLineY + 2, 0xffff0000);
                anchorLineX += (getX() + getWidth() / 2 - anchorX) / 5;
                anchorLineY += (getY() + getHeight() / 2 - anchorY) / 5;
            }

            for (FeaturePosition.Anchor anchor : FeaturePosition.Anchor.values()) {
                int anchorPosX = (int) (anchor.x / 2d * MilloMod.MC.getWindow().getScaledWidth());
                int anchorPosY = (int) (anchor.y / 2d * MilloMod.MC.getWindow().getScaledHeight());
                context.fill(anchorPosX - 2, anchorPosY - 2, anchorPosX + 2, anchorPosY + 2, 0xffffff00);
            }

            context.fill(anchorX - 3, anchorY - 3, anchorX + 3, anchorY + 3, 0xffff00ff);

        }

        super.renderWidget(context, mouseX, mouseY, deltaTicks);
    }
}
