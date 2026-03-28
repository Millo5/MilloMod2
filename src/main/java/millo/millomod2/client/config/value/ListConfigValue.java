package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.client.config.Instantiable;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListConfigValue<T extends ConfigValue<?> & Instantiable<T>> extends ConfigValue<List<T>> implements Instantiable<ListConfigValue<T>> {

    private final T prototype;

    public ListConfigValue(T prototype, List<T> defaultValue) {
        super(List.copyOf(defaultValue));
        this.prototype = prototype;
        if (prototype == null) {
            MilloLog.error("ListConfigValue created with null prototype!");
            throw new IllegalArgumentException("Prototype cannot be null");
        }
    }

    public ListConfigValue(T defaultValue) {
        this(defaultValue, List.of());
    }

    public ListConfigValue(List<T> defaultValue) {
        this(defaultValue.getFirst(), defaultValue);
    }

    @Override
    public ClickableWidget createWidget() {
//        ListWidget a = ListWidget.create()
//                .withDimensions(400, 100, true)
//                .withPadding(10, 10, 5);
//        a.withAlignment(WidgetPos.Alignment.CENTER);
//
//        for (T item : value) {
//            a.addWidget(item.createWidget());
//        }
//
//        if (value.isEmpty()) {
//            a.addWidget(TextWidget.create().withText(Text.literal("Empty List")));
//        }
//        return a;

        ListElement container = ListElement.create(400, 100)
                .crossAlign(CrossAxisAlignment.CENTER)
                .padding(10)
                .gap(5);

        ArrayList<ClickableWidget> itemWidgets = new ArrayList<>();
        for (T item : value) if (!item.isHidden()) itemWidgets.add(item.createWidget());
        container.addChildren(itemWidgets);
        if (value.isEmpty()) {
            container.addChild(TextElement.create("Empty List"));
        }
        return container;

    }

    @Override
    public void deserialize(Object obj) {
        if (prototype == null) return;
        if (obj instanceof List<?> list) {
            setValue(Collections.emptyList());
            for (Object item : list) {
                if (item != null) {
                    try {
                        T newValue = prototype.createNewInstance();
                        newValue.deserialize(item);
                        addValue(newValue);
                    } catch (Exception e) {
                        MilloLog.error("Failed to deserialize list item: " + e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public Object serialize() {
        return value.stream().map(ConfigValue::serialize).toList();
    }

    public void addValue(T value) {
        List<T> newList = new ArrayList<>(this.value);
        newList.add(value);
        setValue(newList);
    }

    @Override
    public ListConfigValue<T> createNewInstance() {
        return new ListConfigValue<>(prototype.createNewInstance());
    }

//    public ListConfigValue(List<T> defaultValue) {
//        super(new ArrayList<>(defaultValue));
//        this.prototype = defaultValue.get(0);
//    }
//
//    @Override
//    public void setValue(List<T> value) {
//        super.setValue(List.copyOf(value));
//    }
//
//    public void addValue(T value) {
//        List<T> newList = new ArrayList<>(this.value);
//        newList.add(value);
//        setValue(newList);
//    }
//
//    @Override
//    public Widget<?> createWidget() {
//        return new TextDisplayWidget(Text.literal(value.toString()), 10, Widget.Alignment.LEFT);
//    }
//
//    @Override
//    public void deserialize(Object obj) {
//        if (defaultValue.isEmpty()) return;
//        if (obj instanceof List<?> list) {
//            setValue(Collections.emptyList());
//            for (Object item : list) {
//                if (item != null) {
//                    try {
//                        T newValue = (T) defaultValue.get(0).getClass().getDeclaredConstructor().newInstance();
//                        newValue.deserialize(item);
//                        addValue(newValue);
//                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
//                             NoSuchMethodException e) {
//                        MilloLog.error("Failed to deserialize list item: " + e.getMessage());
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public Object serialize() {
//        return value.stream().map(ConfigValue::serialize).toList();
//    }
}
