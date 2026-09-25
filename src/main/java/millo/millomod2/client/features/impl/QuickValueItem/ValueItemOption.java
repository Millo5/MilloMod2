package millo.millomod2.client.features.impl.QuickValueItem;

import millo.millomod2.client.util.style.Styles;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import java.awt.*;
import java.util.List;

public abstract class ValueItemOption {

    protected final String id;
    protected final ItemStack icon;

    private boolean selected = false;
    private float hover = 0f;

    public ValueItemOption(Item  icon, String id) {
        this.id = id;
        this.icon = new ItemStack(icon);
    }

    public ItemStack getItem(String value) {
        ItemStack item = new ItemStack(icon.getItem());

        CompoundTag pbv = new CompoundTag();
        pbv.putString("hypercube:varitem", getVarItemString(item, value));

        CompoundTag custom_nbt = new CompoundTag();
        custom_nbt.putInt("CustomModelData", 5000);
        custom_nbt.put("PublicBukkitValues", pbv);

        CustomData custom_data = CustomData.of(custom_nbt);

        item.set(DataComponents.CUSTOM_DATA, custom_data);

        return item;
    }

    protected abstract String getVarItemString(ItemStack item, String value);

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    private float x, y;
    public void draw(GuiGraphics context, int x, int y, float delta, float shown) {
        hover = Mth.clampedLerp(delta, hover, isSelected() ? 1f : 0f);

        this.x = x;
        this.y = y;

        context.pose().pushMatrix();
        context.pose().translate(x, y);
        context.pose().scale(shown, shown);
        context.pose().scale(hover*0.2f+1f, hover*0.2f+1f);

        int color = new Color(0f, 0f, 0f, 0.2f + hover * 0.3f).hashCode();
        int borderCol = new Color(1f-hover, 1f, 1f, 1f).hashCode();
        context.fill(-8, -8, 8, 8, color);
        context.renderOutline(-8, -8, 16, 16, borderCol);

        context.renderItem(icon, -8, -8);

        context.pose().popMatrix();
    }

    public void draw(GuiGraphics context, int x, int y, float delta) {
        this.x = Mth.clampedLerp(delta, this.x, x);
        this.y = Mth.clampedLerp(delta, this.y, y);

        context.pose().pushMatrix();
        context.pose().translate(this.x, this.y);

        context.renderItem(icon, -8, -8);

        context.pose().popMatrix();
    }


    public static class NumberOption extends ValueItemOption {
        public NumberOption() {
            super(Items.SLIME_BALL, "num");
        }

        @Override
        protected String getVarItemString(ItemStack item, String value) {
            if (value.equalsIgnoreCase("z")) value = "0"; // Quick hand for 0. as I can only reach up to 9 without moving my hand. (I know, I am lazy)
            item.set(DataComponents.CUSTOM_NAME, Component.literal(value).setStyle(Style.EMPTY.withColor(0xff5555).withItalic(false)));
            return "{\"id\":\"num\",\"data\":{\"name\":\"" + value + "\"}}";
        }
    }

    public static class StringOption extends ValueItemOption {
        public StringOption() {
            super(Items.STRING, "txt");
        }

        @Override
        protected String getVarItemString(ItemStack item, String value) {
            item.set(DataComponents.CUSTOM_NAME, Component.literal(value).setStyle(Style.EMPTY.withItalic(false)));
            return "{\"id\":\"txt\",\"data\":{\"name\":\"" + value + "\"}}";
        }
    }

    public static class VarOption extends ValueItemOption {
        public VarOption() {
            super(Items.MAGMA_CREAM, "var");
        }


        private enum Scope {
            LINE("line", "-i", Component.literal("LINE").setStyle(Styles.LINE.getStyle().withItalic(false))),
            LOCAL("local", "-l", Component.literal("LOCAL").setStyle(Styles.LOCAL.getStyle().withItalic(false))),
            SAVED("saved", "-s", Component.literal("SAVE").setStyle(Styles.SAVED.getStyle().withItalic(false))),
            UNSAVED("unsaved", "\n", Component.literal("GAME").setStyle(Styles.UNSAVED.getStyle().withItalic(false)));

            private final String scope, key;
            private final Component lore;

            Scope(String scope, String key, Component lore) {
                this.scope = scope;
                this.key = key;
                this.lore = lore;
            }
        }

        @Override
        protected String getVarItemString(ItemStack item, String value) {
            Scope scope = Scope.UNSAVED;
            for (Scope s : Scope.values()) {
                if (value.endsWith(s.key)) {
                    scope = s;
                    value = value.substring(0, value.length() - 3);
                    break;
                }
            }

            ItemLore lore = new ItemLore(List.of(scope.lore));

            item.set(DataComponents.CUSTOM_NAME, Component.literal(value).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withItalic(false)));
            item.set(DataComponents.LORE, lore);

            return "{\"id\":\"var\",\"data\":{\"name\":\"" + value + "\",\"scope\":\"" + scope.scope + "\"}}";
        }
    }

}
