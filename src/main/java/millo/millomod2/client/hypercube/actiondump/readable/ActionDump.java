package millo.millomod2.client.hypercube.actiondump.readable;

import millo.millomod2.client.hypercube.actiondump.Action;
import millo.millomod2.client.hypercube.actiondump.RawActionDump;
import millo.millomod2.client.hypercube.actiondump.Sound;
import millo.millomod2.client.util.MilloLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ActionDump {

    private static ActionDump instance;
    public static Optional<ActionDump> getActionDump() {
        if (instance != null) return Optional.of(instance);

        Optional<RawActionDump> raw = RawActionDump.getActionDump();
        if (raw.isEmpty()) {
            MilloLog.error("ActionDump not found!");
            return Optional.empty();
        }

        instance = new ActionDump(raw.get());
        return Optional.of(instance);
    }

    // ---

    private final Map<String, Sound> soundMap;
    private final Map<String, String> codeBlockIdNameMap;
    private final Map<String, CodeBlock> codeBlockMap;
    public ActionDump(RawActionDump raw) {
        this.codeBlockMap = new HashMap<>(raw.codeblocks.length);
        this.codeBlockIdNameMap = new HashMap<>(raw.codeblocks.length);
        this.soundMap = new HashMap<>(raw.sounds.length);

        for (int i = 0; i < raw.codeblocks.length; i++) {
            CodeBlock codeBlock = new CodeBlock(raw.codeblocks[i]);
            this.codeBlockMap.put(codeBlock.getName(), codeBlock);
            this.codeBlockIdNameMap.put(codeBlock.getIdentifier(), codeBlock.getName());
        }
        for (Action action : raw.actions) {
            CodeBlock codeBlock = this.codeBlockMap.get(action.codeblockName);
            if (codeBlock != null) {
                codeBlock.addAction(action);
            }
        }

        for (Sound sound : raw.sounds) {
            soundMap.put(sound.icon.name, sound);
        }
    }

    public ArrayList<String> getCodeBlockIds() {
        return new ArrayList<>(codeBlockIdNameMap.keySet());
    }

    public Optional<CodeBlock> getCodeBlock(String id) {
        String name = codeBlockIdNameMap.get(id);
        if (name == null) return Optional.empty();
        return Optional.ofNullable(codeBlockMap.get(name));
    }

    public Sound getSoundFromName(String name) {
        return soundMap.get(name);
    }


}
