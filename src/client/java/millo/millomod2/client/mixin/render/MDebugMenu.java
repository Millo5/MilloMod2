package millo.millomod2.client.mixin.render;

import millo.millomod2.client.util.HypercubeInfo;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Locale;

@Mixin(DebugHud.class)
public class MDebugMenu {

    @Unique
    private int insertIndex = -1;

    @Inject(method = "getLeftText", at = @At("RETURN"))
    private void leftText(CallbackInfoReturnable<List<String>> cir) {
        List<String> list = cir.getReturnValue();

        if (insertIndex >= 0) {
            // check if index is still correct (for reduced debug info)
            if (!list.get(insertIndex).matches("XYZ: .*")) {
                insertIndex = -1;
            }
        }
        if (insertIndex == -1) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).matches("XYZ: .*")) {
                    insertIndex = i;
                    break;
                }
            }
        }

        Vec3d pos = HypercubeInfo.getPlotOrigin().toBottomCenterPos();
        String s = String.format(Locale.ROOT, "Plot XYZ: %.3f / %.5f / %.3f", pos.getX(), pos.getY(), pos.getZ());
        if (insertIndex >= 0) list.add(insertIndex+1, s);
        else list.add(s);
    }


}
