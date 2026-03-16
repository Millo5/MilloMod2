package millo.millomod2.client.features.impl.Editor.logic;

import millo.millomod2.client.hypercube.template.Template;
import millo.millomod2.client.util.FileUtil;

import java.nio.file.Path;

public class EditorFileReader {
    private final EditorPlot plot;
    private final EditorFileManager fileManager;

    public EditorFileReader(EditorPlot plot, EditorFileManager editorFileManager) {
        this.plot = plot;
        this.fileManager = editorFileManager;
    }

    public Template readTemplate(String methodName) {
        String fileName = EditorFileManager.serializeMethodName(methodName);
        Path filePath = fileManager.getPlotFolder().resolve(fileName);

        if (!filePath.toFile().exists()) return null;

        String b64Code = FileUtil.read(filePath);
        if (b64Code == null) return null;

        return Template.parseBase64(b64Code);
    }
}
