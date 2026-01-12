package millo.millomod2.client.util;

import millo.millomod2.client.features.impl.TemporaryTracker;
import millo.millomod2.client.hypercube.data.HypercubeLocation;
import millo.millomod2.client.hypercube.data.Plot;
import net.minecraft.util.math.Vec3d;

public class HypercubeAPI {

    public static boolean isInHypercube() {
        return true;
    }

    public static Vec3d getPlotOrigin() {
        return TemporaryTracker.getHypercubeLocation().getPos();
    }

    public enum Mode {
        IDLE,
        DEV,
        BUILD,
        PLAY
    }

    public static Mode getMode() {
        return TemporaryTracker.getMode();
    }

    public static HypercubeLocation getHypercubeLocation() {
        return TemporaryTracker.getHypercubeLocation();
    }

    public static int getPlotId() {
        return ((Plot) TemporaryTracker.getHypercubeLocation()).getId();
    }

    public static String getPlotName() {
        return ((Plot) TemporaryTracker.getHypercubeLocation()).getName();
    }


}
