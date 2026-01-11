package millo.millomod2.client.mixin.render;

import millo.millomod2.client.util.HypercubeInfo;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Locale;

@Mixin(DebugHud.class)
public class MDebugMenu {

    @Unique
    private int insertIndex = -1;

    @Inject(method = "drawText", at = @At(value = "HEAD"))
    private void leftText(DrawContext context, List<String> text, boolean left, CallbackInfo ci) {
        if (!left) return;

        if (insertIndex >= 0) {
            // check if index is still correct (for reduced debug info)
            if (!text.get(insertIndex).matches("XYZ: .*")) {
                insertIndex = -1;
            }
        }
        if (insertIndex == -1) {
            for (int i = 0; i < text.size(); i++) {
                if (text.get(i).matches("XYZ: .*")) {
                    insertIndex = i;
                    break;
                }
            }
        }

        Vec3d pos = HypercubeInfo.getPlotOrigin().toBottomCenterPos();
        String s = String.format(Locale.ROOT, "Plot XYZ: %.3f / %.5f / %.3f", pos.getX(), pos.getY(), pos.getZ());
        if (insertIndex >= 0) text.add(insertIndex+1, s);
        else text.add(s);
    }


}
