package millo.millomod2.client.util;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class HypercubeInfo {

    public static float getPlotCPU() {
        return 0.5f;
    }

    public static BlockPos getPlotOrigin() {
        return new BlockPos(0, 0, 0);
    }

    public enum Mode {
        IDLE,
        DEV,
        BUILD,
        PLAY
    }

    public static Mode getMode() {
        return Mode.DEV;
    }

    public static int getPlotId() {
        return 1;
    }

    public static Text getPlotName() {
        return Text.literal("My Plot");
    }


}
