package millo.millomod2.client.features.impl.Editor.logic;

import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyEntry;
import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyMethod;
import millo.millomod2.client.hypercube.template.Template;
import millo.millomod2.client.util.FileUtil;

import java.nio.file.Path;
import java.util.ArrayList;

public class EditorFileManager {

    private final EditorPlot plot;


    public EditorFileManager(EditorPlot plot) {
        this.plot = plot;
    }


    private Path getRootFolder() {
        return FileUtil.getModFolder().resolve("plots");
    }

    private Path getPlotFolder() {
        return getRootFolder().resolve(String.valueOf(plot.getPlotId()));
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


    public void saveTemplate(Template template) {
        String fileName = serializeMethodName(template.getMethodName());
        Path filePath = getPlotFolder().resolve(fileName);

        if (!filePath.getParent().toFile().exists()) filePath.getParent().toFile().mkdirs();

        FileUtil.write(filePath, template.b64Code);
    }

    public Template readTemplate(String methodName) {
        String fileName = serializeMethodName(methodName);
        Path filePath = getPlotFolder().resolve(fileName);

        if (!filePath.toFile().exists()) return null;

        String b64Code = FileUtil.read(filePath);
        if (b64Code == null) return null;

        return Template.parseBase64(b64Code);
    }

    public void load(ArrayList<HierarchyEntry> hierarchyEntries) {
        hierarchyEntries.clear();
        if (!getPlotFolder().toFile().exists()) return;

        FileUtil.listFiles(getPlotFolder()).forEach(path -> {
            String fileName = path.getFileName().toString();
            Template template = readTemplate(deserializeMethodName(fileName));
            if (template != null) {
                hierarchyEntries.add(new HierarchyMethod(template));
            }
        });
    }
}
