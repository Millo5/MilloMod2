package millo.millomod2.client.features.impl.Editor.logic;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.Editor.Editor;
import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyEntry;
import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyFolder;
import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyMethod;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.client.hypercube.data.Plot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

// Like a Project
public class EditorPlot {

    private final HashMap<String, TemplateModel> templateCache = new HashMap<>();
    private final ArrayList<String> templateNames = new ArrayList<>();

    private final HierarchyFolder rootFolder;
    private Metadata metadata;
    private final int plotId;

    private final EditorFileManager fileManager = new EditorFileManager(this);

    public EditorPlot(Plot plot) {
        this(new Metadata(plot.getId(), plot.getName(), plot.getOwner()));
    }
    public EditorPlot(int plotId) {
        this(new Metadata(plotId, "Unnamed Plot", "Unknown Owner"));
    }
    public EditorPlot(Metadata plotMeta) {
        this.plotId = plotMeta.id();
        this.metadata = plotMeta;
        this.rootFolder = new HierarchyFolder(String.valueOf(plotId));

        load();
    }


    private void load() {
        templateNames.clear();
        templateNames.addAll(fileManager.getAllTemplateNames());

        rootFolder.clear();
        for (String templateName : templateNames) {
            addTemplate(templateName);
        }

//        for (Template template : templates) {
//            addTemplate(template);
//        }

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


    public void addTemplate(TemplateModel template) {
        fileManager.saveTemplate(template);
        templateCache.put(template.getFileName(), template);
        addTemplate(template.getFileName());
    }

    private void addTemplate(String templateName) {
        if (rootFolder.contains(templateName)) return;

        String regex = FeatureHandler.get(Editor.class).getFolderRegex();
        String[] parts = templateName.split(regex);
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
        currentFolder.addEntry(new HierarchyMethod(templateName));
    }

    public HierarchyFolder getRootFolder() {
        return rootFolder;
    }

    public TemplateModel getTemplate(String templateName) {
        if (templateCache.containsKey(templateName)) return templateCache.get(templateName);
        TemplateModel template = fileManager.readTemplate(templateName);
        if (template != null) templateCache.put(templateName, template);
        return template;
    }

    public Optional<TemplateModel> getCachedTemplate(String name) {
        if (!templateCache.containsKey(name)) return Optional.empty();
        return Optional.of(templateCache.get(name));
    }

    public ArrayList<String> getTemplateNames() {
        return templateNames;
    }

    public record Metadata(int id, String name, String owner) {}

}
