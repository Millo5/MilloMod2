package millo.millomod2.client.features.impl.Editor;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.features.addons.OnReceivePacket;
import millo.millomod2.client.features.impl.Editor.logic.model.ModelUtil;
import millo.millomod2.client.features.impl.Editor.logic.model.TemplateModel;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.block.Block;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.regex.Pattern;

public class Editor extends Feature implements Keybound, Configurable {

    // this feature doesn't contain any logic for the Editor Menu itself
    // only the interaction logic with hypercube.

    private final LegacyEditorSupport legacy;
    private EditorMenu screen;

    private boolean fetchingAllTemplates = false;
    private int waitForShulkers = 0;

    @Override
    public String getId() {
        return "editor";
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addString("folder_regex", "[.:]");
    }

    public String getFolderRegex() {
        return config.getString("folder_regex");
    }

    public Editor() {
        this.legacy = new LegacyEditorSupport(this);
    }

    @Override
    public void onTick() {
        while (getKeybind().wasPressed()) {
            onKeyPress();
        }

        if (fetchingAllTemplates && waitForShulkers > 0) {
            // if we're waiting for shulker boxes, but they haven't come in for 5 seconds, abort.
            waitForShulkers++;
            if (waitForShulkers > 10) {
                // all shulkers have been received.
                fetchingAllTemplates = false;
                waitForShulkers = 0;
                player().sendMessage(Text.literal("Finished fetching templates.").setStyle(Styles.ADDED.getStyle()), false);
            }
        }
    }

    @OnReceivePacket
    public boolean slotUpdate(ScreenHandlerSlotUpdateS2CPacket packet) {
        if (fetchingAllTemplates) {
            ComponentMap shulkerComponents = packet.getStack().getComponents();
            if (shulkerComponents == null) return false;

            ContainerComponent containerComponent = shulkerComponents.get(DataComponentTypes.CONTAINER);
            if (containerComponent == null) return false;

            for (ItemStack itemStack : containerComponent.iterateNonEmpty()) {

                String codeTemplateData = ItemUtil.getPBVString(itemStack, "hypercube:codetemplatedata");
                if (codeTemplateData == null) continue;

                TemplateModel templateModel = ModelUtil.parseFromItemNBT(codeTemplateData);

                if (screen == null) continue;
                screen.addTemplate(templateModel);
            }

            waitForShulkers = 1;
            return false;
        }
        return legacy.slotUpdate(packet);
    }

    private void onKeyPress() {
        openEditor();

        if (HypercubeAPI.getMode() != HypercubeAPI.Mode.DEV) return;
        if (MilloMod.MC.world == null || player() == null || net() == null) return;
        if (!(MilloMod.MC.crosshairTarget instanceof BlockHitResult hit)) return;
        if (hit.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = hit.getBlockPos();
        if (MilloMod.MC.world.getBlockEntity(pos) instanceof SignBlockEntity) pos = pos.add(1, 0, 0);
        Block block = MilloMod.MC.world.getBlockState(pos).getBlock();
        if (!Pattern.compile("minecraft:(diamond|emerald|lapis|gold|netherite)_block").matcher(Registries.BLOCK.getId(block).toString()).matches()) return;


        legacy.getMethodFromPosition(pos, player(), net(), (template) -> {
            if (template == null) {
                MilloLog.error("No template found for this block.");
                return;
            }
            if (screen == null) return;
            screen.openTemplate(template);
        });
    }

    @Override
    public void onEnterPlot(Plot plot) {
        EditorMenu.unloadPlot();
    }

    public void openEditor() {
        MC.send(() -> {
            screen = new EditorMenu(null);
            MC.setScreen(screen);
        });
    }

    // ALSO "LEGACY" SUPPORT:

    public void getAllTemplates() {
        if (fetchingAllTemplates) {
            abort();
            return;
        }

        player().sendMessage(Text.literal("Fetching all templates...").setStyle(Styles.ANY.getStyle()), false);
        fetchingAllTemplates = true;
        waitForShulkers = 0;
        PlayerUtil.sendCommand("p totemplate");
    }

    public void abort() {
        player().sendMessage(Text.literal("Aborting template fetching...").setStyle(Styles.SCARY.getStyle()), false);

        fetchingAllTemplates = false;
        waitForShulkers = 0;
    }
}
