package millo.millomod2.client.util.style;

import millo.millomod2.client.MilloMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import java.awt.*;

@SuppressWarnings("unused")
public enum Styles {
    // Common
    DEFAULT (Color.white),
    SCARY(Color.red),
    COMMENT (Style.EMPTY.withItalic(true).withColor(Color.gray.hashCode())),
    LINE_NUM(Color.darkGray),
    TRUE (Color.GREEN),
    FALSE (Color.RED),
    HEADER (new Color(0xacffe7)),
    TITLE (Color.YELLOW),
    NAME (new Color(0xf0acff)),
    ACTION(new Color(0xEFB598)),
    SELECT(new Color(0xDB74EC)),
    CONTROL(new Color(0x6A7696)),

    // Scopes
    UNSAVED(ChatFormatting.GRAY),
    LOCAL(ChatFormatting.GREEN),
    SAVED(ChatFormatting.YELLOW),
    LINE(new Color(0x55aaff)),

    // Item Types
    TEXT(ChatFormatting.AQUA),
    NUMBER(ChatFormatting.RED),
    VARIABLE(ChatFormatting.YELLOW),
    BLOCK_TAG(ChatFormatting.AQUA),
    GAME_VALUE(new Color(0xFFD47F)),
    ITEM(ChatFormatting.GOLD),
    PARAMETER(new Color(0xFFD47F)),


    // Parameter Colors
    ANY(new Color(0xFFD47F)),
    TXT(ChatFormatting.AQUA),
    NUM(NUMBER.style),
    VAR(VARIABLE.style),
    LIST(ChatFormatting.DARK_GREEN),
    DICT(new Color(0x55AAFF)),
    VECTOR(new Color(0x2AFFAA)),
    SOUND(ChatFormatting.BLUE),
    PARTICLE(new Color(0xAA55FF)),
    POTION(new Color(0xFF557F)),
    LOCATION(ChatFormatting.GREEN),
    LOC(LOCATION.style),
    VEC(VECTOR.style),
    COMPONENT(new Color(0x7FD42A)),
    BUCKET(new Color(0xD4AAFF)),


    CHANGED(new Color(0x7F7FFF)),
    ADDED(new Color(0x7FFF7F)),
    REMOVED(new Color(0xFF7F7F)),
    BUG(new Color(0xFF7FFF)),

    PLAYER_EVENT(new Color(0xFF52EACA)),
    ENTITY_EVENT(new Color(0xFFEAD536)),
    PROCESS(new Color(0xFF48EA2B)),
    FUNCTION(new Color(0xFF028AEA)),
    GAME_EVENT(new Color(0x2E2121)),

    HIGHLIGHT(new Color(0x2AFFAA))
    ;

    private final Style style;
    private final int color;
    Styles(Color color) {
        this(Style.EMPTY.withColor(color.hashCode()));
    }
    Styles(Style style) {
        this.style = style;
        TextColor c = style.getColor();
        this.color = c == null ? Color.WHITE.hashCode() : c.getValue();
    }

    Styles(ChatFormatting formatting) {
        this(Style.EMPTY.withColor(formatting));
    }

    public static MutableComponent getTrueFalse(boolean state) {
        if (state) return MilloMod.translatable("enabled").setStyle(TRUE.getStyle());
        return MilloMod.translatable("disabled").setStyle(FALSE.getStyle());
    }

    public static Style ofHex(String hex) {
        if (hex.matches("^#[0-9a-fA-F]{6}$")) {
            var col = Color.decode(hex);
            return Style.EMPTY.withColor(col.hashCode());
        }
        return Styles.UNSAVED.getStyle();
    }

    public Style getStyle() {
        return style;
    }

    public int getColor() {
        return color;
    }
}
