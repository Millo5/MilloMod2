package millo.millomod2.client.hypercube.template;

public enum MethodType {

    EVENT("e "),
    ENTITY_EVENT("e "),
    FUNC,
    PROCESS("p ")

    ;
    private final String prefix;
    MethodType() {
        this.prefix = "";
    }
    MethodType(String prefix) {
        this.prefix = prefix;
    }

    public String prefixString(String methodName) {
        return prefix + methodName;
    }

    public String suffixString(String methodName) {
        return methodName + "." + this.name().toLowerCase();
    }
}
