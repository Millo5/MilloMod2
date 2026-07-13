package millo.millomod2.client.features.impl.Editor.logic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import millo.millomod2.client.hypercube.model.arguments.ArgumentModel;
import millo.millomod2.client.hypercube.model.arguments.ParameterArgumentModel;
import millo.millomod2.client.hypercube.template.MethodType;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public final class MethodIndex {

    public static final String DYNAMIC_CALL_REGEX = "%[^()]+\\([^)]*\\)";
    private static final Pattern DYNAMIC_CALL_PATTERN = Pattern.compile(DYNAMIC_CALL_REGEX);

    private final Map<String, MethodDefinition> definitions = new HashMap<>();
    private final Map<String, ArrayList<MethodUsage>> usages = new HashMap<>();
    private final Map<String, ArrayList<TargetUsage>> usagesBySource = new HashMap<>();
    private final AtomicInteger pendingUpdates = new AtomicInteger();

    public void indexTemplate(String templateName, @Nullable String json) {
        IndexedTemplate indexedTemplate = IndexedTemplate.EMPTY;
        try {
            if (json != null) {
                JsonArray blocks = JsonParser.parseString(json).getAsJsonObject().getAsJsonArray("blocks");
                if (blocks != null) {
                    indexedTemplate = new IndexedTemplate(
                            parseDefinition(templateName, blocks),
                            parseUsages(templateName, blocks)
                    );
                }
            }
        } catch (RuntimeException ignored) {
        }

        applyIndex(templateName, indexedTemplate);
    }

    private @Nullable MethodDefinition parseDefinition(String templateName, JsonArray blocks) {
        MethodType type = MethodType.fromSuffix(templateName);
        if (type != MethodType.FUNC && type != MethodType.PROCESS) return null;
        if (blocks.isEmpty() || !blocks.get(0).isJsonObject()) return null;

        JsonObject starter = blocks.get(0).getAsJsonObject();
        ArrayList<MethodParameter> parameters = new ArrayList<>();
        if (starter.has("args") && starter.get("args").isJsonObject()) {
            JsonObject args = starter.getAsJsonObject("args");
            if (!args.has("items") || !args.get("items").isJsonArray()) return null;

            JsonArray arguments = args.getAsJsonArray("items");
            for (JsonElement argumentElement : arguments) {
                if (!argumentElement.isJsonObject()) continue;
                JsonObject argumentRoot = argumentElement.getAsJsonObject();
                if (!argumentRoot.has("item") || !argumentRoot.get("item").isJsonObject()) continue;

                JsonObject argument = argumentRoot.getAsJsonObject("item");
                if (!"pn_el".equals(getString(argument, "id"))) continue;
                try {
                    ParameterArgumentModel parameter = (ParameterArgumentModel) ArgumentModel.deserializeItemArgument(argumentRoot);
                    parameters.add(new MethodParameter(
                            parameter.getName(),
                            parameter.getType(),
                            parameter.isPlural(),
                            parameter.isOptional(),
                            parameter.getDescription()
                    ));
                } catch (RuntimeException ignored) {
                }
            }
        }

        return new MethodDefinition(templateName, type, List.copyOf(parameters));
    }

    private ArrayList<TargetUsage> parseUsages(String sourceTemplateName, JsonArray blocks) {
        ArrayList<TargetUsage> sourceUsages = new ArrayList<>();
        for (int i = 0; i < blocks.size(); i++) {
            if (!blocks.get(i).isJsonObject()) continue;
            JsonObject block = blocks.get(i).getAsJsonObject();
            if (!"block".equals(getString(block, "id"))) continue;

            String blockType = getString(block, "block");
            if (blockType == null) continue;
            MethodType targetType = switch (blockType) {
                case "call_func" -> MethodType.FUNC;
                case "start_process" -> MethodType.PROCESS;
                default -> null;
            };
            if (targetType == null) continue;

            String target = getString(block, "data");
            if (target == null || isDynamicCall(target)) continue;

            String targetName = targetType.suffixString(target);
            MethodUsage usage = new MethodUsage(sourceTemplateName, i + 1);
            sourceUsages.add(new TargetUsage(targetName, usage));
        }
        return sourceUsages;
    }

    private synchronized void applyIndex(String templateName, IndexedTemplate indexedTemplate) {
        removeTemplateInternal(templateName);

        if (indexedTemplate.definition() != null) {
            definitions.put(templateName, indexedTemplate.definition());
        }
        if (indexedTemplate.usages().isEmpty()) return;

        usagesBySource.put(templateName, indexedTemplate.usages());
        for (TargetUsage targetUsage : indexedTemplate.usages()) {
            usages.computeIfAbsent(targetUsage.targetTemplateName(), ignored -> new ArrayList<>())
                    .add(targetUsage.usage());
        }
    }

    public synchronized void removeTemplate(String templateName) {
        removeTemplateInternal(templateName);
    }

    private void removeTemplateInternal(String templateName) {
        definitions.remove(templateName);

        ArrayList<TargetUsage> sourceUsages = usagesBySource.remove(templateName);
        if (sourceUsages == null) return;
        for (TargetUsage targetUsage : sourceUsages) {
            ArrayList<MethodUsage> targetUsages = usages.get(targetUsage.targetTemplateName());
            if (targetUsages == null) continue;
            targetUsages.remove(targetUsage.usage());
            if (targetUsages.isEmpty()) usages.remove(targetUsage.targetTemplateName());
        }
    }

    public synchronized MethodDefinition getDefinition(MethodType type, String name) {
        return definitions.get(type.suffixString(name));
    }

    public synchronized List<MethodUsage> getUsages(String templateName) {
        if (!usages.containsKey(templateName)) return List.of();
        return List.copyOf(usages.get(templateName));
    }

    public @Nullable MutableText getCallTooltip(MethodType type, String name) {
        MethodDefinition definition = getDefinition(type, name);
        if (definition == null || definition.parameters().isEmpty()) return null;
        return getParameterTooltip(definition);
    }

    public MutableText getUsageTooltip(String templateName) {
        MutableText tooltip = Text.empty();
        List<MethodUsage> methodUsages = getUsages(templateName);
        tooltip.append(Text.literal("Usages: " + methodUsages.size()).setStyle(Styles.ACTION.getStyle()));
        if (isIndexing()) tooltip.append(Text.literal(" (indexing...)").setStyle(Styles.COMMENT.getStyle()));

        for (MethodUsage usage : methodUsages) {
            tooltip.append("\n  ");
            tooltip.append(Text.literal(MethodType.trimSuffix(usage.sourceTemplateName())).setStyle(Styles.NAME.getStyle()));
            tooltip.append(Text.literal(" line " + usage.line()).setStyle(Styles.LINE_NUM.getStyle()));
        }
        return tooltip;
    }

    private MutableText getParameterTooltip(MethodDefinition definition) {
        MutableText tooltip = Text.empty();

        for (int i = 0; i < definition.parameters().size(); i++) {
            MethodParameter parameter = definition.parameters().get(i);
            if (i > 0) tooltip.append("\n");
            if (parameter.optional()) tooltip.append("[");
            tooltip.append(Text.literal(parameter.type()).setStyle(getParameterStyle(parameter.type()).getStyle()));
            if (parameter.plural()) tooltip.append("...");
            tooltip.append(" " + parameter.name());
            if (parameter.optional()) tooltip.append("]");
            if (parameter.description() != null && !parameter.description().isBlank()) {
                tooltip.append(Text.literal(" - " + parameter.description()).setStyle(Styles.COMMENT.getStyle()));
            }
        }
        return tooltip;
    }

    private Styles getParameterStyle(String type) {
        try {
            return Styles.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Styles.PARAMETER;
        }
    }

    public void beginUpdate() {
        pendingUpdates.incrementAndGet();
    }

    public void finishUpdate() {
        pendingUpdates.decrementAndGet();
    }

    public boolean isIndexing() {
        return pendingUpdates.get() > 0;
    }

    private static @Nullable String getString(JsonObject object, String name) {
        if (object == null || !object.has(name) || object.get(name).isJsonNull()) return null;
        return object.get(name).getAsString();
    }

    public static boolean isDynamicCall(String name) {
        return DYNAMIC_CALL_PATTERN.matcher(name).find();
    }

    public record MethodDefinition(String templateName, MethodType type, List<MethodParameter> parameters) {}
    public record MethodParameter(String name, String type, boolean plural, boolean optional, @Nullable String description) {}
    public record MethodUsage(String sourceTemplateName, int line) {}
    private record IndexedTemplate(@Nullable MethodDefinition definition, ArrayList<TargetUsage> usages) {
        private static final IndexedTemplate EMPTY = new IndexedTemplate(null, new ArrayList<>());
    }
    private record TargetUsage(String targetTemplateName, MethodUsage usage) {}
}
