package millo.millomod2.client.features.impl.ValueItemEditor;

import com.google.gson.JsonObject;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.features.addons.UICharTyped;
import millo.millomod2.client.features.impl.ValueItemEditor.modifierWindows.SoundModifierWindow;
import millo.millomod2.client.features.impl.ValueItemEditor.modifierWindows.StringModifierWindow;
import millo.millomod2.client.features.impl.ValueItemEditor.modifierWindows.VariableModifierWindow;
import millo.millomod2.client.hypercube.model.arguments.*;
import millo.millomod2.client.mixin.render.accessors.HandledScreenAccessor;
import millo.millomod2.client.mixin.render.accessors.ScreenAccessor;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.RenderInfo;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.ListElement;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ValueItemEditor extends Feature implements Toggleable, ContainerMod, UICharTyped {

    @Override
    public String getId() {
        return "value_item_editor";
    }

    private boolean open = false;

    private int slot = -1;
    private ItemStack originalStack = ItemStack.EMPTY;
    private ArgumentModel<?> currentModel = null;
    private ListElement window;
    private ModifierWindow modifier;

    private ItemStack oldOffhandItem = null;

    @Override
    public boolean containerSlotClick(int slotId, int button, SlotActionType actionType, PlayerEntity player) {
        if (!isEnabled()) return false;
        if (HypercubeAPI.getMode() != HypercubeAPI.Mode.DEV) return false;

        if (open) return true;
        if (button != 1 || actionType != SlotActionType.QUICK_MOVE) return false;

        ScreenHandler screenHandler = player.currentScreenHandler;
        ItemStack stack = screenHandler.getSlot(slotId).getStack();
        if (stack.isEmpty()) return false;

        JsonObject json = ItemUtil.getVarItem(stack);
        if (json == null) return false;

        ArgumentModel<?> model;
        try {
            model = ArgumentModel.deserializeArgument(json);
        } catch (Exception e) {
            return false;
        }

        ModifierWindow modifier = switch (model) {
            case NumberArgumentModel num -> new StringModifierWindow(num.getValue(), num::setValue, Styles.NUM);
            case TextArgumentModel text -> new StringModifierWindow(text.getValue(), text::setValue, Styles.DEFAULT);
            case ComponentArgumentModel component -> new StringModifierWindow(component.getValue(), component::setValue, Styles.COMPONENT);
            case SoundArgumentModel sound -> new SoundModifierWindow(sound);
            case VariableArgumentModel var -> new VariableModifierWindow(var);
            default -> null;
        };

        if (modifier == null) return false;

        close(false);
        open(model, slotId, stack, modifier);

        return true;
    }

    private void open(ArgumentModel<?> model, int slot, ItemStack originalStack, ModifierWindow modifier) {
        if (!(MC.currentScreen instanceof HandledScreen<?> handledScreen)) return;

        this.currentModel = model;
        this.slot = slot;
        this.originalStack = originalStack;
        this.modifier = modifier;
        this.window = this.modifier.getWindow();
        this.open = true;

        Slot s = player().currentScreenHandler.getSlot(slot);
        window.setPosition(s.x + 24, s.y);
    }

    private void close(boolean confirm) {
        if (MC.currentScreen != null) ((ScreenAccessor) MC.currentScreen).iRemove(window);

        if (confirm) {
            JsonObject serialized = currentModel.serialize();
            ItemStack copy = originalStack.copy();
            ItemUtil.setVarItem(copy, serialized.getAsJsonObject("item"));
            modifier.applyToItem(copy);

            oldOffhandItem = player().getInventory().getStack(45);
            int syncId = player().currentScreenHandler.syncId;
            int slot = this.slot;
            MilloMod.schedule(() -> {
                if (player().currentScreenHandler == null) return;
                if (player().currentScreenHandler.syncId != syncId) return;
                ClientPlayerInteractionManager intMan = MilloMod.MC.interactionManager;
                if (intMan == null) return;

                intMan.clickSlot(syncId, slot, 0, SlotActionType.SWAP, player());
                net().sendPacket(new CreativeInventoryActionC2SPacket(36, copy));
                intMan.clickSlot(syncId, slot, 0, SlotActionType.SWAP, player());
                intMan.clickSlot(syncId, 54, 0, SlotActionType.QUICK_CRAFT, player());

            }, 1);

        }

        this.currentModel = null;
        this.slot = -1;
        this.originalStack = ItemStack.EMPTY;
        this.window = null;
        this.open = false;
        this.modifier = null;
    }

    @Override
    public <T extends ScreenHandler> void containerRender(T handler, RenderInfo info) {
        if (!open || !isEnabled()) return;

        if (!(MC.currentScreen instanceof HandledScreen<?> handledScreen)) return;
        HandledScreenAccessor accessor = (HandledScreenAccessor) handledScreen
                ;
//        window.render(info.context(), info.mouseX(), info.mouseY(), info.deltaTime() * 20f);
        window.render(info.context(), info.mouseX() - accessor.getX(), info.mouseY() - accessor.getY(), info.deltaTime() * 20f);
    }

    @Override
    public void containerMouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        if (!open || !isEnabled()) return;

        if (!(MC.currentScreen instanceof HandledScreen<?> handledScreen)) return;
        HandledScreenAccessor accessor = (HandledScreenAccessor) handledScreen;

        Click local = new Click(click.x() - accessor.getX(), click.y() - accessor.getY(), click.buttonInfo());
        if (!window.mouseClicked(local, doubled)) close(true);
    }

    @Override
    public void containerMouseReleased(Click click, CallbackInfoReturnable<Boolean> cir) {
        if (!open || !isEnabled()) return;

        if (!(MC.currentScreen instanceof HandledScreen<?> handledScreen)) return;
        HandledScreenAccessor accessor = (HandledScreenAccessor) handledScreen;

        Click local = new Click(click.x() - accessor.getX(), click.y() - accessor.getY(), click.buttonInfo());
        window.mouseReleased(local);
    }

    @Override
    public <T extends ScreenHandler> void containerKeyPressed(T handler, KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        if (!open || !isEnabled()) return;
        cir.setReturnValue(true);
        window.keyPressed(input);

        if (input.key() == 256) {
            close(false);
            return;
        }

        if (input.key() == 257 || input.key() == 335) {
            close(true);
        }
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (!open || !isEnabled()) return false;

        window.charTyped(input);
        return true;
    }

    @Override
    public void containerClose(CallbackInfo ci) {
        if (!open || !isEnabled()) return;

        close(false);
    }


    // ---


}
