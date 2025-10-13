package millo.millomod2.client.hypercube.actiondump;

import com.google.gson.Gson;
import millo.millomod2.client.util.FileUtil;

import java.util.Optional;

public class RawActionDump {

    public Sound[] sounds;
    public RawCodeBlock[] codeblocks;
    public Action[] actions;

    private static RawActionDump instance;
    public static Optional<RawActionDump> getActionDump() {
        if (instance != null) return Optional.of(instance);
        try {
            String json = FileUtil.readJson("action_dump.json");
            if (json == null) return Optional.empty();
            instance = new Gson().fromJson(json, RawActionDump.class);
            return Optional.of(instance);
        } catch (Exception e) {
            return Optional.empty();
        }
    }



}
