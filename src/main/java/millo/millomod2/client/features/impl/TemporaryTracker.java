package millo.millomod2.client.features.impl;

import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.PacketEventBus;
import millo.millomod2.client.features.addons.PacketEventSubscriber;
import millo.millomod2.client.hypercube.data.HypercubeLocation;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.hypercube.data.Spawn;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemporaryTracker extends Feature implements PacketEventSubscriber {

    private static double x, z;
    private static Sequence step = Sequence.WAIT_FOR_CLEAR;
    private static HypercubeLocation hypercubeLocation = new HypercubeLocation.UnknownLocation();
    private static HypercubeAPI.Mode mode = HypercubeAPI.Mode.IDLE;
    private static HypercubeAPI.Mode oldMode = HypercubeAPI.Mode.IDLE;

    private static boolean requestPlotId = false;
    private static int requestPlotIdDelay = 0;
    private static Vec3 localPlayerPos;

    private static Vec3 lastModePlayerPos;


    @Override
    public String getId() {
        return "temporary_tracker";
    }

    private static void setMode(HypercubeAPI.Mode mode) {
        TemporaryTracker.mode = mode;
        step = Sequence.WAIT_FOR_CLEAR;
        requestPlotId = true;
        requestPlotIdDelay = 5;

        if (mode == HypercubeAPI.Mode.DEV) {
            hypercubeLocation.setPos(new Vec3(x + 9.5 + 2, 0, z - 10.5));
        }
    }

    private static void setHypercubeLocation(HypercubeLocation location) {
        hypercubeLocation = location;
        if (mode != oldMode) {
            FeatureHandler.onModeChange(oldMode, mode); // Trigger after location has been found
            oldMode = mode;
        }
    }

    @Override
    public void subscribePackets(PacketEventBus eventBus) {
        eventBus.subscribeReceive(ClientboundClearTitlesPacket.class, this::clearTitle);
        eventBus.subscribeReceive(ClientboundPlayerPositionPacket.class, this::positionLook);
        eventBus.subscribeReceive(ClientboundSetActionBarTextPacket.class, this::overlay);
        eventBus.subscribeReceive(ClientboundSystemChatPacket.class, this::gameMessage);
    }

    public boolean clearTitle(ClientboundClearTitlesPacket clear) {
        if (clear.shouldResetTimes()) {
            step = Sequence.WAIT_FOR_POS;
        }
        return false;
    }

    public boolean positionLook(ClientboundPlayerPositionPacket packet) {
        if (step == Sequence.WAIT_FOR_POS) {
            if (player() != null) lastModePlayerPos = player().position();
            x = packet.change().position().x();
            z = packet.change().position().z();
            step = Sequence.WAIT_FOR_MESSAGE;
        }
        return false;
    }

    public boolean overlay(ClientboundSetActionBarTextPacket overlay) {
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
            localPlayerPos = hypercubeLocation.getPos().vectorTo(player().position()).add(-1, 0, 0);
        }

        if (hypercubeLocation instanceof Plot plot && mode == HypercubeAPI.Mode.DEV) {
            if (MC.level != null) {
                BlockState undergroundCheckBlock = MC.level.getBlockState(new BlockPos(
                        (int) plot.getPos().x-1,
                        49,
                        (int) plot.getPos().z
                ));
                if (!undergroundCheckBlock.is(Blocks.VOID_AIR)) plot.setHasUnderground(undergroundCheckBlock.is(Blocks.AIR));

                BlockState megaCheckBlock = MC.level.getBlockState(new BlockPos(
                        (int) plot.getPos().x-21,
                        49,
                        (int) plot.getPos().z
                ));
                if (!megaCheckBlock.is(Blocks.VOID_AIR)) plot.setMega(megaCheckBlock.is(Blocks.STONE) || megaCheckBlock.is(Blocks.AIR));
            }
        }

    }

    public boolean gameMessage(ClientboundSystemChatPacket message) {
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
                        hypercubeLocation.setPos(new Vec3(x + 11.5, 0, z - 10.5));
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


    public static Vec3 getLastModePlayerPos() {
        return lastModePlayerPos;
    }

    public static HypercubeAPI.Mode getMode() {
        return mode;
    }

    public static HypercubeLocation getHypercubeLocation() {
        return hypercubeLocation;
    }

    public static Vec3 getLocalPlayerPos() {
        return localPlayerPos;
    }

    public static Sequence getStep() {
        return step;
    }
}

