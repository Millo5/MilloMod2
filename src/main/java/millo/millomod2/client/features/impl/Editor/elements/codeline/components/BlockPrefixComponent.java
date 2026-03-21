package millo.millomod2.client.features.impl.Editor.elements.codeline.components;

import millo.millomod2.client.features.impl.Editor.elements.codeline.SegmentComponent;
import net.minecraft.util.Identifier;

public class BlockPrefixComponent extends SegmentComponent {

    private final Identifier id;

    public BlockPrefixComponent(Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }
}
