package millo.millomod2.client.mixin.render.accessors;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {

    @Accessor("x")
    int getX();

    @Accessor("y")
    int getY();

    @Accessor("backgroundWidth")
    int getBackgroundWidth();

    @Accessor("backgroundHeight")
    int getBackgroundHeight();

    @Accessor("focusedSlot")
    Slot getFocusedSlot();

}
