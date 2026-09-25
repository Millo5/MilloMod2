package millo.millomod2.client.mixin.render;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import millo.millomod2.menu.elements.ClickableElement;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractWidget.class)
public class MClickableWidget {

    @WrapWithCondition(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/WidgetTooltipHolder;refreshTooltipForNextRenderPass(Lnet/minecraft/client/gui/GuiGraphics;IIZZLnet/minecraft/client/gui/navigation/ScreenRectangle;)V"
            )
    )
    private boolean skipTooltip(WidgetTooltipHolder instance, GuiGraphics context, int mouseX, int mouseY, boolean hovered, boolean focused, ScreenRectangle navigationFocus) {
        AbstractWidget o = (AbstractWidget) (Object) this;
        if (o instanceof ClickableElement<?>) return false;
        return !(o instanceof TextElement);
    }

}
