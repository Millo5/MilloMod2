package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class SimpleArgumentBuilder {

    private final MutableComponent name;
    private Styles style;
    private MutableComponent tooltip;
    private Supplier<@Nullable Component> tooltipSupplier;
    private Supplier<Boolean> onClick;

    public SimpleArgumentBuilder(MutableComponent name) {
        this.name = name;
        this.tooltip = null;
        this.onClick = null;
    }

    public SimpleArgumentBuilder(String name) {
        this(Component.literal(name));
    }

    public SimpleArgumentBuilder tooltip(MutableComponent tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public SimpleArgumentBuilder tooltip(Supplier<@Nullable Component> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
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
        if (tooltipSupplier != null) element.tooltip(tooltipSupplier);
        else if (tooltip != null) element.setTooltip(Tooltip.create(tooltip));
        if (onClick != null) element.onClickListener(onClick);
        return element;
    }

}
