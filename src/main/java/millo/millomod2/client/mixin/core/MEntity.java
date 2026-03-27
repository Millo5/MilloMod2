package millo.millomod2.client.mixin.core;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.DevMovement.DevMovement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MEntity {

    @Shadow
    public abstract int getId();

    @Shadow
    public abstract void setPosition(Vec3d pos);


    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void move(MovementType type, Vec3d movement, CallbackInfo ci) {
        if (MilloMod.player() == null || MilloMod.player().getId() != this.getId()) return;
        if (!DevMovement.getInstance().isNoClipping()) return;

        Vec3d pos = DevMovement.getInstance().entityMove(movement);
        if (pos != null) {
            this.setPosition(pos);
            ci.cancel();
        }
    }

    @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
    private Vec3d modifyMoveVec(Vec3d movement) {
        if (MilloMod.player() == null || MilloMod.player().getId() != this.getId()) return movement;
        if (DevMovement.getInstance().isNoClipping()) return movement;
        if (!DevMovement.getInstance().isEnabled()) return movement;

        Vec3d newMovement = DevMovement.getInstance().entityMove(movement);
        if (newMovement != null) {
            var player = MilloMod.player();
            return new Vec3d(
                    newMovement.x - player.getX(),
                    newMovement.y - player.getY(),
                    newMovement.z - player.getZ()
            );
        }
        return movement;
    }

}
