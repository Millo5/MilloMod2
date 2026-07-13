package millo.millomod2.client.features.impl.Editor.logic;

import millo.millomod2.client.hypercube.model.ModelUtil;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.client.hypercube.template.MethodType;
import millo.millomod2.client.util.FileUtil;

import java.io.IOException;
import java.nio.file.Path;

public class EditorFileReader {
    private final EditorPlot plot;
    private final EditorFileManager fileManager;

    public EditorFileReader(EditorPlot plot, EditorFileManager editorFileManager) {
        this.plot = plot;
        this.fileManager = editorFileManager;
    }

    public TemplateModel readTemplate(String methodName) {
        String fileName = EditorFileManager.serializeMethodName(methodName);
        Path filePath = fileManager.getPlotFolder().resolve(fileName);

        if (!filePath.toFile().exists()) return null;

        String b64Code = FileUtil.read(filePath);
        if (b64Code == null) return null;

        return ModelUtil.parseFromGzip(b64Code);
    }

    public String readIndexData(String methodName) {
        String fileName = EditorFileManager.serializeMethodName(methodName);
        Path filePath = fileManager.getPlotFolder().resolve(fileName);

        if (!filePath.toFile().exists()) return null;

        String b64Code = FileUtil.read(filePath);
        if (b64Code == null) return null;

        try {
            String json = ModelUtil.decompress(b64Code);
            boolean isDefinition = MethodType.FUNC.matches(methodName) || MethodType.PROCESS.matches(methodName);
            boolean hasUsages = json.contains("\"call_func\"") || json.contains("\"start_process\"");
            return isDefinition || hasUsages ? json : null;
        } catch (IOException | IllegalArgumentException e) {
            return null;
        }
    }
}
