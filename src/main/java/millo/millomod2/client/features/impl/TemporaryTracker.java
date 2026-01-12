package millo.millomod2.client.features.impl;

import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.OnReceivePacket;
import millo.millomod2.client.hypercube.data.HypercubeLocation;
import millo.millomod2.client.hypercube.data.Spawn;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.network.packet.s2c.play.ClearTitleS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemporaryTracker extends Feature {

    private static double x, z;
    private static Sequence step = Sequence.WAIT_FOR_CLEAR;
    private static HypercubeLocation hypercubeLocation = new HypercubeLocation.UnknownLocation();
    private static HypercubeAPI.Mode mode = HypercubeAPI.Mode.IDLE;
    private static HypercubeAPI.Mode oldMode = HypercubeAPI.Mode.IDLE;

    private static boolean requestPlotId = false;
    private static int requestPlotIdDelay = 0;
    private static Vec3d localPlayerPos;


    @Override
    public String getId() {
        return "temporary_tracker";
    }

    private static void setMode(HypercubeAPI.Mode mode) {
        TemporaryTracker.mode = mode;
        step = Sequence.WAIT_FOR_CLEAR;
        requestPlotId = true;
        requestPlotIdDelay = 20;

        if (mode == HypercubeAPI.Mode.DEV) {
            hypercubeLocation.setPos(new Vec3d(x + 9.5, 0, z - 10.5));
        }
    }

    private static void setHypercubeLocation(HypercubeLocation location) {
        hypercubeLocation = location;
        if (mode != oldMode) {
            FeatureHandler.onModeChange(oldMode, mode); // Trigger after location has been found
            oldMode = mode;
        }
    }

    @OnReceivePacket
    public boolean clearTitle(ClearTitleS2CPacket clear) {
        if (clear.shouldReset()) {
            step = Sequence.WAIT_FOR_POS;
        }
        return false;
    }

    @OnReceivePacket
    public boolean positionLook(PlayerPositionLookS2CPacket packet) {
        if (step == Sequence.WAIT_FOR_POS) {
            x = packet.change().position().getX();
            z = packet.change().position().getZ();
            step = Sequence.WAIT_FOR_MESSAGE;
        }
        return false;
    }

    @OnReceivePacket
    public boolean overlay(OverlayMessageS2CPacket overlay) {
        if (step == Sequence.WAIT_FOR_MESSAGE && overlay.text().getString().matches("(⏵+ - )?⧈ -?\\d+ Tokens {2}ᛥ -?\\d+ Tickets {2}⚡ -?\\d+ Sparks")) {
            setMode(HypercubeAPI.Mode.IDLE);
        }
        return false;
    }

    @Override
    public void onTick() {
        if (requestPlotId) {
            if (requestPlotIdDelay > 0) requestPlotIdDelay--;
            else {
                requestPlotIdDelay = 120; // retry every 6 seconds
                PlayerUtil.sendCommand("locate");
            }
        }

        if (player() != null && hypercubeLocation.getPos() != null) {
            localPlayerPos = hypercubeLocation.getPos().relativize(player().getEntityPos()).add(-1, 0, 0);
        }
    }

    @OnReceivePacket
    public boolean gameMessage(GameMessageS2CPacket message) {
        String content = message.content().getString();
        if (step == Sequence.WAIT_FOR_MESSAGE) {
            if (content.equals("» You are now in dev mode.")) setMode(HypercubeAPI.Mode.DEV);
            if (content.equals("» You are now in build mode.")) setMode(HypercubeAPI.Mode.BUILD);
            if (content.startsWith("» Joined game: ")) setMode(HypercubeAPI.Mode.PLAY);
        }

        //                                        \nYou are currently coding on:\n\n? Millo5's Game [10764] \n? Owner: Millo5 \n? Server: Node Beta\n
        if (requestPlotId && content.startsWith("                          ")) {
            String regex = "\\[\\d+\\] (?=\\[[\\w-]+\\]\\n|\\n)";
            Matcher matcher = Pattern.compile(regex).matcher(content);
            if (matcher.find()) {

                // plot name
                String nameRegex = "(?<=→ ).*(?= \\[\\d+\\] (?=\\[[\\w-]+\\]\\n|\\n))";
                Matcher nameMatcher = Pattern.compile(nameRegex).matcher(content);
                if (nameMatcher.find()) {
                    String plotName = nameMatcher.group().trim();

                    // plot owner
                    String ownerRegex = "(?<=Owner: ).*(?= \\n)";
                    Matcher ownerMatcher = Pattern.compile(ownerRegex).matcher(content);
                    String plotOwner = "Unknown";
                    if (ownerMatcher.find()) plotOwner = ownerMatcher.group().trim();

                    // plot id
                    String plotIdString = matcher.group().trim().replace("[", "").replace("]", "");
                    int id = Integer.parseInt(plotIdString);
                    setHypercubeLocation(hypercubeLocation.update(plotName, id, plotOwner));
                    if (mode == HypercubeAPI.Mode.DEV) {
                        hypercubeLocation.setPos(new Vec3d(x + 9.5, 0, z - 10.5));
                    }
                    requestPlotId = false;
                    return true;
                }
            }
            regex = "spawn\\n";
            matcher = Pattern.compile(regex).matcher(content);
            if (matcher.find()) {
                if (!(hypercubeLocation instanceof Spawn)) setHypercubeLocation(new Spawn());
                requestPlotId = false;
                return true;
            }
        }

        return false;
    }

    public enum Sequence {
        WAIT_FOR_CLEAR,
        WAIT_FOR_POS,
        WAIT_FOR_MESSAGE,
    }


    //


    public static HypercubeAPI.Mode getMode() {
        return mode;
    }

    public static HypercubeLocation getHypercubeLocation() {
        return hypercubeLocation;
    }

    public static Vec3d getLocalPlayerPos() {
        return localPlayerPos;
    }

    public static Sequence getStep() {
        return step;
    }
}

