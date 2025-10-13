package millo.millomod2.client.features.impl.Editor.template;

import millo.millomod2.client.features.impl.Editor.template.arguments.*;
import millo.millomod2.client.hypercube.template.ArgumentItem;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.hypercube.template.ArgumentItemSlot;
import net.minecraft.text.Text;

public abstract class Argument<T extends Argument<?>> {

    private int slot;
    public int getSlot() {
        return slot;
    }
    public void setSlot(int slot) {
        this.slot = slot;
    }

    protected abstract Text getDisplayText();

    public abstract T from(ArgumentItemData data);

    static Argument<?> from(ArgumentItemSlot itemSlot) {
        ArgumentItem item = itemSlot.item;
        Argument<?> arg = switch (item.id) {
            case "var" -> new VariableArgument().from(item.data);
            case "num" -> new SimpleArgument(SimpleArgument.Type.NUMBER).from(item.data);
            case "txt" -> new SimpleArgument(SimpleArgument.Type.TEXT).from(item.data);
            case "vec" -> new VectorArgument().from(item.data);
            case "loc" -> new LocationArgument().from(item.data);
            case "bl_tag" -> new BlockTagArgument().from(item.data);
            case "g_val" -> new GameValueArgument().from(item.data);
            case "item" -> new ItemArgument().from(item.data);
            case "pn_el" -> new ParameterArgument().from(item.data);
            case "part" -> new ParticleArgument().from(item.data);
            case "comp" -> new ComponentArgument().from(item.data);
            case "pot" -> new PotionArgument().from(item.data);
            case "snd" -> new SoundArgument().from(item.data);
            default -> new UnknownArgument(item.id).from(item.data);
        };
        arg.setSlot(itemSlot.slot);
        return arg;
    }
}
