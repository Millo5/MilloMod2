package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class SimpleArgumentBuilder {

    private final MutableText name;
    private Styles style;
    private MutableText tooltip;
    private Supplier<Boolean> onClick;

    public SimpleArgumentBuilder(MutableText name) {
        this.name = name;
        this.tooltip = null;
        this.onClick = null;
    }

    public SimpleArgumentBuilder(String name) {
        this(Text.literal(name));
    }

    public SimpleArgumentBuilder tooltip(MutableText tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public SimpleArgumentBuilder onClick(Supplier<Boolean> onClick) {
        this.onClick = onClick;
        return this;
    }

    public SimpleArgumentBuilder onClickCmd(String cmd) {
        this.onClick = () -> {
            PlayerUtil.sendCommand(cmd);
            return true;
        };
        return this;
    }

    public SimpleArgumentBuilder style(Styles style) {
        this.style = style;
        return this;
    }

    public TextElement build() {
        if (style != null) name.setStyle(style.getStyle());
        TextElement element = TextElement.create(name);
        if (tooltip != null) element.setTooltip(Tooltip.of(tooltip));
        if (onClick != null) element.onClickListener(onClick);
        return element;
    }

}
