package millo.millomod2.client.features.impl.Editor.logic;

import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyEntry;
import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyMethod;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.hypercube.template.Template;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

// Like a Project
public class EditorPlot {

    private final HashMap<String, File> methodFiles = new HashMap<>();
    private final ArrayList<HierarchyEntry> hierarchyEntries = new ArrayList<>();
    private Metadata metadata;
    private final int plotId;

    private final EditorFileManager fileManager = new EditorFileManager(this);

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


    private void load() {
        fileManager.load(hierarchyEntries);
//        if (!getPlotFolder().toFile().exists()) return;
//

//        // Get metadata
//        String metaJsonStr = FileUtil.readJson(getPlotFolder().resolve("metadata.json"));
//        if (metaJsonStr == null) return;
//
//        Metadata meta = MilloMod.GSON.fromJson(metaJsonStr, Metadata.class);
//        if (meta == null) return;
//
//        this.metadata = meta;
        // get all other files in the plot folder

//        File[] files = getPlotFolder().toFile().listFiles();
//        if (files == null) return;
//        for (File file : files) {
//            if (file.getName().equals("metadata.json")) continue;
//            methodFiles.put(deserializeMethodName(file.getName().replace(".json", "")), file);
//        }
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


    public void addTemplate(Template template) {
        fileManager.saveTemplate(template);

        if (hierarchyEntries.stream().anyMatch(entry -> entry.contains(template.getMethodName())))
            return;

        hierarchyEntries.add(new HierarchyMethod(template));
        hierarchyEntries.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
    }

    public ArrayList<HierarchyEntry> getHierarchyEntries() {
        return hierarchyEntries;
    }

    public record Metadata(String name, String owner) {}

}
