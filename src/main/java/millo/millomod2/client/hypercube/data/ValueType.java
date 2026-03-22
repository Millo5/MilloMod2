package millo.millomod2.client.hypercube.data;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public enum ValueType {
    STRING(Items.STRING),
    STYLED_TEXT(Items.BOOK),
    NUMBER(Items.SLIME_BALL),
    LOCATION(Items.PAPER),
    VECTOR(Items.PRISMARINE_SHARD),
    SOUND(Items.NAUTILUS_SHELL),
    PARTICLE(Items.WHITE_DYE),
    POTION(Items.DRAGON_BREATH),
    ITEM(Items.ITEM_FRAME),
    ANY_VALUE(Items.POTATO),
    VARIABLE(Items.MAGMA_CREAM),
    LIST(Items.SKULL_BANNER_PATTERN),
    DICTIONARY(Items.KNOWLEDGE_BOOK),

    UNKNOWN(Items.STRUCTURE_VOID);


    private final ItemStack icon;
    ValueType(Item item) {
        this.icon = new ItemStack(item);
    }

    public static ValueType fromString(String typeStr) {
        typeStr = typeStr.replaceAll("(\\s+)+", "_").toUpperCase();
        typeStr = typeStr.replaceAll("(\\(S\\)|\\*)", "");
        for (ValueType type : values()) {
            if (type.name().equalsIgnoreCase(typeStr)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public ItemStack getIcon() {
        return icon;
    }

}
