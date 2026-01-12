package millo.millomod2.client.features.impl.Editor.logic;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.util.FileUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

// Like a Project
public class EditorPlot {

    private final HashMap<String, File> methodFiles = new HashMap<>();
    private Metadata metadata;
    private final int plotId;

    public EditorPlot(Plot plot) {
        this.plotId = plot.getId();
        metadata = new Metadata(plot.getName(), plot.getOwner());
        load();
    }
    public EditorPlot(int plotId) {
        this.plotId = plotId;
        metadata = new Metadata("Unnamed Plot", "Unknown Owner");
        load();
    }

    private Path getRootFolder() {
        return FileUtil.getModFolder().resolve("plots");
    }

    private Path getPlotFolder() {
        return getRootFolder().resolve(String.valueOf(plotId));
    }

    private void load() {
        if (!getPlotFolder().toFile().exists()) return;

        // Get metadata
        String metaJsonStr = FileUtil.readJson(getPlotFolder().resolve("metadata.json"));
        if (metaJsonStr == null) return;

        Metadata meta = MilloMod.GSON.fromJson(metaJsonStr, Metadata.class);
        if (meta == null) return;

        this.metadata = meta;
        // get all other files in the plot folder

        File[] files = getPlotFolder().toFile().listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.getName().equals("metadata.json")) continue;
            methodFiles.put(file.getName(), file);
        }
    }

//    public Optional<Template> getMethod(String methodName) {
//        String serializedName = serializeMethodName(methodName);
//
//
//    }
    //

    public int getPlotId() {
        return plotId;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    // Utility methods

    public static String serializeMethodName(String name) {
        StringBuilder sb = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (Character.isLetterOrDigit(c) || c == '_' || c == '-') {
                sb.append(c);
            } else {
                sb.append(String.format("_%04x", (int) c));
            }
        }
        return sb.toString();
    }
    public static String deserializeMethodName(String name) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c != '_' || i + 4 >= name.length()) {
                sb.append(c);
                continue;
            }

            String hex = name.substring(i + 1, i + 5);
            try {
                int code = Integer.parseInt(hex, 16);
                sb.append((char) code);
                i += 4;
            } catch (NumberFormatException e) {
                sb.append(c);
            }
        }
        return sb.toString();
    }



    public record Metadata(String name, String owner) {}

}
