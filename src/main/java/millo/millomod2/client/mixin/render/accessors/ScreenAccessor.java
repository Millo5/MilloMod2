package millo.millomod2.client.mixin.render.accessors;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenAccessor {

    @Invoker("addSelectableChild")
    <T extends Element & Selectable> T iAddSelectableChild(T child);

    @Invoker("remove")
    void iRemove(Element child);

}
