package millo.millomod2.client.mixin.render.accessors;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenAccessor {

    @Invoker("addWidget")
    <T extends GuiEventListener & NarratableEntry> T iAddSelectableChild(T child);

    @Invoker("addRenderableWidget")
    <T extends GuiEventListener & Renderable & NarratableEntry> T iAddDrawableChild(T drawableElement);

    @Invoker("removeWidget")
    void iRemove(GuiEventListener child);

    @Accessor("width")
    int getWidth();

}
