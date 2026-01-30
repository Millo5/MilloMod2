package millo.millomod2.client.features.impl.QuickValueItem;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.features.addons.OnSendPacket;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.mixin.render.accessors.HandledScreenAccessor;
import millo.millomod2.client.mixin.render.accessors.ScreenAccessor;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.client.util.RenderInfo;
import millo.millomod2.menu.elements.TextFieldElement;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.ArrayList;

public class QuickValueItem extends Feature implements Toggleable, ContainerMod {

    @Override
    public String getId() {
        return "quick_value_item";
    }

    private int slot = -1;
    private boolean selectorShown = false;
    private boolean textInputShown = false;
    private String value = "meow";

    private static TextFieldWidget argumentTextField;

    private float animationProgress = 0f;

    private ValueItemOption selectedOption = null;
    private final ArrayList<ValueItemOption> options = new ArrayList<>();
    public QuickValueItem() {
        super();
        options.add(new ValueItemOption.NumberOption());
        options.add(new ValueItemOption.StringOption());
        options.add(new ValueItemOption.VarOption());
    }


    @OnSendPacket
    public boolean onSlotClick(ClickSlotC2SPacket packet) {
        if (!isEnabled()) return false;
        if (HypercubeAPI.getMode() != HypercubeAPI.Mode.DEV) return false;
        if (selectorShown || textInputShown) return true;


        if (!packet.modifiedStacks().isEmpty()) return false;
        if (packet.button() != 1 || packet.actionType() != SlotActionType.QUICK_MOVE) return false;

        openSelector(packet.slot());

        return true;
    }

    private void openSelector(short slot) {
        this.slot = slot;
        this.selectorShown = true;
        this.textInputShown = false;
    }

    private void closeSelector() {
        this.slot = -1;
        this.selectorShown = false;
        this.textInputShown = false;
    }

    private void openTextInput(ValueItemOption option) {
        this.selectorShown = false;
        this.textInputShown = true;
        this.selectedOption = option;

        ScreenHandler handler = ((HandledScreen<?>) MilloMod.MC.currentScreen).getScreenHandler();

        argumentTextField = new TextFieldElement(MilloMod.MC.textRenderer,
                handler.slots.get(this.slot).x + 24,
                handler.slots.get(this.slot).y,
                200,
                16,
                Text.literal(""));
        argumentTextField.setPlaceholder(Text.literal("Enter Value.."));
        argumentTextField.setChangedListener((s) -> this.value = s);
        argumentTextField.setMaxLength(10000);

        ((ScreenAccessor) MilloMod.MC.currentScreen).iAddSelectableChild(argumentTextField);

        MilloMod.MC.currentScreen.setFocused(null);
        argumentTextField.setFocused(false);
        MilloMod.MC.currentScreen.setFocused(argumentTextField);
        argumentTextField.setFocused(true);
    }

    private void closeTextInput() {
        closeTextInput(true);
    }
    private void closeTextInput(boolean success) {
        textInputShown = false;

        if (MilloMod.MC.currentScreen != null) {
            ((ScreenAccessor) MilloMod.MC.currentScreen).iRemove(argumentTextField);
        }
        argumentTextField = null;

        if (!success || selectedOption == null) {
            selectedOption = null;
            value = "meow";
            return;
        }

        if (net() == null || player() == null || MilloMod.MC.interactionManager == null) return;

        ItemStack oldOffhandItem = player().getInventory().getStack(45);
        PlayerUtil.sendOffhandItem(selectedOption.getItem(value));

        MilloMod.MC.interactionManager.clickSlot(player().currentScreenHandler.syncId,
                getSlot(),
                40,
                SlotActionType.SWAP,
                player()
        );

        PlayerUtil.sendOffhandItem(oldOffhandItem);

        selectedOption = null;
        argumentTextField = null;
        value = "meow";
    }


    @Override
    public void onTick() {
        if (!isEnabled()) return;
        if (MilloMod.player() == null) return;

        if (selectorShown) {
            if (MilloMod.MC.currentScreen instanceof HandledScreen<?> screen) {
                selectOption(screen);
            } else {
                closeSelector();
            }
        }
    }

