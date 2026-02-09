package millo.millomod2.client.features.impl.Waypoints;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.TeleportHandler;
import millo.millomod2.client.rendering.world.Renderer;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public final class Waypoint {
    private final WaypointPos position;
    private final String label;
    private final int color;

    private final transient Text text;

    private transient boolean selected;
    private transient float selectionTime;
    private transient float hideTime;


    public Waypoint(Vec3d position, String label, int color) {
        this.position = WaypointPos.from(position);
        this.label = label;
        this.color = color;

        text = Text.literal(label).withColor(color);
    }

    public void render(Renderer renderer, Vec3d camera, Waypoints.WaypointConfigCache configCache) {
        double dist = camera.distanceTo(position.get());
        boolean hide = dist < configCache.hideDistance();
        if (dist < 10f) dist = 10f;

        hideTime = renderer.lerp(hideTime, hide ? 1f : 0f, 12f);
        selectionTime = renderer.lerp(selectionTime, selected ? 1f : 0f, 6f);

        float scale = 0.1f * configCache.scale();
        scale += 0.08f * selectionTime * configCache.focusScale();
        if (hideTime > 0.01f) {
            scale *= (1f - hideTime);
        }

        if (scale < 0.001f) return;

        renderer.text(text, position.get(), (float) dist * scale, 0, 0);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Vec3d position() {
        return position.get();
    }

    public Text label() {
        return text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Waypoint) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, label);
    }

    @Override
    public String toString() {
        return "Waypoint[" +
                "position=" + position + ", " +
                "label=" + label + ']';
    }

    public void teleport() {
        TeleportHandler.teleportTo(position.get(), false, FeatureHandler.get(Waypoints.class).getConfig().getBoolean("auto_dev_mode"));
    }


    // For serialisation
    private static final class WaypointPos {
        private final double x;
        private final double y;
        private final double z;
        private transient final Vec3d cache;

        private WaypointPos(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.cache = new Vec3d(x, y, z);
        }

        public static WaypointPos from(Vec3d vec) {
                return new WaypointPos(vec.x, vec.y, vec.z);
            }

        public Vec3d get() {
            return cache;
        }

    }

}
