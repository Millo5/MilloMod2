package millo.millomod2.client.features.impl.Waypoints;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.features.addons.OnReceivePacket;
import millo.millomod2.client.features.addons.WorldRendered;
import millo.millomod2.client.features.impl.TemporaryTracker;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.menus.WaypointMenu;
import millo.millomod2.client.rendering.world.Renderer;
import millo.millomod2.client.util.FileUtil;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.MilloLog;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Waypoints extends Feature implements WorldRendered, Keybound, Configurable {
    private final static String FILENAME = "waypoints.json";

    private final HashMap<Integer, ArrayList<Waypoint>> savedWaypoints = new HashMap<>();

    private int currentPlot = -1;

    private final ArrayList<Waypoint> waypoints = new ArrayList<>();
    private Waypoint selected = null;
    private Waypoint devExit = null;
    private Waypoint backWaypoint = null;
    private long lastBackTime = 0;

    private WaypointConfigCache configCache = null;

    public Waypoints() {
        try {
            load();
        } catch (Exception e) {
            MilloLog.error("Failed to load waypoints: " + e.getMessage());
        }
    }

    @Override
    public String getId() {
        return "waypoints";
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addBoolean("shown_in_world", true);
        config.addFloat("label_scale", 1.0f);
        config.addFloat("label_focus_scale", 1.0f);
        config.addFloat("label_hide_distance", 5.0f);
        config.addBoolean("auto_dev_mode", true);
        config.addBoolean("back_waypoint", true);
        config.addBoolean("dev_exit_waypoint", true);
    }


    @Override
    public void onTick() {
        if (player() == null) return;
        if (HypercubeAPI.getMode() != HypercubeAPI.Mode.DEV && HypercubeAPI.getMode() != HypercubeAPI.Mode.BUILD) return;
        configCache = new WaypointConfigCache(
                config.getFloat("label_scale"),
                config.getFloat("label_focus_scale"),
                config.getFloat("label_hide_distance")
        );

        while (getKeybind().wasPressed()) {
            if (selected != null && config.getBoolean("shown_in_world")) {
                selected.teleport();
                break;
            }
//            if (player().isSneaking()) new AddWaypointMenu(null, player().getEntityPos()).open();
            if (player().isSneaking()) new WaypointMenu(null).open();
        }

        Vec3d pos = player().getEyePos();
        Vec3d dir = player().getRotationVector();

        double highest = 0.98d;
        Waypoint highestWp = null;
        for (Waypoint waypoint : waypoints) {
            Vec3d off = waypoint.position().subtract(pos).normalize();
            double dot = off.dotProduct(dir);

            if (dot > highest) {
                highest = dot;
                highestWp = waypoint;
            }
        }
        select(highestWp);
    }

    private void select(Waypoint waypoint) {
        if (selected != null) selected.setSelected(false);
        if (waypoint != null) waypoint.setSelected(true);
        selected = waypoint;
    }

    @Override
    public void worldRender(Renderer renderer) {
        if (HypercubeAPI.getMode() != HypercubeAPI.Mode.DEV && HypercubeAPI.getMode() != HypercubeAPI.Mode.BUILD) return;
        if (configCache == null) return;
        if (!config.getBoolean("shown_in_world")) return;

        Vec3d camera = renderer.getRenderState().cameraRenderState.pos;
        for (Waypoint waypoint : new ArrayList<>(waypoints)) {
            waypoint.render(renderer, camera, configCache);
        }
    }

    @Override
    public void onModeChange(HypercubeAPI.Mode oldMode, HypercubeAPI.Mode newMode) {
        if (oldMode == HypercubeAPI.Mode.DEV) {
            if (!config.getBoolean("dev_exit_waypoint")) return;
            if (devExit != null) waypoints.remove(devExit);

            devExit = new Waypoint(TemporaryTracker.getLastModePlayerPos(), "Dev Exit", 0xffff970e);
            add(devExit, false);
        }
    }

    @OnReceivePacket
    public void teleport(PlayerPositionLookS2CPacket packet) {
        if (!config.getBoolean("back_waypoint")) return;
        if (HypercubeAPI.getMode() != HypercubeAPI.Mode.DEV && HypercubeAPI.getMode() != HypercubeAPI.Mode.BUILD) return;

        double dist = packet.change().position().subtract(player().getEntityPos()).length();
        if (dist < 5) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackTime < 200) return;
        lastBackTime = currentTime;

        if (backWaypoint != null) waypoints.remove(backWaypoint);
        backWaypoint = new Waypoint(player().getEntityPos(), "Back", 0xaaffaa);
        add(backWaypoint, false);
    }

    @Override
    public void onEnterPlot(Plot plot) {
        currentPlot = plot.getId();
        load(plot.getId());
    }

    @Override
    public void onEnterSpawn() {
        currentPlot = -1;
    }

    public void add(Waypoint waypoint, boolean save) {
        waypoints.add(waypoint);
        if (save) {
            savedWaypoints.computeIfAbsent(currentPlot, a -> new ArrayList<>()).add(waypoint);
            save();
        }
    }

    public void add(Waypoint waypoint) {
        add(waypoint, true);
    }

    private void load() {
        savedWaypoints.clear();

        String read = FileUtil.readJson(FILENAME);
        if (read == null) return;

        JsonObject json = JsonParser.parseString(read).getAsJsonObject();
        for (String key : json.keySet()) {
            ArrayList<Waypoint> list = new ArrayList<>();
            savedWaypoints.put(Integer.parseInt(key), list);
            JsonArray plot = json.get(key).getAsJsonArray();
            for (JsonElement wpJsonE : plot) {
                JsonObject wpJson = wpJsonE.getAsJsonObject();
                JsonObject pos = wpJson.get("position").getAsJsonObject();

                double x = pos.get("x").getAsDouble();
                double y = pos.get("y").getAsDouble();
                double z = pos.get("z").getAsDouble();
                String label = wpJson.get("label").getAsString();
                int color = wpJson.get("color").getAsInt();

                Waypoint waypoint = new Waypoint(new Vec3d(x, y, z), label, color);
                list.add(waypoint);
            }
        }
    }


    private void load(int id) {
        devExit = null;
        waypoints.clear();

        if (!savedWaypoints.containsKey(id)) return;
        waypoints.addAll(savedWaypoints.get(id));
    }
    private void save() {
        String waypointsJson = MilloMod.GSON.toJson(savedWaypoints);
        FileUtil.writeJson(FILENAME, waypointsJson);
    }


    public void removeWaypoint(Waypoint wp) {
        waypoints.remove(wp);
        if (savedWaypoints.containsKey(currentPlot)) {
            savedWaypoints.get(currentPlot).remove(wp);
        }
        save();
    }


    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }

    public Collection<String> getWaypointLabels() {
        ArrayList<String> labels = new ArrayList<>();
        for (Waypoint waypoint : waypoints) {
            labels.add(waypoint.label().getString());
        }
        return labels;
    }

    public Waypoint getWaypointByLabel(String label) {
        for (Waypoint waypoint : waypoints) {
            if (waypoint.label().getString().equals(label)) {
                return waypoint;
            }
        }
        return null;
    }


    public record WaypointConfigCache(float scale, float focusScale, float hideDistance) {}

}
