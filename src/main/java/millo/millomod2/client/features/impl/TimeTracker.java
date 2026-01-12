package millo.millomod2.client.features.impl;

import com.google.gson.Gson;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.util.FileUtil;
import millo.millomod2.client.util.HypercubeAPI;

import java.util.HashMap;

public class TimeTracker extends Feature implements Toggleable {

    private Data times = new Data();

    private Plot currentPlot = null;
    private HypercubeAPI.Mode currentMode = HypercubeAPI.Mode.IDLE;

    private long startTime = -1;
    private long lastSaveTime = -1;

    @Override
    public String getId() {
        return "time_tracker";
    }

//    @Override
//    public boolean isEnabled() {
//        return false; // todo: Disabled until mod api (haha jk)
//    }

    public TimeTracker() {
        load();

        startTime = System.currentTimeMillis();
    }

    private void load() {
        String json = FileUtil.readJson("time_tracker.json");
        if (json == null) return;
        try {
            Gson gson = new Gson();
            Data data = gson.fromJson(json, Data.class);
            if (data != null) {
                this.times = data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this.times);
            FileUtil.writeJson("time_tracker.json", gson.fromJson(json, com.google.gson.JsonObject.class));
            lastSaveTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTick() {
        if (!isEnabled()) return;

        if (lastSaveTime == -1 || System.currentTimeMillis() - lastSaveTime > 60000) { // Save every minute
            save();
        }
    }

    @Override
    public void onEnterSpawn() {
        if (!isEnabled()) return;
        calculateTime();
        currentPlot = null;
    }

    @Override
    public void onEnterPlot(Plot plot) {
        if (!isEnabled()) return;
        calculateTime();
        currentPlot = plot;
    }

    @Override
    public void onModeChange(HypercubeAPI.Mode oldMode, HypercubeAPI.Mode newMode) {
        if (!isEnabled()) return;
        calculateTime();
        currentMode = newMode;
    }


    private void calculateTime() {
        int plotId = (currentPlot != null) ? currentPlot.getId() : -1;
        calculateTime(plotId, currentMode);
    }

    private void calculateTime(int plotId, HypercubeAPI.Mode mode) {
        long currentTime = System.currentTimeMillis();
        if (startTime != -1) {
            long elapsed = currentTime - startTime;
            PlotData plotData = times.plots.getOrDefault(plotId, new PlotData());
            long previousTime = plotData.times.getOrDefault(mode, 0L);
            plotData.times.put(mode, previousTime + elapsed);
            times.plots.put(plotId, plotData);
        }
        startTime = currentTime;
    }


    private record Data(HashMap<Integer, PlotData> plots) {
        public Data() {
            this(new HashMap<>());
        }
    }
    private record PlotData(HashMap<HypercubeAPI.Mode, Long> times) {
        public PlotData() {
            this(new HashMap<>());
        }
    }

}
