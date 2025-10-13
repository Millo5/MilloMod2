package millo.millomod2.client.features.impl.CommandWheel;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.config.value.ListConfigValue;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.HUDRendered;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.features.addons.MouseScrollable;
import millo.millomod2.client.util.GlobalUtil;
import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.List;

public class CommandWheel extends Feature implements Keybound, HUDRendered, Configurable, MouseScrollable {

    @Override
    public String getId() {
        return "command_wheel";
    }

    @Override
    public void setupConfig(FeatureConfig config) {

        List<ListConfigValue<CommandWheelEntryConfigValue>> pages = List.of(
                new ListConfigValue<>(List.of(
                        new CommandWheelEntryConfigValue("Dev", "dev"),
                        new CommandWheelEntryConfigValue("Play", "play"),
                        new CommandWheelEntryConfigValue("Build", "build")
                )),
                new ListConfigValue<>(List.of(
                        new CommandWheelEntryConfigValue("Not", "not"),
                        new CommandWheelEntryConfigValue("Cancel", "cancel"),
                        new CommandWheelEntryConfigValue("Refer", "reference"),
                        new CommandWheelEntryConfigValue("B.F.S.", "bracket"),
                        new CommandWheelEntryConfigValue("G. Val", "val"),
                        new CommandWheelEntryConfigValue("Values", "values")
                )),
                new ListConfigValue<>(List.of(
                        new CommandWheelEntryConfigValue("Spawn", "s"),
                        new CommandWheelEntryConfigValue("Node 1", "server node1"),
                        new CommandWheelEntryConfigValue("Node 2", "server node2"),
                        new CommandWheelEntryConfigValue("Node 3", "server node3"),
                        new CommandWheelEntryConfigValue("Node 4", "server node4"),
                        new CommandWheelEntryConfigValue("Node 5", "server node5"),
                        new CommandWheelEntryConfigValue("Node 6", "server node6"),
                        new CommandWheelEntryConfigValue("Node 7", "server node7"),
                        new CommandWheelEntryConfigValue("Beta", "server beta")
                )),
                new ListConfigValue<>(List.of(
                        new CommandWheelEntryConfigValue("C l", "c l"),
                        new CommandWheelEntryConfigValue("C g", "c g"),
                        new CommandWheelEntryConfigValue("C n", "c n"),
                        new CommandWheelEntryConfigValue("C dnd", "c dnd")
                )),
                new ListConfigValue<>(List.of(
                        new CommandWheelEntryConfigValue("Creat", "gmc"),
                        new CommandWheelEntryConfigValue("Adven", "gma"),
                        new CommandWheelEntryConfigValue("Survi", "gms"),
                        new CommandWheelEntryConfigValue("Spect", "gmsp")
                ))
        );

        config.addList("pages", new ListConfigValue<>(pages));
    }

    private boolean open = false;
    private int currentPage = 0;
    private List<CommandWheelEntry> currentEntries = List.of();

    @Override
    public boolean onScroll(double amount) {
        if (!open) return false;
        rotateBump = (float) (1.2f * MathHelper.clamp(amount, -1f, 1f));
        setPage(currentPage - (int) amount);
        return true;
    }

    private void setPage(int page) {
        int pageCount = getPages().size();
        page = ((page % pageCount) + pageCount) % pageCount;

        if (page == currentPage) return;
        currentPage = page;
        currentEntries = getPage(currentPage);
    }

    @Override
    public void onTick() {
        if (!GlobalUtil.isKeyDown(getKeybind()) || MilloMod.MC.currentScreen != null) {
            if ( open) {
                MilloMod.MC.mouse.lockCursor();

                currentEntries.stream().filter(CommandWheelEntry::isSelected).forEach(CommandWheelEntry::execute);
            }
            open = false;
            return;
        }

        if (!open) {
            currentPage = 0;
            currentEntries = getPage(currentPage);
        }

        open = true;
        MilloMod.MC.mouse.unlockCursor();

        double mouseX = MilloMod.MC.mouse.getX();
        double mouseY = MilloMod.MC.mouse.getY();

        int cx = MilloMod.MC.getWindow().getWidth() / 2;
        int cy = MilloMod.MC.getWindow().getHeight() / 2;

        int dx = (int) (mouseX - cx);
        int dy = (int) (mouseY - cy);
        double dist = Math.sqrt(dx*dx + dy*dy);
        if (dist < 30) {
            currentEntries.forEach(e -> e.setSelected(false));
            return;
        }

        double angle = Math.atan2(dy, dx) + Math.PI / 2;
        if (angle < 0) angle += Math.PI * 2;

        double segment = (Math.PI * 2) / currentEntries.size();
        angle += segment / 2;
        if (angle >= Math.PI * 2) angle -= Math.PI * 2;

        int index = (int) (angle / segment);
        index = MathHelper.clamp(index, 0, currentEntries.size() - 1);
        for (int i = 0; i < currentEntries.size(); i++) {
            currentEntries.get(i).setSelected(i == index);
        }
    }

    private float shown = 0f;
    private float rotateBump = 0f;
    @Override
    public void HUDRender(RenderInfo renderInfo) {
        shown = renderInfo.lerp(shown, open ? 1f : 0f, 15f);
        if (shown < 0.01f) return;
        rotateBump = renderInfo.lerp(rotateBump, 0f, 15f);

        DrawContext context = renderInfo.context();
        TextRenderer textRenderer = MilloMod.MC.textRenderer;
        int cx = MilloMod.MC.getWindow().getScaledWidth() / 2;
        int cy = MilloMod.MC.getWindow().getScaledHeight() / 2;

        // Render wheel
        for (int i = 0; i < currentEntries.size(); i++) {
            double angle = ((double) i / currentEntries.size()) * Math.PI * 2 + rotateBump - Math.PI / 2 - (1f - shown);
            int x = (int) (cx + Math.cos(angle) * 80 * shown);
            int y = (int) (cy + Math.sin(angle) * 80 * shown);

            CommandWheelEntry entry = currentEntries.get(i);
            entry.draw(renderInfo, x, y, textRenderer, shown);
        }

        // Render page indicator
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(cx, cy + 10);
        context.getMatrices().scale(shown, shown);

        int totalPages = getPages().size();
        int w = totalPages * 5 / 2 - 1;

        for (int i = 0; i < totalPages; i++) {
            if (i == currentPage) {
                context.fill(i * 5 - w - 1, -1, i * 5 + 3 - w, 3, Color.white.hashCode());
            } else {
                context.fill(i * 5 - w, 0, i * 5 + 2 - w, 2, Color.gray.hashCode());
            }
        }

        context.getMatrices().popMatrix();
    }

    private List<ListConfigValue<CommandWheelEntryConfigValue>> getPages() {
        ListConfigValue<ListConfigValue<CommandWheelEntryConfigValue>> pagesConfig = config.get("pages");
        return pagesConfig.getValue();
    }

    private List<CommandWheelEntry> getPage(int page) {
        List<ListConfigValue<CommandWheelEntryConfigValue>> pages = getPages();
        page = ((page % pages.size()) + pages.size()) % pages.size();
        ListConfigValue<CommandWheelEntryConfigValue> pageConfig = pages.get(page);
        List<CommandWheelEntryConfigValue> entriesConfig = pageConfig.getValue();
        return entriesConfig.stream().map(CommandWheelEntryConfigValue::getValue).toList();
    }

}
