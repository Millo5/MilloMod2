package millo.millomod2.client.mixin.render.accessors;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface HandledScreenAccessor {

    @Accessor("leftPos")
    int getX();

    @Accessor("topPos")
    int getY();

    @Accessor("imageWidth")
    int getBackgroundWidth();

    @Accessor("imageHeight")
    int getBackgroundHeight();

    @Accessor("hoveredSlot")
    Slot getFocusedSlot();

}
