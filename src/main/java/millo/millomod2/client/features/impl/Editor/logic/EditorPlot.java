package millo.millomod2.client.features.impl.Editor.logic;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.Editor.Editor;
import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyEntry;
import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyFolder;
import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyMethod;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.hypercube.template.Template;

import java.io.File;
import java.util.HashMap;

// Like a Project
public class EditorPlot {

    private final HashMap<String, File> methodFiles = new HashMap<>();
    private final HierarchyFolder rootFolder;
    private Metadata metadata;
    private final int plotId;

    private final EditorFileManager fileManager = new EditorFileManager(this);

    public EditorPlot(Plot plot) {
        this(plot.getId(), new Metadata(plot.getName(), plot.getOwner()));
    }
    public EditorPlot(int plotId) {
        this(plotId, new Metadata("Unnamed Plot", "Unknown Owner"));
    }

    private EditorPlot(int plotId, Metadata metadata) {
        this.plotId = plotId;
        this.metadata = metadata;
        this.rootFolder = new HierarchyFolder(String.valueOf(plotId));

        load();
    }


    private void load() {
        var templates = fileManager.load();

        rootFolder.clear();
        for (Template template : templates) {
            addTemplate(template);
        }

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

    public int getPlotId() {
        return plotId;
    }

    public Metadata getMetadata() {
        return metadata;
    }


    public void addTemplate(Template template) {
        fileManager.saveTemplate(template);

        if (rootFolder.contains(template.getMethodName())) return;

        String regex = FeatureHandler.get(Editor.class).getFolderRegex();
        String[] parts = template.getMethodName().split(regex);
        HierarchyFolder currentFolder = rootFolder;
        for (int i = 0; i < parts.length - 2; i++) {
            String part = parts[i];
            HierarchyEntry nextEntry = null;
            for (HierarchyEntry entry : currentFolder.getHierarchyEntries()) {
                if (entry.getName().equals(part) && entry instanceof HierarchyFolder) {
                    nextEntry = entry;
                    break;
                }
            }
            if (nextEntry == null) {
                HierarchyFolder newFolder = new HierarchyFolder(part);
                currentFolder.addEntry(newFolder);
                currentFolder = newFolder;
            } else {
                currentFolder = (HierarchyFolder) nextEntry;
            }
        }
        currentFolder.addEntry(new HierarchyMethod(template));
    }

    public HierarchyFolder getRootFolder() {
        return rootFolder;
    }

    public record Metadata(String name, String owner) {}

}
