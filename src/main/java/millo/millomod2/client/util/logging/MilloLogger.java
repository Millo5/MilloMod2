package millo.millomod2.client.util.logging;

import java.util.Stack;

public class MilloLogger {

    private final Stack<String> tree = new Stack<>();

    public MilloLogger push(String branch) {
        tree.add(branch);
        return this;
    }

    public void pop() {
        if (!tree.isEmpty()) tree.pop();
    }

    public void log(String message) {
        StringBuilder prefix = new StringBuilder();
        for (String branch : tree) {
            prefix.append(branch).append(" > ");
        }
        MilloLog.log(prefix + message);
    }

    public void warn(String message) {
        StringBuilder prefix = new StringBuilder();
        for (String branch : tree) {
            prefix.append(branch).append(" > ");
        }
        MilloLog.logWarning(prefix + message);
    }

}
