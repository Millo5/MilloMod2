package millo.millomod2.client.features.impl;

import com.google.gson.Gson;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.util.FileUtil;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.MilloLog;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TimeTracker extends Feature implements Toggleable {

    private static final long SAVE_INTERVAL = TimeUnit.MINUTES.toNanos(1);

    private Data times = new Data();

    private Plot currentPlot = null;
    private HypercubeAPI.Mode currentMode = HypercubeAPI.Mode.IDLE;

    private long startTime = -1;
    private long lastSaveTime = -1;
    private boolean trackingInitialized;

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
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> flush());
    }

    private void load() {
        String json = FileUtil.readJson("time_tracker.json");
        if (json == null) return;
        try {
            Gson gson = new Gson();
            Data data = gson.fromJson(json, Data.class);
            if (data != null && data.plots() != null) {
                data.plots().entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().times() == null);
                this.times = data;
            }
        } catch (Exception e) {
            MilloLog.error("Failed to load time tracker data: " + e.getMessage());
        }
    }

    private void save() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this.times);
            writeSnapshot(json);
            lastSaveTime = System.nanoTime();
        } catch (IOException e) {
            MilloLog.error("Failed to save time tracker data: " + e.getMessage());
        }
    }

    private void flush() {
        if (trackingInitialized && isEnabled()) calculateTime();
        String json = new Gson().toJson(this.times);
        try {
            writeSnapshot(json);
        } catch (IOException e) {
            MilloLog.error("Failed to flush time tracker data: " + e.getMessage());
        }
    }

    private void writeSnapshot(String json) throws IOException {
        Path target = FileUtil.getModFolder().resolve("time_tracker.json");
        Path temporary = target.resolveSibling(target.getFileName() + ".tmp");
        Files.writeString(temporary, json);
        try {
            Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public void enabledChanged(boolean enabled) {
        trackingInitialized = true;
        if (enabled) {
            startTime = System.nanoTime();
            return;
        }

        calculateTime();
        startTime = -1;
        save();
    }

    @Override
    public void onTick() {
        if (!trackingInitialized) {
            trackingInitialized = true;
            if (isEnabled()) startTime = System.nanoTime();
        }
        if (!isEnabled()) return;

        if (lastSaveTime == -1 || System.nanoTime() - lastSaveTime > SAVE_INTERVAL) {
            calculateTime();
            save();
        }
    }

    @Override
    public void onEnterSpawn() {
        if (isEnabled()) calculateTime();
        currentPlot = null;
    }

    @Override
    public void onEnterPlot(Plot plot) {
        if (isEnabled()) calculateTime();
        currentPlot = plot;
    }

    @Override
    public void onModeChange(HypercubeAPI.Mode oldMode, HypercubeAPI.Mode newMode) {
        if (isEnabled()) calculateTime();
        currentMode = newMode;
    }


    private void calculateTime() {
        int plotId = (currentPlot != null) ? currentPlot.getId() : -1;
        calculateTime(plotId, currentMode);
    }

    private void calculateTime(int plotId, HypercubeAPI.Mode mode) {
        long currentTime = System.nanoTime();
        if (startTime != -1) {
            long elapsed = TimeUnit.NANOSECONDS.toMillis(currentTime - startTime);
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
