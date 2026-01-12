package millo.millomod2.menu;

import org.joml.Matrix3x2fStack;

public interface FadeElement {

    Fade getFade();

    class Fade {
        private float progress;
        private final Direction direction;
        private boolean locked = false;
        private Fade parentFade = null;

        public Fade() {
            this(Direction.UP);
        }

        public Fade(Direction direction) {
            this.direction = direction;
            this.progress = 0f;
        }

        public boolean progress(float delta) {
            if (locked) return getProgress() >= 1f;
            progress = Math.min(progress + delta * 0.2f, 1f);
            return getProgress() >= 1f;
        }

        private float getProgress() {
            if (locked && parentFade != null) {
                return parentFade.getProgress();
            }
            return progress;
        }

        private int getXOffset() {
            if (locked) return 0;
            return (int)(direction.dx * ((1f - getProgress()) * 10f));
        }

        private int getYOffset() {
            if (locked) return 0;
            return (int)(direction.dy * ((1f - getProgress()) * 10f));
        }

        public void applyTranslation(Matrix3x2fStack matrices) {
            matrices.translate(getXOffset(), getYOffset());
        }

        public int getColor(int r, int g, int b, int a) {
            int alpha = (int)(a * getProgress());
            return (alpha << 24) | (r << 16) | (g << 8) | b;
        }
        public int getColor(int color) {
            int a = (color >> 24) & 0xFF;
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            return getColor(r, g, b, a);
        }

        public void lock(Fade fade) {
            this.locked = true;
            this.parentFade = fade;
        }

        public void unlock() {
            this.locked = false;
        }

        public void reset() {
            this.progress = 0f;
        }

        public enum Direction {
            UP(0, -1),
            DOWN(0, 1),
            LEFT(-1, 0),
            RIGHT(1, 0);

            private final int dx, dy;
            Direction(int dx, int dy) {
                this.dx = dx;
                this.dy = dy;
            }
        }
    }
}
