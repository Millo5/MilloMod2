package millo.millomod2.client.config;

import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfigValue<T> {
    protected final T defaultValue;
    protected T value;

    private final List<ConfigValueChangeListener<T>> listeners = new ArrayList<>();

    public ConfigValue(T defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public void setValue(T value) {
        T oldValue = this.value;
        this.value = value;

        if (!oldValue.equals(value)) {
            for (ConfigValueChangeListener<T> listener : listeners) {
                listener.onChange(oldValue, value);
            }
        }
    }

    public boolean isHidden() {
        return false;
    }

    public T getValue() {
        return value;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public abstract ClickableWidget createWidget();

    public Object serialize() {
        return value;
    }

    public abstract void deserialize(Object obj);

    public void addListener(ConfigValueChangeListener<T> listener) {
        listeners.add(listener);
    }

    @FunctionalInterface
    public interface ConfigValueChangeListener<T> {
        void onChange(T from, T to);
    }

}
