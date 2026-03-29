package millo.millomod2.client.features;

import millo.millomod2.client.MilloMod;

public class FeaturePosition {


    public enum Anchor {
        TOP_LEFT(0, 0),
        TOP_CENTER(1, 0),
        TOP_RIGHT(2, 0),
        CENTER_LEFT(0, 1),
        CENTER(1, 1),
        CENTER_RIGHT(2, 1),
        BOTTOM_LEFT(0, 2),
        BOTTOM_CENTER(1, 2),
        BOTTOM_RIGHT(2, 2);

        public final byte x;
        public final byte y;
        Anchor(int x, int y) {
            this.x = (byte) x;
            this.y = (byte) y;
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
        if (anchor.x == 2) return MilloMod.MC.getWindow().getScaledWidth() - x - width;
        if (anchor.x == 1) return MilloMod.MC.getWindow().getScaledWidth() / 2 - width / 2 + x;
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setScreenX(int x) {
        if (anchor.x == 2) this.x = MilloMod.MC.getWindow().getScaledWidth() - x - width;
        else if (anchor.x == 1) this.x = x - MilloMod.MC.getWindow().getScaledWidth() / 2 + width / 2;
        else this.x = x;
    }

    public int getY() {
        if (anchor.y == 2) return MilloMod.MC.getWindow().getScaledHeight() - y - height;
        if (anchor.y == 1) return MilloMod.MC.getWindow().getScaledHeight() / 2 - height / 2 + y;
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setScreenY(int y) {
        if (anchor.y == 2) this.y = MilloMod.MC.getWindow().getScaledHeight() - y - height;
        else if (anchor.y == 1) this.y = y - MilloMod.MC.getWindow().getScaledHeight() / 2 + height / 2;
        else this.y = y;
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

    public int getTrueX() {
        return x;
    }

    public int getTrueY() {
        return y;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }
}
