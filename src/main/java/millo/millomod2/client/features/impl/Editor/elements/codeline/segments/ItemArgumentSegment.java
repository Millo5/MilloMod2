package millo.millomod2.client.features.impl.Editor.elements.codeline.segments;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.hypercube.model.arguments.ItemArgumentModel;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.menu.elements.ItemStackElement;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemArgumentSegment extends CodeLineSegment<ItemArgumentModel> {

    public ItemArgumentSegment(ItemArgumentModel model) {
        super(model);
    }

    @Override
    public Class<ItemArgumentModel> getModelClass() {
        return ItemArgumentModel.class;
    }

    @Override
    public void buildVisual(CodeLineElement lineElement) {
        ItemStack item = ItemUtil.fromNbt(model.getValue());

        lineElement.addChild(new ItemStackElement(
                -2, -4, 12, 8,
                Component.empty(), item, true, true
        ));

    }
}
