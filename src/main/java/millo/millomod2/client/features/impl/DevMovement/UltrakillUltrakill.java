package millo.millomod2.client.features.impl.DevMovement;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.util.HypercubeAPI;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec3;

public class UltrakillUltrakill {

    private LocalPlayer player;

    private Vec3 velocity = Vec3.ZERO;

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
    private Vec3 boostDirection;
    private Vec3 dashDirection;
    private Vec3 topSpeedVel;
    private double topSpeed;

    private Vec3 cameraRelativeInput;

    private AttributeModifier scaleModifier = new AttributeModifier(Identifier.fromNamespaceAndPath("ultrakill_ultrakill", "scale"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);


    public UltrakillUltrakill() {

    }

    public void reset() {
        velocity = MilloMod.player().getDeltaMovement();
    }

    public Vec3 input(LocalPlayer player, Vec3 movementInput) {
        if (!(HypercubeAPI.getHypercubeLocation() instanceof Plot plot)) return null;
        this.player = player;
        Input input = player.getLastSentInput();
        cameraRelativeInput = cameraRelativeInput(movementInput);

        // Press inputs
        boolean sneakReleased = holdingSneak & !input.shift();
        boolean sneakPressed = !holdingSneak & (holdingSneak = input.shift());

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
        if (player.onGround()) {
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
            if (player.onGround()) {
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
                boostDirection = new Vec3(0, -2, 0);
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

            dashDirection = cameraRelativeInput.scale(0.5d);
            if (dashDirection.lengthSqr() == 0d) {
                dashDirection = new Vec3(
                        -Mth.sin(player.getYRot() * ((float)Math.PI / 180F)),
                        0,
                        Mth.cos(player.getYRot() * ((float)Math.PI / 180F))
                ).scale(0.5d);
            }
            dashTime = 4;
        }
        if (dashTime > 0) {
            if (--dashTime == 0) {
                velocity = dashDirection;
            } else {
                velocity = dashDirection.scale(3d);
            }
        }


        // X-Z input movement
        velocity = velocity.add(0, -0.05, 0);
        if (boost) boost();
        else move();

        return movePlayer(movementInput, plot);
    }

    private void move() {
        if (player.onGround() && dashTime == 0) {
            velocity = velocity.add(cameraRelativeInput.scale(1.4));
            velocity = velocity.scale(0.2);
        } else {
            double dot = cameraRelativeInput.dot(velocity);
            if (dot <= 0.5) {
                velocity = velocity.add(cameraRelativeInput.scale(0.05));
            }
        }
    }

    private void boost() {
        if (sliding) {
            double horizontalBoost = 0.5;
            velocity = new Vec3(boostDirection.x * horizontalBoost, velocity.y, boostDirection.z * horizontalBoost);
            return;
        }
        velocity = boostDirection;
    }

    /**
     * Apply movement with collision
     */
    private Vec3 movePlayer(Vec3 movement, Plot plot) {
//        double nearestFloor = Math.max(Math.floor(player.getY() / 5) * 5, plot.getFloorHeight());
        double nearestFloor = plot.getFloorHeight();

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        x += velocity.x;
        y += velocity.y;
        z += velocity.z;

        double halfWidth = player.getBbWidth() / 2;
        x = Math.max(x, plot.getPos().x() - plot.getDepth() + halfWidth);
        z = Math.clamp(z, plot.getPos().z() + halfWidth, plot.getPos().z() + 301 - halfWidth);
        if (x == plot.getPos().x() - plot.getDepth() + halfWidth) velocity = new Vec3(0, velocity.y, velocity.z);
        if (z == plot.getPos().z() + halfWidth || z == plot.getPos().z() + 301 - halfWidth) velocity = new Vec3(velocity.x, velocity.y, 0);

        if (y < nearestFloor) {
            y = nearestFloor;
            velocity = new Vec3(velocity.x, 0, velocity.z);
            player.setOnGround(true);
        } else player.setOnGround(false);

        player.setPos(x, y, z);
        player.lerpMotion(velocity);

        return movement;
    }

    private void startSlide() {
        sliding = true;
        boost = true;

        double speed = Math.max(topSpeed * 2, 1);
        boostDirection = cameraRelativeInput.scale(speed);
        if (boostDirection.lengthSqr() == 0d) {
            boostDirection = new Vec3(
                    -Mth.sin(player.getYRot() * ((float)Math.PI / 180F)),
                    0,
                    Mth.cos(player.getYRot() * ((float)Math.PI / 180F))
            );
        }
        if (dashTime > 0) {
            dashTime = 0;
//            boostDirection = boostDirection.multiply(2);
        }

        if (impactTime > 0 && slamForce > 0) {
            boostDirection = boostDirection.normalize().scale(slamForce * 1.5 + 2);
        }

        scaleModifier = new AttributeModifier(Identifier.fromNamespaceAndPath("ultrakill_ultrakill", "scale"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        AttributeInstance scale = player.getAttribute(Attributes.SCALE);
        if (scale != null) scale.addTransientModifier(scaleModifier);
    }

    private void stopSlide() {
        sliding = false;
        boost = false;

        AttributeInstance scale = player.getAttribute(Attributes.SCALE);
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

    private Vec3 cameraRelativeInput(Vec3 movementInput) {
        float yaw = player.getYRot();

        float f = Mth.sin(yaw * ((float)Math.PI / 180F));
        float g = Mth.cos(yaw * ((float)Math.PI / 180F));
        return new Vec3(
                movementInput.x * g - movementInput.z * f,
                movementInput.y,
                movementInput.z * g + movementInput.x * f
        ).normalize();
    }

}
