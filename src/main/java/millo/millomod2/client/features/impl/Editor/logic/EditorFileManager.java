package millo.millomod2.client.features.impl.Editor.logic;

import millo.millomod2.client.hypercube.model.ModelUtil;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.client.util.FileUtil;

import java.nio.file.Path;
import java.util.ArrayList;

public class EditorFileManager {
    private final EditorFileReader reader;
    private final EditorPlot plot;

    public EditorFileManager(EditorPlot plot) {
        this.plot = plot;
        this.reader = new EditorFileReader(plot, this);
    }

    public static Path getRootFolder() {
        return FileUtil.getModFolder().resolve("plots");
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
