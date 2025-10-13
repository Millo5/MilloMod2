package millo.millomod2.client.hypercube.actiondump.readable;

import millo.millomod2.client.hypercube.actiondump.Action;
import millo.millomod2.client.hypercube.actiondump.Item;
import millo.millomod2.client.hypercube.actiondump.RawCodeBlock;

import java.util.HashMap;
import java.util.Map;

public class CodeBlock {

    private final String name;
    private final String identifier;
    private final Item item;
    private final Map<String, Action> actions;

    public CodeBlock(RawCodeBlock rawCodeBlock) {
        this.actions = new HashMap<>();

        this.name = rawCodeBlock.name;
        this.identifier = rawCodeBlock.identifier;
        this.item = rawCodeBlock.item;
    }

    public void addAction(Action action) {
        actions.put(action.name, action);
    }

    public Map<String, Action> getActions() {
        return actions;
    }

    public String getName() {
        return name;
    }
    public String getIdentifier() {
        return identifier;
    }
    public Item getItem() {
        return item;
    }

}
