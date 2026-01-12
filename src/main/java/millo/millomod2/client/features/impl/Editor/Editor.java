package millo.millomod2.client.features.impl.Editor;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.features.addons.OnReceivePacket;
import millo.millomod2.client.features.impl.Editor.template.TemplateParser;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.MilloLog;
import net.minecraft.block.Block;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.regex.Pattern;

public class Editor extends Feature implements Keybound {

    // this feature doesn't contain any logic for the Editor Menu itself
    // only the interaction logic with hypercube.

    private final LegacyEditorSupport legacy;
    private EditorMenu screen;

    @Override
    public String getId() {
        return "editor";
    }

    public Editor() {
        this.legacy = new LegacyEditorSupport(this);
    }

    @Override
    public void onTick() {
        while (getKeybind().wasPressed()) {
            onKeyPress();
        }
    }

    @OnReceivePacket
    public boolean slotUpdate(ScreenHandlerSlotUpdateS2CPacket packet) {
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
        if (!Pattern.compile("minecraft:(diamond|emerald|lapis|gold)_block").matcher(Registries.BLOCK.getId(block).toString()).matches()) return;


        legacy.getMethodFromPosition(pos, player(), net(), (template) -> {
            if (template == null) {
                MilloLog.logInGame("No template found for this block.");
                return;
            }

            TemplateParser parser = new TemplateParser(template);
            EditorMenu.setLines(parser.getResult());
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

}
