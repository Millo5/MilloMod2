package millo.millomod2.client.config.saving;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.FeatureHandler;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ConfigSaving {

    private static ConfigSaving INSTANCE;

    public static ConfigSaving getInstance() {
        if (INSTANCE == null) INSTANCE = new ConfigSaving("millomod");
        return INSTANCE;
    }

    private final File file;
    private ConfigSaving(String fileName) {
        Path path = FabricLoader.getInstance().getConfigDir();
        file = path.resolve(fileName + ".json").toFile();
    }

    public void save() throws IOException {
        HashMap<String, SavedFeature> configMap = new HashMap<>();

        FeatureHandler.forEach(feature -> {
            if (feature.getConfig().getValues().isEmpty()) return;
            SavedFeature sf = feature.getConfig().getSave();
            configMap.put(feature.getId(), sf);
        });

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            Files.createFile(file.toPath());
        } else Files.write(file.toPath(), new byte[0]);

        String json = new Gson().toJson(configMap);
        Files.write(file.toPath(), json.getBytes());
    }

    public void load() throws IOException {
        if (!file.exists()) return;
        String json = new String(Files.readAllBytes(file.toPath()));
        Type type = new TypeToken<HashMap<String, SavedFeature>>() {}.getType();
        HashMap<String, SavedFeature> configMap = new Gson().fromJson(json, type);
        if (configMap == null) return;

        FeatureHandler.forEach(feature -> {
            if (feature.getConfig().getValues().isEmpty()) return;
            SavedFeature sf = configMap.get(feature.getId());
            if (sf != null) {
                feature.getConfig().loadFrom(sf);
            }
        });

    }

}
