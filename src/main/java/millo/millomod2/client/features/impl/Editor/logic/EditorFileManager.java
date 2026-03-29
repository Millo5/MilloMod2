package millo.millomod2.client.features.impl.Editor.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import millo.millomod2.client.hypercube.model.ModelUtil;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.client.util.FileUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class EditorFileManager {
    private final static String recentPlotsFileName = "recent_plots.json";

    private final EditorFileReader reader;
    private final EditorPlot plot;

    public EditorFileManager(EditorPlot plot) {
        this.plot = plot;
        this.reader = new EditorFileReader(plot, this);
    }

    public static Path getRootFolder() {
        return FileUtil.getModFolder().resolve("plots");
    }

    public static ArrayList<EditorPlot.Metadata> getRecentPlots() {
        Path recentFile = getRootFolder().resolve(recentPlotsFileName);
        if (!recentFile.toFile().exists()) return new ArrayList<>();

        String jsonStr = FileUtil.read(recentFile);
        if (jsonStr == null || jsonStr.isEmpty()) return new ArrayList<>();
        JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();

        ArrayList<EditorPlot.Metadata> recentPlots = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String name = entry.getKey();
            int id = entry.getValue().getAsInt();
            recentPlots.add(new EditorPlot.Metadata(id, name, "n/a"));
        }
        return recentPlots;
    }

    public static void saveRecentPlots(ArrayList<EditorPlot.Metadata> recentPlots) {
        JsonObject json = new JsonObject();
        recentPlots.forEach(metadata -> json.addProperty(metadata.name(), metadata.id()));

        Path root = getRootFolder();
        if (!root.toFile().exists()) root.toFile().mkdirs();
        Path recentFile = root.resolve(recentPlotsFileName);
        FileUtil.write(recentFile, json.toString());
    }

    public Path getPlotFolder() {
        return getRootFolder().resolve(String.valueOf(plot.getPlotId()));
    }

    public static boolean plotExists(int plotId) {
        Path plotFolder = getRootFolder().resolve(String.valueOf(plotId));
        return plotFolder.toFile().exists();
    }

    public static String serializeMethodName(String name) {
        StringBuilder sb = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == '.') {
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


    public void saveTemplate(TemplateModel template) {
        String fileName = serializeMethodName(template.getFileName());
        Path filePath = getPlotFolder().resolve(fileName);

        if (!filePath.getParent().toFile().exists()) filePath.getParent().toFile().mkdirs();

        FileUtil.write(filePath, ModelUtil.compress(template));
    }

    public TemplateModel readTemplate(String methodName) {
        return reader.readTemplate(methodName);
    }

    public ArrayList<String> getAllTemplateNames() {
        ArrayList<String> templateNames = new ArrayList<>();
        if (!getPlotFolder().toFile().exists()) return templateNames;

        FileUtil.listFiles(getPlotFolder()).forEach(path -> {
            String methodName = deserializeMethodName(path.getFileName().toString());
            templateNames.add(methodName);
        });
        return templateNames;
    }


//    public ArrayList<Template> load() {
//        ArrayList<Template> templates = new ArrayList<>();
//        if (!getPlotFolder().toFile().exists()) return templates;
//
//        FileUtil.listFiles(getPlotFolder()).forEach(path -> {
//            Template template = readTemplate(deserializeMethodName(path.getFileName().toString()));
//            if (template != null) templates.add(template);
//        });
//        return templates;
//    }
}
