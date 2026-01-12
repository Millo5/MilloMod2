package millo.millomod2.client.features.impl;

import com.google.gson.Gson;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.util.FileUtil;
import millo.millomod2.client.util.HypercubeAPI;

import java.util.HashMap;

public class TimeTracker extends Feature implements Toggleable {

    private HashMap<Integer, HashMap<HypercubeAPI.Mode, Long>> times = new HashMap<>();

    private long startTime = -1;
    private long lastSaveTime = -1;

    @Override
    public String getId() {
        return "time_tracker";
    }

    @Override
    public boolean isEnabled() {
        return false; // todo: Disabled until mod api
    }

    public TimeTracker() {
        load();
    }

    private void load() {
        String json = FileUtil.readJson("time_tracker.json");
        if (json == null) return;
        try {
            times = new Gson().fromJson(json, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTick() {
        if (!isEnabled()) return;

        long currentTime = System.currentTimeMillis();
        if (startTime == -1) {
            startTime = currentTime;
            lastSaveTime = currentTime;
        }

        long elapsedTime = currentTime - startTime;
        long timeSinceLastSave = currentTime - lastSaveTime;

    }

    private void saveTime() {
        long currentTime = System.currentTimeMillis();
        long passedTime = currentTime - lastSaveTime;
        lastSaveTime = currentTime;
    }

//    @Override // todo: doesn't exist yet
    public void onStateChange(HypercubeAPI.Mode mode, int plotId) {
        if (!isEnabled()) return;
        if (mode == HypercubeAPI.Mode.DEV) {
            startTime = -1;
            lastSaveTime = -1;
        } else {
            saveTime();
        }

    }

}
