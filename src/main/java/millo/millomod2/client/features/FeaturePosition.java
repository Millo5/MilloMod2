package millo.millomod2.client.features;

import millo.millomod2.client.MilloMod;

public class FeaturePosition {

    public enum Anchor {
        TOP_LEFT(false, false),
        TOP_RIGHT(true, false),
        BOTTOM_LEFT(false, true),
        BOTTOM_RIGHT(true, true);

        public final boolean right;
        public final boolean bottom;
        Anchor(boolean right, boolean bottom) {
            this.right = right;
            this.bottom = bottom;
        }
    }

    private int x;
    private int y;
    private final int width;
    private final int height;
    private Anchor anchor = Anchor.TOP_LEFT;

    public FeaturePosition(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public FeaturePosition(int x, int y, int width, int height, Anchor anchor) {
        this(x, y, width, height);
        this.anchor = anchor;
    }

    public int getX() {
        if (anchor.right) return MilloMod.MC.getWindow().getScaledWidth() - x - width;
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        if (anchor.bottom) return MilloMod.MC.getWindow().getScaledHeight() - y - height;
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Anchor getAnchor() {
        return anchor;
    }
}
