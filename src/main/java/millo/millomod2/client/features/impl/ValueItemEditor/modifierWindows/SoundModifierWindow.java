package millo.millomod2.client.features.impl.ValueItemEditor.modifierWindows;

import millo.millomod2.client.features.impl.ValueItemEditor.ModifierWindow;
import millo.millomod2.client.hypercube.model.arguments.SoundArgumentModel;
import millo.millomod2.client.rendering.gui.NumberSliderElement;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import java.util.ArrayList;

public class SoundModifierWindow extends ModifierWindow {

    private final SoundArgumentModel sound;

    public SoundModifierWindow(SoundArgumentModel sound) {
        this.sound = sound;
    }

    @Override
    protected AbstractWidget getElement() {
        ListElement list = ListElement.create(100, 20)
                .direction(ElementDirection.COLUMN)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .gap(5);

        NumberSliderElement pitch = new NumberSliderElement(
                sound.getPitch(), 0, 2f, 0.1f, (value) -> {
                    sound.setPitch(value.floatValue());
                    preview();
        });
        pitch.setHeight(15);

        NumberSliderElement volume = new NumberSliderElement(
                sound.getVolume(), 0, 2f, 0.1f, (value) -> {
                    sound.setVolume(value.floatValue());
                    preview();
        });
        volume.setHeight(15);

        list.addChildren(pitch, volume);

        return list;
    }

    private double lastPitch = 0;
    private double lastVolume = 0;
    private void preview() {
        if (lastPitch != sound.getPitch() || lastVolume != sound.getVolume()) {
            sound.play();
            lastPitch = sound.getPitch();
            lastVolume = sound.getVolume();
        }
    }

    @Override
    protected String getTitle() {
        return "Sound";
    }

    @Override
    public void applyToItem(ItemStack stack) {
        ItemLore lore = stack.get(DataComponents.LORE);
        if (lore == null) return;

        var lines = new ArrayList<>(lore.styledLines());
        lines.set(2, Component.literal("Pitch: ").setStyle(Styles.UNSAVED.getStyle().withItalic(false))
                .append(Component.literal(String.format("%.1f", sound.getPitch())).setStyle(Styles.DEFAULT.getStyle())));
        lines.set(3, Component.literal("Volume: ").setStyle(Styles.UNSAVED.getStyle().withItalic(false))
                .append(Component.literal(String.format("%.1f", sound.getVolume())).setStyle(Styles.DEFAULT.getStyle())));

        stack.set(DataComponents.LORE, new ItemLore(lines));
    }

    @Override
    protected AbstractWidget getDefaultFocus() {
        return null;
    }
}
