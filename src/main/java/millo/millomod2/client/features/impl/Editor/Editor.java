package millo.millomod2.client.features.impl.Editor;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.PacketEventBus;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.features.addons.PacketEventSubscriber;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.hypercube.model.ModelUtil;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.regex.Pattern;

public class Editor extends Feature implements Keybound, Configurable, PacketEventSubscriber {

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
        while (getKeybind().consumeClick()) {
            onKeyPress();
        }

        if (fetchingAllTemplates && waitForShulkers > 0) {
            // if we're waiting for shulker boxes, but they haven't come in for 5 seconds, abort.
            waitForShulkers++;
            if (waitForShulkers > 10) {
                // all shulkers have been received.
                fetchingAllTemplates = false;
                waitForShulkers = 0;
                MilloLog.logInGame(Component.literal("Finished fetching templates.").setStyle(Styles.ADDED.getStyle()));
                screen.getMain().getHierarchy().reload();
            }
        }
    }

    @Override
    public void subscribePackets(PacketEventBus eventBus) {
        eventBus.subscribeReceive(ClientboundContainerSetSlotPacket.class, this::slotUpdate);
    }

    public boolean slotUpdate(ClientboundContainerSetSlotPacket packet) {
        if (fetchingAllTemplates) {
            extractShulkerbox(packet.getItem());
            return false;
        }
        return legacy.slotUpdate(packet);
    }

    private boolean extractShulkerbox(ItemStack item) {
        DataComponentMap shulkerComponents = item.getComponents();
        if (shulkerComponents == null) return false;

        ItemContainerContents containerComponent = shulkerComponents.get(DataComponents.CONTAINER);
        if (containerComponent == null) return false;

        for (ItemStackTemplate itemStack : containerComponent.nonEmptyItems()) {

            String codeTemplateData = ItemUtil.getPBVString(itemStack.create(), "hypercube:codetemplatedata");
            if (codeTemplateData == null) continue;

            TemplateModel templateModel = ModelUtil.parseFromItemNBT(codeTemplateData);

            if (screen == null) continue;
            screen.addTemplate(templateModel);
        }

        waitForShulkers = 1;
        return true;
    }

    private void onKeyPress() {
        openEditor();

        if (HypercubeAPI.getMode() != HypercubeAPI.Mode.DEV) return;
        if (MilloMod.MC.level == null || player() == null || net() == null) return;
        if (!(MilloMod.MC.hitResult instanceof BlockHitResult hit)) return;
        if (hit.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = hit.getBlockPos();
        if (MilloMod.MC.level.getBlockEntity(pos) instanceof SignBlockEntity) pos = pos.offset(1, 0, 0);
        Block block = MilloMod.MC.level.getBlockState(pos).getBlock();
        if (!Pattern.compile("minecraft:(diamond|emerald|lapis|gold|netherite)_block").matcher(BuiltInRegistries.BLOCK.getKey(block).toString()).matches()) return;


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
        MC.schedule(() -> {
            screen = new EditorMenu(null);
            MC.gui.setScreen(screen);
        });
    }

    // ALSO "LEGACY" SUPPORT:

    public void getAllTemplates() {
        if (fetchingAllTemplates) {
            abort();
            return;
        }

        MilloLog.logInGame(Component.literal("Fetching all templates...").setStyle(Styles.ANY.getStyle()));
        fetchingAllTemplates = true;
        waitForShulkers = 0;
        PlayerUtil.sendCommand("p totemplate");
    }

    public void abort() {
        MilloLog.logInGame(Component.literal("Aborting template fetching...").setStyle(Styles.SCARY.getStyle()));

        fetchingAllTemplates = false;
        waitForShulkers = 0;
    }
}
