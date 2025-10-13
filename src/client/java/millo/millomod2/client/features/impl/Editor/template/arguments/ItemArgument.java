package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ItemArgument extends Argument<ItemArgument> {

    private String itemNbt;
    private ItemStack item;

    @Override
    protected Text getDisplayText() {
        return Text.literal(item.getName().getString()).setStyle(Styles.ITEM.getStyle());
    }

    @Override
    public ItemArgument from(ArgumentItemData data) {
        this.itemNbt = data.item;
        item = ItemUtil.fromNbt(itemNbt);
        return this;
    }
}
