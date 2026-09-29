package millo.millomod2.client.mixin.render;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import millo.millomod2.menu.elements.ClickableElement;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractWidget.class)
public class MClickableWidget {

    @WrapWithCondition(
            method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/AbstractWidget;extractTooltipForNextRenderPass(Lnet/minecraft/client/gui/GuiGraphicsExtractor;II)V"
            )
    )
    private boolean skipTooltip(AbstractWidget instance, GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        AbstractWidget o = (AbstractWidget) (Object) this;
        if (o instanceof ClickableElement<?>) return false;
        return !(o instanceof TextElement);
    }

}
