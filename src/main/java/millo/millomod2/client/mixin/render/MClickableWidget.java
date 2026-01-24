package millo.millomod2.client.mixin.render;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import millo.millomod2.menu.elements.ClickableElement;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.tooltip.TooltipState;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClickableWidget.class)
public class MClickableWidget {

    @WrapWithCondition(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/tooltip/TooltipState;render(Lnet/minecraft/client/gui/DrawContext;IIZZLnet/minecraft/client/gui/ScreenRect;)V"
            )
    )
    private boolean skipTooltip(TooltipState instance, DrawContext context, int mouseX, int mouseY, boolean hovered, boolean focused, ScreenRect navigationFocus) {
        ClickableWidget o = (ClickableWidget) (Object) this;
        if (o instanceof ClickableElement<?>) return false;
        return !(o instanceof TextElement);
    }

}
