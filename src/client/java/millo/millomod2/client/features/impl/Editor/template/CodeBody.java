package millo.millomod2.client.features.impl.Editor.template;

import java.util.ArrayList;
import java.util.List;

public class CodeBody implements CodeEntry {

    private final List<CodeEntry> lines;

    public CodeBody(List<CodeEntry> lines) {
        this.lines = lines;
    }

    public CodeBody() {
        this.lines = new ArrayList<>();
    }

    public List<CodeEntry> getLines() {
        return lines;
    }

    public void add(CodeEntry codeLine) {
        lines.add(codeLine);
    }
}
