package millo.millomod2.client.features.impl.Editor.elements.codeline;

import millo.millomod2.client.features.impl.Editor.elements.codeline.segments.BlockCodeBlockSegment;
import millo.millomod2.client.features.impl.Editor.elements.codeline.segments.BracketCodeBlockSegment;
import millo.millomod2.client.features.impl.Editor.elements.codeline.segments.ErrorSegment;
import millo.millomod2.client.features.impl.Editor.elements.codeline.segments.ItemArgumentSegment;
import millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple.*;
import millo.millomod2.client.hypercube.model.arguments.*;
import millo.millomod2.client.hypercube.model.codeblocks.BlockCodeBlockModel;
import millo.millomod2.client.hypercube.model.codeblocks.BracketCodeBlockModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class CodeLineSegment<T> {

    private final Map<Class<? extends SegmentComponent>, SegmentComponent> components = new HashMap<>();
    protected final T model;

    public CodeLineSegment(T model) {
        this.model = model;
    }

    protected void addComponent(SegmentComponent component) {
        components.put(component.getClass(), component);
    }

    public boolean hasComponent(Class<? extends SegmentComponent> componentClass) {
        return components.containsKey(componentClass);
    }

    public <K> K getComponent(Class<K> componentClass) {
        return componentClass.cast(components.get(componentClass));
    }

    /**
     * Returns the class of the model this segment represents. Used for segment creation and type safety.
     * Can return null if the segment doesn't directly represent a model
     * @return
     */
    public abstract @Nullable Class<T> getModelClass();

    public abstract void buildVisual(CodeLineElement lineElement);


    ///


    protected static TextElement text(Text text) {
        return TextElement.create(text);
    }

    protected static TextElement text(MutableText text, Styles style) {
        return text(text.setStyle(style.getStyle()));
    }

    protected static TextElement text(String text) {
        return text(Text.literal(text));
    }

    protected static TextElement text(String text, Styles style) {
        return text(Text.literal(text), style);
    }

    ///

    private record SegmentEntry<K>(Class<K> modelClass, Function<K, CodeLineSegment<K>> factory) {}
    private static final Map<Class<?>, SegmentEntry<?>> segmentRegistry = new HashMap<>();
    static {
        register(BlockCodeBlockModel.class, BlockCodeBlockSegment::new);
        register(BracketCodeBlockModel.class, BracketCodeBlockSegment::new);

        // text, variable
        register(NumberArgumentModel.class, NumberArgumentSegment::new);
        register(SoundArgumentModel.class, SoundArgumentSegment::new);
        register(TextArgumentModel.class, TextArgumentSegment::new);
        register(VariableArgumentModel.class, VariableArgumentSegment::new);
        register(ItemArgumentModel.class, ItemArgumentSegment::new);
        register(BlockTagArgumentModel.class, BlockTagArgumentSegment::new);
        register(ParticleArgumentModel.class, ParticleArgumentSegment::new);
        register(GameValueArgumentModel.class, GameValueArgumentSegment::new);
        register(VectorArgumentModel.class, VectorArgumentSegment::new);
        register(HintArgumentModel.class, HintArgumentSegment::new);
        register(ParameterArgumentModel.class, ParameterArgumentSegment::new);
        register(PotionArgumentModel.class, PotionArgumentSegment::new);
        register(LocationArgumentModel.class, LocationArgumentSegment::new);
        register(ComponentArgumentModel.class, ComponentArgumentSegment::new);
    }
    private static <K> void register(Class<K> clazz, Function<K, CodeLineSegment<K>> supplier) {
        segmentRegistry.put(clazz, new SegmentEntry<>(clazz, supplier));
    }

    @SuppressWarnings("unchecked")
    public static <K> CodeLineSegment<?> create(K model) {
        SegmentEntry<K> entry = (SegmentEntry<K>) segmentRegistry.get(model.getClass());
        if (entry == null) return new ErrorSegment("Missing " + model.getClass().getSimpleName());
        try {
            return entry.factory.apply(model);
        } catch (Exception e) {
            return new ErrorSegment("Error creating segment for " + model.getClass().getSimpleName(), e);
        }
    }

}
