package millo.millomod2.client.hypercube.template;

import millo.millomod2.client.util.style.Styles;

public enum MethodType {

    EVENT(Styles.PLAYER_EVENT, "e "),
    ENTITY_EVENT(Styles.ENTITY_EVENT, "e "),
    FUNC(Styles.FUNCTION),
    PROCESS(Styles.PROCESS, "p "),
    GAME_EVENT(Styles.GAME_EVENT, "e ")

    ;

    private final Styles style;
    private final String prefix;
    MethodType(Styles style) {
        this.style = style;
        this.prefix = "";
    }
    MethodType(Styles style, String prefix) {
        this.style = style;
        this.prefix = prefix;
    }

    public String prefixString(String methodName) {
        return prefix + methodName;
    }

    public String suffixString(String methodName) {
        return methodName + "." + this.name().toLowerCase();
    }

    public Styles getStyle() {
        return style;
    }

    public static MethodType fromSuffix(String methodName) {
        String lower = methodName.toLowerCase();
        for (MethodType type : values()) {
            if (lower.endsWith("." + type.name().toLowerCase())) {
                return type;
            }
        }
        return null;
    }

    public static String trimSuffix(String methodName) {
        MethodType type = fromSuffix(methodName);
        if (type == null) return methodName;
        return methodName.substring(0, methodName.length() - type.name().length() - 1);
    }

    public boolean matches(String name) {
        return name.toLowerCase().endsWith("." + this.name().toLowerCase());
    }
}
