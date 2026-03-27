package millo.millomod2.client.features.impl.DevMovement;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.util.HypercubeAPI;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class UltrakillUltrakill {

    private ClientPlayerEntity player;

    private Vec3d velocity = Vec3d.ZERO;

    private boolean holdingSneak;
    private boolean holdingJump;
    private boolean holdingSprint;

    private boolean falling;
    private boolean sliding;
    private boolean boost;
    private boolean slam;

    private int fallTime;
    private int impactTime;
    private int jumpCooldown;
    private int dashTime;
    private int topSpeedTime;

    private final int jumpPower = 1;
    private double slamForce;
    private Vec3d boostDirection;
    private Vec3d dashDirection;
    private Vec3d topSpeedVel;
    private double topSpeed;

    private Vec3d cameraRelativeInput;

    private EntityAttributeModifier scaleModifier = new EntityAttributeModifier(Identifier.of("ultrakill_ultrakill", "scale"), -0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);


    public UltrakillUltrakill() {

    }

    public void reset() {
        velocity = MilloMod.player().getVelocity();
    }

    public Vec3d input(ClientPlayerEntity player, Vec3d movementInput) {
        if (!(HypercubeAPI.getHypercubeLocation() instanceof Plot plot)) return null;
        this.player = player;
        PlayerInput input = player.getLastPlayerInput();
        cameraRelativeInput = cameraRelativeInput(movementInput);

        // Press inputs
        boolean sneakReleased = holdingSneak & !input.sneak();
        boolean sneakPressed = !holdingSneak & (holdingSneak = input.sneak());

        boolean jumpReleased = holdingJump & !input.jump();
        boolean jumpPressed = !holdingJump & (holdingJump = input.jump());

        boolean sprintReleased = holdingSprint & !input.sprint();
        boolean sprintPressed = !holdingSprint & (holdingSprint = input.sprint());

        // Cooldowns
        if (jumpCooldown > 0) jumpCooldown --;
        if (impactTime > 0) impactTime --;

        double speed = velocity.length();
        if (speed > topSpeed) {
            topSpeed = speed;
            topSpeedVel = velocity;
            topSpeedTime = 5;
        } else if (topSpeedTime > 0) topSpeedTime--;
        else topSpeed = speed;

        // Ground and fall checks
        if (player.isOnGround()) {
            fallTime = 0;
        } else {
            if (fallTime < 5) fallTime ++;
            else if (!falling) {
                falling = true;
                slamForce = 0;
            }
        }

        // Impact
        if (falling) {
            if (player.isOnGround()) {
                falling = false;
                impactTime = 5;

                if (slam) {
                    slam = false;
                    boost = false;
                }
            }

            if (slam) {
                slamForce += 0.3d;
                if (!falling) {
                    slam = false;
                }
            }
        }

        // Jumping
        if (jumpPressed && jumpCooldown == 0) {
            if (!falling) jump();
        }

        // Sliding
        if (sneakPressed) {
            if (!falling && !sliding) startSlide();
            if (falling && !sliding) {
                slam = true;
                slamForce = 0;
                boost = true;
                boostDirection = new Vec3d(0, -2, 0);
            }
        }
        if (sliding) {
            if (sneakReleased) stopSlide();
        }

        // Dashing
        if (sprintPressed) {
            if (slam) {
                slam = false;
                boost = false;
            }
            if (sliding) stopSlide();

            dashDirection = cameraRelativeInput.multiply(0.5d);
            if (dashDirection.lengthSquared() == 0d) {
                dashDirection = new Vec3d(
                        -MathHelper.sin(player.getYaw() * ((float)Math.PI / 180F)),
                        0,
                        MathHelper.cos(player.getYaw() * ((float)Math.PI / 180F))
                ).multiply(0.5d);
            }
            dashTime = 4;
        }
        if (dashTime > 0) {
            if (--dashTime == 0) {
                velocity = dashDirection;
            } else {
                velocity = dashDirection.multiply(3d);
            }
        }


        // X-Z input movement
        velocity = velocity.add(0, -0.05, 0);
        if (boost) boost();
        else move();

        return movePlayer(movementInput, plot);
    }

    private void move() {
        if (player.isOnGround() && dashTime == 0) {
            velocity = velocity.add(cameraRelativeInput.multiply(1.4));
            velocity = velocity.multiply(0.2);
        } else {
            double dot = cameraRelativeInput.dotProduct(velocity);
            if (dot <= 0.5) {
                velocity = velocity.add(cameraRelativeInput.multiply(0.05));
            }
        }
    }

    private void boost() {
        if (sliding) {
            double horizontalBoost = 0.5;
            velocity = new Vec3d(boostDirection.x * horizontalBoost, velocity.y, boostDirection.z * horizontalBoost);
            return;
        }
        velocity = boostDirection;
    }

    /**
     * Apply movement with collision
     */
    private Vec3d movePlayer(Vec3d movement, Plot plot) {
//        double nearestFloor = Math.max(Math.floor(player.getY() / 5) * 5, plot.getFloorHeight());
        double nearestFloor = plot.getFloorHeight();

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        x += velocity.x;
        y += velocity.y;
        z += velocity.z;

        double halfWidth = player.getWidth() / 2;
        x = Math.max(x, plot.getPos().getX() - plot.getDepth() + halfWidth);
        z = Math.clamp(z, plot.getPos().getZ() + halfWidth, plot.getPos().getZ() + 301 - halfWidth);
        if (x == plot.getPos().getX() - plot.getDepth() + halfWidth) velocity = new Vec3d(0, velocity.y, velocity.z);
        if (z == plot.getPos().getZ() + halfWidth || z == plot.getPos().getZ() + 301 - halfWidth) velocity = new Vec3d(velocity.x, velocity.y, 0);

        if (y < nearestFloor) {
            y = nearestFloor;
            velocity = new Vec3d(velocity.x, 0, velocity.z);
            player.setOnGround(true);
        } else player.setOnGround(false);

        player.setPosition(x, y, z);
        player.setVelocityClient(velocity);

        return movement;
    }

    private void startSlide() {
        sliding = true;
        boost = true;

        double speed = Math.max(topSpeed * 2, 1);
        boostDirection = cameraRelativeInput.multiply(speed);
        if (boostDirection.lengthSquared() == 0d) {
            boostDirection = new Vec3d(
                    -MathHelper.sin(player.getYaw() * ((float)Math.PI / 180F)),
                    0,
                    MathHelper.cos(player.getYaw() * ((float)Math.PI / 180F))
            );
        }
        if (dashTime > 0) {
            dashTime = 0;
//            boostDirection = boostDirection.multiply(2);
        }

        if (impactTime > 0 && slamForce > 0) {
            boostDirection = boostDirection.normalize().multiply(slamForce * 1.5 + 2);
        }

        scaleModifier = new EntityAttributeModifier(Identifier.of("ultrakill_ultrakill", "scale"), -0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        EntityAttributeInstance scale = player.getAttributeInstance(EntityAttributes.SCALE);
        if (scale != null) scale.addTemporaryModifier(scaleModifier);
    }

    private void stopSlide() {
        sliding = false;
        boost = false;

        EntityAttributeInstance scale = player.getAttributeInstance(EntityAttributes.SCALE);
        if (scale != null) scale.removeModifier(scaleModifier);
    }

    private void jump() {
        double jumpPower = 0.25d;

        if (sliding) {
            addForce(0, jumpPower * 2, 0);
            stopSlide();
        } else if (dashTime > 0) {
            addForce(0, jumpPower * 1.5, 0);
            dashTime = 0;
        } else if (impactTime > 0 && slamForce > 0) {
            addForce(0, jumpPower * (slamForce + 3), 0);
        } else {
            addForce(0, jumpPower * 2.6, 0);
        }

        jumpCooldown = 4;
        boost = false;
        player.setOnGround(false);
    }

    private void addForce(double x, double y, double z) {
        velocity = velocity.add(x, y, z);
    }

    private Vec3d cameraRelativeInput(Vec3d movementInput) {
        float yaw = player.getYaw();

        float f = MathHelper.sin(yaw * ((float)Math.PI / 180F));
        float g = MathHelper.cos(yaw * ((float)Math.PI / 180F));
        return new Vec3d(
                movementInput.x * g - movementInput.z * f,
                movementInput.y,
                movementInput.z * g + movementInput.x * f
        ).normalize();
    }

}
