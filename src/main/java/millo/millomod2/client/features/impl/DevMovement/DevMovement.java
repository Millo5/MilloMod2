package millo.millomod2.client.features.impl.DevMovement;

import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.util.HypercubeAPI;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

public class DevMovement extends Feature implements Toggleable, Configurable {
    private final static DevMovement INSTANCE = new DevMovement();

    private final UltrakillUltrakill ultrakill = new UltrakillUltrakill();

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

        config.addBoolean("ultrakill_mode", false);
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
                player().getX() < plot.getPos().x();
    }

    public boolean isNoClipping() {
        return isEnabled() && (config.getBoolean("ultrakill_mode") || config.getBoolean("no_clip"));
    }


    public Vec3 entityMove(Vec3 move) {
        if (!isEnabled()) return null;
        if (!(HypercubeAPI.getHypercubeLocation() instanceof Plot plot)) return null;
        LocalPlayer player = player();

        Vec3 vel = player.getDeltaMovement();
        double x = player.getX() + move.x;
        double y = player.getY() + move.y;
        double z = player.getZ() + move.z;

        double halfWidth = player.getBbWidth() / 2;

        if (x > plot.getPos().x() - halfWidth) return null;

        x = Math.max(x, plot.getPos().x() - plot.getDepth() + halfWidth);
        z = Math.clamp(z, plot.getPos().z() + halfWidth, plot.getPos().z() + 301 - halfWidth);


        if (x == plot.getPos().x() - plot.getDepth() + halfWidth) vel = new Vec3(0, vel.y, vel.z);
        if (z == plot.getPos().z() + halfWidth || z == plot.getPos().z() + 301 - halfWidth)
            vel = new Vec3(vel.x, vel.y, 0);

        if (!isNoClipping()) return new Vec3(x, y, z);

        double nearestFloor = Math.floor(player.getY() / 5) * 5;
        if (y < plot.getFloorHeight()) y = plot.getFloorHeight();

        player.setOnGround(false);
        boolean floorCollision = y < nearestFloor && !player.getAbilities().flying;
        boolean desire = player.getXRot() > config.getInt("down_angle");
        if (config.getBoolean("down_sneak") && !player.isShiftKeyDown()) desire = false;
        if (y == plot.getFloorHeight() || (floorCollision && !desire)) {
            vel = new Vec3(vel.x, 0, vel.z);
            y = nearestFloor;
            player.setOnGround(true);
        }

        player.lerpMotion(vel);

        if (y > 256) y = 256;

        return new Vec3(x, y, z);
    }


    private int lastMovePacketTick = 0;
    private float lastYaw, lastPitch;
    private Vec3 lastPos = Vec3.ZERO;

    public boolean sendMovementPackets() {
        if (!isNoClipping()) return false;
        LocalPlayer player = player();

        Vec3 pos = getServerPos();
        boolean idle = lastMovePacketTick++ > 20;
        if (idle) lastMovePacketTick = 0;
        boolean moved = !lastPos.equals(pos) || idle;

        float yaw = player.getYRot();
        float pitch = player.getXRot();
        boolean rotated = lastYaw != yaw || lastPitch != pitch;

        if (moved || rotated) {
            if (moved && rotated) {
                net().send(new ServerboundMovePlayerPacket.PosRot(pos, yaw, pitch, false, true));
            } else if (moved) {
                net().send(new ServerboundMovePlayerPacket.Pos(pos, false, true));
            } else {
                net().send(new ServerboundMovePlayerPacket.Rot(yaw, pitch, false, true));
            }

            lastYaw = yaw;
            lastPitch = pitch;
            lastPos = pos;
        }

        return true;
    }


    private Vec3 getServerPos() {
        LocalPlayer player = player();

        Vec3 middlePos = player.position().add(0, 0.899500400000006, 0);
        AABB box = AABB.ofSize(middlePos, 0.68f, 1.799000800000012, 0.68f); // magic numbers from Entity.class
        boolean insideWall = BlockPos.betweenClosedStream(box).anyMatch((pos) -> {
            BlockState blockState = player.level().getBlockState(pos);
            return !blockState.isAir() &&
                    Shapes.joinIsNotEmpty(blockState.getCollisionShape(player.level(), pos).move(pos), Shapes.create(box), BooleanOp.AND);
        });

        if (insideWall) {
            double y = Math.floor(player.getY() / 5) * 5;
            return new Vec3(player.getX(), y + 2, player.getZ());
        }

        return new Vec3(player.getX(), player.getY(), player.getZ());
    }

    public Float getOffGroundSpeed() {
        if (!isEnabled() || !config.getBoolean("acceleration")) return null;
        if (player().getAbilities().flying) return null;

        return 0.026f * config.getInt("acceleration_amount");
    }

    public Float getJumpVelocity() {
        if (!isEnabled() || !isNoClipping()) return null;
        boolean desire = player().getXRot() < -config.getInt("up_angle");
        if (!desire) return null;
        return 0.91f;
    }

    public Vec3 applyMovementInput(Vec3 movementInput, float slipperiness) {
        if (!config.getBoolean("ultrakill_mode")) return null;
        if (!isEnabled()) {
            ultrakill.reset();
            return null;
        }

        return ultrakill.input(player(), movementInput);
    }
}
