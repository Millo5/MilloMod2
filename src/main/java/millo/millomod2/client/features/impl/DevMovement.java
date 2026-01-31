package millo.millomod2.client.features.impl;

import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.util.HypercubeAPI;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;

public class DevMovement extends Feature implements Toggleable, Configurable {
    private final static DevMovement INSTANCE = new DevMovement();

    @Override
    public String getId() {
        return "dev_movement";
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addBoolean("no_clip", true);
        config.addIntegerRange("down_angle", 50, 10, 90);
        config.addBoolean("down_sneak", true);
        config.addIntegerRange("up_angle", 50, 10, 90);


        config.addBoolean("acceleration", true);
        config.addIntegerRange("acceleration_amount", 1, 1, 10);
    }

    public static DevMovement getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isEnabled() {
        return Toggleable.super.isEnabled() &&
                HypercubeAPI.getMode() == HypercubeAPI.Mode.DEV &&
                player() != null &&
                player().isCreative() &&
                HypercubeAPI.getHypercubeLocation() instanceof Plot plot &&
                player().getX() < plot.getPos().getX();
    }

    public boolean isNoClipping() {
        return isEnabled() && config.getBoolean("no_clip");
    }


    public Vec3d entityMove(Vec3d move) {
        if (!isEnabled()) return null;
        if (!(HypercubeAPI.getHypercubeLocation() instanceof Plot plot)) return null;
        ClientPlayerEntity player = player();

        Vec3d vel = player.getVelocity();
        double x = player.getX() + move.x;
        double y = player.getY() + move.y;
        double z = player.getZ() + move.z;

        double halfWidth = player.getWidth() / 2;
        x = Math.max(x, plot.getPos().getX() - plot.getDepth() + halfWidth);
        z = Math.clamp(z, plot.getPos().getZ() + halfWidth, plot.getPos().getZ() + 301 - halfWidth);


        if (x == plot.getPos().getX() - plot.getDepth() + halfWidth) vel = new Vec3d(0, vel.y, vel.z);
        if (z == plot.getPos().getZ() + halfWidth || z == plot.getPos().getZ() + 301 - halfWidth)
            vel = new Vec3d(vel.x, vel.y, 0);

        if (!isNoClipping()) return new Vec3d(x, y, z);

        double nearestFloor = Math.floor(player.getY() / 5) * 5;
        if (y < plot.getFloorHeight()) y = plot.getFloorHeight();

        player.setOnGround(false);
        boolean floorCollision = y < nearestFloor && !player.getAbilities().flying;
        boolean desire = player.getPitch() > config.getInt("down_angle");
        if (config.getBoolean("down_sneak") && !player.isSneaking()) desire = false;
        if (y == plot.getFloorHeight() || (floorCollision && !desire)) {
            vel = new Vec3d(vel.x, 0, vel.z);
            y = nearestFloor;
            player.setOnGround(true);
        }

        player.setVelocityClient(vel);

        if (y > 256) y = 256;

        return new Vec3d(x, y, z);
    }


    private int lastMovePacketTick = 0;
    private float lastYaw, lastPitch;
    private Vec3d lastPos = Vec3d.ZERO;

    public boolean sendMovementPackets() {
        if (!isNoClipping()) return false;
        ClientPlayerEntity player = player();

        Vec3d pos = getServerPos();
        boolean idle = lastMovePacketTick++ > 20;
        if (idle) lastMovePacketTick = 0;
        boolean moved = !lastPos.equals(pos) || idle;

        float yaw = player.getYaw();
        float pitch = player.getPitch();
        boolean rotated = lastYaw != yaw || lastPitch != pitch;

        if (moved || rotated) {
            if (moved && rotated) {
                net().sendPacket(new PlayerMoveC2SPacket.Full(pos, yaw, pitch, false, true));
            } else if (moved) {
                net().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos, false, true));
            } else {
                net().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, false, true));
            }

            lastYaw = yaw;
            lastPitch = pitch;
            lastPos = pos;
        }

        return true;
    }


    private Vec3d getServerPos() {
        ClientPlayerEntity player = player();

        Vec3d middlePos = player.getEntityPos().add(0, player.getHeight() / 2, 0);
        Box box = Box.of(middlePos, 0.68f, 1.799000800000012, 0.68f); // magic numbers from Entity.class
        boolean insideWall = BlockPos.stream(box).anyMatch((pos) -> {
            BlockState blockState = player.getEntityWorld().getBlockState(pos);
            return !blockState.isAir() &&
                    VoxelShapes.matchesAnywhere(blockState.getCollisionShape(player.getEntityWorld(), pos).offset(pos), VoxelShapes.cuboid(box), BooleanBiFunction.AND);
        });

        if (insideWall) {
            double y = Math.floor(player.getY() / 5) * 5;
            return new Vec3d(player.getX(), y + 2, player.getZ());
        }

        return new Vec3d(player.getX(), player.getY(), player.getZ());
    }

    public Float getOffGroundSpeed() {
        if (!isEnabled() || !config.getBoolean("acceleration")) return null;
        if (player().getAbilities().flying) return null;

        return 0.026f * config.getInt("acceleration_amount");
    }

    public Float getJumpVelocity() {
        if (!isNoClipping()) return null;
        boolean desire = player().getPitch() < -config.getInt("up_angle");
        if (!desire) return null;
        return 0.91f;
    }
}
