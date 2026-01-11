package millo.millomod2.client.features.impl.CommandWheel;

import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.client.config.Instantiable;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.gui.widget.ClickableWidget;

public final class CommandWheelEntryConfigValue extends ConfigValue<CommandWheelEntry> implements Instantiable<CommandWheelEntryConfigValue> {

    public CommandWheelEntryConfigValue(CommandWheelEntry defaultValue) {
        super(defaultValue);
    }

    public CommandWheelEntryConfigValue(String name, String command) {
        this(new CommandWheelEntry(name, command));
    }

    @Override
    public void setValue(CommandWheelEntry value) {
        super.setValue(value);
    }

    @Override
    public ClickableWidget createWidget() {
        return TextElement.create(getValue().name + "; " + getValue().command);
    }

//    @Override
//    public Widget<?> createWidget() {
//        return TextWidget.create().withText(Text.literal(getValue().name + "; " + getValue().command));
//    }

    @Override
    public void deserialize(Object obj) {
        if (obj instanceof String s) {
            s = s.replace("\\:", "\u0000");

            int splitIndex = s.indexOf(':');
            if (splitIndex == -1) return;

            String name = s.substring(0, splitIndex).replace("\u0000", ":").trim();
            String command = s.substring(splitIndex + 1).replace("\u0000", ":").trim();

            setValue(new CommandWheelEntry(name, command));
        }
    }

    @Override
    public Object serialize() {
        CommandWheelEntry value = getValue();
        String safeName = value.name.replace(":", "\\:");
        String safeCommand = value.command.replace(":", "\\:");
        return safeName + ":" + safeCommand;
    }

    @Override
    public CommandWheelEntryConfigValue createNewInstance() {
        return new CommandWheelEntryConfigValue(new CommandWheelEntry("" , ""));
    }
}