    private void selectOption(HandledScreen<?> screen) {
        double mouseX = MilloMod.MC.mouse.getX();
        double mouseY = MilloMod.MC.mouse.getY();

        HandledScreenAccessor screenAcc = (HandledScreenAccessor) screen;

        Slot slot = screen.getScreenHandler().slots.get(this.slot);
        int x = slot.x + screenAcc.getX();
        int y = slot.y + screenAcc.getY();

        x = (x + 8) * MilloMod.MC.getWindow().getScaleFactor();
        y = (y + 8) * MilloMod.MC.getWindow().getScaleFactor();

        int dx = (int) (mouseX - x);
        int dy = (int) (mouseY - y);
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance < 15) {
            options.forEach(i -> i.setSelected(false));
            return;
        }

        double angle = Math.atan2(dy, dx) + Math.PI / 2;
        if (angle < 0) angle += 2 * Math.PI;

        double segment = (Math.PI * 2) / options.size();
        angle += segment / 2;
        if (angle >= Math.PI * 2) angle -= Math.PI * 2;

        int index = (int) (angle / segment);
        index = Math.max(0, Math.min(index, options.size() - 1));
        for (int i = 0; i < options.size(); i++) {
            options.get(i).setSelected(i == index);
            }

//        double angleSize = Math.toRadians(360d / options.size());
//        var offset = Math.toRadians(90);
//        for (int i = 0; i < options.size(); i++) {
//            double optionAngle = i * angleSize - offset;
//            if (optionAngle < 0) optionAngle += 2 * Math.PI;
//
//            options.get(i).setSelected(optionAngle > angle - angleSize / 2d && optionAngle < angle + angleSize / 2d);
//        }
    }

    private int slotX, slotY;

    @Override
    public <T extends ScreenHandler> void containerRender(T handler, RenderInfo info) {
        if (!isEnabled()) return;

        if (textInputShown && argumentTextField != null) {
            argumentTextField.render(info.context(), info.mouseX(), info.mouseY(), info.deltaTime());
        }

        animationProgress = info.lerp(animationProgress, selectorShown ? 1f : 0f, 15f);

        if (slot != -1 && slot <= handler.slots.size()) {
            Slot slot = handler.getSlot(this.slot);
            slotX = slot.x;
            slotY = slot.y;
        }

        DrawContext context = info.context();

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(slotX, slotY);
        context.fill(0, 0, 16, 16, new Color(255, 175, 175, (int)(animationProgress * 255)).hashCode());

        context.getMatrices().translate(8, 8);

        if (textInputShown) {
            selectedOption.setSelected(false);
            selectedOption.draw(context, 0, 0, info.deltaTime());
        }

        for (int i = 0; i < options.size(); i++) {
            double angle = Math.toRadians(i * (360d / options.size()) - 90) - (1f - animationProgress) * 0.8d;
            int xx = (int) (Math.cos(angle) * 20 * animationProgress);
            int yy = (int) (Math.sin(angle) * 20 * animationProgress);

            options.get(i).draw(context, xx, yy, info.deltaTime() * 10f, animationProgress);
        }
        context.getMatrices().popMatrix();

    }


    @Override
    public void containerMouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        if (!isEnabled()) return;
        if (isSelectorShown()) {
            for (ValueItemOption option : options) {
                if (!option.isSelected()) continue;

                openTextInput(option);
                cir.setReturnValue(true);
                return;
            }
            closeSelector();
            cir.setReturnValue(true);
            return;
        }
        if (isTextInputShown()) {
            cir.setReturnValue(true);
            closeTextInput();
        }

    }

    @Override
    public <T extends ScreenHandler> void containerKeyPressed(T handler, KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        if (!isEnabled()) return;
        if (isTextInputShown() && argumentTextField != null) {
            argumentTextField.keyPressed(input);
            cir.setReturnValue(true);
            if (input.key() == 256) { // ESC
                closeTextInput(false);
                cir.setReturnValue(true);
                return;
            }
            if (input.key() == 257) { // ENTER
                closeTextInput();
            }
        }
    }

    @Override
    public void containerClose(CallbackInfo ci) {
        if (!isEnabled()) return;
        if (isSelectorShown() || isTextInputShown()) {
            closeSelector();
            closeTextInput(false);
            animationProgress = 0f;
        }
    }

    public int getSlot() {
        return slot;
    }

    public boolean isSelectorShown() {
        return selectorShown;
    }

    public boolean isTextInputShown() {
        return textInputShown;
    }

}
